package com.abb.e7.core

import groovy.json.JsonBuilder
import io.restassured.RestAssured
import io.restassured.response.Response
import ru.qatools.properties.PropertyLoader

class SupplyCurveCalculationService {

    static def config = PropertyLoader.newInstance()
            .populate(ApplicationProperties.class)

    static def post(JsonBuilder builder) {
        println config.getBaseUrl()
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(builder.toString())
                .when()
                .post(config.getBaseUrl() + "/api/SupplyCurveCalculation/");
        println response.getStatusCode()
        return response.getBody().asString()
    }

    static def postWithLogging(JsonBuilder builder) {
        println builder.toPrettyString()
        post(builder)

    }
}
