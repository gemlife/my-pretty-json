package com.abb.e7.SingleUnit.SimplePositive

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.FuelsInputData
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.StartFuelsIDs
import com.abb.e7.model.Templates.InputJSONWithSinglePeriods
import com.abb.e7.model.UnitParameters
import io.restassured.path.json.JsonPath
import org.junit.Test

class Average_WithAllPosibleParametersTest {
// 1 start fuel and several regular fuels with ratio
  def calculationsParams = new CalculationParameters(
      includeStartupShutdownCost: true,
      shiftPrices: false,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1","Fuel N2"],
      startRatio: [0.5,0.3],
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false,
      handlingCost: 2.0,
      dfcm: 1.1,
  )
  def periodsData = new PeriodsDataFirst(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def json = new InputJSONWithSinglePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: periodsData,
  )

  def inputJson = json.buildInputJSON()

  @Test
  public void post() {
    def pricePatterns = [/^115\.7(\d+)/, /^120\.1(\d+)/, /^124\.6(\d+)/, /^138\.0(\d+)/]
    def quantities = [/75\.0/, /150\.0/, /225\.0/, /300\.0/]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    def quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
    priceArray = extractUnderlyingList(priceArray)
    quantityArray = extractUnderlyingList(quantityArray)
    println priceArray
    println quantityArray

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
