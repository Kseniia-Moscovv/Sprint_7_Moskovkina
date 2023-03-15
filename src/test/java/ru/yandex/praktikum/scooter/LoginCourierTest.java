package ru.yandex.praktikum.scooter;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.scooter.client.CourierClient;
import ru.yandex.praktikum.scooter.model.courier.Courier;
import ru.yandex.praktikum.scooter.model.courier.CourierCredentials;
import ru.yandex.praktikum.scooter.utils.CourierGenerator;

import java.net.HttpURLConnection;

public class LoginCourierTest {
    private CourierClient courierClient = new CourierClient();
    private Courier courier;
    private int courierId;


    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Before
    public void setUp() {
        courier = CourierGenerator.getRandom();

        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    public void loginWithValidParameters() {
        int id = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("id", CoreMatchers.notNullValue())
                .extract().path("id");

        courierId = id;
    }

    @Test
    public void failToLoginCourierWithoutRequiredFields() {
        CourierCredentials credentials = new CourierCredentials("", "");

        courierClient.login(credentials)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", CoreMatchers.is("Недостаточно данных для входа"));
    }

    @Test
    public void failToLoginCourierWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "kokoko");

        courierClient.login(credentials)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", CoreMatchers.is("Учетная запись не найдена"));
    }

    @Test
    public void failToLoginCourierWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "");

        courierClient.login(credentials)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", CoreMatchers.is("Недостаточно данных для входа"));
    }

    @Test
    public void failToLoginNonExistentCourier() {
        CourierCredentials credentials = new CourierCredentials("kokokoshechka", "kokoshka");

        courierClient.login(credentials)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", CoreMatchers.is("Учетная запись не найдена"));
    }
}
