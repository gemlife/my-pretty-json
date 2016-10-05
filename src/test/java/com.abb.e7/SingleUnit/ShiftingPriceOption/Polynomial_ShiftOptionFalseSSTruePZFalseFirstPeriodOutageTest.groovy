package com.abb.e7.SingleUnit.ShiftingPriceOption

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.FuelsInputData
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.PeriodsData.PeriodsDataSecond
import com.abb.e7.model.PeriodsData.PeriodsDataThird
import com.abb.e7.model.StartFuelsIDs
import com.abb.e7.model.Templates.InputJSONWithThreePeriods
import com.abb.e7.model.UnitParameters
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Polynomial_ShiftOptionFalseSSTruePZFalseFirstPeriodOutageTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      firstBidHeatRate: true,
      lastBidHeatRate: true,
      selfScheduledMW: true,
      priceZero: false,
      includeStartupShutdownCost: true
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Polynomial",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1","Fuel N2"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2"],
  )
  def firstPeriod = new PeriodsDataFirst(
      fixedCommitmentType: "Outage",
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      generationPoint: 50,
  )
  def secondPeriod = new PeriodsDataSecond(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      generationPoint: 100,
  )
  def thirdPeriod = new PeriodsDataThird(
      startFuels: startFuels,
      fuels: fuels,
      mw: [],
      hr: [],
      isPolynomialCoefficients: true,
      incMinCap: 25,
      incMaxCap: 200,
      coefficients: [325.0, 9.902258853, 0.030989779, 0.000112221],
      generationPoint: 150
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

    def pricePatternsFistBlock = []
    def pricePatternsSecondBlock = ["^82\\.3(\\d+)", "^119\\.4(\\d+)", "^155\\.6(\\d+)"]
    def pricePatternsThirdBlock = ["^95\\.6(\\d+)", "^158\\.9(\\d+)"]
    def quantityPatternsSecondBlock = ["100\\.0", "141\\.(\\d+)", "200\\.0"]
    def quantityPatternsThirdBlock = ["150\\.0", "200\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")

    priceArraySecondBlock = extractUnderlyingList(priceArraySecondBlock)
    priceArrayThirdBlock = extractUnderlyingList(priceArrayThirdBlock)
    quantityArraySecondBlock = extractUnderlyingList(quantityArraySecondBlock)
    quantityArrayThirdBlock = extractUnderlyingList(quantityArrayThirdBlock)

    println priceArrayFirstBlock
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