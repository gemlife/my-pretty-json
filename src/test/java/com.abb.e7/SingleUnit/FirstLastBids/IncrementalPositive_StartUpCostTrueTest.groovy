package com.abb.e7.SingleUnit.FirstLastBids

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class IncrementalPositive_StartUpCostTrueTest {

  def calculationsParams = new CalculationsParameters(
      includeStartupShutdownCost: true,
      shiftPrices: true,
      includeDVOM: true,
      firstBidHeatRate: true,
      lastBidHeatRate: true,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Incremental",
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
      incMinCap: 50,
      incMaxCap: 250,
      fuels: fuels,
      shutDownCost: 300.0,
  )
  def secondPariod = new PeriodsDataSecond(
      incMinCap: 50,
      incMaxCap: 250,
      fuels: fuels,
      shutDownCost: 300.0,
  )
  def thirdPeriod = new PeriodsDataThird(
      incMinCap: 50,
      incMaxCap: 250,
      fuels: fuels,
      shutDownCost: 300.0,
  )

  def json = new InputJSONWithThreePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsDataFirst: firstPeriod,
      periodsDataSecond: secondPariod,
      periodsDataThird: thirdPeriod,
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^65\\.7(\\d+)", "^67\\.4(\\d+)", "^70\\.8(\\d+)", "^70\\.8(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"] as List<Pattern>

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    List<String> priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    List<String> quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
    priceArray = extractUnderlyingList(priceArray) as List<String>
    quantityArray = extractUnderlyingList(quantityArray) as List<String>
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
    System.out.println(price)
    while (price.size() == 1) {
      price = price.get(0)
    }
    return price
  }


}