package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {


    @Override
    Meal save(Meal Meal);


    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id and m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);


    List<Meal> findAllByUserId(Sort sort, int userId);


    Meal findByIdAndUserId(int id, int userId);


    @Query("select m from Meal m inner join fetch m.user u where m.id = :id and u.id = :userId order by m.dateTime DESC ")
    Meal getWithUser(@Param("id") int id, @Param("userId") int userId);


    List<Meal> findAllByDateTimeAfterAndDateTimeBeforeAndUserId(Sort sort, LocalDateTime startDate, LocalDateTime endDate, int userId);

}
