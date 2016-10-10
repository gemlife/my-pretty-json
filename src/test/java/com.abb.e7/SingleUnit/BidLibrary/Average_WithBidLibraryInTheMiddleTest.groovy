package com.abb.e7.SingleUnit.BidLibrary

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import com.abb.e7.model.BidLibraryFirstPeriod
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Average_WithBidLibraryInTheMiddleTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
  )
  def firstPeriod = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      startFuels: startFuels,
      fuels: fuels,
      isAverageHeatRate: true,
  )
  def bidLibraryFirst = new BidLibraryFirstPeriod(
      bidLibraryPeriod: "2016-07-28T11:00:00",
      priceBL: [40],
      volumeBL: [250],
  )
  def bidLibrarySecond = new BidLibraryFirstPeriod(
      bidLibraryPeriod: "2016-07-28T09:00:00",
      priceBL: [30],
      volumeBL: [200],
  )
  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
      bidLibraryArray: [bidLibrarySecond.buildBLInputJSON(), bidLibraryFirst.buildBLInputJSON()]
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def pricePatternsFistBlock = ["^49\\.6(\\d+)", "^52\\.3(\\d+)", "^60\\.4(\\d+)", "^60\\.4(\\d+)"] as List<Pattern>
    def pricePatternsSecondBlock = ["30\\.0"]
    def pricePatternsThirdBlock = ["^49\\.6(\\d+)", "^52\\.3(\\d+)", "^60\\.4(\\d+)", "^60\\.4(\\d+)"]
    def pricePatternsFourthBlock = ["40\\.0"]
    def quantityPatternsFirstBlock = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"]
    def quantityPatternsSecondBlock = ["200\\.0"]
    def quantityPatternsThirdBlock = ["75\\.0", "150\\.0", "225\\.0", "300\\.0"]
    def quantityPatternsFourthBlock = ["250\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")
    def priceArrayFourthBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[3]")
    def quantityArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[0]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")
    def quantityArrayFourthBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[3]")

    priceArrayFirstBlock = extractUnderlyingList(priceArrayFirstBlock)
    priceArraySecondBlock = extractUnderlyingList(priceArraySecondBlock)
    priceArrayThirdBlock = extractUnderlyingList(priceArrayThirdBlock)
    priceArrayFourthBlock = extractUnderlyingList(priceArrayFourthBlock)
    quantityArrayFirstBlock = extractUnderlyingList(quantityArrayFirstBlock)
    quantityArraySecondBlock = extractUnderlyingList(quantityArraySecondBlock)
    quantityArrayThirdBlock = extractUnderlyingList(quantityArrayThirdBlock)
    quantityArrayFourthBlock = extractUnderlyingList(quantityArrayFourthBlock)

    println priceArrayFirstBlock
    println priceArraySecondBlock
    println priceArrayThirdBlock
    println priceArrayFourthBlock
    println quantityArrayFirstBlock
    println quantityArraySecondBlock
    println quantityArrayThirdBlock
    println quantityArrayFourthBlock

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
    for (def i = 0; i < priceArrayFourthBlock.size(); i++) {
      def currentPrice = priceArrayFourthBlock.get(i).toString()
      def appropriateRegex = pricePatternsFourthBlock.get(i)
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
    for (def i = 0; i < quantityPatternsFourthBlock.size(); i++) {
      def currentQuantity = quantityArrayFourthBlock.get(i).toString()
      def appropriateQuantity = quantityPatternsFourthBlock.get(i)
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