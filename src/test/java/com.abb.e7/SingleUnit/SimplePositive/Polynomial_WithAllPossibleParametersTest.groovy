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

class Polynomial_WithAllPossibleParametersTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Exponential",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1", "Fuel N2"],
      startRatio: [0.6,0.4]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1", "Fuel N2"],
      regularRatio: [0.4,0.6],
      useMinCostFuel: false,
      dfcm: 1.1,
      handlingCost: 2.0,
  )
  def firstPeriod = new PeriodsDataFirst(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25.0,
      incMaxCap: 200.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )

  def json = new InputJSONWithSinglePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: firstPeriod,
  )

  def inputJson = json.buildInputJSON()

  @Test
    public void post() {
      def pricePatterns = [/^165\.5(\d+)/, /^191\.8(\d+)/,/^242\.0(\d+)/,/^308\.9(\d+)/]
      def quantities = [/25\.0/, /83\.3(\d+)/, /141\.6(\d+)/, /200\.0/]

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