package com.abb.e7.SingleUnit.BidSelfSchedule

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class AveragePositive_RatioDefinedBidSSTrueGPzeroTest {

  def calculationsParams = new CalculationsParameters(
      shiftPrices: false,
      includeDVOM: true,
      bidTacticSelfScheduledMW: true,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      startRatio: null,
  )
  def fuels = new FuelsInputData(
      regularRatio: [0.5, 0.3, 0.2],
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      useMinCostFuel: false,
      dfcm: 1.1,
      handlingCost: 2.02,

  )
  def firstPeriod = new PeriodsDataFirst(
      generationPoint: 0,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataSecond(
      generationPoint: 0,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataThird(
      generationPoint: 0,
      shutDownCost: 300,
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

    List<Pattern> pricePatterns = ["0\\.0"]
    List<Pattern> quantityPatterns = ["0\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    List<?> quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
    priceArray = extractUnderlyingList(priceArray)
    quantityArray = extractUnderlyingList(quantityArray)
    println priceArray
    println quantityArray

    for (def i = 0; i < quantityPatterns.size() - 1; i++) {
      List<String> currentQuantityBlock = quantityArray.get(i)
      for (def j = 0; j < currentQuantityBlock.size(); j++) {
        def appropriateQuantity = quantityPatterns.get(j)
        def currentQuantity = currentQuantityBlock.get(j).toString()
        assert currentQuantity.matches(appropriateQuantity)
      }
    }
    for (def i = 0; i < pricePatterns.size() - 1; i++) {
      List<String> currentPriceBlock = priceArray.get(i)
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