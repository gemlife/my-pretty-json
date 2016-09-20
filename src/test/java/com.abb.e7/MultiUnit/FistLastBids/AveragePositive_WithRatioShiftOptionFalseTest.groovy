package com.abb.e7.MultiUnit.FistLastBids

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class AveragePositive_WithRatioShiftOptionFalseTest {

  def calculationsParams = new CalculationsParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
      firstBidHeatRate: true,
      lastBidHeatRate: true,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Incremental",
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
      incMinCap: 50,
      incMaxCap: 350,
      fuels: fuels,
      startFuels: startFuels,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataSecond(
      incMinCap: 50,
      incMaxCap: 350,
      fuels: fuels,
      startFuels: startFuels,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataThird(
      incMinCap: 50,
      incMaxCap: 350,
      fuels: fuels,
      startFuels: startFuels,
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

    List<Pattern> pricePatterns = ["^58\\.4(\\d+)", "^61\\.9(\\d+)", "^65\\.3(\\d+)", "^75\\.6(\\d+)"]
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