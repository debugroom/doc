.. _section1-spring-cloud-function-introduction-label:

Introduction
=====================================================

Spring Cloud Functionは、AWS LambdaやAzureFunction、OpenWiskといったServletless環境下における
ファンクション・関数において、Spring FrameworkのDIコンテナへの組み込み、コンテナ管理下のBeanを利用するためのプロダクトである。
また、各ベンダ・実行環境に依存しない形でファンクションやビジネスロジックを実装できる。

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
AWS Lambda
  * aws-lambda-java-core : 1.1.0
  * aws-lambda-java-events : 2.0.2

[Spring]
Spring Framework Boot 1.5.9.RELEASE
Spring Cloud AWS 1.2.2.RELEASE
Spring Cloud Function 1.0.0.M3

事前準備
-------------------------------------------------------

実際に作成したサンプルは `GitHub <https://github.com/debugroom/sample/tree/develop/sample-spring-cloud-function>`_ を参照のこと。

Mavenプロジェクトは、以下のライブラリを追加しておく。

.. sourcecode:: xml
   :caption: pom.xml
   :linenos:

   <dependencies>
     <!-- Spring Boot, Spring Cloud Function, AWS SDKライブラリの追加。-->
     <!-- ファンクション内でS3へのアクセスなど行う場合は、Spring Cloud AWSのライブラリも追加しておく。-->
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-aws</artifactId>
       <version>${org.springframework.cloud_spring-cloud-starter-aws_version}</version>
     </dependency>
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-function-adapter-aws</artifactId>
       <version>${org.springframework.cloud_spring-cloud-function-adapter-aws_version}</version>
     </dependency>
     <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-function-web</artifactId>
       <version>${org.springframework.cloud_spring-cloud-function-web_version}</version>
     </dependency>
     <dependency>
       <groupId>com.amazonaws</groupId>
       <artifactId>aws-lambda-java-events</artifactId>
       <version>${com.amazonaws_aws-lambda-java-events_version}</version>
     </dependency>
     <dependency>
       <groupId>com.amazonaws</groupId>
       <artifactId>aws-lambda-java-core</artifactId>
       <version>${com.amazonaws_aws-lambda-java-core_version}</version>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-configuration-processor</artifactId>
       <optional>true</optional>
     </dependency>
     <!-- 依存性の注入に＠Injectを用いる。@Autowiredでやる場合は不要。-->
     <dependency>
       <groupId>javax.inject</groupId>
       <artifactId>javax.inject</artifactId>
       <version>${javax.inject_javax.inject_version}</version>
     </dependency>
     </dependencies>

     <!-- Spring Cloud Function 1.0.0.M3を利用するためにspring.ioのMavenレポジトリを指定。-->
     <repositories>
       <repository>
         <id>spring-snapshots</id>
         <name>Spring Snapshots</name>
         <url>https://repo.spring.io/snapshot</url>
         <snapshots>
           <enabled>true</enabled>
         </snapshots>
       </repository>
       <repository>
         <id>spring-milestones</id>
         <name>Spring Milestones</name>
         <url>https://repo.spring.io/milestone</url>
         <snapshots>
           <enabled>false</enabled>
         </snapshots>
       </repository>
     </repositories>

     <pluginRepositories>
       <pluginRepository>
         <id>spring-snapshots</id>
         <name>Spring Snapshots</name>
         <url>https://repo.spring.io/snapshot</url>
         <snapshots>
           <enabled>true</enabled>
         </snapshots>
       </pluginRepository>
       <pluginRepository>
         <id>spring-milestones</id>
         <name>Spring Milestones</name>
         <url>https://repo.spring.io/milestone</url>
         <snapshots>
           <enabled>false</enabled>
         </snapshots>
       </pluginRepository>
     </pluginRepositories>

     <build>
       <plugins>
         <plugin>
           <groupId>org.apache.maven.plugins</groupId>
           <artifactId>maven-deploy-plugin</artifactId>
           <configuration>
             <skip>true</skip>
           </configuration>
         </plugin>
     <!-- Spring アプリケーション起動クラスをメインクラスに指定。-->
         <plugin>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-maven-plugin</artifactId>
           <dependencies>
             <dependency>
               <groupId>org.springframework.boot.experimental</groupId>
               <artifactId>spring-boot-thin-layout</artifactId>
               <version>${wrapper.version}</version>
             </dependency>
           </dependencies>
           <configuration>
             <mainClass>org.debugroom.sample.spring.cloud.function.config.App</mainClass>
           </configuration>
         </plugin>
     <!-- Amazon Lambdaにコードをアップロードする際、依存ライブラリを含めてJARパッケージ化する必要がある。-->
     <!-- mvn packageした際、sharedClassifierNameが付与されたJARファイルができるので、こちらをアップロードする。-->
         <plugin>
           <groupId>org.apache.maven.plugins</groupId>
           <artifactId>maven-shade-plugin</artifactId>
           <configuration>
             <createDependencyReducedPom>false</createDependencyReducedPom>
             <shadedArtifactAttached>true</shadedArtifactAttached>
             <shadedClassifierName>aws</shadedClassifierName>
           </configuration>
         </plugin>
       </plugins>
     </build>
   </project>
