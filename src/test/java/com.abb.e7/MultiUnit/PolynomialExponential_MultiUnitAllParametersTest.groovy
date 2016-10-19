package com.abb.e7.MultiUnit

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class PolynomialExponential_MultiUnitAllParametersTest {

  def calculationsParams = new CalculationParameters(
      includeStartupShutdownCost: true,
      shiftPrices: false,
  )
  def polynomial = new UnitParameters(
      incName: "Polynomial",
  )
  def exponential = new UnitParameters(
      incName: "Exponential",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1", "Fuel N2"],
      startRatio: [0.5, 0.3],
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      regularRatio: [0.5, 0.3, 0.2],
      useMinCostFuel: false,
      handlingCost: 2.0,
      dfcm: 1.1,
  )
  def polynomialFirstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25.0,
      incMaxCap: 200.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def polynomialSecondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 30.0,
      incMaxCap: 220.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def polynomialThirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 50.0,
      incMaxCap: 300.0,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def exponentialFirstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 25.0,
      incMaxCap: 200.0,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def exponentialSecondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 30.0,
      incMaxCap: 220.0,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def exponentialThirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: false,
      incMinCap: 50.0,
      incMaxCap: 300.0,
      coefficients: [325.0, 0.493, 0.009, 0.05],
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 300,
  )
  def polynomialUnit = new UnitData(
      unitCharacteristic: polynomial.buildUCInputJSON(),
      periodsData: [polynomialFirstPeriod.buildPRInputJSON(), polynomialSecondPeriod.buildPRInputJSON(), polynomialThirdPeriod.buildPRInputJSON()],
  )
  def exponentialUnit = new UnitData(
      unitCharacteristic: exponential.buildUCInputJSON(),
      periodsData: [exponentialFirstPeriod.buildPRInputJSON(), exponentialSecondPeriod.buildPRInputJSON(), exponentialThirdPeriod.buildPRInputJSON()],
  )
  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      inputData: [polynomialUnit.buildSPInputJSON(), exponentialUnit.buildSPInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def pricePatternsFistBlock = ["^111\\.6(\\d+)","^138\\.3(\\d+)","^189\\.6(\\d+)","^257\\.8(\\d+)"] as List<Pattern>
    def pricePatternsSecondBlock = ["^110\\.8(\\d+)","^141\\.9(\\d+)","^200\\.7(\\d+)","^279\\.5(\\d+)"] as List<Pattern>
    def pricePatternsThirdBlock = ["^110\\.8(\\d+)","^161\\.9(\\d+)","^255\\.9(\\d+)","^384\\.7(\\d+)"] as List<Pattern>
    def quantityPatternsFirstBlock = ["25\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsSecondBlock = ["30\\.0", "93\\.(\\d+)", "156\\.(\\d+)", "220\\.0"]
    def quantityPatternsThirdBlock = ["50\\.0", "133\\.(\\d+)", "216\\.(\\d+)", "300\\.0"]

    def pricePatternsFistBlockSecondUnit = ["^35\\.4(\\d+)","^35\\.4(\\d+)","^36\\.7(\\d+)","^59\\.3(\\d+)"] as List<Pattern>
    def pricePatternsSecondBlockSecondUnit = ["^33\\.1(\\d+)","^33\\.2(\\d+)","^35\\.7(\\d+)","^93\\.7(\\d+)"] as List<Pattern>
    def pricePatternsThirdBlockSecondUnit = ["^27\\.2(\\d+)","^27\\.8(\\d+)","^67\\.3(\\d+)","^2611\\.8(\\d+)"] as List<Pattern>
    def quantityPatternsFirstBlockSecondUnit = ["25\\.0", "83\\.(\\d+)", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsSecondBlockSecondUnit = ["30\\.0", "93\\.(\\d+)", "156\\.(\\d+)", "220\\.0"]
    def quantityPatternsThirdBlockSecondUnit = ["50\\.0", "133\\.(\\d+)", "216\\.(\\d+)", "300\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def quantityArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[0]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")

    def priceArrayFirstBlockSecondUnit = JsonPath.from(body).get("Results.PQPairs.Blocks[1].Price[0]")
    def priceArraySecondBlockSecondUnit = JsonPath.from(body).get("Results.PQPairs.Blocks[1].Price[1]")
    def priceArrayThirdBlockSecondUnit = JsonPath.from(body).get("Results.PQPairs.Blocks[1].Price[2]")
    def quantityArrayFirstBlockSecondUnit = JsonPath.from(body).get("Results.PQPairs.Blocks[1].Quantity[0]")
    def quantityArraySecondBlockSecondUnit = JsonPath.from(body).get("Results.PQPairs.Blocks[1].Quantity[1]")
    def quantityArrayThirdBlockSecondUnit = JsonPath.from(body).get("Results.PQPairs.Blocks[1].Quantity[2]")

    priceArrayFirstBlock = extractUnderlyingList(priceArrayFirstBlock)
    priceArraySecondBlock = extractUnderlyingList(priceArraySecondBlock)
    priceArrayThirdBlock = extractUnderlyingList(priceArrayThirdBlock)
    quantityArrayFirstBlock = extractUnderlyingList(quantityArrayFirstBlock)
    quantityArraySecondBlock = extractUnderlyingList(quantityArraySecondBlock)
    quantityArrayThirdBlock = extractUnderlyingList(quantityArrayThirdBlock)

    priceArrayFirstBlockSecondUnit = extractUnderlyingList(priceArrayFirstBlockSecondUnit)
    priceArraySecondBlockSecondUnit = extractUnderlyingList(priceArraySecondBlockSecondUnit)
    priceArrayThirdBlockSecondUnit = extractUnderlyingList(priceArrayThirdBlockSecondUnit)
    quantityArrayFirstBlockSecondUnit = extractUnderlyingList(quantityArrayFirstBlockSecondUnit)
    quantityArraySecondBlockSecondUnit = extractUnderlyingList(quantityArraySecondBlockSecondUnit)
    quantityArrayThirdBlockSecondUnit = extractUnderlyingList(quantityArrayThirdBlockSecondUnit)

    println priceArrayFirstBlock
    println priceArraySecondBlock
    println priceArrayThirdBlock
    println quantityArrayFirstBlock
    println quantityArraySecondBlock
    println quantityArrayThirdBlock

    println priceArrayFirstBlockSecondUnit
    println priceArraySecondBlockSecondUnit
    println priceArrayThirdBlockSecondUnit
    println quantityArrayFirstBlockSecondUnit
    println quantityArraySecondBlockSecondUnit
    println quantityArrayThirdBlockSecondUnit

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

    for (def j = 0; j < pricePatternsFistBlockSecondUnit.size(); j++) {
      def appropriatePrice = pricePatternsFistBlockSecondUnit.get(j)
      def currentPrice = priceArrayFirstBlockSecondUnit.get(j).toString()
      assert currentPrice.matches(appropriatePrice)
    }

    for (def i = 0; i < pricePatternsSecondBlockSecondUnit.size(); i++) {
      def currentPrice = priceArraySecondBlockSecondUnit.get(i).toString()
      def appropriateRegex = pricePatternsSecondBlockSecondUnit.get(i)
      assert currentPrice.matches(appropriateRegex)
    }

    for (def i = 0; i < priceArrayThirdBlockSecondUnit.size(); i++) {
      def currentPrice = priceArrayThirdBlockSecondUnit.get(i).toString()
      def appropriateRegex = pricePatternsThirdBlockSecondUnit.get(i)
      assert currentPrice.matches(appropriateRegex)
    }
    for (def i = 0; i < quantityPatternsFirstBlockSecondUnit.size(); i++) {
      def currentQuantity = quantityArrayFirstBlockSecondUnit.get(i).toString()
      def appropriateQuantity = quantityPatternsFirstBlockSecondUnit.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
    for (def i = 0; i < quantityPatternsSecondBlockSecondUnit.size(); i++) {
      def currentQuantity = quantityArraySecondBlockSecondUnit.get(i).toString()
      def appropriateQuantity = quantityPatternsSecondBlockSecondUnit.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
    for (def i = 0; i < quantityPatternsThirdBlockSecondUnit.size(); i++) {
      def currentQuantity = quantityArrayThirdBlockSecondUnit.get(i).toString()
      def appropriateQuantity = quantityPatternsThirdBlockSecondUnit.get(i)
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