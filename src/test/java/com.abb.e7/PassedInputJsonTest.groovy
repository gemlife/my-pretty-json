package com.abb.e7

import com.abb.e7.model.*
import com.abb.e7.model.Emissions.RegularFuelsDataWithEmissions
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.Templates.InputJSONWithSinglePeriods
import org.junit.Test

class PassedInputJsonTest {

  @Test
  public void InputJson() {


    def calculationsParams = new CalculationParameters(
    )
    def unitCharacteristic = new UnitParameters(
    )
    def startFuels = new StartFuelsIDs(
        startFuelIDs: ["Fuel N2"]
    )
    def periodsData = new PeriodsDataFirst(
        startFuels: startFuels,
    )
//    def bidLibrary = new BidLibraryFirstPeriod(
//        bidLibraryPeriod: "2016-07-28T00:00:00",
//        bidLibraryPrice: 25,
//        bidLibraryVolume: 150
//    )
    def json = new InputJSONWithSinglePeriods(
        calculationsParameters: calculationsParams,
        unitCharacteristic: unitCharacteristic,
        periodsData: periodsData,
//        bidLibrary: bidLibrary
    )
    println json.buildInputJSON().toPrettyString()
  }
}