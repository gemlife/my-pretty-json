package com.abb.e7.SingleUnit.StartupcostAdderStartupcostMultiplier

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.PeriodsData.PeriodsDataSecond
import com.abb.e7.model.PeriodsData.PeriodsDataThird
import com.abb.e7.model.Templates.InputJSONWithThreePeriods
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_PriceZeroAndShiftOptionTrueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeStartupShutdownCost: true,
      priceZero: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
      minUpTime: 12,
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
      regularRatio: [0.5, 0.3, 0.2],
      useMinCostFuel: false,
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      dfcm: 1.1,
      handlingCost: 2.0,
  )
  def firstPeriod = new PeriodsDataFirst(
      incMaxCap: 350,
      incMinCap: 50,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      isAverageHeatRate: true,
      fuels: fuels,
      startFuels: startFuels,
  )
  def secondPeriod = new PeriodsDataSecond(
      incMaxCap: 350,
      incMinCap: 50,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      isAverageHeatRate: true,
      fuels: fuels,
      startFuels: startFuels,
  )
  def thirdPeriod = new PeriodsDataThird(
      incMaxCap: 350,
      incMinCap: 50,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      isAverageHeatRate: true,
      fuels: fuels,
      startFuels: startFuels,
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

    List<Pattern> pricePatterns = ["^85\\.9(\\d+)", "^90\\.4(\\d+)", "^112\\.7(\\d+)", "^112\\.7(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["50\\.0", "150\\.0", "225\\.0", "350\\.0"] as List<Pattern>

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