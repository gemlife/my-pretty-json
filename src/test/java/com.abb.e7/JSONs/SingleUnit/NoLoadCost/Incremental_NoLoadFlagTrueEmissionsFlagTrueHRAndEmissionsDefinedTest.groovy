package com.abb.e7.JSONs.SingleUnit.NoLoadCost

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Incremental_NoLoadFlagTrueEmissionsFlagTrueHRAndEmissionsDefinedTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeNoLoadCost: true,
      includeEmissionCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Incremental",
  )
  def EM1 = new FuelEmissions(
      emissionId: "EM1",
      emissionPrice: 0.0017,
      emissionPriceAdder: 0,
      emissionReleaseRate: 108,
      emissionRemovalRate: 0,
  )
  def EM2 = new FuelEmissions(
      emissionId: "EM2",
      emissionPrice: 0.01,
      emissionPriceAdder: 0,
      emissionReleaseRate: 0.2,
      emissionRemovalRate: 0.589,
  )
  def EM3 = new FuelEmissions(
      emissionId: "EM3",
      emissionPrice: 0.0017,
      emissionPriceAdder: 0,
      emissionReleaseRate: 108,
      emissionRemovalRate: 0,
  )
  def EM4 = new FuelEmissions(
      emissionId: "EM4",
      emissionPrice: 0.0018,
      emissionPriceAdder: 0,
      emissionReleaseRate: 0.2,
      emissionRemovalRate: 0,
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"],
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false,
      dfcm: 1.2,
      handlingCost: 2.0,
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: false,
      noLoadHeatRate: 8000.0,
      incMaxCap: 350.0,
      incMinCap: 50.0,
      stationEmissionsArray: [EM1.buildEMInputJSON(), EM2.buildEMInputJSON()],
      fuelEmissionsArray: [[EM2.buildEMInputJSON(),EM3.buildEMInputJSON()],[EM3.buildEMInputJSON()],[EM1.buildEMInputJSON(),EM4.buildEMInputJSON()]],
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: false,
      noLoadHeatRate: 8000.0,
      incMaxCap: 350.0,
      incMinCap: 50.0,
      stationEmissionsArray: [EM1.buildEMInputJSON(), EM2.buildEMInputJSON()],
      fuelEmissionsArray: [[EM2.buildEMInputJSON(),EM3.buildEMInputJSON()],[EM3.buildEMInputJSON()],[EM1.buildEMInputJSON(),EM4.buildEMInputJSON()]],

  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: false,
      noLoadHeatRate: 8000.0,
      incMaxCap: 350.0,
      incMinCap: 50.0,
      stationEmissionsArray: [EM1.buildEMInputJSON(), EM2.buildEMInputJSON()],
      fuelEmissionsArray: [[EM2.buildEMInputJSON(),EM3.buildEMInputJSON()],[EM3.buildEMInputJSON()],[EM1.buildEMInputJSON(),EM4.buildEMInputJSON()]],

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

    List<Pattern> pricePatterns = ["^108\\.2(\\d+)", "^110\\.8(\\d+)", "^112\\.8(\\d+)","^119\\.3(\\d+)"] as List<Pattern>
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