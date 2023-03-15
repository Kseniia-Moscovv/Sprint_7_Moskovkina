package ru.yandex.praktikum.scooter.client.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {
    protected static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/api/v1/";

    protected static final RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }

}
