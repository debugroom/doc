.. include:: ../../module.txt

.. _section1-jbatch-introduction-label:

Introdcution
=====================================================

JSR-352 Batch Applications for the Java Platform はJavaEE7で導入された標準仕様である。
詳細な仕様は `JCPのJSR-352 <https://jcp.org/en/jsr/detail?id=352>`_ に整理されているが、
今回(2016年9月)に簡単な動作検証を以下の環境にて行ったので注意点含めて概要をサマリする。

なお、検証時には、主に、 `Spring Framework JSR352 Support <http://docs.spring.io/spring-batch/reference/html/jsr-352.html>`_ 及び、`マッキーのブログ <https://blog.ik.am/entries/205>`_ を参考にした。

.. _section1-1-jbatch-environment-label:

動作環境
-----------------------------------------------------

[OS] |br|
MacOSX 10.9.5

[JVM] |br|
Java(TM) SE Runtime Environment (build 1.8.0_40-b27) Java HotSpot(TM) 64-Bit Server VM (build 25.40-b25, mixed mode)

[JSR352実装] |br|
Spring Framework Batch 3.0.6.RELEASE

.. _section1-2-jbatch-settings-label:

事前準備
-------------------------------------------------------

Mavenプロジェクトに以下のライブラリを追加しておく。Spring Batchのライブラリ及びcommons-dbcp、hsqlのライブラリは必須となっている。common-dbcpとhsqlはSpring Batchのジョブ管理テーブルと同様内部的なJob管理に利用するために必要な模様。

その他、Lombokや実行時のデータベースアクセスでSpring Dataを追加しているが、こちらは任意である。本ドキュメントでは、これらのライブラリを利用したコードが出てくるので注意すること。


.. sourcecode:: xml
   :linenos:

   <properties>
     <java.version>1.8</java.version>
     <io.spring.platform_platform-bom_version>2.0.1.RELEASE</io.spring.platform_platform-bom_version>
   </properties>

   <dependencyManagement>
     <dependencies>
       <dependency>
         <groupId>io.spring.platform</groupId>
         <artifactId>platform-bom</artifactId>
         <version>${io.spring.platform_platform-bom_version}</version>
         <type>pom</type>
         <scope>import</scope>
       </dependency>
     </dependencies>
   </dependencyManagement>
  
   <dependencies>
     <!-- Required -->
     <dependency>
       <groupId>org.springframework.batch</groupId>
       <artifactId>spring-batch-core</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.batch</groupId>
       <artifactId>spring-batch-infrastructure</artifactId>
     </dependency> 
     <dependency>
       <groupId>javax.inject</groupId>
       <artifactId>javax.inject</artifactId>
     </dependency>
     <dependency>
       <groupId>commons-dbcp</groupId>
       <artifactId>commons-dbcp</artifactId>
     </dependency>
     <dependency>
       <groupId>org.hsqldb</groupId>
       <artifactId>hsqldb</artifactId>
     </dependency>

     <!-- Option -->
     <dependency>
       <groupId>org.springframework.data</groupId>
       <artifactId>spring-data-jpa</artifactId>
     </dependency>
     <dependency>
       <groupId>org.hibernate</groupId>
       <artifactId>hibernate-entitymanager</artifactId>
     </dependency>
     <dependency>
       <groupId>org.aspectj</groupId>
       <artifactId>aspectjrt</artifactId>
     </dependency> 
     <dependency>
       <groupId>org.aspectj</groupId> 
       <artifactId>aspectjweaver</artifactId>
     </dependency>
     <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
     </dependency>
     <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
     </dependency>
   </dependencies>

