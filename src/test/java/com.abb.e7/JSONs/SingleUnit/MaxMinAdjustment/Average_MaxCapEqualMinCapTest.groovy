package com.abb.e7.JSONs.SingleUnit.MaxMinAdjustment

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.modelJSON.*
import com.abb.e7.modelJSON.PeriodsDataInput
import com.abb.e7.modelJSON.InputJSON

import io.restassured.path.json.JsonPath
import org.junit.Test

class Average_MaxCapEqualMinCapTest {
// 1 start fuel and several regular fuels with ratio
  def calculationsParams = new CalculationParameters(
      shiftPrices: true,
      includeDVOM: true,
  )
  def unitCharacteristic = new UnitParameters(
      incName: "Average",
  )
  def startFuels = new StartFuelsIDs(
      startFuelIDs: ["Fuel N1"]
  )
  def fuels = new FuelsInputData(
      fuelIDs: ["Fuel N1","Fuel N2","Fuel N3"],
      regularRatio: [0.5,0.3,0.2],
      useMinCostFuel: false,
      dfcm: 1.1,
      handlingCost: 2.0,

  )
  def periodsData = new PeriodsDataInput(
      startFuels: startFuels,
      fuels: fuels,
      incMaxCap: 75,
      incMinCap: 75,
      isAverageHeatRate: true,
  )
  def singleUnit = new UnitData(
      unitCharacteristic: unitCharacteristic.buildUCInputJSON(),
      periodsData: [periodsData.buildPRInputJSON()],
      bidLibraryArray: []
  )

  def json = new InputJSON (
      calculationsParameters: calculationsParams,
      inputData: [singleUnit.buildSPInputJSON()]
  )

  def inputJson = json.buildSPInputJSON()

  @Test
  public void post() {
    def pricePatterns = ["^50\\.9(\\d+)"]
    def quantities = ["75\\.0"]

    String body = SupplyCurveCalculationService.postWithLogging(inputJson)
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Price[0]")
    def quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks[0].Quantity[0]")
    println priceArray
    println quantityArray

    for (def i = 0; i < priceArray.size(); i++) {
      def currentPrice = priceArray.get(i).toString()
      def appropriateRegex = pricePatterns.get(i)
      assert currentPrice.matches(appropriateRegex)
    }
    for (def i = 0; i < quantities.size(); i++) {
      def currentQuantity = quantityArray.get(i).toString()
      def appropriateQuantity = quantities.get(i)
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
