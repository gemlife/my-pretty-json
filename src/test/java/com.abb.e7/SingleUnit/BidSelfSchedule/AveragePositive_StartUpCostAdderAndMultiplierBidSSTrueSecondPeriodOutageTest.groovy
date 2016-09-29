package com.abb.e7.SingleUnit.BidSelfSchedule

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

class AveragePositive_StartUpCostAdderAndMultiplierBidSSTrueSecondPeriodOutageTest {

  def calculationsParams = new CalculationsParameters(
      shiftPrices: false,
      includeDVOM: true,
      bidTacticSelfScheduledMW: true,
      selfScheduledMW: true,
  )
  def unitCharacteristic = new UnitCharacteristic(
      incName: "Incremental",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      startRatio: null,
  )
  def fuels = new FuelsInputData(
      regularRatio: null,
      fuelIDs: ["Fuel N1", "Fuel N2", "Fuel N3"],
      useMinCostFuel: true,
  )
  def firstPeriod = new PeriodsDataFirst(
      generationPoint: 0,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def secondPeriod = new PeriodsDataSecond(
      generationPoint: 250,
      startFuels: startFuels,
      fuels: fuels,
      fixedCommitmentType: "Outage",
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataThird(
      generationPoint: 500,
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )

  def json = new InputJSONWithThreePeriods(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsDataFirst: firstPeriod,
      periodsDataSecond: secondPeriod,
      periodsDataThird: thirdPeriod,
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def pricePatternsFirstBlock = ["^0\\.0"]
    def pricePatternsSecondBlock = []
    def pricePatternsThirdBlock = ["^0\\.0"]
    def quantityPatternsFirstBlock = ["^0\\.0"]
    def quantityPatternsThirdBlock = ["^500\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def quantityArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[0]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")

    println priceArrayFirstBlock
    println priceArraySecondBlock
    println priceArrayThirdBlock
    println quantityArrayFirstBlock
    println quantityArrayThirdBlock

    for (def j = 0; j < pricePatternsFirstBlock.size(); j++) {
      def appropriatePrice = pricePatternsFirstBlock.get(j)
      def currentPrice = priceArrayFirstBlock.get(j).toString()
      assert currentPrice.matches(appropriatePrice)
    }
    for (def j = 0; j < pricePatternsSecondBlock.size(); j++) {
      def appropriatePrice = pricePatternsSecondBlock.get(j)
      def currentPrice = priceArrayFirstBlock.get(j).toString()
      assert currentPrice.matches(appropriatePrice)
    }
    for (def i = 0; i < priceArrayThirdBlock.size(); i++) {
      def currentPrice = priceArrayThirdBlock.get(i).toString()
      def appropriateRegex = pricePatternsThirdBlock.get(i)
      assert currentPrice.matches(appropriateRegex)
    }
    for (def i = 0; i < quantityPatternsFirstBlock.size(); i++) {
      def currentQuantity = quantityArrayFirstBlock.get(i)toString()
      def appropriateQuantity = quantityPatternsFirstBlock.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
    for (def i = 0; i < quantityPatternsThirdBlock.size(); i++) {
      def currentQuantity = quantityArrayThirdBlock.get(i).toString()
      def appropriateQuantity = quantityPatternsThirdBlock.get(i)
      assert currentQuantity.matches(appropriateQuantity)
    }
  }

  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 1) {
      price = price.get(0)
    }
    return price
  }


}