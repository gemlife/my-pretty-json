package com.abb.e7.SingleUnit.StartUpTransfer

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

class Exponential_StartUpTrueEmissionTRueStartHoursOneVectorTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
      includeEmissionCost: true
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Exponential",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
  )
  def fuels = new FuelsInputData(
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      startHours: [0.5],
      startupCostMultiplier: 1.5,
      startupCostAdder: 100.0,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      startHours: [0.5],
      startupCostMultiplier: 1.5,
      startupCostAdder: 100.0,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      startHours: [0.5],
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

    def startCostValue = [12100.0, 12100.0, 12100.0]
    def startHoursValue = [0.5]

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