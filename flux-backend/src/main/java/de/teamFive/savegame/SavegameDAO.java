package de.teamFive.savegame;

import de.teamFive.common.services.PersistenceService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class SavegameDAO {

    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.entityManager = PersistenceService.getEntityManager();
    }

    public Savegame save(Savegame savegame) {
        entityManager.getTransaction().begin();
        entityManager.persist(savegame);
        entityManager.getTransaction().commit();

        return savegame;
    }
}
