package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            User ref = em.getReference(User.class, userId);
            meal.setUser(ref);
            em.persist(meal);
            return meal;

        } else {
            User ref = em.getReference(User.class, userId);
            meal.setUser(ref);

            return (em.createNamedQuery(Meal.UPDATE)
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("description", meal.getDescription())
                    .executeUpdate() != 0) ? meal : null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {

        try {
            return em.createNamedQuery(Meal.SELECT, Meal.class)
                    .setParameter(1, userId)
                    .setParameter(2, id)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }

    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter(1, userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED_WITH_FILTER, Meal.class)
                .setParameter(1, userId)
                .setParameter(2, startDate)
                .setParameter(3, endDate).getResultList();
    }
}