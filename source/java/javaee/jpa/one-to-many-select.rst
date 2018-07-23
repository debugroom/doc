.. include:: ../../../module.txt

.. _section-jpa-one-to-many-label:

1対多の関係テーブルにおけるデータ取得
===============================================================================

LazyRoadとEagerFetchでのCriteriaAPIデータ検索
--------------------------------------------------------------------------------

注文と注文明細のように、1対多の関連を持つテーブルにおけるSELECTを実施する場合は、単テーブルにおける検索と同様、
JPAのCriteria APIを使用して実装する。GenericDaoの様に共通化したメソッドを経由して実行するとよい。

ただし、ネストしたエンティティの参照が発生する場合、デフォルトでは、遅延ロード(Lazy-load)として、
アクセスしたプロパティのデータ取得するためのSQLが発生する。プロパティへのアクセスの有無やデータ件数に応じてLazy-loadか
テーブル結合してデータ取得するオプション(Eager-Fetch)がよいかは異なる。必要に応じて、オプションを切り替えること。

なお、Lazy-Loadか、Eager-Fetchとするか、フェッチ戦略はデフォルトではエンティティ固定となる。
ある状況下でのみ、Eager-Fetchとする場合などは、JPQLを使用したデータ取得を検討すること。
また、関連するエンティティのデータをキーにしてデータ取得する場合も、JPQLを使用してデータ取得する。

例) Emailをレイジーロードで、addressをイーガーフェッチを使用して、ユーザを検索する場合

test-javaee6-ejb org.debugroom.test.domain.service.impl.ejb.dbaccess.OneToManySelectServiceImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.service.impl.ejb.dbaccess;

   import java.util.List;

   import javax.ejb.EJB;
   import javax.ejb.Stateless;

   import org.debugroom.test.domain.model.Email;
   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;
   import org.debugroom.test.domain.service.dbaccess.OneToManySelectService;

   @Stateless
   public class OneToManySelectServiceImpl implements OneToManySelectService{

       @EJB
       UserRepository userRepository;

       @Override
       public User getUser(String userId) {
           User user = userRepository.findOne(userId);
           System.out.println("Lazy load start.");
           for(Email email : user.getEmails()){
               System.out.println(email.getEmail()); // <--プロパティアクセスのタイミングでSQLが再発行
           }
           return user;
       }

    }

データ取得の方法をEager-Fetchに変更する場合は、エンティティクラスの@OneToManyアノテーションにおけるfetch属性を変更する。

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.model;

   import java.io.Serializable;
   import javax.persistence.*;
   import java.util.Date;
   import java.util.List;

   import lombok.AllArgsConstructor;
   import lombok.Builder;

   /**
    * The persistent class for the duser database table.
    *
    */
   @AllArgsConstructor
   @Builder
   @Entity
   @Table(name="duser")
   @NamedQuery(name="User.findAll", query="SELECT u FROM User u")
   public class User implements Serializable {

       private static final long serialVersionUID = 1L;

       @Id
       @Column(name="user_id")
       private String userId;

       @Column(name="is_login")
       private Boolean isLogin;

       @Temporal(TemporalType.DATE)
       @Column(name="last_updated_date_and_time")
       private Date lastUpdatedDateAndTime;

       @Column(name="user_name")
       private String userName;

       private Integer ver;

       @OneToMany(mappedBy="duser", fetch=FetchType.EAGER) //<- fetch戦略を変更
       private List<Address> addresses;

       @OneToMany(mappedBy="duser")
       private List<Affiliation> affiliations;

       @OneToMany(mappedBy="duser")
       private List<Credential> credentials;

       @OneToMany(mappedBy="duser")　　// <- デフォルトではLazy-Loadのため、
       private List<Email> emails;   //    プロパティアクセスが発生したタイミングでSQL発行する。

       @OneToMany(mappedBy="duser")
       private List<Phone> phones;

       //omitted
   }

このとき、発行されるSQLは以下のようになる。

.. sourcecode:: html
   :linenos:

    select
        user0_.user_id as user_id1_5_1_,
        user0_.is_login as is_login2_5_1_,
        user0_.last_updated_date_and_time as last_upd3_5_1_,
        user0_.user_name as user_nam4_5_1_,
        user0_.ver as ver5_5_1_,
        addresses1_.user_id as user_id2_5_3_,
        addresses1_.address_no as address_1_0_3_,
        addresses1_.user_id as user_id2_0_3_,
        addresses1_.address_no as address_1_0_0_,
        addresses1_.user_id as user_id2_0_0_,
        addresses1_.address as address3_0_0_,
        addresses1_.address_details as address_4_0_0_,
        addresses1_.ver as ver5_0_0_,
        addresses1_.zip_code as zip_code6_0_0_
    from duser user0_
    left outer join Address addresses1_
    　　　　on user0_.user_id=addresses1_.user_id
    where user0_.user_id=?

    [stdout] Lazy load start.

    select
         emails0_.user_id as user_id2_5_1_,
         emails0_.email_no as email_no1_3_1_,
         emails0_.user_id as user_id2_3_1_,
         emails0_.email_no as email_no1_3_0_,
         emails0_.user_id as user_id2_3_0_,
         emails0_.email as email3_3_0_,
         emails0_.ver as ver4_3_0_
    from
    　　　　Email emails0_ where emails0_.user_id=?

    [stdout] debugroom@nttdata.co.jp

    [stdout] debugroom2@nttdata.co.jp

EagerFetchした関連テーブルのデータも常に取得されるようになるため、常にアクセスされるわけではないデータはLazyLoadとしておくほうが望ましい。


例) 関連エンティティのデータをキーにして、データ取得を行う場合。

test-javaee6-ejb org.debugroom.test.domain.service.impl.OneToManyServiceImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.service.impl.ejb.dbaccess;

   import javax.ejb.EJB;
   import javax.ejb.Stateless;

   import org.debugroom.test.domain.model.Email;
   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;
   import org.debugroom.test.domain.service.dbaccess.OneToManySelectService;

   @Stateless
   public class OneToManySelectServiceImpl implements OneToManySelectService{

       @EJB
       UserRepository userRepository;

       @Override
       public User getUser(Email email) {
           return userRepository.findOneByEmail(email.getEmail());
       }
   }

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.UserRepository

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import javax.ejb.Stateless;
   import javax.ejb.TransactionAttribute;
   import javax.ejb.TransactionAttributeType;
   import javax.persistence.Query;
   import javax.persistence.criteria.CriteriaBuilder;
   import javax.persistence.criteria.CriteriaQuery;

   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;

   @Stateless
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public class UserRepositoryImpl extends GenericDaoImpl<User, String> implements UserRepository{

       @Override
       public User findOneByEmail(String email) {
           Query query = entityManager.createQuery(""
                + "SELECT u FROM User u, Email e "
                + "WHERE "
                + "e.email = :email "
                + "and e.id.userId = u.userId");
           query.setParameter("email", email);
           return (User)query.getSingleResult();
       }
   }

このとき、発行するSQLは以下のようになる。

.. sourcecode:: html
   :linenos:

   select
      user0_.user_id as user_id1_5_,
      user0_.is_login as is_login2_5_,
      user0_.last_updated_date_and_time as last_upd3_5_,
      user0_.user_name as user_nam4_5_,
      user0_.ver as ver5_5_
   from duser user0_
   cross join Email email1_
   where email1_.email=?
     and email1_.user_id=user0_.user_id

   select
     addresses0_.user_id as user_id2_5_1_,
     addresses0_.address_no as address_1_0_1_,
     addresses0_.user_id as user_id2_0_1_,
     addresses0_.address_no as address_1_0_0_,
     addresses0_.user_id as user_id2_0_0_,
     addresses0_.address as address3_0_0_,
     addresses0_.address_details as address_4_0_0_,
     addresses0_.ver as ver5_0_0_,
     addresses0_.zip_code as zip_code6_0_0_
   from Address addresses0_
   where addresses0_.user_id=?

上記では、addressに対するデータアクセスが生じていないにも関わらず、SQLが余分に発行されている。
これは、Entityクラスにデフォルトの指定でFetch.Eagerを設定しているためである。

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.model;

   import java.io.Serializable;
   import javax.persistence.*;
   import java.util.Date;
   import java.util.List;

   import lombok.AllArgsConstructor;
   import lombok.Builder;

   /**
    * The persistent class for the duser database table.
    *
    */
   @AllArgsConstructor
   @Builder
   @Entity
   @Table(name="duser")
   @NamedQuery(name="User.findAll", query="SELECT u FROM User u")
   public class User implements Serializable {

       private static final long serialVersionUID = 1L;

       @Id
       @Column(name="user_id")
       private String userId;

       @Column(name="is_login")
       private Boolean isLogin;

       @Temporal(TemporalType.DATE)
       @Column(name="last_updated_date_and_time")
       private Date lastUpdatedDateAndTime;

       @Column(name="user_name")
       private String userName;

       private Integer ver;

       @OneToMany(mappedBy="duser", fetch=FetchType.EAGER) //<- fetchTypeがEagerが指定されているため。
       private List<Address> addresses;

       @OneToMany(mappedBy="duser")
       private List<Affiliation> affiliations;

       @OneToMany(mappedBy="duser")
       private List<Credential> credentials;

       @OneToMany(mappedBy="duser")
       private List<Email> emails;

       @OneToMany(mappedBy="duser")
       private List<Phone> phones;

       //omitted
   }

このように、状況によっては、不要なSQLが実行される可能性があるので、エンティティクラスのフェッチ戦略を変更する場合は、よく検討を行うこと。
