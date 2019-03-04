package ru.javawebinar.topjava.service.DataJPA;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJPATest extends AbstractMealServiceTest {
}
