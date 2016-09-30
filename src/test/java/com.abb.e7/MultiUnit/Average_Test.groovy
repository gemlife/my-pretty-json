package com.abb.e7.MultiUnit

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.PeriodsData.PeriodsDataSecond
import com.abb.e7.model.PeriodsData.PeriodsDataThird
import com.abb.e7.model.Templates.InputJSONWithThreePeriods
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_Test {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
  )
  def firstPeriod = new PeriodsDataFirst(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataSecond(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataThird(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )

  def json = new InputJSONWithThreePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsDataFirst: firstPeriod,
      periodsDataSecond: secondPeriod,
      periodsDataThird: thirdPeriod,
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^53\\.4(\\d+)", "^56\\.4(\\d+)", "^59\\.4(\\d+)", "^68\\.3(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"] as List<Pattern>

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