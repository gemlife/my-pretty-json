package com.abb.e7

import com.abb.e7.model.*
import com.abb.e7.model.FuelEmissions

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
    def EM1 = new FuelEmissions(
        emissionPrice: 1.2,
        emissionId: "EM1",
        emissionPriceAdder: 0.018,
        emissionReleaseRate: 0.36,
        emissionRemovalRate: 0.125
    )
    def EM2 = new FuelEmissions(
        emissionPrice: 1.2,
        emissionId: "EM1",
        emissionPriceAdder: 0.018,
        emissionReleaseRate: 0.36,
        emissionRemovalRate: 0.125
    )
    def startFuels = new StartFuelsIDs(
        startFuelIDs: ["Fuel N2"]
    )
    def fuels = new FuelsInputData(
        fuelIDs: ["Fuel N1"],
        regularRatio: [1.0],
        useMinCostFuel: false,
    )
    def periodsData = new PeriodsDataFirst(
        startFuels: startFuels,
        fuels: fuels,
        fuelEmissionsArray: [[EM1.buildEMInputJSON()],[],[]],
        stationEmissionsArray: [EM1.buildEMInputJSON(),EM2.buildEMInputJSON()],
    )
//    def bidLibrary = new BidLibraryFirstPeriod(
//        bidLibraryPeriod: "2016-07-28T00:00:00",
//        bidLibraryPrice: 25,
//        bidLibraryVolume: 150
//    )
    def json = new InputJSONWithSinglePeriods(
        calculationsParameters: calculationsParams,
        unitCharacteristic: unitCharacteristic,
        periodsData: periodsData

//        bidLibrary: bidLibrary
    )
    println json.buildInputJSON().toPrettyString()
  }
}
