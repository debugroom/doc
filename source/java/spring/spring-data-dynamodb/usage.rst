.. include:: ../../../module.txt

.. _section2-spring-data-dynamodb-usage-label:

Usage
===================================================

実際に作成したサンプルは `GitHub <https://github.com/debugroom/sample-spring-data-dynamodb>`_ を参照のこと。


.. _section2-1-spring-data-dynamodb-usage-config-label:

設定クラス
---------------------------------------------------

設定クラスとして、org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositoriesアノテーションを指定した設定クラスを作成する。
ベースパッケージにはレポジトリクラスのパッケージを指定すること。また、Bean定義として、AmazonDynamoDBクラスを継承したAmazonDynamoDBClientを定義しておく必要がある。
AmazonDynamoDBClientBuilderにて、DynamoDBを作成したリージョンとDynamoDBのエンドポイントURL(東京リージョンでは、https://dynamodb.ap-northeast-1.amazonaws.com)を指定しておくこと。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.data.dynamodb.config.DynamoDBConfig


   package org.debugroom.sample.spring.data.dynamodb.config;

   import com.amazonaws.client.builder.AwsClientBuilder;
   import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
   import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
   import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
   import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;

   @Configuration
   @EnableDynamoDBRepositories(basePackages = "org.debugroom.sample.spring.data.dynamodb.domain.repository")
   public class DynamoDBConfig {

       @Value("${amazon.dynamodb.region}")
       private String region;
       @Value("${amazon.dynamodb.endpoint}")
       private String endpoint;

       @Bean
       public AmazonDynamoDB amazonDynamoDB(){
           return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new EndpointConfiguration(endpoint, region)).build();
       }
    }

.. warning:: AWSの認証プロバイダでは、 DefaultAWSCredentialsProviderChainがデフォルトで使用され、

   #. 環境変数AWS_ACCESS_KEY_IDとAWS_SECRET_ACCESS_KEY
   #. システムプロパティaws.accessKeyIdとaws.secretKey
   #. ユーザのAWS認証情報ファイル(~/.aws/credential)
   #. AWSインスタンスプロファイルの認証情報

   にある順番で読み込まれて行く。認証キーをアプリケーションに記載して取得するやり方はセキュリティ上望ましくないので、開発環境はユーザのAWS認証情報ファイルから、本番環境はAWSインタンスプロファイルの認証情報から取得する様にしておくこと。

.. _section2-1-1-spring-data-dynamodb-usage-entity-label:

エンティティクラスの作成
---------------------------------------------------

テーブルを表現するアノテーションを付与したエンティティクラスを作成する。サンプルテーブルエンティティを表現するSampleTableクラスには@DynamoTBTableを付与し、テーブル"sample_table"と対応づけるためにtableName属性を設定しておく。
パーティションキーとなるプロパティには@DynamoDBHashKeyを付与し、テーブルカラム定義には、@DynamoDBAttributeアノテーションを設定する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.momain.model.entity.User.java

   package org.debugroom.sample.spring.data.dynamodb.domain.model;

   import java.io.Serializable;

   import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
   import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
   import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Data;
   import lombok.NoArgsConstructor;

   import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

   @AllArgsConstructor
   @NoArgsConstructor
   @Builder
   @Data
   @DynamoDBTable(tableName = "sample_table")
   public class SampleTable implements Serializable {

    @DynamoDBHashKey
    private String sample_partition_key;
    @DynamoDBAttribute
    private String sample_sort_key;

}

.. _section2-1-2-spring-data-DynamoDB-usage-repository-label:

レポジトリクラスの作成
---------------------------------------------------

データベースアクセスを実現するレポジトリインターフェースクラスを作成する。org.springrramework.data.repository.CrudRepositoryを継承したインターフェースクラスを作成し、
型パラメータにエンティティクラス及び、プライマリキーとなる型のクラスを指定する。
サンプルテーブルにアクセスしたい場合、SampleTableRepository(名前は任意でよいが慣例的にEntityNameRepositoryとするケースが多い)というインターフェースを作成し、
型パラメータに上記で作成したSampleTableテーブルクラスと、プライマリーキーであるsample_partition_keyの型であるString型を指定する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.UserRepository.java

   package org.debugroom.sample.spring.data.dynamodb.domain.repository;

   import org.debugroom.sample.spring.data.dynamodb.domain.model.SampleTable;
   import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
   import org.springframework.data.repository.CrudRepository;

   @EnableScan
   public interface SampleRepository extends CrudRepository<SampleTable, String> {
   }

.. note:: データベースアクセスの実装クラスを作成せず、インターフェースのみ作成すればよい。Spring Data JPAと同様、基本的なリレーショナルデータベースへのアクセス実装を、
   Generic DAOパターンの形で提供しており、シンプルなデータベースへのアクセスは特に実装クラスを作成せずとも実現できる。
