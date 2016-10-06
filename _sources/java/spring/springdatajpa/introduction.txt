.. include:: ../../../module.txt

.. _section1-springdata-jpa-introduction-label:

Introduction
=====================================================

Spring Data JPAは Spring Frameworkを使用したアプリケーションで、JPAを用いたデータベースアクセス処理において、
データベースアクセスに関わるボイラープレートコードを排除し、処理の記述を簡易化するフレームワークである。
主な特徴は以下の通り挙げられる。

* Spring FrameworkとJPAをベースとしたレポジトリの洗練されたサポート
* QueryDSLとタイプセーフなJPAクエリ記述のサポート
* ドメインクラスの透過的な監視
* ページネーション、動的なクエリの実行、カスタムデータアクセスの統合
* 起動時における@Queryアノテーションを付与したクエリの検証
* XMLベースのエンティティマッピングのサポート
* @EnableJpaRepositoriesを基調としたJavaConfigの設定

今回、以下の環境で簡単な動作検証を実施した。

.. _section1-1-springdata-jpa-environment-label:

動作環境
-----------------------------------------------------

[OS] |br|
MacOSX 10.9.5

[JVM] |br|
Java(TM) SE Runtime Environment (build 1.8.0_40-b27) Java HotSpot(TM) 64-Bit Server VM (build 25.40-b25, mixed mode)

[Spring] |br|
Spring Framework 4.3.2.RELEASE
Spring Framework Data 1.10.2.RELEASE
Spring Framework Boot 1.4.0.RELEASE

[JPA]
JPA2.1

.. _section1-2-springdata-jpa-settings-label:

事前準備
-------------------------------------------------------

.. _section1-2-1-springdata-jpa-settings-pom-label:

pom.xmlの設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Mavenプロジェクトに以下のライブラリを追加しておく。Lombokを追加しているが、こちらは任意である。本ドキュメントでは、これらのライブラリを利用したコードが出てくるので注意すること。


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
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-jpa</artifactId>
     </dependency>
     <dependency>
       <groupId>org.hsqldb</groupId>
       <artifactId>hsqldb</artifactId>
     </dependency>
     <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <scope>provided</scope>
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

.. _section1-2-1-springdata-jpa-settings-database-label:

テーブルの構築
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

本サンプルでは、以下のようなテーブルを構成している。

.. figure:: img/sample.png
   :scale: 100%

テーブル構成は以下の通りである。

.. list-table:: テーブルの構成
   :header-rows: 1
   :widths: 30,20,30,50

   * - テーブル
     - エンティティクラス名
     - 論理名
     - ユーザとの関係
   * - USR
     - User
     - ユーザ
     - −
   * - GRP
     - Group
     - グループ
     - 所属テーブルを介して多対多関連
   * - ADDRESS
     - Address
     - 住所
     - 一対一関連
   * - EMAIL
     - Email
     - メール
     - 一対多関連
   * - AFFILIATION
     - Affiliation
     - 所属
     - ユーザとグループの関連実体

.. sourcecode:: sql
   :caption: src/main/resource/ddl/sample.sql

   /* Drop Tables */
   DROP TABLE IF EXISTS ADDRESS;
   DROP TABLE IF EXISTS AFFILIATION;
   DROP TABLE IF EXISTS EMAIL;
   DROP TABLE IF EXISTS GRP;
   DROP TABLE IF EXISTS USR;

   /* Create Tables */
   CREATE TABLE ADDRESS
   (
       USER_ID varchar(8) NOT NULL,
       ZIP_CD char(8),
       ADDRESS varchar(255),
       VER int,
       LAST_UPDATED_DATE timestamp,
       PRIMARY KEY (USER_ID)
   );

   CREATE TABLE AFFILIATION
   (
       GROUP_ID char(10) NOT NULL,
       USER_ID varchar(8) NOT NULL,
       VER int,
       LAST_UPDATED_DATE timestamp,
       PRIMARY KEY (GROUP_ID, USER_ID),
   );

   CREATE TABLE EMAIL
   (
       USER_ID varchar(8) NOT NULL,
       EMAIL_ID varchar(255) NOT NULL,
       EMAIL varchar(255),
       VER int,
       LAST_UPDATED_DATE timestamp,
       PRIMARY KEY (USER_ID, EMAIL_ID),
   );

   CREATE TABLE GRP
   (
       GROUP_ID char(10) NOT NULL,
       GROUP_NAME varchar(50),
       VER int,
       LAST_UPDATED_DATE timestamp,
       PRIMARY KEY (GROUP_ID)
   );

   CREATE TABLE USR
   (
       USER_ID varchar(8) NOT NULL,
       USER_NAME varchar(50),
       LOGIN_ID varchar(64),
       VER int,
       LAST_UPDATED_DATE timestamp,
       PRIMARY KEY (USER_ID)
   );

   /* Create Foreign Keys */
   ALTER TABLE AFFILIATION
      ADD FOREIGN KEY (GROUP_ID)
      REFERENCES GRP (GROUP_ID)
      ON UPDATE RESTRICT
      ON DELETE RESTRICT
   ;

   ALTER TABLE ADDRESS
      ADD FOREIGN KEY (USER_ID)
      REFERENCES USR (USER_ID)
      ON UPDATE RESTRICT
      ON DELETE RESTRICT
   ;

   ALTER TABLE AFFILIATION
      ADD FOREIGN KEY (USER_ID)
      REFERENCES USR (USER_ID)
      ON UPDATE RESTRICT
      ON DELETE RESTRICT
   ;

   ALTER TABLE EMAIL
      ADD FOREIGN KEY (USER_ID)
      REFERENCES USR (USER_ID)
      ON UPDATE RESTRICT
      ON DELETE RESTRICT
   ;


.. sourcecode:: sql
   :caption: src/main/resource/ddl/data.sql

   DELETE FROM ADDRESS;
   DELETE FROM AFFILIATION;
   DELETE FROM EMAIL;
   DELETE FROM GRP;
   DELETE FROM USR;

   INSERT INTO USR VALUES ('00000000', '(ΦωΦ)', 'org.debugroom.test1', 0, '2015-01-01 00:00:00.0');
   INSERT INTO USR VALUES ('00000001', '(・∀・)', 'org.debugroom.test2', 0, '2015-01-01 00:00:00.0');
   INSERT INTO USR VALUES ('00000002', '(・ω・`)', 'org.debugroom.test3', 0, '2015-01-01 00:00:00.0');

   INSERT INTO GRP VALUES ('0000000000', 'org.debugroom', 0, '2015-01-01 00:00:00.0');
   INSERT INTO GRP VALUES ('0000000001', 'nttdata', 0, '2015-01-01 00:00:00.0');

   INSERT INTO EMAIL VALUES('00000000', 1, 'test@test.co.jp', 0, '2015-01-01 00:00:00.0');
   INSERT INTO EMAIL VALUES('00000000', 2, 'test@test.com', 0, '2015-01-01 00:00:00.0');
   INSERT INTO EMAIL VALUES('00000001', 1, 'test@test.ne.jp', 0, '2015-01-01 00:00:00.0');

   INSERT INTO AFFILIATION VALUES('0000000000', '00000000', 0, '2015-01-01 00:00:00.0');
   INSERT INTO AFFILIATION VALUES('0000000000', '00000001', 0, '2015-01-01 00:00:00.0');
   INSERT INTO AFFILIATION VALUES('0000000001', '00000001', 0, '2015-01-01 00:00:00.0');
   INSERT INTO AFFILIATION VALUES('0000000001', '00000002', 0, '2015-01-01 00:00:00.0');

   INSERT INTO ADDRESS VALUES ('00000000', '135-8671', '東京都江東区豊洲3-3-3', 0, '2015-01-01 00:00:00.0');
   INSERT INTO ADDRESS VALUES ('00000001', '135-8671', '東京都江東区豊洲3-3-9', 0, '2015-01-01 00:00:00.0');
   INSERT INTO ADDRESS VALUES ('00000002', '135-8671', '東京都江東区豊洲3-3-19', 0, '2015-01-01 00:00:00.0');
