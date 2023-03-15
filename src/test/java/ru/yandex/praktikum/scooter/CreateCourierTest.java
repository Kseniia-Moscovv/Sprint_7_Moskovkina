package ru.yandex.praktikum.scooter;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.scooter.client.CourierClient;
import ru.yandex.praktikum.scooter.model.courier.Courier;
import ru.yandex.praktikum.scooter.model.courier.CourierCredentials;
import ru.yandex.praktikum.scooter.utils.CourierGenerator;

import java.net.HttpURLConnection;

public class CreateCourierTest {
    private CourierClient courierClient = new CourierClient();
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    public void createCourierWithValidParameters() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", CoreMatchers.is(true));

        int id = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", CoreMatchers.notNullValue())
                .extract().path("id");

        courierId = id;
    }

    @Test
    public void failToCreateDoubleCourier() {
        Courier courier = new Courier("kotek", "kot", "Gershman");

        courierClient.create(courier);

        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Этот логин уже используется"));
    }

    @Test
    public void failToCreateCourierWithoutRequiredFields() {
        Courier courier = new Courier("", "", "");

        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void failToCreateCourierWithoutPassword() {
        Courier courier = new Courier("kotek", "", "Gershman");

        courierClient.create(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void failToCreateCourierWithSimilarLogin() {
        Courier courier = new Courier("kotek", "kot", "Gershman");
        Courier courierWithSimilarLogin = new Courier("kotek", "koshka", "Ksupchik");

        courierClient.create(courier);

        courierClient.create(courierWithSimilarLogin)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Этот логин уже используется"));
    }
}
