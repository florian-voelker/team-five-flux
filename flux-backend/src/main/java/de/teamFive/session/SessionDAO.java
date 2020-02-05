package de.teamFive.session;

import de.teamFive.common.services.PersistenceService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class SessionDAO {
    private final Logger logger = Logger.getLogger(SessionDAO.class.getCanonicalName());

    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.entityManager = PersistenceService.getEntityManager();
    }

    public Session save(Session session) {
        entityManager.getTransaction().begin();
        entityManager.persist(session);
        entityManager.getTransaction().commit();
        logger.info(session.toString());
        return session;
    }

    public Session update(Session session) {
        entityManager.getTransaction().begin();
        entityManager.merge(session);
        entityManager.getTransaction().commit();
        return session;
    }

    public void delete(Session session) {
        entityManager.getTransaction().begin();
        entityManager.remove(session);
        entityManager.getTransaction().commit();
    }

    public Optional<Session> findBySessionId(String id) {
        entityManager.getTransaction().begin();
        List<Session> results = entityManager.createQuery("SELECT s FROM Session s WHERE s.id = '" + id + "'", Session.class).getResultList();
        entityManager.getTransaction().commit();
        if (results.size() > 0) {
            return Optional.of(results.get(0));
        } else {
            return Optional.empty();
        }
    }
}
