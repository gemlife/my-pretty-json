package com.abb.e7.SingleUnit.EvaluationPeriod

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.FuelsInputData
import com.abb.e7.model.PeriodsDataInput
import com.abb.e7.model.StartFuelsIDs
import com.abb.e7.model.InputJSON
import com.abb.e7.model.UnitParameters
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_EvaluationPeriod2hoursShutdownIncludedTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
      minUpTime: 12.0,
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1","Fuel N2"],
      startRatio: [0.3,0.7],
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false,
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      shutDownCost: 300,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      shutDownCost: 300,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T12:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      shutDownCost: 300,
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^50\\.4(\\d+)", "^53\\.5(\\d+)", "^56\\.6(\\d+)", "^66\\.0(\\d+)"] as List<Pattern>
    List<Pattern> quantityPatterns = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"] as List<Pattern>

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