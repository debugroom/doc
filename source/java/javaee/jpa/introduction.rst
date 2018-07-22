.. include:: ../../../module.txt

.. _section-jpa-introduction-label:

Introdcution
=====================================================

本ドキュメントではJPAにおけるデータベースアクセスの代表的なパターンにおける実装サンプルを記述する。

1. 単テーブルにおけるデータ検索
2. 複数件を取得するデータ検索
3. 単テーブルにおけるデータ追加
4. 単テーブルにおけるデータ更新
5. 単テーブルにおけるデータ削除
6. OneToManyの関連を持つテーブルにおけるデータ検索
7. OneToManyの関連を持つテーブルにおけるデータ追加
8. OneToManyの関連を持つテーブルにおけるデータ更新
9. OneToManyの関連を持つテーブルにおけるデータ削除
10. ManyToManyの関連を持つテーブルにおけるデータ検索
11. ManyToManyの関連を持つテーブルにおけるデータ追加
12. ManyToManyの関連を持つテーブルにおけるデータ更新
13. ManyToManyの関連を持つテーブルにおけるデータ削除

.. _section-jpa-pre-condition-label:

前提事項
-------------------------------------------------

実装サンプルは以下のプロジェクト構成としている。

1. test-parent :共通的なDependencyManagementやPluginManagement、Propertyの定義を行うプロジェクト

2. test-javaee6-ear :earファイルの構成を設定するためのプロジェクト

3. test-javaee6-ejb :Stateless Session Beanにて実装したビジネスロジックやデータベースアクセスロジック実装を含んだプロジェクト

4. test-domain :各ビジネスロジックやデータベースアクセスに関するインターフェースを含んだプロジェクト。基本JavaEEに非依存。

5. test-javaee6-web :JSFをベースに構成されている画面コンポーネントを含んだプロジェクト。Google Material Design Liteを使用。

.. _section-jpa-sample-project-label:

各画面とロジックの対応関係
-------------------------------------------------

test-javaee6-webプロジェクトには、各テーマごとに、対応するページを作成している。JBossを起動した後、以下のURLを実行すると各機能の実行ページに移る。

+----+---------------------+----------------------------------------------------------------------------+
| No | title               | URL                                                                        |
+====+=====================+============================================================================+
| 1  | Simple SELECT       | http://localhost:8080/test-javaee6-web/jsf/dbaccess/simpleSelect.xhtml     |
+----+---------------------+----------------------------------------------------------------------------+
| 2  | Simple SELECT List  | http://localhost:8080/test-javaee6-web/jsf/dbaccess/simpleSelectList.xhtml |
+----+---------------------+----------------------------------------------------------------------------+
| 3  | Simple INSERT       | http://localhost:8080/test-javaee6-web/jsf/dbaccess/simpleInsert.xhtml     |
+----+---------------------+----------------------------------------------------------------------------+
| 4  | Simple UPDATE       | http://localhost:8080/test-javaee6-web/jsf/dbaccess/simpleUpdate.xhtml     |
+----+---------------------+----------------------------------------------------------------------------+
| 5  | Simple DELETE       | http://localhost:8080/test-javaee6-web/jsf/dbaccess/simpleDelete.xhtml     |
+----+---------------------+----------------------------------------------------------------------------+
| 6  | ...(working)        | http://localhost:8080/test-javaee6-web/jsf/dbaccess/xxxxxxxxxxxx.xhtml     |
+----+---------------------+----------------------------------------------------------------------------+

 .. todo:: SampleプロジェクトをGitHubにアップする。

データベースアクセスロジックに関する共通事項
-------------------------------------------------

各エンティティごとに重複した実装を省略するために、本実装サンプルではGenericDAOパターンを採用する。
GenericDAOパターンとは、JPAエンティティクラスとそのキーとなるクラスを型パラメータにもつ、
データベースアクセスに関わる振る舞いを実装したクラスである。

test-domain org.debugroom.test.domain.repository.GenericDAO

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository;

   import java.io.Serializable;

   // 各エンティティに対応する型パラメータTとキーに対応する型パラメータIDをもつ。
   public interface GenericDao<T, ID extends Serializable> {
       //各エンティティに対応する共通的な操作を定義する。
       public T find(ID id);
       public void persist(T entity);
       public void merge(T entity);
       public void remove(T entity);
    }

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.GenericDaoImpl

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

   import java.io.Serializable;
   import java.lang.reflect.ParameterizedType;

   import javax.persistence.EntityManager;
   import javax.persistence.EntityManagerFactory;
   import javax.persistence.PersistenceContext;
   import javax.persistence.PersistenceUnit;

   import org.debugroom.test.domain.repository.GenericDao;

   @SuppressWarnings("unchecked")
   public abstract class GenericDaoImpl<T extends Serializable,
                      ID extends Serializable> implements GenericDao<T, ID>{

       Class<T> clazz;

       @PersistenceContext
       EntityManager entityManager;

       // 型パラメータで指定されたエンティティクラスを設定する。
       public GenericDaoImpl(){
           ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
           this.clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
       }

       //シンプルなテーブル操作では、JPAにおけるCriteria APIを使ったデータアクセス操作を実装。
       @Override
       public T find(ID id) {
           return (T)entityManager.find(clazz, id);
       }

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

GenericDAOを継承し、各エンティティに対応するインターフェース及び、データアクセスロジッククラスを作成する。例として、エンティティクラスUserに対するデータアクセスクラスを以下に示す。

test-domain org.debugroom.test.domain.repository.UserRepository

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository;

   import java.util.List;
   import org.debugroom.test.domain.model.User;

   //GenericDaoクラスを継承する。追加で必要なインターフェースを定義する。
   public interface UserRepository extends GenericDao<User, String> {
       public User findOne(String userId);
       public List<User> findAll();
       public Long count();
       public boolean exists(String userId);
       public boolean save(User user);
       public boolean update(User user);
       public boolean delete(User user);
    }

test-javaee6-ejb

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

   // Stateless Session Beanとして実装する。トランザクションの管理はEJBコンテナに任せる。
   @Stateless
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public class UserRepositoryImpl extends GenericDaoImpl<User, String> implements UserRepository{

       // 各エンティティの操作に対してGenericDaoのメソッドを使用して実装するパターン。
       @Override
       public User findOne(String userId) {
           return find(userId);
       }

       // JPQLを利用して実装するパターン
       @SuppressWarnings("unchecked")
       @Override
       public List<User> findAll() {
           Query query =  entityManager.createQuery("From User u");
           return (List<User>)query.getResultList();
       }

       @Override
       public boolean exists(String userId) {
           Query query =  entityManager.createQuery("select count(u) from User u where u.userId = :userId");
           query.setParameter("userId", userId);
           if(query.getResultList().size() == 0){
               return false;
           }
           return true;
       }

        @Override
        public Long count() {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class)));
            Query query =  entityManager.createQuery(criteriaQuery);
            return (Long)query.getSingleResult();
        }

        //JPAのCriteriaAPIを使って実装するパターン
        @Override
        public boolean save(User user) {
            if(entityManager.contains(user)){
                return false;
            }
            entityManager.persist(user);
            return true;
        }

        @Override
        public boolean update(User user) {
            User updateUser = find(user.getUserId());
            if(null == updateUser){
                return false;
            }
            updateUser.setUserName(user.getUserName());
            updateUser.setLogin(user.isLogin());
            updateUser.setLastUpdatedDateAndTime(new Date());

            return true;
        }

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

本サンプルでのデータベースアクセスに関する実装は基本上記の構成となることに留意すること。
