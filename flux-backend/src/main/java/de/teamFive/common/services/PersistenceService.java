package de.teamFive.common.services;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class PersistenceService {
    private static EntityManager entityManager;

    /**
     * Creates
     *
     * @return
     */
    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("db");
            entityManager = factory.createEntityManager();
        }
        return entityManager;
    }

}
