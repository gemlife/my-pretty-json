package com.abb.e7.JSONs.SingleUnit.NoLoadCost

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Polynomial_WithSSTrueShiftPriceTrueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeNoLoadCost: true,
      includeEmissionCost: true,
      includeStartupShutdownCost: true,
      selfScheduledMW: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Polynomial",
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
      startFuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      startRatio: null,
  )
  def fuels = new FuelsInputData(
      regularRatio: [0.5, 0.3, 0.2],
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      useMinCostFuel: false,
      dfcm: 1.2,
      handlingCost: 2.0,
  )
  def firstPeriod = new PeriodsDataInput(
      mw: [],
      hr: [],
      startFuels: startFuels,
      fuels: fuels,
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      noLoadHeatRate: 8000.0,
      generationPoint: 50.0,
      stationEmissionsArray: [EM1.buildEMInputJSON(), EM2.buildEMInputJSON()],
      fuelEmissionsArray: [[EM2.buildEMInputJSON(),EM3.buildEMInputJSON()],[EM3.buildEMInputJSON()],[EM1.buildEMInputJSON(),EM4.buildEMInputJSON()]],
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00.000Z",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      generationPoint: 0.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      noLoadHeatRate: 8000.0,
      stationEmissionsArray: [EM1.buildEMInputJSON(), EM2.buildEMInputJSON()],
      fuelEmissionsArray: [[EM2.buildEMInputJSON(),EM3.buildEMInputJSON()],[EM3.buildEMInputJSON()],[EM1.buildEMInputJSON(),EM4.buildEMInputJSON()]],
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00.000Z",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      generationPoint: 0.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      noLoadHeatRate: 8000.0,
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

    def pricePatternsFistBlock = ["^179\\.5(\\d+)", "^216\\.7(\\d+)", "^277\\.0(\\d+)","^277\\.0(\\d+)"]as List<Pattern>
    def pricePatternsSecondBlock = ["^170\\.3(\\d+)", "^215\\.6(\\d+)", "^275\\.9(\\d+)","^275\\.9(\\d+)"]
    def pricePatternsThirdBlock = ["^170\\.3(\\d+)", "^215\\.6(\\d+)", "^275\\.9(\\d+)","^275\\.9(\\d+)"]
    def quantityPatternsFirstBlock = ["50\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsSecondBlock = ["25\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsThirdBlock = ["25\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def quantityArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[0]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")

    priceArrayFirstBlock = extractUnderlyingList(priceArrayFirstBlock)
    priceArraySecondBlock = extractUnderlyingList(priceArraySecondBlock)
    priceArrayThirdBlock = extractUnderlyingList(priceArrayThirdBlock)
    quantityArrayFirstBlock = extractUnderlyingList(quantityArrayFirstBlock)
    quantityArraySecondBlock = extractUnderlyingList(quantityArraySecondBlock)
    quantityArrayThirdBlock = extractUnderlyingList(quantityArrayThirdBlock)

    println priceArrayFirstBlock
    println priceArraySecondBlock
    println priceArrayThirdBlock
    println quantityArrayFirstBlock
    println quantityArraySecondBlock
    println quantityArrayThirdBlock

    for (def j = 0; j < pricePatternsFistBlock.size(); j++) {
      def appropriatePrice = pricePatternsFistBlock.get(j)
      def currentPrice = priceArrayFirstBlock.get(j).toString()
      assert currentPrice.matches(appropriatePrice)
    }

    for (def i = 0; i < pricePatternsSecondBlock.size(); i++) {
      def currentPrice = priceArraySecondBlock.get(i).toString()
      def appropriateRegex = pricePatternsSecondBlock.get(i)
      assert currentPrice.matches(appropriateRegex)
    }

    for (def i = 0; i < priceArrayThirdBlock.size(); i++) {
      def currentPrice = priceArrayThirdBlock.get(i).toString()
      def appropriateRegex = pricePatternsThirdBlock.get(i)
      assert currentPrice.matches(appropriateRegex)
    }
    for (def i = 0; i < quantityPatternsFirstBlock.size(); i++) {
      def currentQuantity = quantityArrayFirstBlock.get(i).toString()
      def appropriateQuantity = quantityPatternsFirstBlock.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
    for (def i = 0; i < quantityPatternsSecondBlock.size(); i++) {
      def currentQuantity = quantityArraySecondBlock.get(i).toString()
      def appropriateQuantity = quantityPatternsSecondBlock.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
    for (def i = 0; i < quantityPatternsThirdBlock.size(); i++) {
      def currentQuantity = quantityArrayThirdBlock.get(i).toString()
      def appropriateQuantity = quantityPatternsThirdBlock.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
  }

  private static List<String> extractUnderlyingList(def extractedArray) {
    while (extractedArray.size() == 0) {
      extractedArray = extractedArray.get(0)
    }
    return extractedArray
  }
}