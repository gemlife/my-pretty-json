package com.abb.e7.SingleUnit.StartUpTransfer

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_StartFlagFalseTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: false,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(), secondPeriod.buildPRInputJSON(), thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def startCostValue = [null, null, null]
    def startHoursValue = [null, null, null]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def currentStartCost = JsonPath.from(body).get("Results.PQPairs.StartupCost")
    def currentStartHour = JsonPath.from(body).get("Results.PQPairs.StartHour")

    currentStartCost = extractUnderlyingList(currentStartCost)
    currentStartHour = extractUnderlyingList(currentStartHour)

    println currentStartCost
    println currentStartHour

    assert startHoursValue == currentStartHour
    assert startCostValue == currentStartCost

  }
  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 1) {
      price = price.get(0)
    }
    return price
  }
}
