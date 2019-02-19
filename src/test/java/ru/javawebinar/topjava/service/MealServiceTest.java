package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static long startTestDatetime = System.currentTimeMillis();
    private static long endTestDatetime = System.currentTimeMillis();
    private static Map<String, Long> testsInfoMap = new LinkedHashMap<>();

    // private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);


    static {
        SLF4JBridgeHandler.install();
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TestName testName = new TestName();


    private static final Logger logger = getLogger(MealServiceTest.class);
  //private static final   Logger logger = LoggerFactory.getLogger(MealServiceTest.class);

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        logger.info(String.format("%-5s%-11s%-25s%-11s%n","Код","За единиц","Валюты","Рублей РФ"));
        logger.info(String.format("Test %s %s, spent %d microseconds",testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void succeeded(long nanos, Description description) {
            logInfo(description, "succeeded", nanos);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            logInfo(description, "failed", nanos);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            logInfo(description, "skipped", nanos);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
        }
    };


//    @Rule
//    public TestWatcher watcher = new TestWatcher() {
//
//        @Override
//        protected void starting(Description description) {
//            startTestDatetime = System.currentTimeMillis();
//        }
//
//        @Override
//        protected void finished(Description description) {
//            endTestDatetime = System.currentTimeMillis();
//            String testMethodName = testName.getMethodName();
//            long durationTest = endTestDatetime-startTestDatetime;
//            testsInfoMap.put(testMethodName, durationTest);
//            System.out.println(generateTestInfoMessage(testMethodName, durationTest));
//        }
//    };
//
//    private static String generateTestInfoMessage(String testMethodName, long durationTest) {
//        return "test " + testMethodName + " run time: " + durationTest + " ms";
//    }
//
//    @AfterClass
//    public static void writeTestResults() {
//        testsInfoMap.forEach((testMethodName, duration) -> System.out.println(generateTestInfoMessage(testMethodName, duration)));
//
//        //  testsInfoMap.forEach((testMethodName, duration) -> log.info(generateTestInfoMessage(testMethodName, duration)));
//    }


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