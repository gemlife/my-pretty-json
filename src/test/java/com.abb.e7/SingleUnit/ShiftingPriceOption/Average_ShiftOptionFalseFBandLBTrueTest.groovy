package com.abb.e7.SingleUnit.ShiftingPriceOption

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.PeriodsData.PeriodsDataSecond
import com.abb.e7.model.PeriodsData.PeriodsDataThird
import com.abb.e7.model.Templates.InputJSONWithThreePeriods
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_ShiftOptionFalseFBandLBTrueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      firstBidHeatRate: true,
      lastBidHeatRate: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      regularRatio: [0.5, 0.3, 0.2],
      useMinCostFuel: false,
      handlingCost: 2.0,
      dfcm: 1.1,
  )
  def firstPeriod = new PeriodsDataFirst(
      incMinCap: 100,
      incMaxCap: 250,
      fuels: fuels,
      generationPoint: 50,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataSecond(
      incMinCap: 100,
      incMaxCap: 250,
      fuels: fuels,
      generationPoint: 80,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataThird(
      incMinCap: 100,
      incMaxCap: 250,
      fuels: fuels,
      generationPoint: 150,
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

    List<Pattern> pricePatterns = ["^50\\.9(\\d+)", "^54\\.3(\\d+)", "^57\\.8(\\d+)", "^68\\.1(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"] as List<Pattern>

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    List<?> priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
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