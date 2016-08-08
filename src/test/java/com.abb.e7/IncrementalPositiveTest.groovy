package com.abb.e7

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.CalculationsParameters
import com.abb.e7.model.E7TemplateJSON
import com.abb.e7.model.PeriodsData
import com.abb.e7.model.UnitCharacteristic
import io.restassured.path.json.JsonPath
import org.junit.Test

class IncrementalPositiveTest {

//  @Test
//  public void Incremental() {
//    def calculationsParams = new CalculationsParameters(
//        includeStartupShutdownCost: true
//    )
//    def unitCharacteristic = new UnitCharacteristic(
////        minUpTime: 12
//    )
//    def periodsData = new PeriodsData(
//        dvom: 2
//    )
//    def json = new E7TemplateJSON(
//        calculationsParameters: calculationsParams,
//        unitCharacteristic: unitCharacteristic,
//        PeriodsData: periodsData,
//    )
//    println json.buildInputJSON().toPrettyString()
//  }

  def e7TemplateJSON = new E7TemplateJSON()
  def inputJson = e7TemplateJSON.buildInputJSON()

  def calculationsParams = new CalculationsParameters(
//      includeStartupShutdownCost: true,
      includeDVOM: true
  )
  def unitCharacteristic = new UnitCharacteristic(
        minUpTime: 12
  )
  def periodsData = new PeriodsData(
//      dvom: 1
  )
  def json = new E7TemplateJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      PeriodsData: periodsData,
  )
//  def incrementalPositive = new E7TemplateJSON()
//  def inputJson = json.buildInputJSON()

  @Test
  public void postExample() {
    def pricePatterns = [/^5[1-2]\.(\d+)/, /^53\.(\d+)/, /^5[6-7]\.(\d+)/, /^5[6-7]\.(\d+)/]
    def quantities = [/75\.0/, /150\.0/, /225\.0/, /300\.0/]

    String body = SupplyCurveCalculationService.post(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    def quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
    println priceArray
    println quantityArray
    priceArray = extractUnderlyingList(priceArray)
    quantityArray = extractUnderlyingList(quantityArray)

    for (def i = 0; i < priceArray.size(); i++) {
      def currentPrice = priceArray.get(i).toString()
      def appropriateRegex = pricePatterns.get(i)
      assert currentPrice.matches(appropriateRegex)
    }
    for (def i = 0; i < quantities.size(); i++) {
      def currentQuantity = quantityArray.get(i).toString()
      def appropriateQuantity = quantities.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
  }

  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 1) {
      price = price.get(0)
    }
    return price
  }

}
