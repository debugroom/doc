.. include:: ../../../module.txt

.. _section1-springdata-dynamodb-introduction-label:

Introduction
=====================================================

Spring Data DynamoDBは Spring DataのコミュニティプロジェクトでSpring Data JPAなどと同様、データベースアクセス処理を抽象化し、CRUD操作などの処理をフレームワークとして提供する。
主な特徴は以下の通り挙げられる。

* DynamoDBテーブルに対するCRUDメソッドの実装の提供。
* クエリ名をベースとした動的クエリの生成
* ページネーション、動的なクエリの実行、カスタムデータアクセスの統合
* データ射影
* Spring Data RestとのRESTサポート

今回、以下の環境で簡単な動作検証を実施した。

.. _section1-1-springdata-dynamodb-environment-label:

動作環境
-----------------------------------------------------

[OS] |br|
MacOSX 10.13.6

[JVM] |br|
Java(TM) SE Runtime Environment (build 1.8.0_171-b11)Java HotSpot(TM) 64-Bit Server VM (build 25.171-b11, mixed mode)

[Spring] |br|
Spring Framework Boot 2.1.2.RELEASE| br|
Spring Framework Data DynamoDB 5.1.0 |br|

.. _section1-2-springdata-dynamodb-settings-label:

事前準備
-------------------------------------------------------

.. _section1-2-1-springdata-dynamodb-settings-pom-label:

pom.xmlの設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Mavenプロジェクトに以下のライブラリを追加しておく。Lombokを追加しているが、こちらは任意である。本ドキュメントでは、これらのライブラリを利用したコードが出てくるので注意すること。


.. sourcecode:: xml
   :caption: pom.xml
   :linenos:

   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.1.2.RELEASE</version>
       <relativePath/>
   </parent>

   <dependencies>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-thymeleaf</artifactId>
       </dependency>

       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <optional>true</optional>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
           <scope>test</scope>
       </dependency>
       <dependency>
           <groupId>com.github.derjust</groupId>
           <artifactId>spring-data-dynamodb</artifactId>
           <version>5.1.0</version>
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

.. _section1-2-1-springdata-dynamodb-settings-database-label:

AWS DynamoDBの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

事前にAWS コンソールのメニューから、DynamoDBのテーブルを作成しておく。 :ref:`section6-4-2-dynamodb-create-label` の通り、テーブル名、パーティションキーを入力しておく。

* テーブル名：sample_db
* パーティションキー：sample_key
* ソートキー：sort_key
* デフォルト設定の使用：チェックする

また、事前にDynamoDBへのフルアクセス権限を付与したローカル開発向けのアカウントを作成しておき、認証キーとシークレットキーをユーザホームディレクトリの.aws/credentialに指定しておくこと。
