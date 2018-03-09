.. _section1-spring-cloud-aws-introduction-label:

Introduction
=====================================================

Spring Cloud AWSは、Amazon Web Service上のサービスやSDKで提供されているAPI等を簡易的に利用できるプロダクトである。
以下のような、API、機能を提供する。

* AWS SQSをSpring Messaging APIを使って実装する機能
* ElasticCacheを使ったキャッシュAPIの実装
* SNSエンドポイントのアノテーションベースの実装
* CloudFormationで定義した論理名のリソースアクセス
* RDSインスタンスの論理名を使用したJDBCデータソースアクセス
* S3バケットへのResourceLoaderを使ったアクセス

今回、以下の環境で簡単な動作検証を実施した。

動作環境
-----------------------------------------------------

[OS]
MacOSX 10.12.6

[JVM]
java version "1.8.0_151"
Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)

[実行環境及び、SDKライブラリバージョン]
AWS Dynamo
  * aws-java-sdk-dynamodb : 1.11.125

[Spring]
Spring Framework Boot 1.5.9.RELEASE
Spring Cloud AWS 1.2.2.RELEASE

事前準備
-------------------------------------------------------

実際に作成したサンプルは `GitHub <https://github.com/debugroom/sample/tree/develop/sample-spring-cloud-aws>`_ を参照のこと。

Mavenプロジェクトは、以下のライブラリを追加しておく。

.. sourcecode:: xml
   :linenos:

   <dependencies>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-aws</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-aws-messaging</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-context</artifactId>
     </dependency>
     <dependency>
       <groupId>com.amazonaws</groupId>
       <artifactId>aws-java-sdk-dynamodb</artifactId>
       <version>1.11.125</version>
     </dependency>
   </dependencies>
