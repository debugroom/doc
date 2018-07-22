.. include:: ../../../module.txt

.. _section-jpa-usage-simple-select-label:

単テーブルにおける検索
==================================================================

CriteriaAPIを使ったデータ検索
------------------------------------------------------------------

単テーブルにおけるSELECTを実施する場合は、JPAのCriteria APIを使用して実装する。GenericDaoの様に共通化したメソッドを経由して実行するとよい。

例) ユーザを単体検索する場合

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.UserRepositoryImpl

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
       public User findOne(String userId) {
           // GenericDaoが持つfindメソッドを呼ぶ。
           return find(userId);
       }
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

       // 実際には、型パラメータTがUserとして、このメソッドが実行される。
       @Override
       public T find(ID id) {
           return (T)entityManager.find(clazz, id);
       }

    }
