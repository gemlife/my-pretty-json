package com.abb.e7

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.E7TemplateJSON
import io.restassured.path.json.JsonPath
import org.junit.Test

class IncrementalPositiveTest {
//
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

    def bodyOfMe = "{\n" +
            "              \"Results\": [{\n" +
            "                            \"Unit\": {\n" +
            "                                           \"Id\": 1,\n" +
            "                                           \"Name\": \"Copernicus GT\"\n" +
            "                            },\n" +
            "                            \"PQPairs\": [{\n" +
            "                                           \"Period\": \"2016-03-07T08:00:00Z\",\n" +
            "                                           \"Blocks\": [{\n" +
            "                                                         \"Id\": 0,\n" +
            "                                                         \"Price\": 51.699090000000005,\n" +
            "                                                         \"Quantity\": 75.0\n" +
            "                                           },\n" +
            "                                           {\n" +
            "                                                         \"Id\": 1,\n" +
            "                                                         \"Price\": 53.379780000000004,\n" +
            "                                                         \"Quantity\": 150.0\n" +
            "                                           },\n" +
            "                                           {\n" +
            "                                                         \"Id\": 2,\n" +
            "                                                         \"Price\": 56.741160000000008,\n" +
            "                                                         \"Quantity\": 225.0\n" +
            "                                           },\n" +
            "                                           {\n" +
            "                                                         \"Id\": 3,\n" +
            "                                                         \"Price\": 56.741160000000008,\n" +
            "                                                         \"Quantity\": 300.0\n" +
            "                                           }]\n" +
            "                            }]\n" +
            "              }]\n" +
            "}"
    def e7TemplateJSON = new E7TemplateJSON();
    def inputJson = e7TemplateJSON.buildInputJSON()

    @Test
    public void postExample() {
        def pricePatterns = [/^5[1-2]\.(\d+)/, /^53\.(\d+)/, /^5[6-7]\.(\d+)/, /^5[6-7]\.(\d+)/]
        def quantity = [75, 150, 225, 300]

        String body = SupplyCurveCalculationService.post(inputJson)
    }

    @Test
    public void newTest () {
        def pricePatterns = [/^5[1-2]\.(\d+)/, /^53\.(\d+)/, /^5[6-7]\.(\d+)/, /^5[6-7]\.(\d+)/]

        def price = JsonPath.from(bodyOfMe).get("Results.PQPairs.Blocks.Price")
        println price
        price = extractUnderlyingList(price)

        for (def i = 0; i < price.size(); i++) {
            def currentPrice = price.get(i).toString()
            def appropriateRegex = pricePatterns.get(i)
            assert currentPrice.matches(appropriateRegex)
        }
    }

    private static List<String> extractUnderlyingList(def price) {
        while (price.size() == 1) {
            price = price.get(0)
        }
        return price
    }

}
