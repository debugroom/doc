.. include:: ../../../module.txt

.. _section2-spring-data-jpa-usage-label:

Usage
===================================================

実際に作成したサンプルは `GitHub <https://github.com/debugroom/sample/tree/develop/sample-spring-jpa>`_ を参照のこと。

.. _section2-1-spring-data-jpa-usage-simple-access-label:

シンプルなデータベースアクセス
---------------------------------------------------

基本的なデータベースアクセスについて、Spring Bootのコンフィグレーションクラスの設定を踏まえて記述する。


.. _section2-1-1-spring-data-jpa-usage-simple-access-entity-label:

エンティティクラスの作成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

テーブルを表現するアノテーションを付与したエンティティクラスを作成する。

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.momain.model.entity.User.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.util.Date;
   import java.util.HashSet;
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

   @Entity
   @Table(name = "usr", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "login_id") )
   public class User implements java.io.Serializable {

       private String userId;
       private String userName;
       private String loginId;
       private Integer ver;
       private Date lastUpdatedDate;
       private Address address;
       private Set<Affiliation> affiliations = new HashSet<Affiliation>(0);
       private Set<Email> emails = new HashSet<Email>(0);

       public User() {
       }

       public User(String userId) {
           this.userId = userId;
       }

       public User(String userId, String userName, String loginId, Integer ver, Date lastUpdatedDate, Address address,
               Set<Affiliation> affiliations, Set<Email> emails) {
           this.userId = userId;
           this.userName = userName;
           this.loginId = loginId;
           this.ver = ver;
           this.lastUpdatedDate = lastUpdatedDate;
           this.address = address;
           this.affiliations = affiliations;
           this.emails = emails;
       }

       @Id
       @Column(name = "user_id", unique = true, nullable = false, length = 8)
       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       @Column(name = "user_name", length = 50)
       public String getUserName() {
           return this.userName;
       }

       public void setUserName(String userName) {
           this.userName = userName;
       }

       @Column(name = "login_id", unique = true, length = 64)
       public String getLoginId() {
           return this.loginId;
       }

       public void setLoginId(String loginId) {
           this.loginId = loginId;
       }

       @Column(name = "ver")
       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       @Temporal(TemporalType.TIMESTAMP)
       @Column(name = "last_updated_date", length = 29)
       public Date getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Date lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       @OneToOne(fetch = FetchType.LAZY, mappedBy = "usr")
       public Address getAddress() {
           return this.address;
       }

       public void setAddress(Address address) {
           this.address = address;
       }

       @OneToMany(fetch = FetchType.LAZY, mappedBy = "usr")
       public Set<Affiliation> getAffiliations() {
           return this.affiliations;
       }

       public void setAffiliations(Set<Affiliation> affiliations) {
           this.affiliations = affiliations;
       }

       @OneToMany(fetch = FetchType.LAZY, mappedBy = "usr")
       public Set<Email> getEmails() {
           return this.emails;
       }

       public void setEmails(Set<Email> emails) {
           this.emails = emails;
       }
   }

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Group.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.util.Date;
   import java.util.HashSet;
   import java.util.Set;
   import javax.persistence.Column;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.Id;
   import javax.persistence.OneToMany;
   import javax.persistence.Table;
   import javax.persistence.Temporal;
   import javax.persistence.TemporalType;

   @Entity
   @Table(name = "grp", schema = "public")
   public class Group implements java.io.Serializable {

       private String groupId;
       private String groupName;
       private Integer ver;
       private Date lastUpdatedDate;
       private Set<Affiliation> affiliations = new HashSet<Affiliation>(0);

       public Group() {
       }

       public Group(String groupId) {
           this.groupId = groupId;
       }

       public Group(String groupId, String groupName, Integer ver, Date lastUpdatedDate, Set<Affiliation> affiliations) {
           this.groupId = groupId;
           this.groupName = groupName;
           this.ver = ver;
           this.lastUpdatedDate = lastUpdatedDate;
           this.affiliations = affiliations;
       }

       @Id
       @Column(name = "group_id", unique = true, nullable = false, length = 10)
       public String getGroupId() {
           return this.groupId;
       }

       public void setGroupId(String groupId) {
           this.groupId = groupId;
       }

       @Column(name = "group_name", length = 50)
       public String getGroupName() {
           return this.groupName;
       }

       public void setGroupName(String groupName) {
           this.groupName = groupName;
       }

       @Column(name = "ver")
       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       @Temporal(TemporalType.TIMESTAMP)
       @Column(name = "last_updated_date", length = 29)
       public Date getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Date lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

       @OneToMany(fetch = FetchType.LAZY, mappedBy = "grp")
       public Set<Affiliation> getAffiliations() {
           return this.affiliations;
       }

       public void setAffiliations(Set<Affiliation> affiliations) {
           this.affiliations = affiliations;
       }

   }

.. sourcecode:: java
   :caption: org.degugroom.sample.spring.jpa.domain.entity.Address

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.util.Date;
   import javax.persistence.Column;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.GeneratedValue;
   import javax.persistence.Id;
   import javax.persistence.OneToOne;
   import javax.persistence.PrimaryKeyJoinColumn;
   import javax.persistence.Table;
   import javax.persistence.Temporal;
   import javax.persistence.TemporalType;
   import org.hibernate.annotations.GenericGenerator;
   import org.hibernate.annotations.Parameter;

   @Entity
   @Table(name = "address", schema = "public")
   public class Address implements java.io.Serializable {

       private String userId;
       private User usr;
       private String zipCd;
       private String address;
       private Integer ver;
       private Date lastUpdatedDate;

       public Address() {
       }

       public Address(User usr) {
           this.usr = usr;
       }

       public Address(User usr, String zipCd, String address, Integer ver, Date lastUpdatedDate) {
           this.usr = usr;
           this.zipCd = zipCd;
           this.address = address;
           this.ver = ver;
           this.lastUpdatedDate = lastUpdatedDate;
       }

       @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "usr") )
       @Id
       @GeneratedValue(generator = "generator")
       @Column(name = "user_id", unique = true, nullable = false, length = 8)
       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       @OneToOne(fetch = FetchType.LAZY)
       @PrimaryKeyJoinColumn
       public User getUsr() {
           return this.usr;
       }

       public void setUsr(User usr) {
           this.usr = usr;
       }

       @Column(name = "zip_cd", length = 8)
       public String getZipCd() {
           return this.zipCd;
       }

       public void setZipCd(String zipCd) {
           this.zipCd = zipCd;
       }

       @Column(name = "address")
       public String getAddress() {
           return this.address;
       }

       public void setAddress(String address) {
           this.address = address;
       }

       @Column(name = "ver")
       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       @Temporal(TemporalType.TIMESTAMP)
       @Column(name = "last_updated_date", length = 29)
       public Date getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Date lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

   }

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Email.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.util.Date;
   import javax.persistence.AttributeOverride;
   import javax.persistence.AttributeOverrides;
   import javax.persistence.Column;
   import javax.persistence.EmbeddedId;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.JoinColumn;
   import javax.persistence.ManyToOne;
   import javax.persistence.Table;
   import javax.persistence.Temporal;
   import javax.persistence.TemporalType;

   @Entity
   @Table(name = "email", schema = "public")
   public class Email implements java.io.Serializable {

       private EmailId id;
       private User usr;
       private String email;
       private Integer ver;
       private Date lastUpdatedDate;

       public Email() {
       }

       public Email(EmailId id, User usr) {
           this.id = id;
           this.usr = usr;
       }

       public Email(EmailId id, User usr, String email, Integer ver, Date lastUpdatedDate) {
           this.id = id;
           this.usr = usr;
           this.email = email;
           this.ver = ver;
           this.lastUpdatedDate = lastUpdatedDate;
       }

       @EmbeddedId
       @AttributeOverrides({
           @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false, length = 8) ),
           @AttributeOverride(name = "emailId", column = @Column(name = "email_id", nullable = false) ) })
       public EmailId getId() {
           return this.id;
       }

       public void setId(EmailId id) {
           this.id = id;
       }

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
       public User getUsr() {
           return this.usr;
       }

       public void setUsr(User usr) {
           this.usr = usr;
       }

       @Column(name = "email")
       public String getEmail() {
           return this.email;
       }

       public void setEmail(String email) {
           this.email = email;
       }

       @Column(name = "ver")
       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       @Temporal(TemporalType.TIMESTAMP)
       @Column(name = "last_updated_date", length = 29)
       public Date getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Date lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

   }

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.EmailId.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import javax.persistence.Column;
   import javax.persistence.Embeddable;

   @Embeddable
   public class EmailId implements java.io.Serializable {

       private String userId;
       private String emailId;

       public EmailId() {
       }

       public EmailId(String userId, String emailId) {
           this.userId = userId;
           this.emailId = emailId;
       }

       @Column(name = "user_id", nullable = false, length = 8)
       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       @Column(name = "email_id", nullable = false)
           public String getEmailId() {
           return this.emailId;
       }

       public void setEmailId(String emailId) {
           this.emailId = emailId;
       }

       public boolean equals(Object other) {
           if ((this == other))
               return true;
           if ((other == null))
               return false;
           if (!(other instanceof EmailId))
               return false;
           EmailId castOther = (EmailId) other;

           return ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
                  && castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())))
                  && ((this.getEmailId() == castOther.getEmailId()) || (this.getEmailId() != null
                  && castOther.getEmailId() != null && this.getEmailId().equals(castOther.getEmailId())));
       }

       public int hashCode() {
           int result = 17;

           result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
           result = 37 * result + (getEmailId() == null ? 0 : this.getEmailId().hashCode());
           return result;
       }

   }

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.Affiliation.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import java.util.Date;
   import javax.persistence.AttributeOverride;
   import javax.persistence.AttributeOverrides;
   import javax.persistence.Column;
   import javax.persistence.EmbeddedId;
   import javax.persistence.Entity;
   import javax.persistence.FetchType;
   import javax.persistence.JoinColumn;
   import javax.persistence.ManyToOne;
   import javax.persistence.Table;
   import javax.persistence.Temporal;
   import javax.persistence.TemporalType;

   @Entity
   @Table(name = "affiliation", schema = "public")
   public class Affiliation implements java.io.Serializable {

       private AffiliationId id;
       private Group grp;
       private User usr;
       private Integer ver;
       private Date lastUpdatedDate;

       public Affiliation() {
       }

       public Affiliation(AffiliationId id, Group grp, User usr) {
           this.id = id;
           this.grp = grp;
           this.usr = usr;
       }

       public Affiliation(AffiliationId id, Group grp, User usr, Integer ver, Date lastUpdatedDate) {
           this.id = id;
           this.grp = grp;
           this.usr = usr;
           this.ver = ver;
           this.lastUpdatedDate = lastUpdatedDate;
       }

       @EmbeddedId
       @AttributeOverrides({
       @AttributeOverride(name = "groupId", column = @Column(name = "group_id", nullable = false, length = 10) ),
       @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false, length = 8) ) })
       public AffiliationId getId() {
           return this.id;
       }

       public void setId(AffiliationId id) {
           this.id = id;
       }

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
       public Group getGrp() {
           return this.grp;
       }

       public void setGrp(Group grp) {
           this.grp = grp;
       }

       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
       public User getUsr() {
           return this.usr;
       }

       public void setUsr(User usr) {
           this.usr = usr;
       }

       @Column(name = "ver")
       public Integer getVer() {
           return this.ver;
       }

       public void setVer(Integer ver) {
           this.ver = ver;
       }

       @Temporal(TemporalType.TIMESTAMP)
       @Column(name = "last_updated_date", length = 29)
       public Date getLastUpdatedDate() {
           return this.lastUpdatedDate;
       }

       public void setLastUpdatedDate(Date lastUpdatedDate) {
           this.lastUpdatedDate = lastUpdatedDate;
       }

   }

.. sourcecode:: java
   :caption: org.debugroom.sample.spring.jpa.domain.entity.AffiliationId.java

   package org.debugroom.sample.spring.jpa.domain.entity;

   import javax.persistence.Column;
   import javax.persistence.Embeddable;

   @Embeddable
   public class AffiliationId implements java.io.Serializable {

       private String groupId;
       private String userId;

       public AffiliationId() {
       }

       public AffiliationId(String groupId, String userId) {
           this.groupId = groupId;
           this.userId = userId;
       }

       @Column(name = "group_id", nullable = false, length = 10)
       public String getGroupId() {
           return this.groupId;
       }

       public void setGroupId(String groupId) {
           this.groupId = groupId;
       }

       @Column(name = "user_id", nullable = false, length = 8)
       public String getUserId() {
           return this.userId;
       }

       public void setUserId(String userId) {
           this.userId = userId;
       }

       public boolean equals(Object other) {
           if ((this == other))
               return true;
           if ((other == null))
               return false;
           if (!(other instanceof AffiliationId))
               return false;
           AffiliationId castOther = (AffiliationId) other;

           return ((this.getGroupId() == castOther.getGroupId()) || (this.getGroupId() != null
                   && castOther.getGroupId() != null && this.getGroupId().equals(castOther.getGroupId())))
                   && ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
                   && castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())));
       }

       public int hashCode() {
           int result = 17;

           result = 37 * result + (getGroupId() == null ? 0 : this.getGroupId().hashCode());
           result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
           return result;
       }

   }

各アノテーションの概要は以下の通りである。

.. list-table:: javax.persistenceアノテーションの概要
   :header-rows: 1
   :widths: 30,20,50

   * - アノテーション
     - 設定
     - 概要
   * - @Entity
     - クラス
     -
   * - @Table
     - クラス
     -
   * - @Id
     - フィールド、メソッド
     -





