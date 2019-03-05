package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);


    @Query("select u from User u left join fetch u.meals m where u.id = :id ORDER BY m.dateTime DESC ")
    User getWithMeals(@Param("id") int id);


    @Override
    User save(User user);


    default User getById(Integer id) {
        return findById(id).orElse(null);
    }

    @Override
    List<User> findAll(Sort sort);

    User getByEmail(String email);
}
