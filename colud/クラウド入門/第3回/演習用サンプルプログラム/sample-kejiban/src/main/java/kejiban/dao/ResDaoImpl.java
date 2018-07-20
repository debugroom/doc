package kejiban.dao;

import java.util.List;

import kejiban.dao.ResDao;

import org.seasar.hibernate3.S2SessionFactory;
import org.hibernate.Session;

public class ResDaoImpl {

    private S2SessionFactory sessionFactory;

    public ResDaoImpl(S2SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        if (sessionFactory != null) {
            return sessionFactory.getSession();
        } else {
            return null;
        }
    }

    public List getReses() {
        List result = getSession().createQuery("from Res r order by r.id").list();
        return result;
    }


}
