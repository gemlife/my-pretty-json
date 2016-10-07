package com.abb.e7.SingleUnit.BidSelfSchedule

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import com.abb.e7.model.PeriodsDataInput
import com.abb.e7.model.InputJSON
import io.restassured.path.json.JsonPath
import org.junit.Test

import java.util.regex.Pattern

class Incremental_RatioNullBidAdderMultiplierDefinedBidSSTrueTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: false,
      includeDVOM: true,
      bidTacticSelfScheduledMW: true,
  )
  def unitCharacteristic = new UnitParameters(
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
  def firstPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T08:00:00",
      generationPoint: 150,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      bidMultiplier: 1.5,
      bidAdder: 0.5,
  )
  def secondPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T09:00:00",
      generationPoint: 250,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,
      bidMultiplier: 1.5,
      bidAdder: 0.5,
  )
  def thirdPeriod = new PeriodsDataInput(
      dateOfPeriod: "2016-07-28T10:00:00",
      generationPoint: 350,
      shutDownCost: 300,
      startFuels: startFuels,
      fuels: fuels,      bidMultiplier: 1.5,
      bidAdder: 0.5,
  )

  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    List<Pattern> pricePatterns = ["^0\\.0"] as List<Pattern>
    def quantityPatternsFirstBlock = ["^150\\.0"]
    def quantityPatternsSecondBlock = ["^250\\.0"]
    def quantityPatternsThirdBlock = ["^350\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    List<?> priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    def quantityArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[0]")
    def quantityArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[1]")
    def quantityArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[2]")

    priceArray = extractUnderlyingList(priceArray)

    println priceArray
    println quantityArrayFirstBlock
    println quantityArraySecondBlock
    println quantityArrayThirdBlock

    for (def i = 0; i < pricePatterns.size() - 1; i++) {
      List<String> currentPriceBlock = priceArray.get(i) as List<String>
      for (def j = 0; j < currentPriceBlock.size(); j++) {
        def appropriatePrice = pricePatterns.get(j)
        def currentPrice = currentPriceBlock.get(j).toString()
        assert currentPrice.matches(appropriatePrice)
      }
    }

    for (def i = 0; i < quantityPatternsFirstBlock.size(); i++) {
      def currentQuantity = quantityArrayFirstBlock.get(i)toString()
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
    while (price.size() == 1) {
      price = price.get(0)
    }
    return price
  }


}