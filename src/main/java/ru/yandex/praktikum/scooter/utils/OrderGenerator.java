package ru.yandex.praktikum.scooter.utils;

        import org.apache.commons.lang3.RandomStringUtils;
        import org.apache.commons.lang3.RandomUtils;
        import ru.yandex.praktikum.scooter.model.order.Order;

        import java.text.SimpleDateFormat;
        import java.util.Date;

public class OrderGenerator {
    public static Order getRandom() {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String address = RandomStringUtils.randomAlphabetic(10);
        String metroStation = RandomStringUtils.randomAlphabetic(10);
        String phone = RandomStringUtils.randomAlphabetic(11);
        int rentTime = RandomUtils.nextInt(1, 10);
        String deliveryDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String comment = RandomStringUtils.randomAlphabetic(10);
        String[] color = new String[]{"BLACK"};



        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}