package com.abb.e7.JSONs.SingleUnit.StartUpTransfer

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.*
import io.restassured.path.json.JsonPath
import org.junit.Test

class Average_StartUpTrueWithRatioAdderAndMultiplierStartHoursPresentTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      startRatio: [0.3,0.2,0.5]
  )
  def fuels = new FuelsInputData(
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      startHours: [0.5, 1.0, 1.5],
      startupCostMultiplier: 1.5,
      startupCostAdder: 100.0,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      startHours: [0.5, 1.0, 1.5],
      startupCostMultiplier: 1.5,
      startupCostAdder: 100.0,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      startHours: [0.5, 1.0, 1.5],
      startupCostMultiplier: 1.5,
      startupCostAdder: 100.0,
  )

  def singleUnit = new UnitData(
      unitCharacteristic: unitCharacteristic.buildUCInputJSON(),
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(), thirdPeriod.buildPRInputJSON()],
      bidLibraryArray: []
  )

  def json = new InputJSON (
      calculationsParameters: calculationsParams,
      inputData: [singleUnit.buildSPInputJSON()]
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def startCostValue = [13300.0, 13300.0, 13300.0]
    def startHoursValue = [0.5, 1.0, 1.5]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def currentStartCost = JsonPath.from(body).get("Results.PQPairs.StartupCost")
    def currentStartHour = JsonPath.from(body).get("Results.PQPairs.StartHour")

    currentStartCost = extractUnderlyingList(currentStartCost)
    currentStartHour = extractUnderlyingList(currentStartHour)

    println currentStartCost
    println currentStartHour

//    for (def i = 0; i < startCostValue.size() - 1; i++) {
//      List<String> currentBlock = currentStartCost.get(i) as List<String>
//      for (def j = 0; j < currentBlock.size(); j++) {
//        def appropriateValue = startCostValue.get(j)
//        def currentValue = currentBlock.get(j).toString()
//        assert currentValue.contains(appropriateValue as String)
//      }
//    }

    assert currentStartCost == startCostValue
    for (def i = 0; i < startHoursValue.size() - 1; i++) {
      List<String> currentBlock = currentStartHour.get(i) as List<String>
      for (def j = 0; j < currentBlock.size(); j++) {
        def appropriateValue = startHoursValue.get(j)
        def currentValue = currentBlock.get(j).toString()
        assert currentValue.matches(appropriateValue as String)
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