.. include:: ../../../module.txt

.. _section-jpa-usage-delete-label:

単テーブルに対するデータ削除
================================================

CriteriaAPIを使用したデータ削除
------------------------------------------------

単テーブルにおいてデータ更新する場合は、JPAのCriteria APIを使用して実装する。

Criteria APIにおけるデータ削除では、EntityManager経由で取得した、永続化コンテキストに管理されているManaged状態のオブジェクトをremoveすることで実現する。
エンティティの管理状態に関して、意図せぬ更新が発生する可能性があるので、以下の内容を必ず理解しておくこと。
http://terasolunaorg.github.io/guideline/5.1.0.RELEASE/ja/ArchitectureInDetail/DataAccessJpa.html#entity

実際のDELETE文はトランザクションがコミットされるタイミングで実行されるので注意すること。

例) ユーザデータを削除する場合

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
       public boolean delete(User user) {
           //削除対象のエンティティをEntityManager経由で取得する。
           User deleteUser = find(user.getUserId());
           if(null == deleteUser){
            return false;
           }
           // remove()メソッドを実行する。
           remove(deleteUser);
           return true;
       }
    }
