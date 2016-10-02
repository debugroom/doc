.. include:: ../../module.txt

.. _section5-cassandra-spring-integration-label:

Cassandra - SpringFrameworkアプリケーション
===============================================================

実際に作成したサンプルは `GitHub <https://github.com/debugroom/sample/tree/develop/sample-spring-cassandra>`_ を参照のこと。

.. _section5-1-environment-label:

動作環境
-----------------------------------------------------

Cassandraのインストール手順は、`Cassandraのインストール <section1-cassandra-install-label>`_ を、キースペース、テーブルの作成は、`データベースの定義 <section3-cassandra-database-definition-label>`_ を参照すること。

また、アプリケーションの作成は、Spring Bootを利用しているため詳細は、`Spring Boot <http>`_ を参照すること。

[OS] |br| 
MacOSX 10.9.5

[JVM] |br| 
Java(TM) SE Runtime Environment (build 1.8.0_40-b27) Java HotSpot(TM) 64-Bit Server VM (build 25.40-b25, mixed mode)

[Cassandraバージョン] |br| 

* cqlsh 5.0.1
* Cassandra 3.7
* CQL spec 3.4.2
* Native protocol v4
* Cassandra driver 3.0.3

[Springバージョン] |br| 

* Spring Boot 1.4.0.RELEASE(Spring Framework 4.3.2.RELEASE)
* Spring Data Cassandra 1.5.0.M1

.. note:: 2016年9月時点では、Spring Data Cassandra 1.4.3.RELEASEが最新だが、 `Cassandra 2.Xのみをサポートしている。 <http://docs.spring.io/spring-data/cassandra/docs/1.4.3.RELEASE/reference/html/#requirements>`_ Cassandra3.Xへの対応は `Spring Data Cassandra 1.5以降を予定しているため <https://spring.io/blog/2016/07/27/spring-data-release-train-ingalls-m1-released>`_ 、Spring Data Release Train Ingalls M1を使用する。

.. _section5-2-cassandra-settings-label:

事前準備
----------------------------------------------------

.. _section5-2-1-setting-pom-label:

pom.xml設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

上述の通り、Spring Data Release Train Ingalls M1を使用するため、pom.xmlに以下の通り設定を行っておく必要がある。

* parentとして、spring-boot-starter-parentを指定
* Spring Milestone Repositoryを追加
* プロパティにIngalls-M1を指定
* Cassandra driverのバージョンを指定
* Spring Data Cassandraに必要なライブラリを追加

   * spring-boot-starter-aop
   * spring-boot-starter-data-cassandra
   * cassandra-driver-core
   * jackson-databind


.. sourcecode:: xml

   <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>1.4.0.RELEASE</version>
   </parent>

   <properties>
     <apt.version>1.1.3</apt.version>
     <querydsl.version>4.1.3</querydsl.version>
     <cassandra-driver.version>3.0.3</cassandra-driver.version>
     <spring-data-releasetrain.version>Ingalls-M1</spring-data-releasetrain.version>
   </properties>

   <repositories>
     <repository>
       <id>spring-milestones</id>
       <name>Spring Milestones</name>
       <url>https://repo.spring.io/libs-milestone</url>
       <snapshots>
         <enabled>false</enabled>
       </snapshots>
     </repository>
   </repositories>

   <dependencies>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-aop</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-cassandra</artifactId>
     </dependency>
     <dependency>
       <groupId>com.datastax.cassandra</groupId>
       <artifactId>cassandra-driver-core</artifactId>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
     </dependency>
     <dependency>
       <groupId>com.fasterxml.jackson.core</groupId>
       <artifactId>jackson-databind</artifactId>
     </dependency>
  </dependencies>


.. note:: 公式の `Spring Data Examples <https://github.com/spring-projects/spring-data-examples>`_  もあわせて適宜参考にすること。

.. _section5-3-setting-cassandra-label:

キースペース、テーブル、データ設定
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

アクセスするCassandraの環境を事前に構築しておく。ここでは、テーブル構築とデータ設定は別ファイルchema.sql、data.sqlに切り出しておき、Cassandraに接続後、キースペースを作成して、データ設定を行う。

.. sourcecode:: sql
   :caption: schema.cql

   USE sample;

   DROP TABLE IF EXISTS users;

   CREATE TABLE users (
           user_id bigint,
           user_name text,
           first_name text,
           last_name text,
           PRIMARY KEY (user_id)
       ); 

.. sourcecode:: sql
   :caption: data.cql

   USE sample;

   INSERT INTO users JSON '{"user_id": 0, "user_name": "org.debugroom", "first_name": "org", "last_name": "debugroom" }' IF NOT EXISTS ;
   INSERT INTO users JSON '{"user_id": 1, "user_name": "(ΦωΦ)", "first_name": "(・∀・)", "last_name": "(・ω・`)" }' IF NOT EXISTS ;

cqlshを起動して、データ設定を行う。

.. sourcecode:: bash

   cqlsh localhost 9042

   cqlsh> CREATE KEYSPACE sample WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

   cqlsh> source 'schema.cql'

   calsh> source 'data.cql'


.. _section5-3-cassandra-create-application-label:

アプリケーションの作成
----------------------------------------------------

作成したテーブルに対応するエンティティクラスを作成する。

.. sourcecode:: java

   package org.debugroom.sample.cassandra.entity;

   import lombok.Data;
   import lombok.NoArgsConstructor;

   import org.springframework.data.cassandra.mapping.Column;
   import org.springframework.data.cassandra.mapping.PrimaryKey;
   import org.springframework.data.cassandra.mapping.Table;

   @Data
   @NoArgsConstructor
   @Table("users")
   public class User {

	   @PrimaryKey("user_id") private Long userId;

	   @Column("user_name") private String userName;
	   @Column("first_name") private String firstName;
	   @Column("last_name") private String lastName;

	   public User(Long userId) {
	       this.setUserId(userId);
	   }
   }


Repositoryクラスを作成する。

.. sourcecode:: java
  
   package org.debugroom.sample.cassandra.repository;

   import org.springframework.data.repository.CrudRepository;

   import org.debugroom.sample.cassandra.entity.User;

   public interface UserRepository extends CrudRepository<User, Long>{
   }

Serviceインターフェース及び、実装クラスを作成する。

.. sourcecode:: java

   package org.debugroom.sample.cassandra.service;

   import java.util.List;

   import org.debugroom.sample.cassandra.entity.User;

   public interface SampleService {

       public List<User> getUsers();
	
   }

.. sourcecode:: java

   package org.debugroom.sample.cassandra.service;

   import java.util.List;

   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Service;

   import org.debugroom.sample.cassandra.entity.User;
   import org.debugroom.sample.cassandra.repository.UserRepository;

   @Service("sampleService")
   public class SampleServiceImpl implements SampleService{

       @Autowired
       UserRepository userRepository;
	
       @Override
       public List<User> getUsers() {
           Iterable<User> users = userRepository.findAll();
           return (List<User>)users;
       }

   }

コンフィグレーション及びアプリケーション実行クラスを作成する。

.. sourcecode:: java

   package org.debugroom.sample.cassandra.config;

   import com.datastax.driver.core.Session;

   import java.util.List;

   import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
   import org.springframework.boot.builder.SpringApplicationBuilder;
   import org.springframework.context.ConfigurableApplicationContext;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.data.cassandra.config.SchemaAction;
   import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
   import org.springframework.data.cassandra.core.CassandraTemplate;
   import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

   import org.debugroom.sample.cassandra.entity.User;
   import org.debugroom.sample.cassandra.service.SampleService;
   import org.debugroom.sample.cassandra.service.SampleServiceImpl;

   import lombok.extern.slf4j.Slf4j;

   @Slf4j
   @Configuration
   @EnableAutoConfiguration
   public class SimpleApp {

       public static void main(String[] args){
           ConfigurableApplicationContext context = new SpringApplicationBuilder(
				SimpleApp.class).web(false).run(args);
           SampleService sampleService = context.getBean(SampleService.class);
           List<User> users = sampleService.getUsers();
           log.info(SimpleApp.class.getName() + " : users ");
           for(User user : users){
               log.info(SimpleApp.class.getName() + " :     - " + user.toString());
           }
        }
	
        @Bean SampleService sampleService(){
            return new SampleServiceImpl();
        }

        @ComponentScan
        @Configuration
        @EnableCassandraRepositories("org.debugroom.sample.cassandra.repository")
        static class CassandraConfig extends AbstractCassandraConfiguration{

            @Override
            protected String getKeyspaceName() {
                return "sample";
            }
		
            @Override
            public String[] getEntityBasePackages() {
                return new String[] { "org.debugroom.sample.cassandra.entity" };
            }

            @Override
            public SchemaAction getSchemaAction() {
                return SchemaAction.CREATE_IF_NOT_EXISTS;
            }

            @Bean
            public CassandraTemplate cassandraTemplate(Session session){
                return new CassandraTemplate(session);
            }

        }
   }

.. todo:: Spring Data CassandraのEntity、Repository、Configurationクラスのオプションを整理。
