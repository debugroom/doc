.. _section1-springboot-introduction-label:

Introduction
=====================================================

Spring BootはSpring Frameworkを使用したアプリケーションを簡単に構築するためのスターターである。
主な特徴として以下のようなものが挙げられる。

* スタンドアロンベースのSpringアプリケーションの作成
* WARファイル作成を必要としないサーバ組み込み
* 依存性管理の簡素化
* 自律駆動的な設定
* 自動生成やXML定義の除外

今回、以下の環境で簡単な動作検証を実施した。

動作環境
-----------------------------------------------------

[OS]
MacOSX 10.9.5

[JVM]
Java(TM) SE Runtime Environment (build 1.8.0_40-b27) Java HotSpot(TM) 64-Bit Server VM (build 25.40-b25, mixed mode)

[SpringBoot]
Spring Framework Boot 1.4.0.RELEASE


事前準備
-------------------------------------------------------

Mavenプロジェクトに以下のライブラリを追加しておく。LombokやJUnitを追加しているが、こちらは任意である。本ドキュメントでは、これらのライブラリを利用したコードが出てくるので注意すること。


.. sourcecode:: xml
   :caption: pom.xml
   :linenos:

   <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>1.4.0.RELEASE</version>
   </parent>

   <dependencies>
     <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <scope>provided</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-devtools</artifactId>
       <scope>provided</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
     </dependency>
   </dependencies>

   <build>
     <plugins>
       <plugin>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-maven-plugin</artifactId>
       </plugin>
     </plugins>
   </build>

