.. include:: ../../../module.txt

.. _section2-jbatch-usage-label:

Usage
=====================================================

実際に作成したサンプルは、`GitHub <https://github.com/debugroom/sample/tree/develop/sample-javaee/sample-javaee7/sample-jbatch>`_ を参照のこと。

.. section2-1-jbatch-usage-batchlet-label:

Batchlet
-----------------------------------------------------

バッチジョブを実行する場合は以下の通り、javax.batch.runtime.BatchRuntimeからJobOperatorを取得し、jobIdを指定してJobOperator#start()メソッドを実行する。
ジョブにパラメータを渡す場合は、Propertiesクラスにパラメータをセットして、JobOperatorの引数として渡す。


.. sourcecode:: java
   :caption: org.debugroom.sample.jbatch.app.batch.launcher.SampleBatchletLauncher.java

   package org.debugroom.sample.jbatch.app.batch.launcher;

   import java.util.Properties;

   import javax.batch.operations.JobOperator;
   import javax.batch.runtime.BatchRuntime;
   import javax.batch.runtime.BatchStatus;
   import javax.batch.runtime.JobExecution;

   public class SampleBatchletLauncher {

       public static void main(String[] args){

           Properties properties = new Properties();
           properties.setProperty("param", "param1");

           JobOperator jobOperator = BatchRuntime.getJobOperator();
           long executionId = jobOperator.start("sampleBatchlet", properties);
           JobExecution jobExecution = jobOperator.getJobExecution(executionId);
           BatchStatus status = jobExecution.getBatchStatus();
           System.out.println(status.toString());

       }
   }

指定したjobIdと同じ名前をもつ、jobId.xmlを作成し、クラスパス直下のMETA-INF/batch-jobs以下に配置する。
このジョブ定義ファイルに実行するBatchletファイルクラスを指定する。
ジョブ実行時には新しくSpringのApplicationContextが構成されるため、ジョブ内でSpringのDIコンテナからオブジェクトを取得する場合は、Bean定義ファイルをインポートしておく。

.. sourcecode:: xml
   :caption: src/main/resources/META-INF/batch-jobs/sampleBatchlet.xml

   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context"
      xmlns:batch="http://www.springframework.org/schema/batch"
      xsi:schemaLocation="http://www.springframework.org/schema/batch http://www.springframework.org/schema/batch/spring-batch.xsd
          http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
          http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/jobXML_1_0.xsd">

       <context:property-placeholder
            location="classpath*:/META-INF/spring/*.properties,
                      classpath*:/META-INF/*.properties" />

       <import resource="classpath:/META-INF/spring/applicationContext.xml"/>

   <!--
       <context:component-scan base-package="org.debugroom.sample.jbatch.app.batch.job" />
   -->

       <job id="sampleBatchlet" xmlns="http://xmlns.jcp.org/xml/ns/javaee" version="1.0">
           <step id="step1">
               <batchlet ref="org.debugroom.sample.jbatch.app.batch.job.SampleBatchlet"></batchlet>
           </step>
       </job>

    </beans>

.. note:: ref属性にBean名を付与して、コンポーネントスキャンで@Namedアノテーションを付与したBatchletクラスを直接取得しようとすると、後述するJobContextがセットできない模様。上記ではref属性に直接BeanクラスをFQCNで指定している。

実行ジョブとなる、Batchletのサンプルクラスを以下に示す。javax.batch.api.AbstractBatchletを継承して、process()メソッドを実装する。パラメータを取得する場合は、JobOperatorからexecutionIdを取得して、実行クラスで指定したPropertiesクラスから取得する。

.. sourcecode:: java

   package org.debugroom.sample.jbatch.app.batch.job;

   import java.util.List;
   import java.util.Properties;

   import javax.inject.Inject;
   import javax.inject.Named;

   import javax.batch.runtime.BatchRuntime;
   import javax.batch.runtime.context.JobContext;
   import javax.batch.api.AbstractBatchlet;

   import org.debugroom.sample.jbatch.domain.model.entity.Company;
   import org.debugroom.sample.jbatch.domain.service.SampleService;

   @Named
   public class SampleBatchlet extends AbstractBatchlet{

       @Inject
       JobContext jobContext;

       @Inject
       SampleService sampleService;

       @Override
       public String process() throws Exception {
           Properties properties = BatchRuntime.getJobOperator()
                                               .getParameters(jobContext.getExecutionId());
           System.out.println(this.getClass().getName() + " : param : " + properties.getProperty("param"));
           List<Company> companyList = sampleService.getCompanyList();
           System.out.println(this.getClass().getName() + ": companyList :");
           for(Company company : companyList){
               System.out.println(this.getClass().getName() + " :           - {"
                    + company.getCompanyId() + ", "+ company.getCompanyName() + "}");
           }
           return null;
       }
   }

.. todo:: Chunk方式のサンプルを記載する。
