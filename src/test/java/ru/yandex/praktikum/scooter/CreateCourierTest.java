package ru.yandex.praktikum.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
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
    @DisplayName("Positive check to create courier")
    @Description("Check to create courier with valid parameters: login/password/first name")
    public void createCourierWithValidParameters() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .and()
                .assertThat()
                .body("ok", CoreMatchers.is(true));

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int id = loginResponse.extract().path("id");
        courierId = id;

        loginResponse.assertThat()
                .body("id", CoreMatchers.notNullValue());
    }

    @Test
    @DisplayName ("Negative check to create double courier")
    @Description("Check that double courier doesn't create")
    public void failToCreateDoubleCourier() {
        Courier courier = new Courier("kotek", "kot", "Gershman");

        courierClient.createCourier(courier);

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Этот логин уже используется"));
    }

    @Test
    @DisplayName ("Negative check to create courier without required fields")
    @Description("Check that courier doesn't create without login and password")
    public void failToCreateCourierWithoutRequiredFields() {
        Courier courier = new Courier("", "", "");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName ("Negative check to create courier without password")
    @Description("Check that courier doesn't create without login OR password")
    public void failToCreateCourierWithoutPassword() {
        Courier courier = new Courier("kotek", "", "Gershman");

        courierClient.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName ("Negative check to create courier with similar login")
    @Description("Check that courier doesn't create with similar login")
    public void failToCreateCourierWithSimilarLogin() {
        Courier courier = new Courier("kotek", "kot", "Gershman");
        Courier courierWithSimilarLogin = new Courier("kotek", "koshka", "Ksupchik");

        courierClient.createCourier(courier);

        courierClient.createCourier(courierWithSimilarLogin)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .and()
                .assertThat()
                .body("message", CoreMatchers.is("Этот логин уже используется"));
    }
}
