package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

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
        UsersUtil.USERS.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return (repository.remove(id) != null);
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            int counter_local = counter.incrementAndGet();
            user.setId(counter_local);

            return repository.putIfAbsent(user.getId(), generateUserWithUniqueUserName(user, counter_local));
        }

        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    private User generateUserWithUniqueUserName(User user, int counter_local) {
        if (repository.values().stream().filter(user1 -> user1.getName().equals(user.getName())).collect(Collectors.toList()).size() > 0) {
            user.setName(user.getName() + counter_local);
        }
        return user;
    }


    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");

        List<User> userList = repository.values()
                .stream()
                .sorted(((user1, user2) -> (user1.getName().compareTo(user2.getName()) == 0 ? user1.getEmail().compareTo(user2.getEmail()) : user1.getName().compareTo(user2.getName()))))
                .collect(Collectors.toList());
        return userList;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);

        return (((repository.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList()).size()) > 0) ?
                repository.values()
                        .stream()
                        .filter(user -> user.getEmail().equals(email))
                        .collect(Collectors.toList()).get(0) : null);
    }
}
