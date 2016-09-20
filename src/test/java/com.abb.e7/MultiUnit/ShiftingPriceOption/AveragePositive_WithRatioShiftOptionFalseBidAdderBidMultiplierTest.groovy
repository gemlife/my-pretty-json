package com.abb.e7.MultiUnit.ShiftingPriceOption

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class AveragePositive_WithRatioShiftOptionFalseBidAdderBidMultiplierTest {

  def calculationsParams = new CalculationsParameters(
      includeDVOM: true,
      includeStartupShutdownCost: true,
      shiftPrices: false,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Average",
      minUpTime: 12,
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      startRatio: [0.5,0.3,0.2],
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false,
      dfcm: 1.1,

  )
  def firstPeriod = new PeriodsDataFirst(
      incMinCap: 75,
      incMaxCap: 300,
      fuels: fuels,
      isAverageHeatRate: true,
      shutDownCost: 0.0,
      startFuels: startFuels,
      bidMultiplier: 1.3,
      bidAdder: 0.5,
  )
  def secondPeriod = new PeriodsDataSecond(
      incMinCap: 75,
      incMaxCap: 300,
      fuels: fuels,
      isAverageHeatRate: true,
      shutDownCost: 0.0,
      startFuels: startFuels,
      bidMultiplier: 1.3,
      bidAdder: 0.5,
  )
  def thirdPeriod = new PeriodsDataThird(
      incMinCap: 75,
      incMaxCap: 300,
      fuels: fuels,
      isAverageHeatRate: true,
      shutDownCost: 0.0,
      startFuels: startFuels,
      bidMultiplier: 1.3,
      bidAdder: 0.5,
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

    List<Pattern> pricePatterns = ["^78\\.6(\\d+)","^83\\.0(\\d+)","^87\\.5(\\d+)","^100\\.9(\\d+)"]
    List<Pattern> quantityPatterns = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"]

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