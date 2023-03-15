package ru.yandex.praktikum.scooter;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.scooter.client.CourierClient;
import ru.yandex.praktikum.scooter.client.OrderClient;
import ru.yandex.praktikum.scooter.model.courier.Courier;
import ru.yandex.praktikum.scooter.model.courier.CourierCredentials;
import ru.yandex.praktikum.scooter.model.order.Order;
import ru.yandex.praktikum.scooter.model.order.OrderId;
import ru.yandex.praktikum.scooter.model.order.OrderListResponse;
import ru.yandex.praktikum.scooter.model.order.OrderResponse;
import ru.yandex.praktikum.scooter.utils.CourierGenerator;
import ru.yandex.praktikum.scooter.utils.OrderGenerator;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class GetOrderListTest {
    private OrderClient orderClient = new OrderClient();
    private CourierClient courierClient = new CourierClient();

    private final int NUMBER_OF_ORDERS = 5;

    private int courierId;
    private ArrayList<Integer> orderIds = new ArrayList<>();

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Before
    public void setUp() {
        Courier courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        int id = courierClient.login(CourierCredentials.from(courier))
                .extract().path("id");
        courierId = id;

        for (int i = 0; i < NUMBER_OF_ORDERS; i++) {
            Order order = OrderGenerator.getRandom();
            int track = orderClient.create(order)
                    .extract().path("track");

            int orderId = orderClient.getOrderIdByTrack(track)
                    .extract().path("order.id");
            orderIds.add(orderId);
            orderClient.accept(orderId, courierId);

            if (i % 2 != 0) {
                orderClient.finish(new OrderId(orderId));
            }
        }
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    public void getOrderList() {
        OrderListResponse orderList = orderClient.getList(courierId)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract().as(OrderListResponse.class);

        ArrayList<OrderResponse> orders = orderList.getOrders();

        ArrayList<Integer> responseOrderIds = new ArrayList<>();

        for (int i = 0; i < orders.size(); i++) {
            responseOrderIds.add(orders.get(i).id);
        }

        assertThat("Expected Order IDs do not match", orderIds, containsInAnyOrder(responseOrderIds));
    }
}
