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
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = REST_URL + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable("id") int id) {
        super.update(meal, id);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = REST_URL)
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = REST_URL + "/filter")
    public List<MealTo> getBetweenOptional(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                           @RequestParam(value = "startTime", required = false) LocalTime startTime,
                                           @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                           @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @GetMapping(REST_URL)
    public List<MealTo> getAll() {
        return super.getAll();
    }

}