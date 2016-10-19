package com.abb.e7.MultiUnit

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class AverageIncremental_MultiUnitAllParametersTest {

  def calculationsParams = new CalculationParameters(
      includeStartupShutdownCost: true,
      shiftPrices: false,
  )
  def average = new UnitParameters(
      incName: "Average",
  )
  def incremental = new UnitParameters(
      incName: "Incremental",
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
  def averageFirstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 50,
      shutDownCost: 250,
      incMinCap: 50,
      incMaxCap: 250,
  )
  def averageSecondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 75,
      shutDownCost: 300,
      incMinCap: 75,
      incMaxCap: 300,
  )
  def averageThirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 350,
      incMinCap: 100,
      incMaxCap: 350,
  )
  def incrementalFirstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 50,
      shutDownCost: 250,
      incMinCap: 50,
      incMaxCap: 250,
  )
  def incrementalSecondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      startFuels: startFuels,
      fuels: fuels,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 75,
      shutDownCost: 300,
      incMinCap: 75,
      incMaxCap: 300,
  )
  def incrementalThirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostMultiplier: 1.4,
      startupCostAdder: 100,
      shutDownCost: 350,
      incMinCap: 100,
      incMaxCap: 350,
  )
  def averageUnit = new UnitData(
      unitCharacteristic: average.buildUCInputJSON(),
      periodsData: [averageFirstPeriod.buildPRInputJSON(), averageSecondPeriod.buildPRInputJSON(), averageThirdPeriod.buildPRInputJSON()],
  )
  def incrementalUnit = new UnitData(
      unitCharacteristic: incremental.buildUCInputJSON(),
      periodsData: [incrementalFirstPeriod.buildPRInputJSON(), incrementalSecondPeriod.buildPRInputJSON(), incrementalThirdPeriod.buildPRInputJSON()],
  )
  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      inputData: [averageUnit.buildSPInputJSON(), incrementalUnit.buildSPInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def pricePatternsFistBlock = ["^84\\.6(\\d+)","^90\\.6(\\d+)","^95\\.0(\\d+)","^99\\.5(\\d+)"] as List<Pattern>
    def pricePatternsSecondBlock = ["^83\\.0(\\d+)","^87\\.4(\\d+)","^91\\.9(\\d+)","^105\\.3(\\d+)"] as List<Pattern>
    def pricePatternsThirdBlock = ["^82\\.2(\\d+)","^85\\.2(\\d+)","^89\\.7(\\d+)","^112\\.0(\\d+)"] as List<Pattern>
    def quantityPatternsFirstBlock = ["50\\.0", "150\\.0", "225\\.0", "250\\.0"]
    def quantityPatternsSecondBlock = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"]
    def quantityPatternsThirdBlock = ["100\\.0", "150\\.0", "225\\.0", "350\\.0"]

    def pricePatternsFistBlockSecondUnit = ["^85\\.4(\\d+)","^88\\.3(\\d+)","^90\\.6(\\d+)","^92\\.1(\\d+)"] as List<Pattern>
    def pricePatternsSecondBlockSecondUnit = ["^83\\.0(\\d+)","^85\\.2(\\d+)","^87\\.4(\\d+)","^91\\.9(\\d+)"] as List<Pattern>
    def pricePatternsThirdBlockSecondUnit = ["^81\\.5(\\d+)","^83\\.0(\\d+)","^85\\.2(\\d+)","^92\\.6(\\d+)"] as List<Pattern>
    def quantityPatternsFirstBlockSecondUnit = ["50\\.0", "150\\.0", "225\\.0", "250\\.0"]
    def quantityPatternsSecondBlockSecondUnit = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"]
    def quantityPatternsThirdBlockSecondUnit = ["100\\.0", "150\\.0", "225\\.0", "350\\.0"]

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