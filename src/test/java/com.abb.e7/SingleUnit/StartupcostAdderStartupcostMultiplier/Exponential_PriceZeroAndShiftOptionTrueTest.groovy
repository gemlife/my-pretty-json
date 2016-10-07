package com.abb.e7.SingleUnit.StartupcostAdderStartupcostMultiplier

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Exponential_PriceZeroAndShiftOptionTrueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeStartupShutdownCost: true,
      priceZero: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Exponential",
      minUpTime: 12,
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
      regularRatio: [0.7, 0.3],
      useMinCostFuel: false,
      fuelIDs: ["Fuel N1", "Fuel N2"],
      dfcm: 1.1,
      handlingCost: 2.0,
  )
  def firstPeriod = new PeriodsDataInput(
      incMaxCap: 200,
      incMinCap: 25,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      incMaxCap: 200,
      incMinCap: 25,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      incMaxCap: 200,
      incMinCap: 25,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^36\\.4(\\d+)", "^37\\.6(\\d+)", "^58\\.4(\\d+)", "^58\\.4(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["25\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"] as List<Pattern>

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    List<?> quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
    priceArray = extractUnderlyingList(priceArray)
    quantityArray = extractUnderlyingList(quantityArray)
    println priceArray
    println quantityArray

    for (def i = 0; i < quantityPatterns.size() - 1; i++) {
      List<String> currentQuantityBlock = quantityArray.get(i) as List<String>
      for (def j = 0; j < currentQuantityBlock.size(); j++) {
        def appropriateQuantity = quantityPatterns.get(j)
        def currentQuantity = currentQuantityBlock.get(j).toString()
        assert currentQuantity.matches(appropriateQuantity)
      }
    }
    for (def i = 0; i < pricePatterns.size() - 1; i++) {
      List<String> currentPriceBlock = priceArray.get(i) as List<String>
      for (def j = 0; j < currentPriceBlock.size(); j++) {
        def appropriatePrice = pricePatterns.get(j)
        def currentPrice = currentPriceBlock.get(j).toString()
        assert currentPrice.matches(appropriatePrice)
      }
    }
  }

  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 1) {
      price = price.get(0)
    }
    return price
  }


}