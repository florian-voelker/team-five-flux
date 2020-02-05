package de.teamFive.common.user;

import de.teamFive.common.services.PersistenceService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class UserDAO {
    private final Logger log = Logger.getLogger(UserDAO.class.getCanonicalName());

    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.entityManager = PersistenceService.getEntityManager();
    }

    public User save(User user) {
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        return user;
    }

    public User update(User user) {
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        return user;
    }

    public List<User> getAllUser() {
        entityManager.getTransaction().begin();
        List<User> result = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        entityManager.getTransaction().commit();
        return result;
    }


    public Optional<User> getUserById(Long id) {
        entityManager.getTransaction().begin();
        User result = entityManager.createQuery("SELECT u FROM User u WHERE id = " + id + "", User.class).getSingleResult();
        entityManager.getTransaction().commit();
        if (result != null) {
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    public boolean doesExist(String username) {
        entityManager.getTransaction().begin();
        List<User> result = entityManager.createQuery("SELECT u FROM User u WHERE username = '" + username + "'", User.class).getResultList();
        entityManager.getTransaction().commit();
        return !result.isEmpty();
    }

}
