package ru.yandex.praktikum.scooter;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.client.OrderClient;
import ru.yandex.praktikum.scooter.model.order.Order;

import java.net.HttpURLConnection;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    OrderClient orderClient = new OrderClient();

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "{index}: Order")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"Камилла", "Роуд", "Малхолланд Драйв", "4", "+79999999999", 3, "2023/03/20", "", new String[]{"BLACK"}},
                {"Диана", "Селуин", "Малхолланд Драйв", "4", "+711111111111", 3, "2023/03/25", "Hello!", new String[]{"GREY", "BLACK"}},
                {"Дэвид", "Линч", "Малхолланд Драйв", "4", "+70000000000", 3, "2023/03/30", "", new String[]{}},

        };
    }

    @Test
    public void createOrder() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

         orderClient.create(order)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .and()
                .assertThat()
                .body("track", CoreMatchers.notNullValue());

    }

}
