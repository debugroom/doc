.. include:: ../../../module.txt

.. _section-jpa-usage-update-label:

単テーブルにおけるデータ更新
=======================================

実装方針
---------------------------------------
 
単テーブルにおいてデータ更新する場合は、JPAのCriteria APIを使用して実装する。各エンティティクラスにより更新する対象のデータ項目は異なるので、GerericDaoを継承したクラス内で実装する。
Criteria APIにおけるデータ更新では、EntityManager経由で取得した、永続化コンテキストに管理されているManaged状態のオブジェクトのプロパティを変更することで実現する。
エンティティの管理状態に関して、意図せぬ更新が発生する可能性があるので、以下の内容を必ず理解しておくこと。
http://terasolunaorg.github.io/guideline/5.1.0.RELEASE/ja/ArchitectureInDetail/DataAccessJpa.html#entity

実際のUPDATE文はトランザクションがコミットされるタイミングで実行されるので注意すること。

例) ユーザデータを更新する場合

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.UserRepositoryImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import java.util.Date;

   import javax.ejb.Stateless;
   import javax.ejb.TransactionAttribute;
   import javax.ejb.TransactionAttributeType;

   import org.debugroom.test.domain.model.User;
   import org.debugroom.test.domain.repository.UserRepository;

   @Stateless
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public class UserRepositoryImpl extends GenericDaoImpl<User, String> implements UserRepository{

       @Override
      public boolean update(User user) {
           //EntityManagerを経由して、永続化コンテキスト管理下にあるオブジェクトを取得する。
           //※GenericDaoのFind()メソッドで取得するのと同じこと。
           User updateUser = find(user.getUserId());
           if(null == updateUser){
                return false;
           }
           // 取得したオブジェクトのプロパティを変更する。
           updateUser.setUserName(user.getUserName());
           updateUser.setLogin(user.isLogin());
           updateUser.setLastUpdatedDateAndTime(new Date());

           return true;
       }

なお、JPAの楽観ロック機能を使用する場合は、@Versionが付与されたLong型のプロパティを作成しておく必要があるが、自動で更新してくれるので、ユーザが明示的に変更する必要はない。
