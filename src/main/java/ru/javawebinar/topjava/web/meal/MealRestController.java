package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meal";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = REST_URL + "/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping(REST_URL + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = REST_URL + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = REST_URL + "/filter")
    public List<MealTo> getBetweenOptional(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                           @RequestParam(value = "startTime", required = false) LocalTime startTime,
                                           @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                           @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween((startDate == null) ? LocalDate.of(1970, 1, 1) : startDate,
                (startTime == null) ? LocalTime.of(0, 0) : startTime,
                (endDate == null) ? LocalDate.of(3000, 1, 1) : endDate,
                (endTime == null) ? LocalTime.of(23, 59, 59) : endTime);
    }

}