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


.. _section2-1-spring-data-jpa-usage-one-to-one-label:

1対1の関連を持つテーブル間でのデータ操作
---------------------------------------------------

1対1の関連テーブル(本サンプルでは、ユーザと住所)のデータ操作に関して以下のようなユースケースを考える。

* 指定されたユーザの住所を取得する。
* 特定の郵便番号を持つユーザ一覧を取得する。
* 特定の郵便番号を持たないユーザ一覧を取得する
* 指定されたユーザの住所を更新する。
* 指定されたユーザの住所を削除する。
* 指定されたユーザの情報を住所を含めて削除する。

