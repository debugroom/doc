.. include:: ../../../module.txt

.. _section2-springboot-usage-label:

Usage
===================================================


.. _section2-1-spring-cloud-function-usage-label:

Implementation
---------------------------------------------------

Spring Cloud Functionでは、基本的に、以下の3種類のクラスを実装しておく必要がある。

#. クラウド実行環境でイベントをハンドリングするクラス
#. Applicaitonの実行・起動・設定を行うクラス
#. ファンクションコードを実装したクラス

Amazon Lambdaでは、イベントが発生すると、ハンドラとして、ユーザが継承して実装したcom.amazonaws.services.lambda.runtime.RequestHandlerクラスが
呼び出されことになる。Spring Funciton Cloudでは当クラスをイベントの種類ごとに複数提供しており、
このイベントクラス内で、DIコンテナの構築処理行われることになる。もっともスタンダードなハンドラクラスである、
org.springframework.cloud.function.adapter.aws.SpringBootRequestHandlerを使用する場合、
イベントをハンドリングするクラスを以下の通り、IOとなる型パラメータを指定して作成すれば良い。

.. sourcecode:: java
   :caption: 1 org.debugroom.sample.spring.boot.config.SimpleApp.java

   package org.debugroom.sample.spring.cloud.function;

   import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

   public class Handler extends SpringBootRequestHandler<String, String>{
   }

.. note:: 上記のクラスでは、型パラメータを指定している以外は、
   SpringBootRequestHandlerのデフォルトメソッドを利用するため、特に何もオーバーライドはしていない。
   作成したハンドラクラスは、AWSコンソールで、フルパス・メソッド登録する。

イベントの種類ごとに作られた上記のハンドラクラスから、SpringのDIコンテナ構築処理が呼ばれることになる。
(Amazon Lambdaであれば、org.springframework.cloud.function.adapter.aws.SpringFunctionInitializer#initializeが呼ばれる)

.. note:: アプリケーション起動するためのクラスはMavenのSpringBootプラグインの中で、指定。

Applicationの実行・起動及び設定を行うクラスでは、以下の通り、org.springframework.boot.autoconfigure.SpringBootApplicationアノテーションを付与し、
メインメソッド内にSpringアプリケーションを実行する処理を記述しておく。
S3Client等、その他ファンクションの実行に必要な設定があれば、同時に設定しておく。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.cloud.function.config.App.java

   package org.debugroom.sample.spring.cloud.function.config;

   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;

   @SpringBootApplication
   public class App {

	   public static void main(String args[]){
		   SpringApplication.run(App.class, args);
	   }

   }

最後に、ハンドラクラス内で呼び出されるファンクションクラスをjava.util.function.Functionクラスを継承して実装する。
以下は、インプットとして文字列を0個以上のストリーム(Flux型)として受け取り、アウトプットに
"test"という文字列をストリームとして返す例である。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.cloud.function.config.App.java

   package org.debugroom.sample.spring.cloud.function;

   import reactor.core.publisher.Flux;

   public class StringFunction implements
       java.util.function.Function<Flux<String>, Flux<String>>{

	   @Override
	   public Flux<String> apply(Flux<String> t) {
		     System.out.println(t.toString());
		     return Flux.just("test");
	   }

   }

.. note:: Flux型については `こちら <https://github.com/reactor/reactor-core>`_ も参照のこと。
   要は多量の文字列データをノンブロッキングに高速に処理することを目的とした型と解釈すれば良い。

Functionクラスは、デフォルトでコンフィグクラスに@Beanで登録するか
org.springframework.cloud.function.context.FunctionScanアノテーションを付与してSPringにコンポーネントスキャンさせるか
いずれかの方法で登録しておく。@FunctionScanアノテーションは、設定ファイルで、
spring.cloud.function.scan.packagesで指定できる(デフォルトはfunctions)

.. sourcecode:: java

   spring:
     cloud:
       function:
         scan:
           packages: org.debugroom.sample.spring.cloud.function

.. note:: S3などspring-cloud-awsを利用する場合は、EC2を前提とする設定を以下の通り変更してく。

.. sourcecode:: java

   cloud:
     aws:
       stack:
         auto: false
       credentials:
         instanceProfile: false
         profileName:
       region:
         auto: false

実装後は、mvn packageで依存ライブラリを含むJARファイルを作成し、クラウド実行環境へデプロイする。
Amazon Lambdaへのアップロード及び、API Gatewayの設定はこちらを参照のこと。
