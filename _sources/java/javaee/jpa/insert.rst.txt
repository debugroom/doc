.. include:: ../../../module.txt

.. _section-jpa-usage-insert-label:

単テーブルにおけるデータ追加
===========================================

実装方針
-------------------------------------------

単テーブルにおいてINSERTを実施する場合は、JPAのCriteria APIを使用して実装する。
GenericDaoの様に共通化したメソッドを経由して実行するとよい。

例) ユーザデータのみを追加する場合

test-javaee6-ejb org.debugroom.test.domain.service.impl.ejb.dbaccess.SimpleInsertService

.. sourcecode:: java
  :linenos:

  package org.debugroom.test.domain.service.impl.ejb.dbaccess;

  import java.util.Date;

  import javax.ejb.EJB;
  import javax.ejb.Stateless;
  import javax.ejb.TransactionAttribute;
  import javax.ejb.TransactionAttributeType;

  import org.debugroom.test.common.exception.BusinessException;
  import org.debugroom.test.domain.model.User;
  import org.debugroom.test.domain.repository.UserRepository;
  import org.debugroom.test.domain.service.dbaccess.SimpleInsertService;

  @Stateless
  public class SimpleInsertServiceImpl implements SimpleInsertService{

      @EJB
      UserRepository userRepository;

      @Override
      @TransactionAttribute(TransactionAttributeType.REQUIRED)
      public User saveUser(User user) throws BusinessException {

          //新規追加するユーザのデータを作成する。IDは現在のデータ件数に+1して、8桁に0パディングして作成する。
          Long numberOfUser = userRepository.count();

          String idFormat = new StringBuilder()
                                   .append("00000000")
                                   .append(numberOfUser)
                                   .toString();

          user.setUserId(idFormat.substring(idFormat.length() - 8, idFormat.length()));
          user.setLastUpdatedDateAndTime(new Date());

          // 既にユーザが存在している場合は、業務例外の仕様とする。(いったん対象のユーザを削除させる)
          if(!userRepository.save(user)){
              throw new BusinessException("E-0001");
          }
          return user;
       }
   }

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.UserRepositoryImpl

.. sourcecode:: java
  :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import javax.ejb.Stateless;
   import javax.ejb.TransactionAttribute;
   import javax.ejb.TransactionAttributeType;

   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;

   // トランザクションの管理はEJBに任せて、Stateless Session Beanとして実装する。
   // Serviceから呼ばれるため、トランザクションの伝播属性を一応、REQUIREDにしておく。(デフォルトも多分REQUIREDだと思うが)
   @Stateless
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public class UserRepositoryImpl extends GenericDaoImpl<User, String> implements UserRepository{

   @Override
   public boolean save(User user) {
       if(entityManager.contains(user)){
           return false;
       }
       //
       entityManager.persist(user);
       return true;
   }

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.GenericDaoImpl

.. sourcecode:: java
  :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import java.io.Serializable;
   import java.lang.reflect.ParameterizedType;

   import javax.persistence.EntityManager;
   import javax.persistence.PersistenceContext;

   import org.debugroom.test.domain.repository.GenericDao;

   @SuppressWarnings("unchecked")
   public abstract class GenericDaoImpl<T extends Serializable, ID extends Serializable>
                               implements GenericDao<T, ID>{

       Class<T> clazz;

       @PersistenceContext
       EntityManager entityManager;

       public GenericDaoImpl(){
           ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
           this.clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
       }

       @Override
       public T find(ID id) {
           return (T)entityManager.find(clazz, id);
       }

       // Criteria APIのpersit()メソッドで実現する。
       @Override
       public void persist(T entity) {
           entityManager.persist(entity);
       }

       @Override
       public void merge(T entity) {
           entityManager.merge(entity);
       }

       @Override
       public void remove(T entity) {
           entityManager.remove(entity);
       }
   }
