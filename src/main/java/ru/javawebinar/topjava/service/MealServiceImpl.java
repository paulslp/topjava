package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.List;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(int id, int authUserId) {
        checkNotFoundWithId(repository.delete(id, authUserId), id);
    }

    @Override
    public List<Meal> getAll(Predicate<Meal> filter) {
        return repository.getAll(filter);
    }


    @Override
    public Meal get(int id, int authUserId) {
        return checkNotFoundWithId(repository.get(id, authUserId), id);
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }


}