.. include:: ../../../module.txt

.. _section2-spring-data-jpa-usage-label:

Usage
===================================================

実際に作成したサンプルは `GitHub <https://github.com/debugroom/sample/tree/develop/sample-spring-jpa>`_ を参照のこと。

.. note:: 本文は執筆中につき記載が必ずしも正確でない部分が残っている事に注意。

.. _section2-1-spring-data-jpa-usage-simple-access-label:

シンプルなデータベースアクセス
---------------------------------------------------

基本的なデータベースアクセスについて、Spring Bootのコンフィグレーションクラスの設定を踏まえて記述する。ユースケースとして以下を考える。

* 全てのユーザを検索する。
* 全ての住所を検索する。
* 全てのメールアドレスを検索する。
* 全てのグループを検索する。
* 特定のユーザを検索する。
* 特定のユーザのアドレスを検索する。
* 特定のユーザがもつEmailアドレスを検索する。
* 指定したグループ名を元にグループを検索する。

.. _section2-1-1-spring-data-jpa-usage-simple-access-entity-label:

エンティティクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

テーブルを表現するアノテーションを付与したエンティティクラスを作成する。JPA標準のjavax.persistenceパッケージの、代表的なアノテーションの概要は以下の通りである。

.. list-table:: javax.persistenceアノテーションの概要
   :header-rows: 1
   :widths: 30,20,50

   * - アノテーション
     - 設定
     - 概要
   * - @Entity
     - クラス
     - エンティティとして表現するクラスに付与する。
   * - @Table
     - クラス
     - エンティティとマッピングする物理テーブル名を指定する。
   * - @Embeddable
     - クラス
     - 主に複合IDとして表現する組み込みクラスに付与する。
   * - @Id
     - フィールド |br| メソッド
     - エンティティクラスのプライマリキーのフィールドに指定する。
   * - @EmbeddedId
     - フィールド |br| メソッド
     - エンティティクラスの複合IDプライマリーキーのフィールドに指定する。
   * - @Column
     - フィールド |br| メソッド
     - フィールドとデータベースのカラム名のマッピングを定義する。
   * - @JoinColumns
     - フィールド |br| メソッド
     - @JoinColumnを複数同時に記述する場合に利用する。
   * - @JoinColumn
     - フィールド |br| メソッド
     - 結合のための外部キーカラム及び外部参照制約オプションを指定する。
   * - @PraimaryKeyJoinColums
     - フィールド |br| メソッド
     - @PrimaryKeyJoinColumnを複数同時に記述する場合に利用する。
   * - @PraimaryKeyJoinColumn
     - フィールド |br| メソッド
     - 他のテーブルと結合する場合に、外部キーとして扱われるカラム
   * - @OneToOne
     - フィールド |br| メソッド
     - OneToOneリレーションシップであることを示す場合に付与する。
   * - @OneToMany
     - フィールド |br| メソッド
     - OneToManyリレーションシップであることを示す場合に付与する。
   * - @ManyToOne
     - フィールド |br| メソッド
     - ManyToOneリレーションシップであることを示す場合に付与する。
   * - @AttributeOverrides
     - フィールド |br| メソッド
     - @AttributeOverrideを複数記述する場合に利用する。
   * - @AttributeOverride
     - フィールド |br| メソッド
     - マッピング情報をオーバーライドする。
   * - @Transient
     - フィールド |br| メソッド
     - 永続化しないエンティティクラス、マップドスーパークラス、または組み込みクラスのフィールドであることを示す。
   * - @Temporal
     - フィールド |br| メソッド
     - 時刻を示す型(java.util.Date及び、java.util.Calendar)を持つフィールドに付与する。
   * - @GeneratedValue
     - フィールド |br| メソッド
     - プライマリーキーカラムにユニークな値を自動生成、付与する方法を指定する。
   * - @NamedQuery
     - クラス
     - JPQLの名前付きクエリを指定する。


ユーザエンティティを表現するUserクラスには@Entityを付与し、テーブル"usr"と対応づけるために@Tableアノテーションを設定する。各プロパティには@Columnを付与し、テーブルカラム定義に従ってuniqueやlengthなどのオプションや、@Temporalアノテーション、@Transientなど設定する。Userエンティティは、1対1の関連を持つAddressエンティティ、1対Nの関連を持つEmailエンティティ、Affilicationエンティティを関連実体としてN対Nの関連を持つGroupエンティティと関連をもつ。こうした関連エンティティには各々@OneToOneアノテーション、@OneToManyアノテーションを付与する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.momain.model.entity.User.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;
   import java.sql.Timestamp;
   import java.util.Set;

   import javax.persistence.Column;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.Id;
   import javax.persistence.OneToMany;
   import javax.persistence.OneToOne;
   import javax.persistence.Table;
   import javax.persistence.Temporal;
   import javax.persistence.TemporalType;
   import javax.persistence.UniqueConstraint;
   import javax.persistence.NamedQuery;

   @Entity
   @Table(name = "usr", schema = "public", 
       uniqueConstraints = @UniqueConstraint(columnNames = "login_id") )
   @NamedQuery(name="User.findAll", query="SELECT u FROM User u")
   public class User implements Serializable {

       private static final long serialVersionUID = -3097005474180552877L;

       @Id
       @Column(name = "user_id", unique = true, nullable = false, length = 8)
       private String userId;

       @Column(name = "last_updated_date")
       private Timestamp lastUpdatedDate;

       @Column(name = "login_id", unique = true, length = 64)
       private String loginId;

       @Column(name = "user_name", length = 50)
       private String userName;

       @Column(name = "ver")
       private Integer ver;

       @OneToOne(mappedBy="usr")
       private Address address;

       @OneToMany(fetch = FetchType.LAZY, mappedBy = "usr")
       private Set<Affiliation> affiliations;

       @OneToMany(mappedBy="usr")
       private Set<Email> emails;

       public User() {
       }

       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       public Timestamp getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       public String getLoginId() {
           return this.loginId;
       }

       public void setLoginId(String loginId) {
           this.loginId = loginId;
       }

       public String getUserName() {
           return this.userName;
       }

       public void setUserName(String userName) {
           this.userName = userName;
       }

       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       public Address getAddress() {
           return this.address;
       }

       public void setAddress(Address address) {
           this.address = address;
       }

       public Set<Affiliation> getAffiliations() {
           return this.affiliations;
       }

       public void setAffiliations(Set<Affiliation> affiliations) {
           this.affiliations = affiliations;
       }

       public Affiliation addAffiliation(Affiliation affiliation) {
           getAffiliations().add(affiliation);
           affiliation.setUsr(this);

           return affiliation;
       }

       public Affiliation removeAffiliation(Affiliation affiliation) {
           getAffiliations().remove(affiliation);
           affiliation.setUsr(null);

           return affiliation;
       }

       public Set<Email> getEmails() {
           return this.emails;
       }

       public void setEmails(Set<Email> emails) {
           this.emails = emails;
       }

       public Email addEmail(Email email) {
           getEmails().add(email);
           email.setUsr(this);

           return email;
       }

       public Email removeEmail(Email email) {
           getEmails().remove(email);
           email.setUsr(null);

           return email;
       }

   }

.. note:: 関連のコレクションのデータ型にjava.util.Setを使用しているのは、JPAの仕様上、エンティティのキーとなるプロパティの重複を許さないためである。

グループエンティティを表現するGroupクラスには@Entityを付与し、テーブル"grp"と対応づけるために@Tableアノテーションを設定する。各プロパティには@Columnを付与し、テーブルカラム定義に従ってuniqueやlengthなどのオプションや、@Temporalアノテーション、@Transientなど設定する。Groupエンティティは、Affilicationエンティティを関連実体として、N対NでUserエンティティと関連をもつため、Affiliationのコレクションに@OneToManyアノテーションを付与する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Group.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;
   import java.sql.Timestamp;
   import java.util.Set;

   import javax.persistence.Column;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.Id;
   import javax.persistence.OneToMany;
   import javax.persistence.Table;
   import javax.persistence.NamedQuery;

   @Entity
   @Table(name="grp")
   @NamedQuery(name="Group.findAll", query="SELECT g FROM Group g")
   public class Group implements Serializable {

       private static final long serialVersionUID = -5846174125268574714L;

       @Id
       @Column(name = "group_id", unique = true, nullable = false, length = 10)
       private String groupId;

       @Column(name = "group_name", length = 50)
       private String groupName;

       @Column(name="last_updated_date")
       private Timestamp lastUpdatedDate;

       @Column(name = "ver")
       private Integer ver;

       @OneToMany(fetch = FetchType.LAZY, mappedBy = "grp")
       private Set<Affiliation> affiliations;

       public Group() {
       }

       public String getGroupId() {
           return this.groupId;
       }

       public void setGroupId(String groupId) {
           this.groupId = groupId;
       }

       public String getGroupName() {
           return this.groupName;
       }

       public void setGroupName(String groupName) {
           this.groupName = groupName;
       }

       public Timestamp getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       public Set<Affiliation> getAffiliations() {
           return this.affiliations;
       }

       public void setAffiliations(Set<Affiliation> affiliations) {
           this.affiliations = affiliations;
       }

       public Affiliation addAffiliation(Affiliation affiliation) {
           getAffiliations().add(affiliation);
           affiliation.setGrp(this);

           return affiliation;
       }

       public Affiliation removeAffiliation(Affiliation affiliation) {
           getAffiliations().remove(affiliation);
           affiliation.setGrp(null);

           return affiliation;
       }

   }

住所エンティティを表現するAddressクラスには@Entityを付与し、テーブル"address"と対応づけるために@Tableアノテーションを設定する。各プロパティには@Columnを付与し、テーブルカラム定義に従ってuniqueやlengthなどのオプションや、@Temporalアノテーション、@Transientなど設定する。Addressエンティティは、1対1でUserエンティティと関連をもつため、@OneToOneアノテーション及び@PrimaryKeyJoinColumnアノテーションをUserプロパティに設定しておく。

.. sourcecode:: java
   :caption: org.degugroom.sample.spring.jpa.domain.entity.Address

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;
   import java.sql.Timestamp;
   import javax.persistence.Column;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.GeneratedValue;
   import javax.persistence.Id;
   import javax.persistence.OneToOne;
   import javax.persistence.PrimaryKeyJoinColumn;
   import javax.persistence.Table;
   import javax.persistence.NamedQuery;

   @Entity
   @Table(name = "address", schema = "public")
   @NamedQuery(name="Address.findAll", query="SELECT a FROM Address a")
   public class Address implements Serializable {

       private static final long serialVersionUID = 2339974563902001678L;

       @Id
       @GeneratedValue(generator = "generator")
       @Column(name = "user_id", unique = true, nullable = false, length = 8)
       private String userId;

       @Column(name = "address")
       private String address;

       @Column(name="last_updated_date")
       private Timestamp lastUpdatedDate;

       @Column(name = "ver")
       private Integer ver;

       @Column(name = "zip_cd", length = 8)
       private String zipCd;

       @OneToOne(optional=false, fetch = FetchType.LAZY)
       @PrimaryKeyJoinColumn
       private User usr;

       public Address() {
       }

       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       public String getAddress() {
           return this.address;
       }

       public void setAddress(String address) {
           this.address = address;
       }

       public Timestamp getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       public String getZipCd() {
           return this.zipCd;
       }

       public void setZipCd(String zipCd) {
           this.zipCd = zipCd;
       }

       public User getUsr() {
           return this.usr;
       }

       public void setUsr(User usr) {
           this.usr = usr;
       }

   }

メールエンティティを表現するEmailクラスには@Entityを付与し、テーブル"email"と対応づけるために@Tableアノテーションを設定する。Emailでは主キーが、ユーザID及びメールIDの複合主キーとなるため、複合主キーを示すEmailPKクラスを別途作成するが、その場合、@EmbeddedIdアノテーションを設定する形になる。各プロパティには@Columnを付与し、テーブルカラム定義に従ってuniqueやlengthなどのオプションや、@Temporalアノテーション、@Transientなど設定する。Emailエンティティは、N対1でUserエンティティと関連をもつため、@ManyToOne及び、@JoinColumnアノテーションをUserプロパティに設定しておく。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Email.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;
   import java.sql.Timestamp;

   import javax.persistence.AttributeOverride;
   import javax.persistence.AttributeOverrides;
   import javax.persistence.Column;
   import javax.persistence.EmbeddedId;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.JoinColumn;
   import javax.persistence.ManyToOne;
   import javax.persistence.Table;
   import javax.persistence.NamedQuery;

   @Entity
   @Table(name = "email", schema = "public")
   @NamedQuery(name="Email.findAll", query="SELECT e FROM Email e")
   public class Email implements Serializable {

       private static final long serialVersionUID = 7714950945678285107L;

       @EmbeddedId
       @AttributeOverrides({
           @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false, length = 8) ),
           @AttributeOverride(name = "emailId", column = @Column(name = "email_id", nullable = false) ) })
       private EmailPK id;

       @Column(name = "email")
       private String email;

       @Column(name="last_updated_date")
       private Timestamp lastUpdatedDate;

       @Column(name = "ver")
       private Integer ver;

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
       private User usr;

       public Email() {
       }

       public EmailPK getId() {
           return this.id;
       }

       public void setId(EmailPK id) {
           this.id = id;
       }

       public String getEmail() {
           return this.email;
       }

       public void setEmail(String email) {
           this.email = email;
       }

       public Timestamp getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       public User getUsr() {
           return this.usr;
       }

       public void setUsr(User usr) {
           this.usr = usr;
       }

   }

Emailの複合主キークラスであるEmailPKクラスには埋め込みクラスである事を示す@Embeddableアノテーションを設定する。@ColumnにはEmailテーブルの複合主キーとなる、user_id及びemail_idを設定する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.EmailPK.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;

   import javax.persistence.Column;
   import javax.persistence.Embeddable;

   @Embeddable
   public class EmailPK implements Serializable {

       private static final long serialVersionUID = -3546965750394227468L;

       @Column(name="user_id", insertable=false, updatable=false)
       private String userId;

       @Column(name="email_id")
       private String emailId;

       public EmailPK() {
       }

       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       public String getEmailId() {
           return this.emailId;
       }

       public void setEmailId(String emailId) {
           this.emailId = emailId;
       }

       public boolean equals(Object other) {
           if (this == other) {
               return true;
           }
           if (!(other instanceof EmailPK)) {
               return false;
           }
           EmailPK castOther = (EmailPK)other;
           return  this.userId.equals(castOther.userId)
                     && this.emailId.equals(castOther.emailId);
       }

       public int hashCode() {
           final int prime = 31;
           int hash = 17;
           hash = hash * prime + this.userId.hashCode();
           hash = hash * prime + this.emailId.hashCode();
    
           return hash;
       }
   }


.. note::  equals()メソッドとhashcode()をオーバーライドしているのは、これらの主キーの値が同じ場合のみ同一のオブジェクトと見なす必要があるためである。オーバーライドしなければ、たとえ、user_id及び、emial_idがそれぞれ一致していたとしても、インスタンスが異なれば、同一と見なされない。


所属エンティティを表現するAffiliationクラスには@Entityを付与し、テーブル"affiliation"と対応づけるために@Tableアノテーションを設定する。Affiliationでは主キーが、ユーザID及びグループIDの複合主キーとなるため、複合主キーを示すAffiliationPKクラスを別途作成するが、その場合、@EmbeddedIdアノテーションを設定する形になる。各プロパティには@Columnを付与し、テーブルカラム定義に従ってuniqueやlengthなどのオプションや、@Temporalアノテーション、@Transientなど設定する。Affiliationエンティティは、ユーザ及びグループエンティティの関連実体であり、N対Nのユーザとグループの関連を各々1対１にする役割を持つ。@ManyToOne及び、@JoinColumnアノテーションをUser、Groupプロパティに設定しておく。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Affiliation.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;
   import java.sql.Timestamp;
   import javax.persistence.AttributeOverride;
   import javax.persistence.AttributeOverrides;
   import javax.persistence.Column;
   import javax.persistence.EmbeddedId;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.JoinColumn;
   import javax.persistence.ManyToOne;
   import javax.persistence.Table;
   import javax.persistence.NamedQuery;

   @Entity
   @Table(name = "affiliation", schema = "public")
   @NamedQuery(name="Affiliation.findAll", query="SELECT a FROM Affiliation a")
   public class Affiliation implements Serializable {

       private static final long serialVersionUID = 3832620537736917777L;

       @EmbeddedId
       @AttributeOverrides({
           @AttributeOverride(name = "groupId", column = @Column(name = "group_id", nullable = false, length = 10) ),
           @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false, length = 8) ) })
       private AffiliationPK id;

       @Column(name="last_updated_date")
       private Timestamp lastUpdatedDate;

       @Column(name = "ver")
       private Integer ver;

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
       private Group grp;

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
       private User usr;

       public Affiliation() {
       }

       public AffiliationPK getId() {
           return this.id;
       }

       public void setId(AffiliationPK id) {
           this.id = id;
       }

       public Timestamp getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       public Group getGrp() {
           return this.grp;
       }

       public void setGrp(Group grp) {
           this.grp = grp;
       }

       public User getUsr() {
           return this.usr;
       }

       public void setUsr(User usr) {
           this.usr = usr;
       }
   }

Affiliationの複合主キークラスであるAffiliationPKクラスには埋め込みクラスである事を示す@Embeddableアノテーションを設定する。@ColumnにはAffiliationテーブルの複合主キーとなる、user_id及びgroup_idを設定する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.AffiliationId.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.io.Serializable;

   import javax.persistence.Column;
   import javax.persistence.Embeddable;

   @Embeddable
   public class AffiliationPK implements Serializable {

       private static final long serialVersionUID = -1873418684097700980L;

       @Column(name="group_id", insertable=false, updatable=false, length=10)
       private String groupId;

       @Column(name="user_id", insertable=false, updatable=false, length=8)
       private String userId;

       public AffiliationPK() {
       }

       public String getGroupId() {
           return this.groupId;
       }

       public void setGroupId(String groupId) {
           this.groupId = groupId;
       }
       
       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       public boolean equals(Object other) {
           if (this == other) {
               return true;
           }
           if (!(other instanceof AffiliationPK)) {
               return false;
           }
           AffiliationPK castOther = (AffiliationPK)other;
           return this.groupId.equals(castOther.groupId)
                    && this.userId.equals(castOther.userId);
       }

       public int hashCode() {
           final int prime = 31;
           int hash = 17;
           hash = hash * prime + this.groupId.hashCode();
           hash = hash * prime + this.userId.hashCode();
    
           return hash;
       }
   }


また、クラスパス配下にMETA-INF/を作成し、persistence.xmlを以下の通り作成し、エンティティクラスを指定しておく。

.. sourcecode:: xml
   :caption: src/main/resource/META-INF/persistence.xml

   <?xml version="1.0" encoding="UTF-8"?>
   <persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" 
                          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                 xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence 
                                     http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">

     <persistence-unit name="sample-spring-jpa">
       <provider>org.hibernate.ejb.HibernatePersistence</provider>
       <class>org.debugroom.sample.spring.jpa.domain.entity.Address</class>
       <class>org.debugroom.sample.spring.jpa.domain.entity.Affiliation</class>
       <class>org.debugroom.sample.spring.jpa.domain.entity.Email</class>
       <class>org.debugroom.sample.spring.jpa.domain.entity.Group</class>
       <class>org.debugroom.sample.spring.jpa.domain.entity.User</class>
       <class>org.debugroom.sample.spring.jpa.domain.entity.AffiliationPK</class>
       <class>org.debugroom.sample.spring.jpa.domain.entity.EmailPK</class>
     </persistence-unit>
   </persistence>


.. _section2-1-2-spring-data-jpa-usage-simple-access-repository-label:

レポジトリクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

データベースアクセスを実現するレポジトリインターフェースクラスを作成する。org.springrramework.data.jpa.repository.JpaRepositoryを継承したインターフェースクラスを作成し、型パラメータにエンティティクラス及び、プライマリキーとなる型のクラスを指定する。ユーザテーブルにアクセスしたい場合、UserRepository(名前は任意でよいが慣例的にEntityNameRepositoryとするケースが多い)というインターフェースを作成し、型パラメータに上記で作成したUserエンティティクラスと、プライマリーキーであるuser_idの型であるString型を指定する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.UserRepository.java

   package org.debugroom.sample.spring.jpa.domain.repository;

   import org.springframework.data.jpa.repository.JpaRepository;

   import org.debugroom.sample.spring.jpa.domain.entity.User;

   public interface UserRepository extends JpaRepository<User, String>{
   }


.. note:: データベースアクセスの実装クラスを作成せず、インターフェースのみ作成すればよい。Spring Data JPAでは、基本的なリレーショナルデータベースへのアクセス実装を、Generic DAOパターンの形で提供しており、シンプルなデータベースへのアクセスは特に実装クラスを作成せずとも実現できる。

同様に、Address、Group、Emailにおいてレポジトリインターフェースを作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.AddressRepository.java

   package org.debugroom.sample.spring.jpa.domain.repository;

   import org.springframework.data.jpa.repository.JpaRepository;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;

   public interface AddressRepository extends JpaRepository<Address, String>{
   }

GroupRepositoryでは、指定したグループ名を元にグループを検索するユースケースのために、インターフェースにfindByGroupName(String groupName)メソッドを追加する。Spring Data JPAでは、インターフェースのメソッド名をキーワードを元にした命名規約により、クエリを動的に組み立てる事が可能であり、ここでは、グループ名をキーにグループを検索するfindByGroupName()メソッドを追加する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.GroupRepository.java

   package org.debugroom.sample.spring.jpa.domain.repository;

   import org.springframework.data.jpa.repository.JpaRepository;

   import org.debugroom.sample.spring.jpa.domain.entity.Group;

   public interface GroupRepository extends JpaRepository<Group, String>{
  
       public Group findByGroupName(String groupName);
  
   }

.. note:: クエリのキーワードについては、`Springの公式ドキュメント Appendix C: Repository query keyword <http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords>`_ を参考にすること。

EmailRepositoryでは、指定したユーザIDを元にそのユーザが持つメールアドレスを検索するユースケースのために、インターフェースにfindByIdUserId(String userId)を追加する。


.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.EmailRepository.java

   package org.debugroom.sample.spring.jpa.domain.repository;

   import java.util.List;
   import org.springframework.data.jpa.repository.JpaRepository;

   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.EmailPK;

   public interface EmailRepository extends JpaRepository<Email, EmailPK>{
  
       public List<Email> findByIdUserId(String userId);

   }


.. note:: パラメータがネストする場合は、エンティティの変数名をキャメルケースで指定する。上記のEmailの例では、EmailPKクラスの中にuserIdがあり、Emailクラスの中でEmailPKは変数名を"id"でフィールド宣言しているため、findBy以降のパラメータ部の名称は"id"と"userId"をキャメルケースでつなげた"IdUserId"となり、結局メソッドはfindByIdUserId(String userId)となる。


.. _section2-1-3-spring-data-jpa-usage-simple-access-service-label:

サービスクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

ユースケースに応じて、インターフェースを作成する。

* 全てのユーザを検索する。→ getUsers()
* 全ての住所を検索する。→ getAddressList()
* 全てのメールアドレスを検索する。→ getEmails()
* 全てのグループを検索する。→ getGroups()
* 特定のユーザを検索する。→ getUser(String userId)
* 特定のユーザのアドレスを検索する。→ getAddress(User user)
* 特定のユーザがもつEmailアドレスを検索する。→ getEmails(User user)
* 指定したグループ名を元にグループを検索する。→ getGroup(String groupName)

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.SimpleAccessService.java

   package org.debugroom.sample.spring.jpa.domain.service;

   import java.util.List;

   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.Group;

   public interface SimpleAccessService {

       public List<User> getUsers();
  
       public List<Address> getAddressList();
  
       public List<Email> getEmails();
  
       public List<Group> getGroups();
  
       public User getUser(String userId);
  
       public Address getAddress(User user);
  
       public List<Email> getEmails(User user);
  
       public Group getGroup(String groupName);

   }

サービスの実装クラスでは、それぞれのユースケースを実現するためのレポジトリを各々呼び出し、結果を表示するサービスとして以下の通り実装する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.SimpleAccessServiceImpl.java
   
   package org.debugroom.sample.spring.jpa.domain.service;

   import java.util.List;

   import org.springframework.beans.factory.annotation.Autowired;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.Group;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.repository.UserRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.AddressRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.EmailRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.GroupRepository;

   import lombok.extern.slf4j.Slf4j;

   @Slf4j
   public class SimpleAccessServiceImpl implements SimpleAccessService{

       @Autowired
       UserRepository userRepository;
  
       @Autowired
       AddressRepository addressRepository;
  
       @Autowired
       EmailRepository emailRepository;
  
       @Autowired
       GroupRepository groupRepository;
  
       @Override
       public List<User> getUsers() {
           List<User> users = userRepository.findAll();
           log.info(this.getClass().getName() + " : users ");
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                         + user.getUserId() + ", " + user.getUserName() + "}");
           }
           return users;
       }

       @Override
       public List<Address> getAddressList() {
           List<Address> addressList = addressRepository.findAll();
           log.info(this.getClass().getName() + " : addressList ");
           for(Address address : addressList){
               log.info(this.getClass().getName() + "            - {"
                        + address.getUserId() + ", " + address.getAddress() + "}");
           }
           return addressList;
       }

       @Override
       public List<Email> getEmails() {
           List<Email> emails = emailRepository.findAll();
           log.info(this.getClass().getName() + " : emails ");
           for(Email email : emails){
               log.info(this.getClass().getName() + "            - {"
                        + email.getId().getUserId() + ", " + email.getId().getEmailId() 
                        + "," + email.getEmail() + "}");
           }
           return emails;
       }

       @Override
       public List<Group> getGroups() {
           List<Group> groups = groupRepository.findAll();
           log.info(this.getClass().getName() + " : groups ");
           for(Group group : groups){
               log.info(this.getClass().getName() + "            - {"
                        + group.getGroupId() + ", " + group.getGroupName() + "}");
           }
           return groups;
       }

       @Override
       public User getUser(String userId) {
           User user = userRepository.findOne(userId);
           log.info(this.getClass().getName() + " : userId " + userId);
           log.info(this.getClass().getName() + "            - {"
                    + user.getUserName() + ", " + user.getLoginId() + "}");
           return user;
       }

       @Override
       public Address getAddress(User user) {
           Address address = addressRepository.findOne(user.getUserId());
           log.info(this.getClass().getName() + " : address of " + user.getUserId());
           log.info(this.getClass().getName() + "            - {"
                    + address.getZipCd() + ", " + address.getAddress() + "}");
           return address;
       }

       @Override
       public List<Email> getEmails(User user) {
           List<Email> emails = emailRepository.findByIdUserId(user.getUserId());
           log.info(this.getClass().getName() + " : emails of userId : " + user.getUserId());
           for(Email email : emails){
           log.info(this.getClass().getName() + "            - {"
                    + email.getId().getUserId() + ", " + email.getId().getEmailId() 
                    + "," + email.getEmail() + "}");
           }
           return emails;
       }

       @Override
       public Group getGroup(String groupName) {
           Group group = groupRepository.findByGroupName(groupName);
           log.info(this.getClass().getName() + " : Group of " + groupName);
           log.info(this.getClass().getName() + "            - {"
                    + group.getGroupId() + ", " + group.getGroupName() + "}");
           return group;
       }
   }

.. _section2-1-5-spring-data-jpa-usage-simple-access-configuration-label:

コンフィグレーションクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

`Spring Bootのサンプル <http://debugroom.github.io/doc/java/spring/springboot/usage.html#jpa>`_ 同様、HSQLを使用してサービスを簡易的に実行するスタンドアロンアプリケーションを作成する。プロファイルアノテーションを使用し、開発環境向けの設定クラスは以下の通り作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.config.infra.DbConfigDev.java
   
   package org.debugroom.sample.spring.jpa.config.infra;

   import javax.sql.DataSource;

   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.annotation.Profile;
   import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
   import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

   @Configuration
   @Profile("dev")
   public class DbConfigDev {

       @Bean
       public DataSource dataSource(){
           return (new EmbeddedDatabaseBuilder())
                        .setType(EmbeddedDatabaseType.HSQL) 
                        .addScript("classpath:ddl/sample.sql")
                        .addScript("classpath:ddl/data.sql")
                        .build();
       }
   }

デフォルトで使用するプロファイルは、devに設定しておくため、application.ymlをクラスパス配下に作成する。

.. sourcecode:: properties
   :caption: src/main/resources/application.yml

   spring:
       profiles:
           active: dev


商用、開発共用となるデータベース関連設定クラスを作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.config.infra.SampleAppInfra.java
   
   package org.debugroom.sample.spring.jpa.config.infra;

   import java.util.Properties;

   import javax.sql.DataSource;

   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
   import org.springframework.orm.jpa.JpaVendorAdapter;
   import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
   import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
   import org.springframework.transaction.annotation.EnableTransactionManagement;

   @Configuration
   @EnableTransactionManagement
   @EnableJpaRepositories(basePackages="org.debugroom.sample.spring.jpa.domain.repository")
   public class SampleAppInfra {
  
       @Autowired
       DataSource dataSource;
  
       @Bean
       public LocalContainerEntityManagerFactoryBean entityManagerFactory(){

           JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    
           Properties properties = new Properties();
           properties.setProperty("hibernate.show_sql", "true");
           properties.setProperty("hibernate.format_sql", "true");
    
           LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
           emfb.setPackagesToScan("org.debugroom.sample.spring.jpa.domain.entity");
           emfb.setJpaProperties(properties);
           emfb.setJpaVendorAdapter(adapter);  
           emfb.setDataSource(dataSource);
    
           return emfb;
       }
   }

アプリケーションの設定、実行を行うコンフィグレーションクラスを作成する。@ComponentScanで上記のSampleAppInfra設定クラスを詠み込むようパッケージを指定しておく。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.config.SampleApp.java
   
   package org.debugroom.sample.spring.jpa.config;

   import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
   import org.springframework.boot.builder.SpringApplicationBuilder;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;

   import org.springframework.context.ConfigurableApplicationContext;

   import java.util.List;

   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.service.SimpleAccessService;
   import org.debugroom.sample.spring.jpa.domain.service.SimpleAccessServiceImpl;

   @ComponentScan("org.debugroom.sample.spring.jpa.config.infra")
   @Configuration
   @EnableAutoConfiguration
   public class SampleApp {

       public static void main(String[] args){
           ConfigurableApplicationContext context = new SpringApplicationBuilder(
                                                           SampleApp.class).web(false).run(args);

           SimpleAccessService simpleAccessService = context.getBean(SimpleAccessService.class);
    
           List<User> users = simpleAccessService.getUsers();
           simpleAccessService.getAddressList();
           simpleAccessService.getEmails();
           simpleAccessService.getGroups();
           simpleAccessService.getUser(users.get(0).getUserId());
           simpleAccessService.getAddress(users.get(0));
           simpleAccessService.getEmails(users.get(0));
           simpleAccessService.getGroup("org.debugroom");
    
       }

       @Bean SimpleAccessService sampleService(){
           return new SimpleAccessServiceImpl();
       }

   }


.. _section2-1-6-spring-data-jpa-usage-simple-access-result-label:

アプリケーション実行の結果
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

* 全てのユーザを検索する。

simpleAccessService.getUsers()実行の結果、userRepository.findAll()が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql
   
   select
        user0_.user_id as user_id1_3_,
        user0_.last_updated_date as last_upd2_3_,
        user0_.login_id as login_id3_3_,
        user0_.user_name as user_nam4_3_,
        user0_.ver as ver5_3_ 
    from
        public.usr user0_

   select
        address0_.user_id as user_id1_0_0_,
        address0_.address as address2_0_0_,
        address0_.last_updated_date as last_upd3_0_0_,
        address0_.ver as ver4_0_0_,
        address0_.zip_cd as zip_cd5_0_0_ 
    from
        public.address address0_ 
    where
        address0_.user_id=?

    select
        address0_.user_id as user_id1_0_0_,
        address0_.address as address2_0_0_,
        address0_.last_updated_date as last_upd3_0_0_,
        address0_.ver as ver4_0_0_,
        address0_.zip_cd as zip_cd5_0_0_ 
    from
        public.address address0_ 
    where
        address0_.user_id=?

    select
        address0_.user_id as user_id1_0_0_,
        address0_.address as address2_0_0_,
        address0_.last_updated_date as last_upd3_0_0_,
        address0_.ver as ver4_0_0_,
        address0_.zip_cd as zip_cd5_0_0_ 
    from
        public.address address0_ 
    where
        address0_.user_id=?

.. note:: 上記でAddressテーブル側にもSQLが複数発行されている(N+1)。OneToOneアノテーションが付与されたAddressオブジェクトにデフォルトでFetchType.Eagerが設定されているためである。Addressオブジェクトを使用しない場合は、不要なSQLの発行を避けるために、optional=false、fetch=FetchType.Lazyを指定する。

.. note:: OneToOneの関連でメインとなるデータを更新・削除した際、関連先のデータを更新・削除する場合は、cascade属性を変更する。

.. sourcecode:: java
   :caption: @OneToOneアノテーションの設定例

   @OneToOne(mappedBy="usr", optional=false, fetch= FetchType.LAZY,
             cascade= CascadeType.ALL)
   private Address address;

* 全ての住所を検索する。

simpleAccessService.getAddressList()実行の結果、addressRepository.findAll()が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql
   
   select
        address0_.user_id as user_id1_0_,
        address0_.address as address2_0_,
        address0_.last_updated_date as last_upd3_0_,
        address0_.ver as ver4_0_,
        address0_.zip_cd as zip_cd5_0_ 
    from
        public.address address0_

* 全てのメールアドレスを検索する。

simpleAccessService.getEmails()実行の結果、emailRepository.findAll()が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       email0_.email_id as email_id1_2_,
       email0_.user_id as user_id2_2_,
       email0_.email as email3_2_,
       email0_.last_updated_date as last_upd4_2_,
       email0_.ver as ver5_2_ 
   from
       public.email email0_

* 全てのグループを検索する。

simpleAccessService.getGroups()実行の結果、groupRepository.findAll()が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       group0_.group_id as group_id1_4_,
       group0_.group_name as group_na2_4_,
       group0_.last_updated_date as last_upd3_4_,
       group0_.ver as ver4_4_ 
   from
       grp group0_

* 特定のユーザを検索する。

simpleAccessService.getUser(String userId)実行の結果、userRepository.findOne(String userId)が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

* 特定のユーザのアドレスを検索する。

simpleAccessService.getAddress(User user)実行の結果、addressRepository.findOne(String userId)が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       address0_.user_id as user_id1_0_0_,
       address0_.address as address2_0_0_,
       address0_.last_updated_date as last_upd3_0_0_,
       address0_.ver as ver4_0_0_,
       address0_.zip_cd as zip_cd5_0_0_ 
   from
       public.address address0_ 
   where
       address0_.user_id=?

* 特定のユーザがもつEmailアドレスを検索する。

simpleAccessService.getEmails(User user)実行の結果、emailRepository.findByIdUserId(String userId)が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       email0_.email_id as email_id1_2_,
       email0_.user_id as user_id2_2_,
       email0_.email as email3_2_,
       email0_.last_updated_date as last_upd4_2_,
       email0_.ver as ver5_2_ 
   from
       public.email email0_ 
   where
       email0_.user_id=?

* 指定したグループ名を元にグループを検索する。

simpleAccessService.getGroup(String groupName)実行の結果、groupRepository.findByGroupName(String groupName)が呼ばれ、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       group0_.group_id as group_id1_4_,
       group0_.group_name as group_na2_4_,
       group0_.last_updated_date as last_upd3_4_,
       group0_.ver as ver4_4_ 
   from
       grp group0_ 
   where
       group0_.group_name=?   


.. _section2-2-spring-data-jpa-usage-one-to-one-label:

1対1の関連テーブルにおけるデータ操作
---------------------------------------------------

1対1の関連テーブル(本サンプルでは、ユーザと住所)のデータ操作に関して以下のようなユースケースを考える。

* 指定されたユーザの住所を取得する。
* 特定の郵便番号を持つユーザ一覧を取得する。
* 特定の郵便番号を持たないユーザ一覧を取得する
* 指定されたユーザの住所を追加する。
* 指定されたユーザの住所を更新する。
* 指定されたユーザの住所を削除する。
* 指定されたユーザの情報を住所を含めて削除する。

基本的には、:ref:`前章、シンプルなデータアクセス<section2-1-spring-data-jpa-usage-simple-access-label>` で作成したエンティティクラス、及びレポジトリクラスはそのまま流用する。

.. _section2-2-1-spring-data-jpa-usage-one-to-one-service-label:

サービスインターフェースの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

* 指定されたユーザの住所を取得する。→ getAddress(User user)
* 特定の郵便番号を持つユーザ一覧を取得する。→ getUsersWith(String zipCd)
* 特定の郵便番号を持たないユーザ一覧を取得する → getUsersWithout(String zipCd)
* 指定されたユーザの住所を追加する。 → addAddress(Address address)
* 指定されたユーザの住所を更新する。 → updateAddress(String userId, Address address)
* 指定されたユーザの住所を削除する。 → deleteAddress(String userId)
* 指定されたユーザの情報を住所を含めて削除する。 → deleteUser(String userId)


.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleService.java

   package org.debugroom.sample.spring.jpa.domain.service;

   import java.util.List;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.User;

   public interface OneToOneSampleService {

       public Address getAddress(User user);
  
       public List<User> getUsersWith(String zipCd);
  
       public List<User> getUsersWithout(String zipCd);
  
       public User addAddress(Address address);

       public void updateAddress(String userId, Address address);
  
       public void deleteAddress(String userId);
  
       public void deleteUser(String userId);
  
   }

.. _section2-2-2-spring-data-jpa-usage-one-to-one-configuration-label:

コンフィグレーションクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

データベースの環境の設定は、:ref:`前章 シンプルなデータアクセス時の設定<section2-1-5-spring-data-jpa-usage-simple-access-configuration-label>` を流用し、サービスを実行する設定ファイルクラスを新規作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.config.OneToOneSampleApp.java

   package org.debugroom.sample.spring.jpa.config;

   import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
   import org.springframework.boot.builder.SpringApplicationBuilder;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.ConfigurableApplicationContext;
   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleService;
   import org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl;

   @ComponentScan("org.debugroom.sample.spring.jpa.config.infra")
   @Configuration
   @EnableAutoConfiguration
   public class OneToOneSampleApp {

       public static void main(String[] args){
           ConfigurableApplicationContext context = new SpringApplicationBuilder(
                                                         OneToOneSampleApp.class).web(false).run(args);
    
           OneToOneSampleService service = context.getBean(OneToOneSampleService.class);
           String zipCd = "135-8670";
           service.getUsersWith(zipCd);
           service.getUsersWithout(zipCd);
           User user = service.addAddress(
           Address.builder().zipCd("000-0000").address("Tokyo").build());
           service.updateAddress(user.getUserId(), 
           Address.builder().zipCd("100-1000").address("Japan").build());
           service.getAddress(user);
           service.deleteAddress(user.getUserId());
           service.getAddress(user);
   //      service.deleteUser(user.getUserId());
           service.deleteUser("00000000");
       }

       @Bean OneToOneSampleService oneToOneSampleService(){
           return new OneToOneSampleServiceImpl();
       }

   }

.. _section2-2-3-spring-data-jpa-usage-one-to-one-service-impl-label:

サービス実装クラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

上記で定義したサービスインターフェースを実装した結果を以下に示す。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl

   package org.debugroom.sample.spring.jpa.domain.service;

   import java.util.List;

   import javax.transaction.Transactional;

   import org.springframework.beans.factory.annotation.Autowired;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.repository.AddressRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.UserRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.specification.FindUsersByNotZipCd;
   import org.debugroom.sample.spring.jpa.domain.repository.specification.FindUsersByZipCd;

   import lombok.extern.slf4j.Slf4j;

   @Slf4j
   @Transactional
   public class OneToOneSampleServiceImpl implements OneToOneSampleService{

       @Autowired
       UserRepository userRepository;
  
       @Autowired
       AddressRepository addressRepository;
  
       @Override
       public Address getAddress(User user) {
           Address address = addressRepository.findOne(user.getUserId());
           log.info(this.getClass().getName() + " : address of " + user.getUserId());
           if(address != null){
               log.info(this.getClass().getName() + "            - {"
                         + address.getZipCd() + ", " + address.getAddress() + "}");
           }else{
               log.info(this.getClass().getName() + "            - {null, null}");
           }
           return address;
       }

       @Override
       public List<User> getUsersWith(String zipCd) {
           List<User> users = userRepository.findAll(FindUsersByZipCd.builder()
                                                        .zipCd(zipCd).build());
           log.info(this.getClass().getName() + " : users with zipCd " + zipCd);
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                          + user.getUserId() + ", " + user.getUserName() + ", "
                          + user.getAddress().getZipCd() + ", " 
                          + user.getAddress().getAddress() + "}");
           }
           return users;
       }

       @Override
       public List<User> getUsersWithout(String zipCd) {
           List<User> users = userRepository.findAll(FindUsersByNotZipCd.builder()
                                                        .zipCd(zipCd).build());
           log.info(this.getClass().getName() + " : users without zipCd " + zipCd);
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                          + user.getUserId() + ", " + user.getUserName() + ", "
                          + user.getAddress().getZipCd() + ", " 
                          + user.getAddress().getAddress() + "}");
           }
           return users;
       }

       @Override
       public User addAddress(Address address) {
           String sequence = new StringBuilder()
                                     .append("00000000")
                                     .append(userRepository.count())
                                     .toString();
           String newUserId = sequence.substring(
                                          sequence.length()-8, sequence.length());
           address.setUserId(newUserId);
           User newUser = User.builder()
                                  .userId(newUserId)
                                  .userName("NewUser(・∀・)b")
                                  .loginId("loginId")
                                  .build();
           userRepository.save(newUser);
           newUser.setAddress(address);
           addressRepository.save(address);
           userRepository.flush();
           List<User> users = userRepository.findAll();
           log.info(this.getClass().getName() + " : users ");
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                          + user.getUserId() + ", " + user.getUserName() + "}");
               if(user.getUserId().equals(newUser.getUserId())){
                   Address newAddress = newUser.getAddress();
                   log.info(this.getClass().getName() + "  Add Address - {"
                              + newAddress.getUserId() + ", " + newAddress.getAddress() + "}");
               }
           }
           return newUser;
       }

       @Override
       public void updateAddress(String userId, Address address) {
           Address updateAddress = getAddress(User.builder().userId(userId).build()); 
           updateAddress.setZipCd(address.getZipCd());
           updateAddress.setAddress(address.getAddress());
       }

       @Override
       public void deleteAddress(String userId) {
           Address address = addressRepository.findOne(userId);
           addressRepository.delete(address);
   //      user.setAddress(null);
   //      userRepository.save(user);
   //      addressRepository.delete(user.getAddress());
       }

       @Override
       public void deleteUser(String userId) {
           User deleteUser = userRepository.findOne(userId);
           addressRepository.delete(userId);
           userRepository.delete(deleteUser);
           List<User> users = userRepository.findAll();
           log.info(this.getClass().getName() + " : users ");
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                          + user.getUserId() + ", " + user.getUserName() + "}");
           }
      }

   }

以降、実装したサービスクラスの実装の詳細をユースケースごとに記述する。

* 指定されたユーザの住所を取得する。

oneToOneSampleService.getAddress(User user)実行の結果、addressRepository.findOne(String userId)が呼ばれ、以下のようなSQLが発行される。基本的にシンプルなデータベースアクセスにおけるプライマリキー指定時の呼び出しと同じである。

.. sourcecode:: sql

   select
       address0_.user_id as user_id1_0_0_,
       address0_.address as address2_0_0_,
       address0_.last_updated_date as last_upd3_0_0_,
       address0_.ver as ver4_0_0_,
       address0_.zip_cd as zip_cd5_0_0_ 
   from
       public.address address0_ 
   where
       address0_.user_id=?

* 特定の郵便番号を持つユーザ一覧を取得する。

oneToOneSampleService.getUsersWith(String zipCd)に実装しているが、住所テーブルの郵便番号を指定し、指定した郵便番号と一致した住所テーブルのデータのプライマリキーであるユーザIDを使って、ユーザテーブルと結合して、一致するユーザの一覧を取得する。
JPAと同じく、テーブル結合する場合、Criteria API、JPQL、Native SQLいずれでも可能だが、ここでは、CriteriaAPIを使用して、データ取得する場合を記述する。Spring Data JPAでCriteria APIを使ってテーブル結合するには、以下の通り、結合条件に該当するorg.springframework.data.jpa.domain.Specificationクラスを継承した条件クラスを作成し、toPredicate()メソッドをオーバーライドして、結合条件を指定したPredicateクラスを戻り値で返却する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.specification.FindUsersByZipCd.java

   package org.debugroom.sample.spring.jpa.domain.repository.specification;

   import java.util.ArrayList;
   import java.util.List;

   import javax.persistence.criteria.CriteriaBuilder;
   import javax.persistence.criteria.CriteriaQuery;
   import javax.persistence.criteria.Predicate;
   import javax.persistence.criteria.Root;
   import javax.persistence.criteria.Join;

   import org.springframework.data.jpa.domain.Specification;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.Address_;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.entity.User_;

   import lombok.Data;
   import lombok.AllArgsConstructor;
   import lombok.Builder;

   @AllArgsConstructor
   @Builder
   @Data
   public class FindUsersByZipCd implements Specification<User>{

       private String zipCd;

       @Override
       public Predicate toPredicate(Root<User> root, 
                                      CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

           List<Predicate> predicates = new ArrayList<Predicate>();
    
           Join<User, Address> joinAddress = root.join(User_.address);
           predicates.add(criteriaBuilder.equal(joinAddress.get(Address_.zipCd), zipCd));
    
           return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
       }
   }

まず、最終的にデータ取得するエンティティ(ここでは、ユーザの一覧)のRoot<User>クラスを起点として、結合する２つのテーブルに相当するエンティティクラスを型パラメータにもつJoinクラスをjoinメソッドを通じて作成する。join()メソッドの引数には結合する際に使用するキーを `エンティティのメタモデルクラス <http://docs.jboss.org/hibernate/jpamodelgen/1.0/reference/en-US/html_single/#d0e72>`_ で指定する。上記の例では、ユーザテーブルと、アドレステーブルを結合するキーのメタモデルクラスとして、User＿.addressを引数に指定指定している。メタモデルクラスの実装は以下の通りである。

.. sourcecode:: java
   :caption: `org.debugroom.sample.spring.jpa.domain.entity.User_.java`

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.sql.Timestamp;
   import javax.persistence.metamodel.SetAttribute;
   import javax.persistence.metamodel.SingularAttribute;
   import javax.persistence.metamodel.StaticMetamodel;

   @StaticMetamodel(User.class)
   public class User_ {
       public static volatile SingularAttribute<User, String> userId;
       public static volatile SingularAttribute<User, Timestamp> lastUpdatedDate;
       public static volatile SingularAttribute<User, String> loginId;
       public static volatile SingularAttribute<User, String> userName;
       public static volatile SingularAttribute<User, Integer> ver;
       public static volatile SingularAttribute<User, Address> address;
       public static volatile SetAttribute<User, Affiliation> affiliations;
       public static volatile SetAttribute<User, Email> emails;
   }

Joinクラスの中で、指定したzipCdとイコールとなる条件をjava.persistence.criteria.Predicateのリストに追加し、最終的にCriteriaBuilderで一つのPredicateクラスとして返却する。

.. note:: 上記のメタモデルクラスはIDEのオプションで自動生成できる。以下はEclipseで作成したエンティティクラスに対して、自動的にメタモデルを作成するためのオプションの指定する画面である。JPAプロジェクト化してあるプロジェクトの設定画面を開き、JPAメニューの"正規メタモデル"を出力するソースコードを指定する。

   .. figure:: img/eclipse-metamodel-generation-setting.png
      :scale: 100%

上述して作成したSpecificationクラスを利用するにはレポジトリクラスに、追加で、org.springframework.data.jpa.repository.JpaSpecificationExecutorを継承する必要がある。継承する事で、上記のSpecificationクラスを引数にとるfindAll()メソッドがレポジトリから使用可能になる。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.UserRepository.javaに更にJpaSpecificationを継承
   
   package org.debugroom.sample.spring.jpa.domain.repository;

   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

   import org.debugroom.sample.spring.jpa.domain.entity.User;

   public interface UserRepository extends JpaRepository<User, String>, 
                                             JpaSpecificationExecutor<User>{
   }

実際にサービスから、郵便番号をパラメータとして、上記のSpecificationクラスを生成し、レポジトリの引数に渡して実行すれば以下のようなテーブル結合したSQLが発行される。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl#getUsersWith(String zipCode)
   
   // omit

       @Override
       public List<User> getUsersWith(String zipCd) {
           List<User> users = userRepository.findAll(
                   FindUsersByZipCd.builder().zipCd(zipCd).build());

.. note:: 上記はSpecificationクラスはLombokのBuilderを利用してインスタンス生成している。

.. sourcecode:: sql
   
   select
       user0_.user_id as user_id1_3_,
       user0_.last_updated_date as last_upd2_3_,
       user0_.login_id as login_id3_3_,
       user0_.user_name as user_nam4_3_,
       user0_.ver as ver5_3_ 
   from
       public.usr user0_ 
   inner join
       public.address address1_ 
       on user0_.user_id=address1_.user_id 
   where
       address1_.zip_cd=?


.. note:: `Spring Dataの公式ドキュメント 5.5 Specification <http://docs.spring.io/spring-data/jpa/docs/1.10.4.RELEASE/reference/html/#specifications>`_ もあわせて参照すること。

* 特定の郵便番号を持たないユーザ一覧を取得する

oneToOneSampleService.getUsersWithout(String zipCd)に実装しているが、基本的には、前述のユースケース「特定の郵便番号を持つユーザ一覧を取得する」と実装する内容はほぼ同じである。唯一の違いは、結合条件を指定するクラスの実装で、副問い合わせ使用して、住所テーブルの郵便番号を指定し、指定した郵便番号と一致した住所テーブルのデータのプライマリキーであるユーザIDを使って、ユーザテーブルと結合して、一致するユーザの一覧を取得する。その結果を元にそれに該当しないユーザをNotInを用いてユーザテーブルから抽出するクエリの条件クラスを作成する形である。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.specification.FindUsersByNotZipCd
   
   package org.debugroom.sample.spring.jpa.domain.repository.specification;

   import javax.persistence.criteria.CriteriaBuilder;
   import javax.persistence.criteria.CriteriaQuery;
   import javax.persistence.criteria.Predicate;
   import javax.persistence.criteria.Root;
   import javax.persistence.criteria.Subquery;
   import javax.persistence.criteria.Join;
   import javax.persistence.criteria.Path;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.Address_;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.entity.User_;
   import org.springframework.data.jpa.domain.Specification;

   import lombok.Data;
   import lombok.AllArgsConstructor;
   import lombok.Builder;

   @AllArgsConstructor
   @Builder
   @Data
   public class FindUsersByNotZipCd implements Specification<User>{
  
       private String zipCd;

       @Override
       public Predicate toPredicate(Root<User> root, 
           CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    
           Path<Object> path = root.get("userId");
           Subquery<User> subQuery = criteriaBuilder.createQuery().subquery(User.class);
           Root<User> subQueryRoot = subQuery.from(User.class);
           Join<User, Address> subQueryJoinAddress = subQueryRoot.join(User_.address);
           Predicate subQueryPredicate = criteriaBuilder.equal(
                                             subQueryJoinAddress.get(Address_.zipCd), zipCd);
           subQuery.select(subQueryRoot.get("userId"));
           subQuery.where(subQueryPredicate);
    
           return criteriaBuilder.not(criteriaBuilder.in(path).value(subQuery));
       }

   }

上記の検索条件クラスは、サブクエリとして、起点となるUserテーブルのRootから、AddressテーブルへJoinするクラスを作成し、結合するキーとなるクラスを同じくエンティティメタモデルクラスで指定する(User＿.address)。また、アドレステーブルのzip_cdと指定したzipCdが一致する条件をCriteriaBuilderを通じて作成し、サブクエリの条件に加える。サブクエリでは、郵便番号が一致したユーザのuserIdを抽出する形として、それをメインのクエリのNotInの条件として構成する。結局、こうして組み立てた条件クラスの実行及び、発行されるSQLは以下の通りとなる。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl#getUsersWithout(String zipCode)

    //omit

       @Override
       public List<User> getUsersWithout(String zipCd) {
           List<User> users = userRepository.findAll(FindUsersByNotZipCd.builder().zipCd(zipCd).build());
           //omit
       }

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_,
       user0_.last_updated_date as last_upd2_3_,
       user0_.login_id as login_id3_3_,
       user0_.user_name as user_nam4_3_,
       user0_.ver as ver5_3_ 
   from
       public.usr user0_ 
   where
       user0_.user_id not in  (
           select
               user1_.user_id 
           from
               public.usr user1_ 
           inner join
               public.address address2_ 
                   on user1_.user_id=address2_.user_id 
           where
               address2_.zip_cd=?
       )


* 指定されたユーザの住所を追加する。

oneToOneSampleService.addAddress(Address address)に実装しているが、後述するAddressデータの更新や削除、ユーザの削除のためにサービスの中で新規ユーザデータを作成し、住所データを追加する実装を記述した。実際にアドレスを追加する基本的にはやり方としては、エンティティクラスを作成して、レポジトリクラスのsave()メソッドの引数として渡してしまえば完結するが、OneToOneの関連では、同じユーザIDを、Userテーブルと、Addressテーブルで主キーとして利用する以上、先にユーザデータを登録してからアドレスデータを追加する必要がある。通常、JPAにはカスケード属性があり、メインとなるエンティティ(ここではUser)に住所のエンティティクラスをプロパティにセットして、UserRepoisotry#save()メソッドだけ実行すれば十分なはずであるが、先に住所データを作成しにいく挙動をとったため、一気にやろうとすると、異常終了した(コメントアウト)。

.. todo:: OneToOne関連のデータ登録の再検証が必要。Spring Dataを使用せず、純粋なJPAのAPIを用いてOneToOne関連のデータ登録を実行した場合の結果を含めて確認する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl#addAddress(Address address)

   
   // omit
       @Override
       public User addAddress(Address address) {
           String sequence = new StringBuilder()
                                   .append("00000000")
                                   .append(userRepository.count())
                                   .toString();
           String newUserId = sequence.substring(
                                   sequence.length()-8, sequence.length());
           address.setUserId(newUserId);
           User newUser = User.builder()
                                .userId(newUserId)
                                .userName("NewUser(・∀・)b")
   //                           .address(address)
                                .loginId("loginId")
                                .build();
           userRepository.save(newUser);
           newUser.setAddress(address);
           addressRepository.save(address);
           userRepository.flush();
           List<User> users = userRepository.findAll();
           log.info(this.getClass().getName() + " : users ");
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                          + user.getUserId() + ", " + user.getUserName() + "}");
               if(user.getUserId().equals(newUser.getUserId())){
                   Address newAddress = newUser.getAddress();
                   log.info(this.getClass().getName() + "  Add Address - {"
                              + newAddress.getUserId() + ", " + newAddress.getAddress() + "}");
               }
           }
           return newUser;
      }

以下のようなSQLが発行される。

.. sourcecode:: sql

   insert 
       into
           public.usr
           (last_updated_date, login_id, user_name, ver, user_id) 
       values
            (?, ?, ?, ?, ?)
   insert 
       into
           public.address
           (address, last_updated_date, ver, zip_cd, user_id) 
       values
           (?, ?, ?, ?, ?)


* 指定されたユーザの住所を更新する。

oneToOneSampleService.updateAddress(String userId, Address address)に実装している。基本的なデータ更新のやり方としては、更新対象のエンティティクラスを取得して、値を更新するのみで良い。トランザクション境界をまたぐと自動的にupdate文が発行される。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl#updateAddress(String userId, Address address)
   
   // omit
       @Override
       public void updateAddress(String userId, Address address) {
           Address updateAddress = getAddress(User.builder().userId(userId).build()); 
           updateAddress.setZipCd(address.getZipCd());
           updateAddress.setAddress(address.getAddress());
       }

.. sourcecode:: sql

   update
       public.address 
   set
       address=?,
       last_updated_date=?,
       ver=?,
       zip_cd=? 
   where
       user_id=?  

.. note:: パラメータを更新しただけでUPDATE文が発行される理由については、`エンティティのライフサイクル管理 <http://terasolunaorg.github.io/guideline/5.2.0.RELEASE/ja/ArchitectureInDetail/DataAccessDetail/DataAccessJpa.html#entity>`_ を十分理解しておく必要がある。

* 指定されたユーザの住所を削除する。

oneToOneSampleService.deleteAddress(String userId)に実装している。基本的なデータ削除のやり方としては、AddressRepository.delete()メソッドでエンティティを指定すればよい。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl#deleteAddress(String userId)
   
   // omit
       @Override
       public void deleteAddress(String userId) {
           Address address = addressRepository.findOne(userId);
           addressRepository.delete(address);
   //    user.setAddress(null);
   //    userRepository.save(user);
   //    addressRepository.delete(user.getAddress());
       }

.. sourcecode:: sql

   delete 
       from
           public.address 
       where
           user_id=?

.. todo:: Userのaddressプロパティを明示的にnullにすれば、データ削除されるはずではあるが、特に更新はされず。@OneToOneアノテーションに関連する理由か検証は必要。

* 指定されたユーザの情報を住所を含めて削除する。

oneToOneSampleService.deleteUser(String userId)に実装している。基本的なデータ削除のやり方としては、UserRepository.delete()メソッドでエンティティを指定すればよい。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToOneSampleServiceImpl#deleteUser(String userId)

   //omit
       @Override
       public void deleteUser(String userId) {
           User deleteUser = userRepository.findOne(userId);
           addressRepository.delete(userId);
           userRepository.delete(deleteUser);
           List<User> users = userRepository.findAll();
           log.info(this.getClass().getName() + " : users ");
           for(User user : users){
               log.info(this.getClass().getName() + "            - {"
                          + user.getUserId() + ", " + user.getUserName() + "}");
           }
       }

ただし、ユーザデータのように住所以外にもEmailやAffiliationなど事前にデータが残っていると削除処理で異常終了する。まとめて削除するにはエンティティの関連アノテーションのカスケード属性を設定しておく必要がある。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.User

   //omit

       @OneToOne(mappedBy="usr", optional=false, fetch= FetchType.LAZY,
                 cascade= CascadeType.ALL)
       private Address address;

       @OneToMany(fetch = FetchType.LAZY, mappedBy = "usr", cascade= CascadeType.ALL)
       private Set<Affiliation> affiliations;

       @OneToMany(mappedBy="usr", cascade= CascadeType.ALL)
       private Set<Email> emails;

   //omit

発行されるSQLは以下の通りである。

.. sourcecode:: sql

   delete 
       from
           public.address 
       where
           user_id=?

   delete 
       from
           public.affiliation 
       where
           group_id=? 
       and user_id=?

   delete 
      from
          public.email 
      where
          email_id=? 
      and user_id=?

   delete 
      from
          public.email 
      where
          email_id=? 
      and user_id=?

   delete 
     from
          public.usr 
     where
          user_id=?

.. todo:: Userのaddressプロパティの@OneToOneアノテーションのcascade属性をALLにしていても、データ削除されるはずではあるが、特に更新はされず。@OneToOneアノテーションに関連する理由か検証は必要。

.. _section2-3-spring-data-jpa-usage-one-to-many-label:

1対多関連テーブルにおけるデータ操作
---------------------------------------------------

1対多の関連テーブル(本サンプルでは、ユーザとEmail)のデータ操作に関して以下のようなユースケースを考える。

* 指定されたユーザのEmailの一覧を取得する。
* 特定のメールアドレスを持つユーザを検索する。
* 指定されたユーザのメールアドレスを追加する。
* 指定されたユーザをメールアドレスを含めて追加する。
* 指定されたユーザのメールアドレスを更新する。
* 指定されたユーザのメールアドレスを1件削除する。
* 指定されたユーザのメールアドレスを全件削除する。
* 指定されたユーザの情報をメールアドレスを含めて削除する。

基本的には、:ref:`前章、シンプルなデータアクセス<section2-1-spring-data-jpa-usage-simple-access-label>` で作成したエンティティクラス、及びレポジトリクラスはそのまま流用する。

.. _section2-3-1-spring-data-jpa-usage-one-to-many-service-label:

サービスインターフェースの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

* 指定されたユーザのEmailの一覧を取得する。 → getEmails(User user)
* 特定のメールアドレスを持つユーザを検索する。 → getUser(String email)
* 指定されたユーザのメールアドレスを追加する。 → addEmail(User user, String email)
* 指定されたユーザをメールアドレスを含めて追加する。 → addUser(User user, String email)
* 指定されたユーザのメールアドレスを更新する。 → updateEmail(User user, String email)
* 指定されたユーザのメールアドレスを1件削除する。 → deleteEmail(User user, String email)
* 指定されたユーザのメールアドレスを全件削除する。 → deleteEmails(User user)
* 指定されたユーザの情報をメールアドレスを含めて削除する。 → deleteUser(User user)


.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleService.java

   package org.debugroom.sample.spring.jpa.domain.service;

   import java.util.List;

   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.User;

   public interface OneToManySampleService {

       List<Email> getEmails(User user);
  
       User getUser(String email);
  
       User addEmail(User user, String email);
  
       User addUser(User user, String email);
  
       User updateEmail(User user, String email);
  
       User deleteEmail(User user, String email);
  
       User deleteEmails(User user);
  
       void deleteUser(User user);

   }


.. _section2-3-2-spring-data-jpa-usage-one-to-many-configuration-label:

コンフィグレーションクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

データベースの環境の設定は、:ref:`前章 シンプルなデータアクセス時の設定<section2-1-5-spring-data-jpa-usage-simple-access-configuration-label>` を流用し、サービスを実行する設定ファイルクラスを新規作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.config.OneToManySampleApp.java

   package org.debugroom.sample.spring.jpa.config;

   import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
   import org.springframework.boot.builder.SpringApplicationBuilder;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.ComponentScan;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.ConfigurableApplicationContext;

   import java.util.HashSet;

   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.EmailPK;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.service.OneToManySampleService;
   import org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl;

   @ComponentScan("org.debugroom.sample.spring.jpa.config.infra")
   @Configuration
   @EnableAutoConfiguration
   public class OneToManySampleApp {

       public static void main(String[] args){
           ConfigurableApplicationContext context = new SpringApplicationBuilder(
                                                         OneToManySampleApp.class).web(false).run(args);

           OneToManySampleService service = context.getBean(OneToManySampleService.class);

           String email = "test@test.com";
           User user = service.getUser(email);
           service.getEmails(user);
           service.addEmail(user, "(ΦωΦ)@test.com");
           service.addEmail(user, "(ΦωΦ)@test.co.jp");
           service.getEmails(user);
           User addUser = service.addUser(User.builder()
                                              .userName("(ΦωΦ)")
                                              .emails(new HashSet<Email>())
                                              .build(), email);
           service.getEmails(addUser);
           service.updateEmail(user, Email.builder()
                                          .id(EmailPK.builder().userId("00000000").emailId("1").build())
                                          .email("(・∀・)@test.com")
                                          .build());
           service.getEmails(user);
           service.deleteEmail(user, Email.builder()
                                          .id(EmailPK.builder().userId("00000000").emailId("1").build())
                                          .email("(・∀・)@test.com")
                                          .build());
           service.getEmails(user);
           service.deleteEmails(user);
           service.getEmails(user);
           service.deleteUser(addUser);
       }
  
       @Bean OneToManySampleService oneToManySampleService(){
           return new OneToManySampleServiceImpl();
       }
   }

.. _section2-3-3-spring-data-jpa-usage-one-to-many-service-impl-label:

サービス実装クラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

上記で定義したサービスインターフェースを実装した結果を以下に示す。

.. sourcecode:: java


   package org.debugroom.sample.spring.jpa.domain.service;

   import java.util.Iterator;
   import java.util.Set;

   import javax.transaction.Transactional;

   import org.springframework.beans.factory.annotation.Autowired;

   import org.debugroom.sample.spring.jpa.domain.entity.Address;
   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.EmailPK;
   import org.debugroom.sample.spring.jpa.domain.entity.User;
   import org.debugroom.sample.spring.jpa.domain.repository.AddressRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.EmailRepository;
   import org.debugroom.sample.spring.jpa.domain.repository.UserRepository;

   import lombok.extern.slf4j.Slf4j;

   @Slf4j
   @Transactional
   public class OneToManySampleServiceImpl implements OneToManySampleService{

       @Autowired
       EmailRepository emailRepository;
  
       @Autowired
       UserRepository userRepository;
  
       @Autowired
       AddressRepository addressRepository;
  
       @Override
       public Set<Email> getEmails(User user) {
           User findUser = userRepository.findOne(user.getUserId());
           log.info(this.getClass().getName() + " : emails of " + user.getUserId());
           for(Email email : findUser.getEmails()){
               log.info(this.getClass().getName() + "           - {"
                          + email.getId().getEmailId() + ", " + email.getEmail() + "}");
           }
           return findUser.getEmails();
       }

       @Override
       public User getUser(String email) {
           Email findEmail = emailRepository.findByEmail(email);
           return findEmail.getUsr();
       }

       @Override
       public User addEmail(User user, String email) {
           User findUser = userRepository.findOne(user.getUserId());
           String sequence = new StringBuilder()
                                    .append("00000000")
                                    .append(findUser.getEmails().size())
                                    .toString();
           String newEmailId = sequence.substring(
           sequence.length()-8, sequence.length());
           Email newEmail = Email.builder()
                                 .id(EmailPK.builder()
                                            .userId(findUser.getUserId())
                                            .emailId(newEmailId)
                                            .build())
                                 .email(email)
                                 .build();
           findUser.addEmail(newEmail);
           return findUser;
       }

       @Override
       public User addUser(User user, String email) {
           String sequence = new StringBuilder()
                                     .append("00000000")
                                     .append(userRepository.count())
                                     .toString();
           String newUserId = sequence.substring(
                                  sequence.length()-8, sequence.length());
           user.setUserId(newUserId);
           user.addEmail(Email.builder().id(EmailPK.builder()
                                                   .userId(newUserId)
                                                   .emailId("00000000")
                                                   .build())
                                        .email(email)
                                        .build());
           User addUser = userRepository.save(user);
           addressRepository.save(Address.builder().userId(newUserId).build());
           return addUser;
       }

       @Override
       public User updateEmail(User user, Email email) {
           User findUser = userRepository.findOne(user.getUserId());
           for(Email updateEmail : findUser.getEmails()){
               if(updateEmail.getId().getEmailId().equals(email.getId().getEmailId())){
                   updateEmail.setEmail(email.getEmail());
               }
           }
           return findUser;
       }

       @Override
       public User deleteEmail(User user, Email email) {
           User findUser = userRepository.findOne(user.getUserId());
           for(Iterator<Email> iterator = findUser.getEmails().iterator(); iterator.hasNext();){
               Email deleteEmail = iterator.next();
               if(deleteEmail.getId().getEmailId().equals(
               email.getId().getEmailId())){
                   iterator.remove();
               }
           }
           return findUser;
       }

       @Override
       public User deleteEmails(User user) {
           User findUser = userRepository.findOne(user.getUserId());
           findUser.getEmails().clear();
           return findUser;
       }

       @Override
       public void deleteUser(User user) {
           User findUser = userRepository.findOne(user.getUserId());
           Address address = addressRepository.findOne(user.getUserId());
           addressRepository.delete(address);
           userRepository.delete(findUser);
       }
   }


以降、実装の詳細をユースケースごとに詳述する。

* 指定されたユーザのEmailの一覧を取得する。

oneToManySampleService.getEmails(User user)実行の結果、emailRepository.findOne(String userId)が呼ばれ、以下のようなSQLが発行される。基本的にシンプルなデータベースアクセスにおけるプライマリキー指定時の呼び出しと同じである。

.. sourcecode:: sql

   select
       emails0_.user_id as user_id2_2_0_,
       emails0_.email_id as email_id1_2_0_,
       emails0_.email_id as email_id1_2_1_,
       emails0_.user_id as user_id2_2_1_,
       emails0_.email as email3_2_1_,
       emails0_.last_updated_date as last_upd4_2_1_,
       emails0_.ver as ver5_2_1_ 
   from
       public.email emails0_ 
   where
       emails0_.user_id=?

* 特定のメールアドレスを持つユーザを検索する。

`キーワード <http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords>`_ に従って、EmailRepositoryクラスにfindByEmail(String email)メソッドを作成する。Spring Data JPAの機能により、String型のemailをキーにEmailエンティティを取得するクエリの自動組み立てが可能になる。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.repository.EmailRepository.javaへfindByEmail(String email)を追加

   package org.debugroom.sample.spring.jpa.domain.repository;

   import java.util.List;
   import org.springframework.data.jpa.repository.JpaRepository;

   import org.debugroom.sample.spring.jpa.domain.entity.Email;
   import org.debugroom.sample.spring.jpa.domain.entity.EmailPK;

   public interface EmailRepository extends JpaRepository<Email, EmailPK>{
  
       public List<Email> findByIdUserId(String userId);

       public Email findByEmail(String email);

   }

サービスでは、emailをキーにEmailエンティティを取得し、EmailのUserプロパティを戻り値として返せば良い。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#getUser(String email)

       @Override
       public User getUser(String email) {
           Email findEmail = emailRepository.findByEmail(email);
           return findEmail.getUsr();
       }

OneToManySampleServiceクラスでは、javax.transaction.Transactionalアノテーションを付与する事で、トランザクション境界を当サービスのメソッドに設定している。上記の呼び出し方だと、デフォルトではフェッチ戦略がLazyに設定されており、トランザクション境界の外でユーザプロパティのアクセスが発生した場合、異常終了してしまうため、Emailエンティティクラスのユーザプロパティに対するフェッチ戦略をEagerに変更しておく。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Email#User

   
      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
      private User usr;

実行した結果、以下のようなSQLが発行される。

.. sourcecode:: sql
 
   select
       email0_.email_id as email_id1_2_,
       email0_.user_id as user_id2_2_,
       email0_.email as email3_2_,
       email0_.last_updated_date as last_upd4_2_,
       email0_.ver as ver5_2_ 
   from
       public.email email0_ 
   where
       email0_.email=?

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

   select
        user0_.user_id as user_id1_3_0_,
        user0_.last_updated_date as last_upd2_3_0_,
        user0_.login_id as login_id3_3_0_,
        user0_.user_name as user_nam4_3_0_,
        user0_.ver as ver5_3_0_ 
    from
        public.usr user0_ 
    where
        user0_.user_id=?

.. note:: Spring MVCのControllerクラスからのLazy Fetchについては、`OpenEntityManagerInViewInterceptor <http://terasolunaorg.github.io/guideline/5.2.0.RELEASE/ja/ArchitectureInDetail/DataAccessDetail/DataAccessJpa.html#openentitymanagerinviewinterceptor>`_ を使用する。

* 指定されたユーザのメールアドレスを追加する。

oneToManySampleService#addEmail(User user, String email)にて、指定されたユーザのUserエンティティを取得し、プライマリーキー(emailId)を新しく生成して、Emailエンティティを追加する。単にエンティティを追加するだけで、INSERT文が発行される。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#addEmail(User user, String email)

       @Override
       public User addEmail(User user, String email) {
           User findUser = userRepository.findOne(user.getUserId());
           String sequence = new StringBuilder()
                                   .append("00000000")
                                   .append(findUser.getEmails().size())
                                   .toString();
           String newEmailId = sequence.substring(
                               sequence.length()-8, sequence.length());
           Email newEmail = Email.builder()
                                 .id(EmailPK.builder()
                                            .userId(findUser.getUserId())
                                            .emailId(newEmailId)
                                            .build())
                                 .email(email)
                                 .build();
           findUser.addEmail(newEmail);
           return findUser;
       }

実行した結果、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

   select
       emails0_.user_id as user_id2_2_0_,
       emails0_.email_id as email_id1_2_0_,
       emails0_.email_id as email_id1_2_1_,
       emails0_.user_id as user_id2_2_1_,
       emails0_.email as email3_2_1_,
       emails0_.last_updated_date as last_upd4_2_1_,
       emails0_.ver as ver5_2_1_ 
   from
       public.email emails0_ 
   where
       emails0_.user_id=?

   insert into
       public.email
       (email, last_updated_date, ver, email_id, user_id) 
   values
       (?, ?, ?, ?, ?)

* 指定されたユーザをメールアドレスを含めて追加する。

oneToManySampleService#addUser(User user, String email)にて、プライマリーキー(userId)を新しく生成して、Userエンティティ及び、Emailエンティティを追加する。なお、OneToOneのAddressとの関連があるため、Addressエンティティも追加しておく。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#addUser(User user, String email)

       @Override
       public User addUser(User user, String email) {
       String sequence = new StringBuilder()
                                .append("00000000")
                                .append(userRepository.count())
                                .toString();
       String newUserId = sequence.substring(
                                 sequence.length()-8, sequence.length());
       user.setUserId(newUserId);
       user.addEmail(Email.builder().id(EmailPK.builder()
                                               .userId(newUserId)
                                               .emailId("00000000")
                                               .build())
                                    .email(email)
                                    .build());
       User addUser = userRepository.save(user);
       addressRepository.save(Address.builder().userId(newUserId).build());
       return addUser;
   }

.. note:: Addressエンティティは追加しなくても制約上問題はないが、削除処理をかけようとすると異常終了するため、ユーザエンティティ登録時には必ずセットで追加するようにしておく。

上記を実行すると、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       count(*) as col_0_0_ 
   from
       public.usr user0_

   select
       user0_.user_id as user_id1_3_1_,
       user0_.last_updated_date as last_upd2_3_1_,
       user0_.login_id as login_id3_3_1_,
       user0_.user_name as user_nam4_3_1_,
       user0_.ver as ver5_3_1_,
       affiliatio1_.user_id as user_id2_1_3_,
       affiliatio1_.group_id as group_id1_1_3_,
       affiliatio1_.group_id as group_id1_1_0_,
       affiliatio1_.user_id as user_id2_1_0_,
       affiliatio1_.last_updated_date as last_upd3_1_0_,
       affiliatio1_.ver as ver4_1_0_ 
   from
       public.usr user0_ 
   left outer join
       public.affiliation affiliatio1_ 
           on user0_.user_id=affiliatio1_.user_id 
   where
       user0_.user_id=?

   select
       email0_.email_id as email_id1_2_0_,
       email0_.user_id as user_id2_2_0_,
       email0_.email as email3_2_0_,
       email0_.last_updated_date as last_upd4_2_0_,
       email0_.ver as ver5_2_0_ 
   from
       public.email email0_ 
   where
       email0_.email_id=? 
       and email0_.user_id=?

   select
       address0_.user_id as user_id1_0_0_,
       address0_.address as address2_0_0_,
       address0_.last_updated_date as last_upd3_0_0_,
       address0_.ver as ver4_0_0_,
       address0_.zip_cd as zip_cd5_0_0_ 
   from
       public.address address0_ 
   where
       address0_.user_id=?

   insert into
       public.usr
       (last_updated_date, login_id, user_name, ver, user_id) 
   values
       (?, ?, ?, ?, ?)

   insert into
       public.email
       (email, last_updated_date, ver, email_id, user_id) 
   values
       (?, ?, ?, ?, ?)

   insert into
       public.address
       (address, last_updated_date, ver, zip_cd, user_id) 
   values
       (?, ?, ?, ?, ?)

* 指定されたユーザのメールアドレスを更新する。

oneToManySampleService#updateEmail(User user, String email)にて、指定されたユーザのUserエンティティを取得し、キーが合致する(ここでは、userIdとemailId)Emailオブジェクトのemailプロパティ属性を変更する。プロパティを変更するだけで、自動的にUPDATE文が発行される。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#updateEmail(User user, Email email)

       @Override
       public User updateEmail(User user, Email email) {
           User findUser = userRepository.findOne(user.getUserId());
           for(Email updateEmail : findUser.getEmails()){
               if(updateEmail.getId().getEmailId().equals(email.getId().getEmailId())){
                   updateEmail.setEmail(email.getEmail());
               }
           }
           return findUser;
       }

上記を実行すると、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

   select
       emails0_.user_id as user_id2_2_0_,
       emails0_.email_id as email_id1_2_0_,
       emails0_.email_id as email_id1_2_1_,
       emails0_.user_id as user_id2_2_1_,
       emails0_.email as email3_2_1_,
       emails0_.last_updated_date as last_upd4_2_1_,
       emails0_.ver as ver5_2_1_ 
   from
       public.email emails0_ 
   where
       emails0_.user_id=?

   update
       public.email 
   set
       email=?,
       last_updated_date=?,
       ver=? 
   where
       email_id=? 
       and user_id=?

* 指定されたユーザのメールアドレスを1件削除する。

oneToManySampleService#deleteEmail(User user, Email email)にて、指定されたユーザのUserエンティティを取得し、キーが合致する(ここでは、userIdとemailId)Emailオブジェクトをemailsリストから除外する。なお、コレクションから対象のオブジェクトを削除する事で、削除のSQLが実行されるためには、Userエンティティの@OneToManyアノテーションのcascade属性と、orphanRemoval属性を変更しておく必要がある事に注意する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#deleteEmail(User user, Email email)

       @Override
       public User deleteEmail(User user, Email email) {
           User findUser = userRepository.findOne(user.getUserId());
           for(Iterator<Email> iterator = findUser.getEmails().iterator(); iterator.hasNext();){
               Email deleteEmail = iterator.next();
               if(deleteEmail.getId().getEmailId().equals(email.getId().getEmailId())){
                   iterator.remove();
               }
           }
           return findUser;
       }

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.User#emails

       @OneToMany(mappedBy="usr", cascade= CascadeType.ALL, orphanRemoval = true)
       private Set<Email> emails;

上記のdeleteEmail()を実行すると、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

   select
       emails0_.user_id as user_id2_2_0_,
       emails0_.email_id as email_id1_2_0_,
       emails0_.email_id as email_id1_2_1_,
       emails0_.user_id as user_id2_2_1_,
       emails0_.email as email3_2_1_,
       emails0_.last_updated_date as last_upd4_2_1_,
       emails0_.ver as ver5_2_1_ 
   from
       public.email emails0_ 
   where
       emails0_.user_id=?

   delete from
       public.email 
   where
       email_id=? 
       and user_id=?

* 指定されたユーザのメールアドレスを全件削除する。

oneToManySampleService#deleteEmails(User user)にて、指定されたUserのエンティティを取得し、emailsの中身を空にする。なお、コレクションから対象のオブジェクトを削除する事で、削除のSQLが実行されるためには、Userエンティティの@OneToManyアノテーションのcascade属性と、orphanRemoval属性を変更しておく必要がある事に注意する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#deleteEmails(User user)

       @Override
       public User deleteEmails(User user) {
           User findUser = userRepository.findOne(user.getUserId());
           findUser.getEmails().clear();
           return findUser;
       }

上記のdeleteEmailsを実行すると、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

   select
       emails0_.user_id as user_id2_2_0_,
       emails0_.email_id as email_id1_2_0_,
       emails0_.email_id as email_id1_2_1_,
       emails0_.user_id as user_id2_2_1_,
       emails0_.email as email3_2_1_,
       emails0_.last_updated_date as last_upd4_2_1_,
       emails0_.ver as ver5_2_1_ 
   from
       public.email emails0_ 
   where
       emails0_.user_id=?

   delete from
       public.email 
   where
       email_id=? 
       and user_id=?

   delete from
       public.email 
   where
       email_id=? 
       and user_id=?

   delete from
       public.email 
   where
       email_id=? 
       and user_id=?

.. note:: 子要素全てに対してDELETE文が発生する事から、件数が多い場合は、JPQL等、コレクションからの要素削除以外を検討する。

* 指定されたユーザの情報をメールアドレスを含めて削除する。

oneToManySampleService#deleteUser(User user)にて、指定されたユーザのエンティティを取得し、userRepository#delete(User user)を実行する。なお、UserにはOneToOne関連を持つAddressエンティティが存在するため、先にaddressRepository#delete(Address address)を実行しておく。
ユーザの関連エンティティには全てcascadeType.ALLが付与されているので、Userエンティティを削除すると、全ての関連データも付随して削除される。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.service.OneToManySampleServiceImpl#deleteUser(User user)

       @Override
       public void deleteUser(User user) {
           User findUser = userRepository.findOne(user.getUserId());
           Address address = addressRepository.findOne(user.getUserId());
           addressRepository.delete(address);
           userRepository.delete(findUser);
       }

上記を実行すると、以下のようなSQLが発行される。

.. sourcecode:: sql

   select
       user0_.user_id as user_id1_3_0_,
       user0_.last_updated_date as last_upd2_3_0_,
       user0_.login_id as login_id3_3_0_,
       user0_.user_name as user_nam4_3_0_,
       user0_.ver as ver5_3_0_ 
   from
       public.usr user0_ 
   where
       user0_.user_id=?

   select
       address0_.user_id as user_id1_0_0_,
       address0_.address as address2_0_0_,
       address0_.last_updated_date as last_upd3_0_0_,
       address0_.ver as ver4_0_0_,
       address0_.zip_cd as zip_cd5_0_0_ 
   from
       public.address address0_ 
   where
       address0_.user_id=?

   select
       affiliatio0_.user_id as user_id2_1_0_,
       affiliatio0_.group_id as group_id1_1_0_,
       affiliatio0_.group_id as group_id1_1_1_,
       affiliatio0_.user_id as user_id2_1_1_,
       affiliatio0_.last_updated_date as last_upd3_1_1_,
       affiliatio0_.ver as ver4_1_1_ 
   from
       public.affiliation affiliatio0_ 
   where
       affiliatio0_.user_id=?

   select
       emails0_.user_id as user_id2_2_0_,
       emails0_.email_id as email_id1_2_0_,
       emails0_.email_id as email_id1_2_1_,
       emails0_.user_id as user_id2_2_1_,
       emails0_.email as email3_2_1_,
       emails0_.last_updated_date as last_upd4_2_1_,
       emails0_.ver as ver5_2_1_ 
   from
       public.email emails0_ 
   where
       emails0_.user_id=?

   delete from
       public.address 
   where
       user_id=?

   delete from
       public.email 
   where
       email_id=? 
       and user_id=?

   delete from
       public.usr 
   where
       user_id=?
       