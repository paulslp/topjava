package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);


    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.USERS.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        repository.computeIfPresent(id, (idUser, oldUser) -> null);
        return true;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            int counter_local = counter.incrementAndGet();
            user.setId(counter_local);
            if (repository.values().contains(user.getName())) {
                user.setName(user.getName() + counter_local);
            }
            repository.put(user.getId(), user);
            return user;
        }

        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> userList = new ArrayList<>(repository.values());
        userList.sort(Comparator.comparing(AbstractNamedEntity::getName));
        return userList;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.entrySet().stream()
                .filter(integerUserEntry -> integerUserEntry.getValue().getEmail().equals(email))
                .collect(Collectors.toList())
                .get(0)
                .getValue();
    }
}
