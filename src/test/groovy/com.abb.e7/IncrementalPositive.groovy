package com.abb.e7

import org.junit.Test

class IncrementalPositive {

  @Test
  public void Incremental() {
    def calculationsParams = new CalculationsParameters(
        includeStartupShutdownCost: true
    )
    def unitCharacteristic = new UnitCharacteristic(
        minUpTime: 12
    )
//    def heatRatePairs = new MWHeatRatePair(
//
//    )
    def periodsData = new PeriodsData(
//        dvom: 2
    )


    def json = new E7TemplateJSON(
        calculationsParameters: calculationsParams,
        unitCharacteristic: unitCharacteristic,
        PeriodsData: periodsData,
//        mwHRPoints: heatRatePairs
    )
    println json.buildInputJSON().toPrettyString()
  }

  @Test
  public void HP() {
    def json = new MWHeatRatePair()

    println json.buildInputJSON()
  }

  @Test
  public void modelE7() throws Exception {
    def model = new E7TemplateJSON();
    def json = model.buildInputJSON();
    println model.buildInputJSON().toPrettyString()

  }
//  @Test
//  public void postExample() {
//    def e7TemplateJSON = new E7TemplateJSON();
//    def olga = e7TemplateJSON.buildInputJSON()
//    RestAssured.baseURI = "http://pl-s-depmdb01.pl.abb.com/api/SupplyCurveCalculation/";
//
//    Response r = RestAssured.given()
//        .contentType("application/json").
//        body(olga.toString()).
//        when().
//        post("");
//
//    //    assert r.get("/lotto").then().body("lotto.lottoId", equalTo(5));
//    println r.getStatusCode();
//    String body = r.getBody().asString();
//    System.out.println(body);
//
//     }
}
