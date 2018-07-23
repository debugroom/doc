.. include:: ../../../module.txt

.. _section-jpa-usage-one-to-many-delete-label:

1対多の関係テーブルにおけるデータ削除
==============================================================================

CriteriaAPIを使った親子テーブルの一括データ削除
-------------------------------------------------------------------------------

注文と注文明細のように、1対多の関連を持つテーブルにおけるデータ削除を実施する場合は、単テーブルにおける削除と同様、
JPAのCriteria APIを使用して実装する。基本的には関連するデータの中で親となるテーブルのデータを取得して、remove()メソッドを呼び出す。トランザクションがコミットしたタイミングでDELETE文が発行される。

例) ユーザと、関連テーブルとして、住所、メール、電話番号をまとめて削除する場合。

test-javaee6-ejb org.debugroom.test.domain.service.impl.ejb.dbaccess.OneToManyDeleteServiceImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.service.impl.ejb.dbaccess;

   import java.util.List;

   import javax.ejb.EJB;
   import javax.ejb.Stateless;
   import javax.ejb.TransactionAttribute;
   import javax.ejb.TransactionAttributeType;

   import org.debugroom.test.common.exception.BusinessException;
   import org.debugroom.test.common.support.dozer.DozerInstantiator;
   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;
   import org.debugroom.test.domain.service.dbaccess.OneToManyDeleteService;

   @Stateless
   public class OneToManyDeleteServiceImpl implements OneToManyDeleteService{

       @EJB
       UserRepository userRepository;

       @EJB
       DozerInstantiator dozerInstantiator;

       @Override
       @TransactionAttribute(TransactionAttributeType.REQUIRED)
       public User deleteUser(User user) throws BusinessException {
           if(!userRepository.delete(user)){
               throw new BusinessException("E0004");
           }
           return dozerInstantiator.getMapper().map(
                user, User.class);
       }

   }

test-javaee6-ejb org.debugroom.test.domain.repository.impl.UserRepository

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import javax.ejb.Stateless;
   import javax.ejb.TransactionAttribute;
   import javax.ejb.TransactionAttributeType;

   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;

   @Stateless
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public class UserRepositoryImpl extends GenericDaoImpl<User, String> implements UserRepository{

       @Override
       public boolean delete(User user) {
           User deleteUser = find(user.getUserId());
           if(null == deleteUser){
               return false;
           }
           remove(deleteUser);
           return true;
       }
    }

関連テーブルを更新するためには、エンティティクラスの@OneToManyアノテーションにおけるCascadeType属性をREMOVEもしくはALLにしておく必要がある。また、ここではアノテーション@Versionを使用して、楽観ロックによるロングトランザクションの排他制御を実現する。

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.model;

   import java.io.Serializable;
   import javax.persistence.*;
   import java.util.Date;
   import java.util.List;

   import lombok.AllArgsConstructor;
   import lombok.Builder;

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

       // 楽観ロックとして@Versionアノテーションを指定
       @Version
       private Integer ver;

       // CascadeTypeをALLに設定
       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Address> addresses;

       // CascadeTypeをALLに設定
       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Affiliation> affiliations;

       // CascadeTypeをALLに設定
       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Credential> credentials;

       // CascadeTypeをALLに設定
       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Email> emails;

       // CascadeTypeをALLに設定
       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Phone> phones;

       //omitted
    }

このとき、発行するSQLは以下のようになる。 デフォルトでは関連するテーブル毎にLazy-Loadでデータ取得が行われる。
関連するテーブルが多い場合は、JPQLでEager-Fetchによるデータ取得を検討すること。

.. sourcecode:: html
   :linenos:

   select
       user0_.user_id as user_id1_5_0_,
       user0_.is_login as is_login2_5_0_,
       user0_.last_updated_date_and_time as last_upd3_5_0_,
       user0_.user_name as user_nam4_5_0_,
       user0_.ver as ver5_5_0_
   from duser user0_
   where user0_.user_id=?

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

   select
       affiliatio0_.user_id as user_id2_5_2_,
       affiliatio0_.group_id as group_id1_1_2_,
       affiliatio0_.user_id as user_id2_1_2_,
       affiliatio0_.group_id as group_id1_1_1_,
       affiliatio0_.user_id as user_id2_1_1_,
       affiliatio0_.ver as ver3_1_1_,
       group1_.group_id as group_id1_6_0_,
       group1_.group_name as group_na2_6_0_,
       group1_.ver as ver3_6_0_
    from Affiliation affiliatio0_
    left outer join grp group1_ on
        affiliatio0_.group_id=group1_.group_id
    where affiliatio0_.user_id=?

    select
       credential0_.user_id as user_id2_5_1_,
       credential0_.credential_id as credenti1_2_1_,
       credential0_.user_id as user_id2_2_1_,
       credential0_.credential_id as credenti1_2_0_,
       credential0_.user_id as user_id2_2_0_,
       credential0_.credential_key as credenti3_2_0_,
       credential0_.credential_type as credenti4_2_0_,
       credential0_.expired_date_and_time as expired_5_2_0_,
       credential0_.ver as ver6_2_0_
    from Credential credential0_ where credential0_.user_id=?

    select
       emails0_.user_id as user_id2_5_1_,
       emails0_.email_no as email_no1_3_1_,
       emails0_.user_id as user_id2_3_1_,
       emails0_.email_no as email_no1_3_0_,
       emails0_.user_id as user_id2_3_0_,
       emails0_.email as email3_3_0_,
       emails0_.ver as ver4_3_0_
    from Email emails0_
    where emails0_.user_id=?

    select
       phones0_.user_id as user_id2_5_1_,
       phones0_.phone_no as phone_no1_4_1_,
       phones0_.user_id as user_id2_4_1_,
       phones0_.phone_no as phone_no1_4_0_,
       phones0_.user_id as user_id2_4_0_,
       phones0_.phone_number as phone_nu3_4_0_,
       phones0_.ver as ver4_4_0_
    from Phone phones0_
    where phones0_.user_id=?

    delete from Address
    where address_no=?
         and user_id=?

    delete from Email
    where email_no=?
       and user_id=?

    delete from Phone
    where phone_no=?
       and user_id=?

    delete from duser
    where user_id=?
          and ver=?
