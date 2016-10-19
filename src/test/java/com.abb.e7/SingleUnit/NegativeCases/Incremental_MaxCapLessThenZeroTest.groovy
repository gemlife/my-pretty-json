package com.abb.e7.SingleUnit.NegativeCases

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import org.junit.Test

class Incremental_MaxCapLessThenZeroTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Incremental",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      startRatio: null,
  )
  def fuels = new FuelsInputData(
      regularRatio: [0.5, 0.3, 0.2],
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      useMinCostFuel: false,
      dfcm: 1.1,
      handlingCost: 2.02,

  )
  def firstPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T08:00:00",
      generationPoint: null,
      incMaxCap: -75,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      generationPoint: 0,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      generationPoint: 300,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
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

    def errorMessage = "MinCapacity cannot be greater than MaxCapacity"

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)

    println body

    assert body.contains(errorMessage)
  }
}