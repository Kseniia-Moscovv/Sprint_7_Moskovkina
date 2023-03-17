package ru.yandex.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.scooter.client.base.Client;
import ru.yandex.praktikum.scooter.model.courier.Courier;
import ru.yandex.praktikum.scooter.model.courier.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends Client {
    protected static final String COURIER_URI = BASE_URI + "courier/";

    @Step("Create courier {courier}")
    public ValidatableResponse createCourier(Courier courier) {
        return given().spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Login courier {courierCredentials}")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given().spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(COURIER_URI + "login")
                .then();
    }

    @Step("Delete courier {id}")
    public ValidatableResponse delete(int id) {
        return given().spec(getBaseSpec())
                .when()
                .delete(COURIER_URI + id)
                .then();
    }
}
