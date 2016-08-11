package com.abb.e7

import com.abb.e7.model.*
import org.junit.Test

class _BidLibraryTest {

  @Test
  public void BidLibrary() {
    def calculationsParams = new CalculationsParameters(
    )
    def unitCharacteristic = new UnitCharacteristic(
    )
    def startFuels = new StartFuelsIDs(
        startFuelIDs: ["Fuel N2"]
    )
    def periodsData = new PeriodsData(
        startFuels: startFuels,
    )
    def bidLibrary = new BidLibraryPeriodsData(
        bidLibraryPeriod: "2016-07-28T00:00:00",
        bidLibraryPrice: 25,
        bidLibraryVolume: 150
    )
    def json = new E7TemplateJSONBidLibrary(
        calculationsParameters: calculationsParams,
        unitCharacteristic: unitCharacteristic,
        periodsData: periodsData,
        bidLibrary: bidLibrary
    )
    println json.buildInputJSON().toPrettyString()
  }
//
//  def calculationsParams = new CalculationsParameters(
//      includeDVOM: true
//  )
//  def unitCharacteristic = new UnitCharacteristic(
//      minUpTime: 12
//  )
//  def startFuels = new StartFuelsIDs(
//      startFuelIDs: ["Fuel N2"]
//  )
//  def periodsData = new PeriodsData(
//      startFuels: startFuels
//
//  )
//  def json = new E7TemplateJSON(
//      calculationsParameters: calculationsParams,
//      unitCharacteristic: unitCharacteristic,
//      periodsData: periodsData,
//  )
//
//  def inputJson = json.buildInputJSON()

//  @Test
//  public void BidLibraryPost() {
//    def pricePatterns = [/^5[1-2]\.(\d+)/, /^53\.(\d+)/, /^5[6-7]\.(\d+)/, /^5[6-7]\.(\d+)/]
//    def quantities = [/75\.0/, /150\.0/, /225\.0/, /300\.0/]
//
//    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
//    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
//    def quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
//    println priceArray
//    println quantityArray
//    priceArray = extractUnderlyingList(priceArray)
//    quantityArray = extractUnderlyingList(quantityArray)
//
//    for (def i = 0; i < priceArray.size(); i++) {
//      def currentPrice = priceArray.get(i).toString()
//      def appropriateRegex = pricePatterns.get(i)
//      assert currentPrice.matches(appropriateRegex)
//    }
//    for (def i = 0; i < quantities.size(); i++) {
//      def currentQuantity = quantityArray.get(i).toString()
//      def appropriateQuantity = quantities.get(i)
//      assert currentQuantity.matches(appropriateQuantity)
//    }
//  }
//
//  private static List<String> extractUnderlyingList(def price) {
//    while (price.size() == 1) {
//      price = price.get(0)
//    }
//    return price
//  }

}
