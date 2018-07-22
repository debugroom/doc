.. include:: ../../../module.txt

.. _section-jpa-usage-select-list-label:

複数件データを取得するSELECT
====================================================

JPQL・CriteriaBuilderを利用したデータ検索
-----------------------------------------------------

結果が複数件のデータとなるSELECTを実施する場合は、JPQLを使用して実装する。実行結果のソートや条件指定がある場合は、適宜CriteriaBuilderで組み立ててもよい。

例)ユーザの全件検索、条件指定検索、CriteriaBuilderを使用した検索

test-javaee6-ejb org.debugroom.test.domain.repository.impl.jpa.UserRepository

.. sourcecode:: java
   :linenos:

   package org.debugroom.test.domain.repository.impl.jpa;

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

       @SuppressWarnings("unchecked")
       @Override
       public List<User> findAll() {
           //JPQLを使用して、データアクセスする。結果のソート等必要に応じて
           Query query =  entityManager.createQuery("From User u");
           return (List<User>)query.getResultList();
       }

       @Override
       public boolean exists(String userId) {
           //条件指定を行う場合のJPQL
           Query query =  entityManager.createQuery("select count(u) from User u where u.userId = :userId");
           query.setParameter("userId", userId);
           if(query.getResultList().size() == 0){
               return false;
           }
        return true;
           }

        @Override
        public Long count() {
            // CriteriaBuilderを使用する場合
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class)));
            Query query =  entityManager.createQuery(criteriaQuery);
            return (Long)query.getSingleResult();
        }
