package com.abb.e7.JSONs.SingleUnit.MarketProduct

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.*
import io.restassured.path.json.JsonPath
import org.junit.Test

class Exponential_MarketProduceValuesTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Exponential",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1"],
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

    def marketCode = "CAISO"
    def marketTypeCode = "DAM"
    def participantId = "036123294152"
    def marketProductType = "MP type"
    def marketSelfScheduleType = "M SS type"
    def marketId = "03245443124542523"
    def status = "New"

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def marketCodeCurrent = JsonPath.from(body).get("MarketCode")
    def statusCurrent = JsonPath.from(body).get("Status")
    def marketTypeCodeCurrent = JsonPath.from(body).get("MarketTypeCode")
    def participantIdCurrent = JsonPath.from(body).get("ParticipantId")
    def marketIdCurrent = JsonPath.from(body).get("Results.Unit.MarketId[0]")
    def marketProductTypeCurrent = JsonPath.from(body).get("Results.Unit.MarketProductType[0]")
    def marketSelfScheduleTypeCurrent = JsonPath.from(body).get("Results.Unit.MarketSelfScheduleType[0]")

    println marketCodeCurrent
    println statusCurrent
    println marketTypeCodeCurrent
    println participantIdCurrent
    println marketIdCurrent
    println marketProductTypeCurrent
    println marketSelfScheduleTypeCurrent

    assert marketCode.matches(marketCodeCurrent as String)
    assert marketTypeCode.matches(marketTypeCodeCurrent as String)
    assert participantId.matches(participantIdCurrent as String)
    assert marketProductType.matches(marketProductTypeCurrent as String)
    assert marketSelfScheduleType.matches(marketSelfScheduleTypeCurrent as String)
    assert status.matches(statusCurrent as String)
    assert marketId.matches(marketIdCurrent as String)
  }
}