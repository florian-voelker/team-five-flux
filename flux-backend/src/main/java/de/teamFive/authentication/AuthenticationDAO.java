package de.teamFive.authentication;

import de.teamFive.common.services.PersistenceService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Stateless
public class AuthenticationDAO {

    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.entityManager = PersistenceService.getEntityManager();
    }


    public Authentication save(Authentication authentication) {
        entityManager.getTransaction().begin();
        entityManager.persist(authentication);
        entityManager.getTransaction().commit();

        return authentication;
    }

    public Optional<Authentication> getAuthenticationByUsername(String username) {
        entityManager.getTransaction().begin();
        List<Authentication> result = entityManager.createQuery("SELECT a FROM Authentication a WHERE user.username = '" + username + "'", Authentication.class).getResultList();
        entityManager.getTransaction().commit();
        if (result.size() > 0) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
    }

}
