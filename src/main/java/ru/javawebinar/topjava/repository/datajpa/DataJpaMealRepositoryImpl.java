package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    private static final Sort SORT_DATE_TIME = new Sort(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {

        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        } else {
            User user = crudUserRepository.getOne(userId);
            if (user == null) {
                return null;
            } else {
                meal.setUser(user);
                return crudRepository.save(meal);
            }
        }
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }


    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findByIdAndUserId(id, userId);
        return meal;
    }

    @Transactional
    @Override
    public Meal getWithUser(int id, int userId) {
        return crudRepository.getWithUser(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserId(SORT_DATE_TIME, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findAllByDateTimeAfterAndDateTimeBeforeAndUserId(SORT_DATE_TIME, startDate, endDate, userId);
    }
}
