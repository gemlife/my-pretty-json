package com.abb.e7.MultiUnit

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

class Exponential_Test {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Exponential",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1"],
  )
  def firstPeriod = new PeriodsDataFirstWithoutHR(
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: false,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 0.493, 0.009, 0.05],
  )
  def secondPeriod = new PeriodsDataSecondWithoutHR(
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: false,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 0.493, 0.009, 0.05],
  )
  def thirdPeriod = new PeriodsDataThirdWithoutHR(
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: false,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 0.493, 0.009, 0.05],
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

    List<Pattern> pricePatterns = ["^27\\.8(\\d+)", "^28\\.0(\\d+)", "^29\\.6(\\d+)", "^49\\.1(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["50\\.0", "100\\.0", "150\\.0", "200\\.0"] as List<Pattern>

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    List<?> quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
//    priceArray = extractUnderlyingList(priceArray)
//    quantityArray = extractUnderlyingList(quantityArray)
    println priceArray
    println quantityArray

//    for (def i = 0; i < quantityPatterns.size() - 1; i++) {
//      List<String> currentQuantityBlock = quantityArray.get(i)
//      for (def j = 0; j < currentQuantityBlock.size(); j++) {
//        def appropriateQuantity = quantityPatterns.get(j)
//        def currentQuantity = currentQuantityBlock.get(j).toString()
//        assert currentQuantity.matches(appropriateQuantity)
//      }
//    }
//    for (def i = 0; i < pricePatterns.size() - 1; i++) {
//      List<String> currentPriceBlock = priceArray.get(i)
//      for (def j = 0; j < currentPriceBlock.size(); j++) {
//        def appropriatePrice = pricePatterns.get(j)
//        def currentPrice = currentPriceBlock.get(j).toString()
//        assert currentPrice.matches(appropriatePrice)
//      }
//    }
//  }
//
//  private static List<String> extractUnderlyingList(def price) {
//    while (price.size() == 1) {
//      price = price.get(0)
//    }
//    return price
  }


}