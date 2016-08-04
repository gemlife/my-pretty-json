package com.abb.e7

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import io.restassured.RestAssured
import io.restassured.response.Response
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.junit.Test
import sun.net.httpserver.DefaultHttpServerProvider

class IncrementalPositive {

//  @Test
//  public void Incremental() {
//    def calculationsParams = new CalculationsParameters(
//        includeStartupShutdownCost: true
//    )
//    def unitCharacteristic = new UnitCharacteristic(
//        minUpTime: 12
//    )
//    def periodsData = new PeriodsData(
////        dvom: 2
//    )
//
//
//    def json = new E7TemplateJSON(
//        calculationsParameters: calculationsParams,
//        unitCharacteristic: unitCharacteristic,
//        PeriodsData: periodsData,
//    )
//    println json.buildInputJSON().toPrettyString()
//  }

  @Test
  public void postExample() {
    def price = [51.699090000000005, 53.379780000000004, 56.741160000000008, 56.741160000000008]
    def quantity = [75, 150, 225, 300]
    def price1 = 51.699090000000005
    def e7TemplateJSON = new E7TemplateJSON();
    def builder = e7TemplateJSON.buildInputJSON()
    RestAssured.baseURI = "http://pl-s-depmdb01.pl.abb.com/api/SupplyCurveCalculation/";

    Response r = RestAssured.given()
        .contentType("application/json").
        body(builder.toString()).
        when().
        post("");
    println r.getStatusCode();
    String body = r.getBody().asString();
    System.out.println(body);

    //prices
    assert body.contains("51")
    assert body.contains("53")
    assert body.contains("56")
    assert body.contains("56")
    //quantities
    assert body.contains("75")
    assert body.contains("150")
    assert body.contains("225")
    assert body.contains("300")
//    , quantity[0], price[1], quantity[1], price[2], quantity[2], price[3], quantity[3]")
  }
}
