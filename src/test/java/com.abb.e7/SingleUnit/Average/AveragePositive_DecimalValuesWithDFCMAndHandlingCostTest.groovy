package com.abb.e7.SingleUnit.Average

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

class AveragePositive_DecimalValuesWithDFCMAndHandlingCostTest {
// with Handling cost and DFCM, using decimal values
  def calculationsParams = new CalculationsParameters(
      shiftPrices: true,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false,
      handlingCost: 2.005,
      dfcm: 1.103,
  )
  def periodsData = new PeriodsDataFirst(
      startFuels: startFuels,
      fuels: fuels,
      incMaxCap: 299.001,
      incMinCap: 73.999,
      isAverageHeatRate: true,
  )
  def json = new InputJSONWithSinglePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: periodsData,
  )

  def inputJson = json.buildInputJSON()

  @Test
  public void post() {
    def pricePatterns = [/^5[4-5]\.(\d+)/, /^5[7-8]\.(\d+)/, /^6[8-9]\.(\d+)/, /^6[8-9]\.(\d+)/]
    def quantities = [/73\.(\d+)/, /150\.0/, /225\.0/, /299\.(\d+)/]

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
