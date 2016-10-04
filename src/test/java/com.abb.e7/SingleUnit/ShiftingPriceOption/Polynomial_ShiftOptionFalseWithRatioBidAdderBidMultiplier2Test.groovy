package com.abb.e7.SingleUnit.ShiftingPriceOption

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.FuelsInputData
import com.abb.e7.model.PeriodsData.PeriodsDataFirstWithoutHR
import com.abb.e7.model.PeriodsData.PeriodsDataSecondWithoutHR
import com.abb.e7.model.PeriodsData.PeriodsDataThirdWithoutHR
import com.abb.e7.model.StartFuelsIDs
import com.abb.e7.model.Templates.InputJSONWithThreePeriodsWithoutHR
import com.abb.e7.model.UnitParameters
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Polynomial_ShiftOptionFalseWithRatioBidAdderBidMultiplier2Test {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Polynomial",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1","Fuel N2"],
      startRatio: [0.6, 0.4]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2"],
      regularRatio: [0.4, 0.6],
      useMinCostFuel: false
  )
  def firstPeriod = new PeriodsDataFirstWithoutHR(
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
  )
  def secondPeriod = new PeriodsDataSecondWithoutHR(
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
  )
  def thirdPeriod = new PeriodsDataThirdWithoutHR(
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,

  )

  def json = new InputJSONWithThreePeriodsWithoutHR(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsDataFirst: firstPeriod,
      periodsDataSecond: secondPeriod,
      periodsDataThird: thirdPeriod,
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^75\\.6(\\d+)", "^99\\.4(\\d+)", "^145\\.1(\\d+)", "^206\\.0(\\d+)"] as List<Pattern>
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