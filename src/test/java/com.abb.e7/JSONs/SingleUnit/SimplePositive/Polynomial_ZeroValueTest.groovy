package com.abb.e7.JSONs.SingleUnit.SimplePositive

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.CalculationParameters
import com.abb.e7.modelJSON.FuelsInputData
import com.abb.e7.modelJSON.PeriodsDataInput
import com.abb.e7.modelJSON.StartFuelsIDs
import com.abb.e7.modelJSON.InputJSON
import com.abb.e7.modelJSON.UnitData
import com.abb.e7.modelJSON.UnitParameters
import io.restassured.path.json.JsonPath
import org.junit.Test

class Polynomial_ZeroValueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Polynomial",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1"],
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 0.0,
      incMaxCap: 200.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
  )

  def singleUnit = new UnitData(
      unitCharacteristic: unitCharacteristic.buildUCInputJSON(),
      periodsData: [firstPeriod.buildPRInputJSON()],
      bidLibraryArray: []
  )

  def json = new InputJSON (
      calculationsParameters: calculationsParams,
      inputData: [singleUnit.buildSPInputJSON()]
  )

  def inputJson = json.buildSPInputJSON()

  @Test
    public void post() {
      def pricePatterns = [/^0\.0(\d+)/, /^59\.1(\d+)/, /^91\.1(\d+)/, /^136\.6(\d+)/]
      def quantities = [/0\.0(\d+)/, /66\.6(\d+)/, /133\.3(\d+)/, /200\.(\d+)/]

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