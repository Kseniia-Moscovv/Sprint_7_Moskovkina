package ru.yandex.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.scooter.client.base.Client;
import ru.yandex.praktikum.scooter.model.order.Order;
import ru.yandex.praktikum.scooter.model.order.OrderId;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    protected static final String ORDER_URI = BASE_URI + "orders/";

    @Step("Create order {order}")
    public ValidatableResponse createOrder(Order order) {
        return given().spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Get orderID by Track {t}")
    public ValidatableResponse getOrderIdByTrack(int t) {
        return given().spec(getBaseSpec())
                .when()
                .get(ORDER_URI + "track?t=" + t)
                .then();
    }

    @Step("Accept order {id} for courier {courierId}")
    public ValidatableResponse accept(int id, int courierId) {
        return given().spec(getBaseSpec())
                .when()
                .put(ORDER_URI + "accept/" + id + "?courierId=" + courierId)
                .then();
    }

    @Step("Finish order {id}")
    public ValidatableResponse finish(OrderId orderId) {
        return given().spec(getBaseSpec())
                .body(orderId)
                .when()
                .put(ORDER_URI + "finish/" + orderId.getId())
                .then();
    }

    @Step("Get orders for courier {courierId}")
    public ValidatableResponse getList(int courierId) {
        return given().spec(getBaseSpec())
                .when()
                .get(ORDER_URI + "?courierId=" + courierId)
                .then();
    }

}
