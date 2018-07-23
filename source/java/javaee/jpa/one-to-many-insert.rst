.. include:: ../../../module.txt

.. _section-jpa-usage-one-to-many-insert-label:

1対多の関係テーブルにおけるデータ追加
========================================================

CriteriaAPIを使った親子テーブルへのデータ追加
-----------------------------------------------------------------------------

注文と注文明細のように、1対多の関連を持つテーブルにおいてデータ追加を実施する場合は、単テーブルにおける追加と同様、
JPAのCriteria APIを使用して実装する。GenericDaoの様に共通化したメソッドを経由して実行するとよい。

基本的な所作としては、1対多の関連のうち、1に相当する(親子関係で言えば、親に相当する)オブジェクトに必要な関連テーブルのオブジェクト(子に相当する)をセットして、persist()メソッドを実行する。
関連テーブルのオブジェクトも合わせて登録するためには、親となるエンティティクラスのOneToManyアノテーションにcascade属性を設定しておくことが必要である。

例) Userオブジェクトに加えて、関連テーブルである、Address、Email、Phoneも一緒にデータ登録する場合。

BackingBeanにて、リクエストパラメータを受け取り、Address、Email、Phoneをリストに含めて保持するUserオブジェクト作成し、ユーザ保存のロジックを呼び出す。

test-javaee6-web org.debugroom.test.app.web.dbaccess.OneToManyInsertFormBackingBean

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.app.web.dbaccess;

   import java.util.List;
   import java.util.ArrayList;

   import javax.ejb.EJB;
   import javax.enterprise.inject.Model;

   import org.debugroom.test.common.exception.BusinessException;
   import org.debugroom.test.common.exception.SystemException;
   import org.debugroom.test.common.support.dozer.DozerInstantiator;
   import org.debugroom.test.domain.model.Address;
   import org.debugroom.test.domain.model.AddressPK;
   import org.debugroom.test.domain.model.EmailPK;
   import org.debugroom.test.domain.model.Phone;
   import org.debugroom.test.domain.model.PhonePK;
   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.model.Email;
   import org.debugroom.test.domain.service.dbaccess.OneToManyInsertService;
   import lombok.Data;

   @Data
   @Model
   public class OneToManyInsertFormBackingBean {

       @EJB
       OneToManyInsertService oneToManyInsertService;

       @EJB
       DozerInstantiator dozerInstantiator;

       // オブジェクトの初期化
       private User user;
       { user = new User(); }

       private Email email;
       { email = new Email();
         email.setId(new EmailPK());
         email.getId().setEmailNo(0);
       }

       private Address address;
       { address = new Address();
         address.setId(new AddressPK());
         address.getId().setAddressNo(0);
       }

       private Phone phone;
       { phone = new Phone();
         phone.setId(new PhonePK());
         phone.getId().setPhoneNo(0);
       }

       public String register() throws SystemException{

           // データ保存用のsaveUserオブジェクトを作成し、リクエストパラメータをコピーする。
           // データのマッピングにはDozerライブラリを用いるが、SingletonBeanとしてdozerInstantiatorでラップしてマッピング実行
           // マッピングが煩雑なので、Helperのようなクラスを本当は作ったほうがよい。今回はサンプルのためそのまま記述。
           User saveUser = dozerInstantiator.getMapper().map(user, User.class);
           List<Email> emails = new ArrayList<Email>();
           List<Phone> phones = new ArrayList<Phone>();
           List<Address> addresses = new ArrayList<Address>();
           saveUser.setAddresses(addresses);
           saveUser.setPhones(phones);
           saveUser.setEmails(emails);

           Address saveAddress = dozerInstantiator.getMapper().map(address, Address.class);
           Email saveEmail = dozerInstantiator.getMapper().map(email, Email.class);
           Phone savePhone = dozerInstantiator.getMapper().map(phone, Phone.class);
           addresses.add(saveAddress);
           phones.add(savePhone);
           emails.add(saveEmail);

           try {
               // リクエストパラメータをマッピングした後、保存ロジックを実行
               saveUser = oneToManyInsertService.saveUser(saveUser);
               // 実行した結果をビュー向けのオブジェクトにマッピングしなおす。
               dozerInstantiator.getMapper().map(saveUser, user);
           } catch (BusinessException e) {
               throw new SystemException("E0003", e);
           }
           return "/jsf/dbaccess/oneToManyInsertOutput??faces-redirect-true";
       }
    }

test-javaee6-ejb org.debugroom.test.domain.service.impl.OneToManyInsertServiceImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.service.impl.ejb.dbaccess;

   import java.util.Date;

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
   import org.debugroom.test.domain.service.dbaccess.OneToManyInsertService;

   @Stateless
   public class OneToManyInsertServiceImpl implements OneToManyInsertService{

       @EJB
       UserRepository userRepository;

       @Override
       @TransactionAttribute(TransactionAttributeType.REQUIRED)
       public User saveUser(User user) throws BusinessException {
           // Userに新しいユーザIDと更新日時を設定する。
           Long numberOfUser = userRepository.count();
           String idFormat = new StringBuilder()
                                   .append("00000000")
                                   .append(numberOfUser)
                                   .toString();
           String newUserId = idFormat.substring(idFormat.length() - 8, idFormat.length());
           user.setUserId(newUserId);
           user.setLastUpdatedDateAndTime(new Date());

           // Addressオブジェクトに新しいユーザIDを設定する。
           for(Address address : user.getAddresses()){
              address.getId().setUserId(newUserId);
           }

           // Emailオブジェクトに新しいユーザIDを設定する。
           for(Email email : user.getEmails()){
              email.getId().setUserId(newUserId);
           }

           // Phoneオブジェクトに新しいユーザIDを設定する。
           for(Phone phone : user.getPhones()){
              phone.getId().setUserId(newUserId);
           }

          // GenericDaoImplのsave()メソッドを実行する。
           if(!userRepository.save(user)){
              throw new BusinessException("E-0001");
           };
           return user;
       }
    }

データ保存のメソッドは単一テーブル保存のときと変わらない。

test-javaee6-ejb org.debugroom.test.domain.service.impl.OneToManyInsertServiceImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import java.util.Date;
   import java.util.List;

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
       public boolean save(User user) {
           if(entityManager.contains(user)){
               return false;
           }
           persist(user);
           return true;
       }
   }

関連テーブルのデータもまとめて追加するには、エンティティクラスの@OneToManyアノテーションにおけるcascade属性を変更する。

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

       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Address> addresses;

       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Affiliation> affiliations;

       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Credential> credentials;

       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Email> emails;

       @OneToMany(mappedBy="duser", cascade = CascadeType.ALL)
       private List<Phone> phones;

       //omitted
   }

このとき、発行されるSQLは以下のようになる。

.. sourcecode:: html
   :linenos:

   insert into duser (is_login, last_updated_date_and_time, user_name, ver, user_id) values (?, ?, ?, ?, ?)

   insert into Address (address, address_details, ver, zip_code, address_no, user_id) values (?, ?, ?, ?, ?, ?)

   insert into Email (email, ver, email_no, user_id) values (?, ?, ?, ?)

   insert into Phone (phone_number, ver, phone_no, user_id) values (?, ?, ?, ?)


Userに加えて、Address, Email, Phoneも追加される。Credentialなどは値が設定されていないので更新されない。
