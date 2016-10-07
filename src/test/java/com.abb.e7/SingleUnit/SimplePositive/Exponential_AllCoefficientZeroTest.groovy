package com.abb.e7.SingleUnit.SimplePositive

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.FuelsInputData
import com.abb.e7.model.PeriodsDataInput
import com.abb.e7.model.StartFuelsIDs
import com.abb.e7.model.InputJSON

import com.abb.e7.model.UnitParameters
import io.restassured.path.json.JsonPath
import org.junit.Test

class Exponential_AllCoefficientZeroTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
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
      useMinCostFuel: false
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 25.0,
      incMaxCap: 200.0,
      coefficients: [325.0, 0.0, 0.0, 0.0],
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
    public void post() {
      def pricePatterns = [/^3\.0/, /^3\.0/,/^3\.0/,/^3\.0/]
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