package com.abb.e7.SingleUnit.FixedCommitmentType

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Incremental_SStruePZtrueOutageFirstPeriodTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeStartupShutdownCost: true,
      selfScheduledMW: true,
      priceZero: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Incremental",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      regularRatio: [0.5, 0.3, 0.2],
      useMinCostFuel: false,
      handlingCost: 2.0,
      dfcm: 1.1,
  )
  def firstPeriod = new PeriodsDataInput(
      fixedCommitmentType: "Outage",
      incMaxCap: 350,
      incMinCap: 50,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      generationPoint: 50,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      incMaxCap: 350,
      incMinCap: 50,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      generationPoint: 300,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      incMaxCap: 350,
      incMinCap: 50,
      bidAdder: 0.5,
      bidMultiplier: 1.3,
      startupCostAdder: 100,
      startupCostMultiplier: 1.4,
      shutDownCost: 300,
      fuels: fuels,
      startFuels: startFuels,
      generationPoint: 400,
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def pricePatternsFistBlock = []
    def pricePatternsSecondBlock = ["0.0", "^93\\.3(\\d+)"]
    def pricePatternsThirdBlock = ["^93\\.3(\\d+)"]
    def quantityPatternsSecondBlock = ["300\\.0", "350\\.0"]
    def quantityPatternsThirdBlock = ["350\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")

    priceArraySecondBlock = extractUnderlyingList(priceArraySecondBlock)
    priceArrayThirdBlock = extractUnderlyingList(priceArrayThirdBlock)
    quantityArraySecondBlock = extractUnderlyingList(quantityArraySecondBlock)
    quantityArrayThirdBlock = extractUnderlyingList(quantityArrayThirdBlock)

    println priceArraySecondBlock
    println priceArrayThirdBlock
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