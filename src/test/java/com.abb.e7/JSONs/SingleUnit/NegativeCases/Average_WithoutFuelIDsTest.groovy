package com.abb.e7.JSONs.SingleUnit.NegativeCases

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.*
import org.junit.Test

class Average_WithoutFuelIDsTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: [],
      startRatio: null,
  )
  def fuels = new FuelsInputData(
      fuelIDs: null,
      dfcm: 1.1,
      handlingCost: 2.02,

  )
  def firstPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T08:00:00",
      generationPoint: null,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      generationPoint: 0,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      generationPoint: 300,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
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

    def errorMessage = "Fuels must have Ids"
    
    String body = SupplyCurveCalculationService.postWithLogging(inputJson)

    println body

    assert body.contains(errorMessage)
  }
}