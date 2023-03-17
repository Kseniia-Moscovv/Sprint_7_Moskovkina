package ru.yandex.praktikum.scooter.utils;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.praktikum.scooter.model.courier.Courier;

public class CourierGenerator {
    public static Courier getRandom() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);

        return new Courier(login, password, firstName);
    }
}
