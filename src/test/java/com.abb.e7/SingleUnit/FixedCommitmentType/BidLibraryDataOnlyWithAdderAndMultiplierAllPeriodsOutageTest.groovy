package com.abb.e7.SingleUnit.FixedCommitmentType

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import com.abb.e7.model.BidLibraryFirstPeriod
import io.restassured.path.json.JsonPath
import org.junit.Test

class BidLibraryDataOnlyWithAdderAndMultiplierAllPeriodsOutageTest {

  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeDVOM: true,
      includeStartupShutdownCost: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Polynomial",
  )
  def startFuels = new StartFuelsIDs(
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1"],
  )
  def bidLibraryFirst = new BidLibraryFirstPeriod(
      bidLibraryPeriod: "2016-07-28T08:00:00",
      fixedCommitmentType: "Outage",
      priceBL: [20],
      volumeBL: [150],
      bidAdderLib: 0.5,
      bidMultiplierLib: 1.3,
  )
  def bidLibrarySecond = new BidLibraryFirstPeriod(
      bidLibraryPeriod: "2016-07-28T09:00:00",
      fixedCommitmentType: "Outage",
      priceBL: [30],
      volumeBL: [200],
      bidAdderLib: 0.5,
      bidMultiplierLib: 1.3,
  )
  def bidLibraryThird = new BidLibraryFirstPeriod(
      bidLibraryPeriod: "2016-07-28T10:00:00",
      fixedCommitmentType: "Outage",
      priceBL: [40],
      volumeBL: [250],
      bidAdderLib: 0.5,
      bidMultiplierLib: 1.3,
  )
  def json = new InputJSON(
      calculationsParameters: calculationsParams,
      unitCharacteristic: unitCharacteristic,
      periodsData: [],
      bidLibraryArray: [bidLibraryFirst.buildBLInputJSON(), bidLibrarySecond.buildBLInputJSON(), bidLibraryThird.buildBLInputJSON()]
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {

    def pricePatternsFistBlock = []
    def pricePatternsSecondBlock = []
    def pricePatternsThirdBlock = []

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArrayFirstBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def priceArraySecondBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[1]")
    def priceArrayThirdBlock = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[2]")

    println priceArrayFirstBlock
    println priceArraySecondBlock
    println priceArrayThirdBlock

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
  }

  private static List<String> extractUnderlyingList(def price) {
    while (price.size() == 0) {
      price = price.get(0)
    }
    return price
  }
}