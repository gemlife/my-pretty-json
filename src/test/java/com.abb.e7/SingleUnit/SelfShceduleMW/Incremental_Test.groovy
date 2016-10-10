package com.abb.e7.SingleUnit.SelfShceduleMW

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Incremental_Test {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Incremental",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^78\\.6(\\d+)", "^80\\.8(\\d+)", "^83\\.0(\\d+)", "^87\\.5(\\d+)"] as List<Pattern>
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