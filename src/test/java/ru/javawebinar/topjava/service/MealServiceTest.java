package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {


    @Autowired
    private MealService service;

    private static Map<String, Long> testsInfoMap = new LinkedHashMap<>();


    static {
        SLF4JBridgeHandler.install();
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TestName testName = new TestName();


    private static final Logger logger = getLogger(MealServiceTest.class);


    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        logger.info("                                                            ");
        logger.info(String.format("%-20s%-20s%-20s", "Test name", "Status", "Duration,ms"));
        logger.info("------------------------------------------------------------");
        logger.info(String.format("%-20s%-20s%-20d", testName, status, TimeUnit.NANOSECONDS.toMillis(nanos)));
        logger.info("------------------------------------------------------------");
        logger.info("                                                            ");

        testsInfoMap.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
        }
    };

    @AfterClass
    public static void writeTestResults() {
        logger.info(String.format("%-20s%s", "Test name", "Duration,ms"));
        logger.info("------------------------------------------------------------");
        for (Map.Entry<String, Long> entry : testsInfoMap.entrySet()) {
            logger.info(String.format("%-20s%-20s", entry.getKey(), entry.getValue()));
        }
        logger.info("------------------------------------------------------------");
    }


    @Test
    public void delete() {
        logger.info("starting");
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void create() {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }


    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }


}