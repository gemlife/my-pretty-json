package com.abb.e7.core

import groovy.xml.XmlUtil
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
                .post(config.getXmlUrl() + "nmarket-core/rest/inbound/MC/MCAPIGEN?cluster=DEV&timeZone=EST&env=Production&sourceType=FILE&username=nmarket&password=nmarket")
        println response.getStatusCode()
        return response.getBody().asString()

    }

    static def postWithLogging(builder) {
        def xmlFormat = new XmlUtil().serialize(builder)
        println xmlFormat
//        grails.converters.default.pretty.print (builder)
//        grails.converters.default.pretty.print (Boolean)
        post(builder)

    }
}
