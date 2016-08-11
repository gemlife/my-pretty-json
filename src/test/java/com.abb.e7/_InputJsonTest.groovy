package com.abb.e7

import com.abb.e7.model.*
import org.junit.Test

class _InputJsonTest {

  @Test
  public void InputJson() {
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
    def json = new E7TemplateJSON(
        calculationsParameters: calculationsParams,
        unitCharacteristic: unitCharacteristic,
        periodsData: periodsData,
        bidLibrary: bidLibrary
    )
    println json.buildInputJSON().toPrettyString()
  }
}
