.. include:: ../../../module.txt

.. _section-jpa-usage-one-to-many-update-label:

1対多の関係テーブルにおけるデータ更新
========================================================

CriteriaAPIを使った親子テーブルの一括データ更新
------------------------------------------------------------------------------------------

注文と注文明細のように、1対多の関連を持つテーブルにおけるデータ更新を実施する場合は、単テーブルにおける更新と同様、
JPAのCriteria APIを使用して実装する。基本的には関連するデータの中で親となるテーブルのデータを取得して、更新したいプロパティに入力値をセットするだけでよい。トランザクションがコミットしたタイミングでUPDATE文が発行される。

例) ユーザテーブルにおけるユーザ名と、関連テーブルとして、住所、メール、電話番号をまとめて更新する場合。

test-javaee6-ejb org.debugroom.test.domain.service.impl.ejb.dbaccess.OneToManyUpdateServiceImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.service.impl.ejb.dbaccess;

   import javax.ejb.EJB;
   import javax.ejb.Stateless;
   import javax.ejb.TransactionAttribute;
   import javax.ejb.TransactionAttributeType;

   import org.debugroom.test.common.exception.BusinessException;
   import org.debugroom.test.domain.model.Address;
   import org.debugroom.test.domain.model.Email;
   import org.debugroom.test.domain.model.Phone;
   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;
   import org.debugroom.test.domain.service.dbaccess.OneToManyUpdateService;

   @Stateless
   public class OneToManyUpdateServiceImpl implements OneToManyUpdateService{

       @EJB
       UserRepository userRepository;

       @Override
       @TransactionAttribute(TransactionAttributeType.REQUIRED)
       public User updateUser(User user) throws BusinessException {
           // 変更対象データで、親テーブルに相当するエンティティを取得する。
           User updateUser =  userRepository.find(user.getUserId());

           // 入力パラメータがNULLではなく、かつ変更があるのであれば、プロパティを更新する。
           if(user.getUserName() != null && !user.getUserName().equals(updateUser.getUserName())){
               updateUser.setUserName(user.getUserName());
           }

           // 関連テーブルはList型で保持されているため、ループでキーが一致し、変更があるもののみ更新する実装とする。
           // 件数が多くなる場合は別の実装方法でプロパティ変更してもよい。
           for(Address address : user.getAddresses()){
               for(Address updateAddress: updateUser.getAddresses()){
                   // 入力パラメータがNULLではなく、キーが一致し、かつ変更があるのであれば、プロパティを更新する。
                   if(address != null && address.getZipCode() != null &&
                           address.getId().equals(updateAddress.getId()) &&
                           !address.getZipCode().equals(updateAddress.getZipCode())){
                       updateAddress.setZipCode(address.getZipCode());
                   }
                   // 入力パラメータがNULLではなく、キーが一致し、かつ変更があるのであれば、プロパティを更新する。
                   if(address != null && address.getAddress() != null &&
                           address.getId().equals(updateAddress.getId()) &&
                           !address.getAddress().equals(updateAddress.getAddress())){
                       updateAddress.setAddress(address.getAddress());
                   }
                   // 入力パラメータがNULLではなく、キーが一致し、かつ変更があるのであれば、プロパティを更新する。
                   if(address != null && address.getAddressDetails() != null &&
                           address.getId().equals(updateAddress.getId()) &&
                           !address.getAddressDetails().equals(updateAddress.getAddressDetails())){
                       updateAddress.setAddressDetails(address.getAddressDetails());
                   }
               }
           }

           // 関連テーブルはList型で保持されているため、ループでキーが一致し、変更があるもののみ更新する実装とする。
           // 件数が多くなる場合は別の実装方法でプロパティ変更してもよい。
           for(Email email : user.getEmails()){
               for(Email updateEmail : updateUser.getEmails()){
                   // 入力パラメータがNULLではなく、キーが一致し、かつ変更があるのであれば、プロパティを更新する。
                   if(email != null && email.getEmail() != null &&
                           email.getId().equals(updateEmail.getId()) &&
                           !email.getEmail().equals(updateEmail.getEmail())){
                       updateEmail.setEmail(email.getEmail());
                   }
               }
           }

           // 関連テーブルはList型で保持されているため、ループでキーが一致し、変更があるもののみ更新する実装とする。
           // 件数が多くなる場合は別の実装方法でプロパティ変更してもよい。
           for(Phone phone : user.getPhones()){
               for(Phone updatePhone : updateUser.getPhones()){
                   // 入力パラメータがNULLではなく、キーが一致し、かつ変更があるのであれば、プロパティを更新する。
                   if(phone != null && phone.getPhoneNumber() != null &&
                           phone.getId().equals(updatePhone.getId()) &&
                           ! phone.getPhoneNumber().equals(updatePhone.getPhoneNumber())){
                       updatePhone.setPhoneNumber(phone.getPhoneNumber());
                   }
               }
           }
           return updateUser;
       }

関連テーブルを更新するためには、エンティティクラスの@OneToManyアノテーションにおけるCascadeType属性をMERGEもしくはALLにしておく必要がある。また、ここではアノテーション@Versionを使用して、楽観ロックによるロングトランザクションの排他制御を実現する。

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

       // 楽観ロックとして@Versionカラムを指定
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
関連するテーブルが多い場合は、JPQLでテーブル結合したデータ取得を検討すること。

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

   update duser set
     is_login=?,
     last_updated_date_and_time=?,
     user_name=?,
     ver=?
   where user_id=?

   update Address set
     address=?,
     address_details=?,
     ver=?,
     zip_code=?
   where address_no=?
     and user_id=?

   update Phone set
     phone_number=?,
     ver=?
   where phone_no=?
     and user_id=?

   update Email set
     email=?,
     ver=?
   where email_no=?
     and user_id=?
