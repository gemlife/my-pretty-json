package com.abb.e7

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

class IncrementalPositive_WithoutRegularRatioTest {
// 1 start fuel and several regular fuels with ratio
  def calculationsParams = new CalculationsParameters(
      shiftPrices: true,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Incremental",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      regularRatio: null,
  )
  def periodsData = new PeriodsDataFirst(
      startFuels: startFuels,
      fuels: fuels,
  )
  def json = new InputJSONWithSinglePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: periodsData,
  )

  def inputJson = json.buildInputJSON()

  @Test
  public void post() {
    def pricePatterns = [/^4[0-1]\.(\d+)/, /^4[1-2]\.(\d+)/, /^4[4-5]\.(\d+)/, /^4[4-5]\.(\d+)/]
    def quantities = [/75\.0/, /150\.0/, /225\.0/, /300\.0/]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
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
