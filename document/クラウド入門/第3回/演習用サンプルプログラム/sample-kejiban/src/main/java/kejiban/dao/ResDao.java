package kejiban.dao;

import java.util.List;


import kejiban.entity.PageRequest;
import kejiban.entity.Res;
import kejiban.hibernate.Hibernate;

public class ResDao {

    public List getResesByPage(PageRequest pageRequest) {
        return Hibernate.currentSession()
                .createQuery("from Res as r order by r.id")
                .setFirstResult(pageRequest.getStart() - 1)
                .setMaxResults(pageRequest.getPageSize())
                .list();
    }

    public List getCountRes() {
        return Hibernate.currentSession()
                .createQuery("select count(*) from Res").list();

    }

    public void insertRes(Res res) {
        Hibernate.currentSession().save(res);
    }
}

