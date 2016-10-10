package com.abb.e7.SingleUnit.SelfShceduleMW

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Polynomial_Test {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
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
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^93\\.6(\\d+)", "^121\\.5(\\d+)", "^159\\.4(\\d+)", "^207\\.0(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["50\\.0", "100\\.0", "150\\.0", "200\\.0"] as List<Pattern>

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    List<?> quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
//    priceArray = extractUnderlyingList(priceArray)
//    quantityArray = extractUnderlyingList(quantityArray)
    println priceArray
    println quantityArray
//
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