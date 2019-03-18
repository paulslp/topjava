package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;


    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            deleteRoles(user.getId());
        }
        insertRoles(user);
        return user;
    }


    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }


    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return getUserWithRoles(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getUserWithRoles(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, r.role FROM users u LEFT JOIN user_roles r on u.id = r.user_id ORDER BY name, email", new UserResultSetExtractor());
    }

    private User getUserWithRoles(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            return null;
        } else {
            user.setRoles(jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?",
                    (resultSet, i) -> Role.valueOf(resultSet.getString(1)),
                    user.getId()));
            return user;
        }
    }

    private void insertRoles(User user) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = new ArrayList<Role>(user.getRoles()).get(i);
                ps.setInt(1, user.getId());
                ps.setString(2, role.name());
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
    }

    private boolean deleteRoles(int id) {
        return jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id) != 0;
    }

    static final class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException {
            Map<Integer, User> mapUser = new LinkedHashMap<>();
            User user;
            while (rs.next()) {
                int key = rs.getInt("id");
                if (!mapUser.containsKey(key)) {
                    user = new User(rs.getInt("id"), rs.getString("name")
                            , rs.getString("email"), rs.getString("password")
                            , rs.getInt("calories_per_day"), rs.getBoolean("enabled")
                            , rs.getDate("registered"), null
                    );
                    user.setRoles(Collections.singleton(Role.valueOf(rs.getString("role"))));
                    mapUser.put(key, user);
                } else {
                    user = mapUser.get(key);
                    user.getRoles().add(Role.valueOf(rs.getString("role")));
                }
            }
            return new ArrayList<>(mapUser.values());
        }
    }
}
