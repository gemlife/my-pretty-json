package com.abb.e7.SingleUnit.StartupcostAdderStartupcostMultiplier

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Exponential_SStruePZTrueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeStartupShutdownCost: true,
      selfScheduledMW: true,
      priceZero: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Exponential",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1", "Fuel N2"],
      regularRatio: [0.7, 0.3],
      useMinCostFuel: false,
      handlingCost: 2.0,
      dfcm: 1.1,
  )
  def firstPeriod = new PeriodsDataInput(
      incMaxCap: 200,
      incMinCap: 25,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      generationPoint: 20.0,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      incMaxCap: 200,
      incMinCap: 25,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      generationPoint: 100.0,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      incMaxCap: 200,
      incMinCap: 25,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      generationPoint: 200.0,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
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

    def pricePatternsFistBlock = ["36\\.4(\\d+)", "^37\\.6(\\d+)", "^58\\.4(\\d+)", "^58\\.4(\\d+)"] as List<Pattern>
    def pricePatternsSecondBlock = ["0\\.0", "^58\\.4(\\d+)", "^58\\.4(\\d+)"]
    def pricePatternsThirdBlock = ["0\\.0"]
    def quantityPatternsFirstBlock = ["25\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsSecondBlock = ["100\\.0", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsThirdBlock = ["200\\.0"]

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

  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 0) {
      price = price.get(0)
    }
    return price
  }
}