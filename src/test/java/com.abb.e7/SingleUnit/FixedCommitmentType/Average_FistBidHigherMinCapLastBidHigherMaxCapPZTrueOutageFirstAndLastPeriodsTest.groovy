package com.abb.e7.SingleUnit.FixedCommitmentType

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_FistBidHigherMinCapLastBidHigherMaxCapPZTrueOutageFirstAndLastPeriodsTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeDVOM: true,
      firstBidHeatRate: true,
      priceZero: true,
      lastBidHeatRate: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
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
      incMinCap: 50,
      incMaxCap: 250,
      fuels: fuels,
      isAverageHeatRate: true,
      fixedCommitmentType: "Outage",
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      incMinCap: 50,
      incMaxCap: 250,
      fuels: fuels,
      isAverageHeatRate: true
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      incMinCap: 50,
      incMaxCap: 250,
      fuels: fuels,
      isAverageHeatRate: true,
      fixedCommitmentType: "Outage",
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
    def pricePatternsSecondBlock = ["^54\\.3(\\d+)", "^57\\.8(\\d+)", "^68\\.1(\\d+)", "^68\\.1(\\d+)"]
    def pricePatternsThirdBlock = []
    def quantityPatternsSecondBlock = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")

    priceArraySecondBlock = extractUnderlyingList(priceArraySecondBlock)
    quantityArraySecondBlock = extractUnderlyingList(quantityArraySecondBlock)

    println priceArrayFirstBlock
    println priceArraySecondBlock
    println priceArrayThirdBlock
    println quantityArraySecondBlock

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
  }

  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 0) {
      price = price.get(0)
    }
    return price
  }
}