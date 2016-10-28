package com.abb.e7.core

import io.restassured.RestAssured
import io.restassured.response.Response
import ru.qatools.properties.PropertyLoader

class NMarketService {

    static def config = PropertyLoader.newInstance()
            .populate(ApplicationProperties.class)

    static def post(builder) {
        println config.getXmlUrl()
        Response response = RestAssured.given()
                .contentType("application/xml")
                .body(builder.toString())
                .when()
                .post(config.getXmlUrl() + "nmarket-core/rest/inbound/IRELAND/IRETXPTAPI?username=nmarket&password=nmarket&timeZone=CPT&sourceType=FILE")
        println response.getStatusCode()
        return response.getBody().asString()

    }

    static def postWithLogging(builder) {
        println builder
        post(builder)

    }
}
