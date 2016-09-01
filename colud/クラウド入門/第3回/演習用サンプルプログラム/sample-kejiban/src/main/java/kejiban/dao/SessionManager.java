package kejiban.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class SessionManager {

	private static Log log = LogFactory.getLog(SessionManager.class);
	
	private static SessionManager sessionManager = new SessionManager();
	
	private static SessionFactory sessionFactory;
/*
 	static {
		try {
			System.out.println("aaaaaaaaaaaaaa");
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch(HibernateException ex) {
			throw new RuntimeException("Configuration problem: " + ex.getMessage(), ex);
		}
	}
*/	
	private static final ThreadLocal threadSession = new ThreadLocal();
	
	public static Session currentSession() throws HibernateException {
		log.debug("Hibernate Session Manager :Fetching a hibernate session");
        Session s = (Session) threadSession.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = sessionFactory.openSession();
            threadSession.set(s);
        } 
        if (!s.isOpen()) {
            s = null;
            s = sessionFactory.openSession();
            threadSession.set(s);
        }
        
        if (!s.isConnected()){
        	s.reconnect(null);
        }
    	log.debug("Hibernate Session Manager :Fetching a hibernate session done.");
        return s;
	}
	
    public static void closeSession() throws HibernateException {
        log.debug("Hibernate Session Manager : closeSession");
        Session s = (Session) threadSession.get();
        threadSession.set(null);
        if (s != null && s.isOpen()) {
            log.debug("Hibernate Session Manager : closing session");
            s.close();
            log.debug("Hibernate Session Manager : session closed");
        }
        s = null;
    }
    
    public static void reconnect(Session session) throws HibernateException {
        if (!session.isConnected()) {
        	session.reconnect(null);
        }
        threadSession.set(session);
    }
    
    public static Session disconnectSession() throws HibernateException {
        Session session = currentSession();
    	if (session.isConnected() && session.isOpen()) {
			session.disconnect();
        }
        return session;
    }
    
    public SessionManager() {
        super();
    }

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public static Session openSession() throws HibernateException {
        return SessionManager.currentSession();
    }

}
