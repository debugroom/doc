.. include:: ../../../module.txt

.. _section2-spring-cloud-aws-usage-label:

Usage
===================================================

.. _section2-1-spring-cloud-aws-sqs-usage-label:

Amazon SQSを利用する実装
-----------------------------------------------------------------

Amazon SQSに関する概要は :ref:`section7-2-1-sqs-overview-label` を参照のこと。
ここでは、SQSのキューへのメッセージ送信・受信を行う簡単なアプリケーションを作成する。
事前の環境設定は :ref:`section1-spring-cloud-aws-introduction-label` を参照すること。

SQSを利用する場合は、コンフィグクラス、メッセージ送信・受信用メソッドをもつ任意のクラス、アプリケーション起動クラスを作成すれば良い。

メッセージ受信用のクラスは@EnableSqsアノテーション及び、@SqsListenerアノテーションを付与したメソッドを作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.cloud.aws.app.listener.QueueListener.java

   package org.debugroom.sample.spring.cloud.aws.app.listener;

   import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
   import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
   import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
   import org.springframework.stereotype.Component;

   import lombok.extern.slf4j.Slf4j;

   @Slf4j // ログ出力用のLombokのSlf4jアノテーションを設定。
   @EnableSqs // EnableSqsアノテーションを設定。
   @Component // コンポーネントスキャンでリスナーを読み込む場合設定。
   public class QueueListener {

       // AWSコンソール上で、SampleNotifiyというキューを定義した場合の受信キュー設定。受信するとメッセージを削除。
       @SqsListener(value = "SampleNotify", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
       public void onMessage(String message) throws Exception{
           log.info(message);
       }

   }

上記のメッセージ受信リスナークラスを使用するための、コンフィグクラスの実装は以下の通りである。

.. sourcecode:: java
   :caption: 1 org.debugroom.sample.spring.cloud.aws.config.SqsConfig.java

   package org.debugroom.sample.spring.cloud.aws.config;

   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
   import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
   import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
   import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
   import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;

   import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
   import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
   import com.amazonaws.services.sqs.AmazonSQSAsync;
   import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

   import org.debugroom.sample.spring.cloud.aws.app.listener.QueueListener;

   @Configuration
   public class SqsConfig {

       // キューをポーリングするためのエンドポイントを指定。フォーマットは後述。
       @Value("${queue.endpoint}")
       private String queueEndpoint;
       // AWSで作成したキューにアクセスできる権限をもつアカウントの認証情報、リージョン
       @Value("${cloud.aws.credentials.accessKey}")
       private String accessKey;
       @Value("${cloud.aws.credentials.secretKey}")
       private String secretKey;
       @Value("${cloud.aws.region.static}")
       private String region;

       // 上述で作成したメッセージ受信用のクラス。アプリケーション起動すると当該定期的にメッセージポーリングするようになる。
       @Bean
       public QueueListener queueListener(){
           return new QueueListener();
       }

  　　　// AmazonSqsClientの作成。認証情報はプロパティファイルから取得し、環境変数に設定して取得する方式とする。
       @Bean
       public AmazonSQSAsync amazonSQSClient(){
           if(System.getProperty("aws.accessKeyId") == null ||
               System.getProperty("aws.accessKeyId").equals("")){
               System.setProperty("aws.accessKeyId", accessKey);
           }
           if(System.getProperty("aws.secretKeyId") == null ||
               System.getProperty("aws.secretKeyId").equals("")){
    		       System.setProperty("aws.secretKey", secretKey);
           }
           return AmazonSQSAsyncClientBuilder
                    .standard()
                    .withEndpointConfiguration(new EndpointConfiguration(
                        queueEndpoint, region))
                    .withCredentials(new SystemPropertiesCredentialsProvider())
                    .build();
       }

       // 以降、メッセージ送信用のテンプレート及び、リスナーファクトリークラスの設定。
       @Bean
       public QueueMessagingTemplate queueMessagingTemplate() {
           return new QueueMessagingTemplate(amazonSQSClient());
       }

       @Bean
       public SimpleMessageListenerContainer simpleMessageListenerContainer() {
           SimpleMessageListenerContainer msgListenerContainer = simpleMessageListenerContainerFactory().createSimpleMessageListenerContainer();
           msgListenerContainer.setMessageHandler(queueMessageHandler());
           return msgListenerContainer;
       }

       @Bean
       public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory() {
           SimpleMessageListenerContainerFactory msgListenerContainerFactory = new SimpleMessageListenerContainerFactory();
           msgListenerContainerFactory.setAmazonSqs(amazonSQSClient());
           return msgListenerContainerFactory;
       }

       @Bean
       public QueueMessageHandler queueMessageHandler() {
           QueueMessageHandlerFactory queueMsgHandlerFactory = new QueueMessageHandlerFactory();
           queueMsgHandlerFactory.setAmazonSqs(amazonSQSClient());
           QueueMessageHandler queueMessageHandler = queueMsgHandlerFactory.createQueueMessageHandler();
           return queueMessageHandler;
       }

   }

キューのエンドポイントとしては、以下のようなアカウントIDを覗くURLフォーマットを設定すれば良い。

.. sourcecode:: java

   queue:
     endpoint: https://sqs.ap-northeast-1.amazonaws.com/

.. note:: キューのURLは通常アカウントIDを含むが、Spring Cloud AWSを通してアクセスする際はキューへの認証情報から参照されるので、メッセージ送受信時にキュー名称だけ指定すれば良い。

キューへのメッセージ送信クラスを以下の通り作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.cloud.aws.domain.service.SampleServiceImpl.java

   package org.debugroom.sample.spring.cloud.aws.domain.service;

   import javax.inject.Inject;

   import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
   import org.springframework.stereotype.Service;

   @Service("sampleService")
   public class SampleServiceImpl {

       // QueueMessagingTemplateをインジェクション
       @Inject
       QueueMessagingTemplate queueMessagingTemplate;

       // 作成したSampleNotifyトピックに対してメッセージを送信。
       public void send(){
           queueMessagingTemplate.convertAndSend("SampleNotify", "test-1");
       }

   }

メッセージ受信のリスナークラスの起動及びメッセージを送信するための起動クラスを作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.cloud.aws.config.App.java

   package org.debugroom.sample.spring.cloud.aws.config;

   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.boot.builder.SpringApplicationBuilder;
   import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
   import org.springframework.context.ConfigurableApplicationContext;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;

   import org.debugroom.sample.spring.cloud.aws.domain.service.SampleServiceImpl;

   // メッセージ送信クラスをコンポーネントスキャンするための設定。
   // コンポーネントスキャンを設定するとコンフィグクラスと同一パッケージにあるクラスをスキャンしなくなるため、config用のパッケージも一緒に指定。
   @ComponentScan({
       "org.debugroom.sample.spring.cloud.aws.domain.service",
       "org.debugroom.sample.spring.cloud.aws.config"
   })
   @Configuration
   @SpringBootApplication(exclude={ElastiCacheAutoConfiguration.class})
   public class App {
       public static void main(String[] args){
	         ConfigurableApplicationContext context = new SpringApplicationBuilder(
				       App.class).web(false).run(args);
           SampleServiceImpl sampleService = context.getBean(SampleServiceImpl.class);
           sampleService.send();
       }
   }

AWSのキューの設定・送信は、 :ref:`section7-2-1-sqs-create-queue-label` を参考のこと。上記では、キューの名称で"SampleNotify"を指定して作成すれば良い。
