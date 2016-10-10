package com.abb.e7

import com.abb.e7.model.*
import com.abb.e7.model.BidLibraryFirstPeriod
import com.abb.e7.model.FuelEmissions

import com.abb.e7.model.PeriodsDataInput
import com.abb.e7.model.InputJSON
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
        emissionPrice: 1.8,
        emissionId: "EM2",
        emissionPriceAdder: 0.019,
        emissionReleaseRate: 0.42,
        emissionRemovalRate: 0.177
    )
    def startFuels = new StartFuelsIDs(
        startFuelIDs: ["Fuel N2"]
    )
    def fuels = new FuelsInputData(
        fuelIDs: ["Fuel N1"],
        regularRatio: [1.0],
        useMinCostFuel: false,
    )
    def firstPeriod = new PeriodsDataInput(
        dateOfPeriod: "2016-07-28T10:00:00",
        startFuels: startFuels,
        fuels: fuels,
        fuelEmissionsArray: [[EM2.buildEMInputJSON()],[],[]],
        stationEmissionsArray: [EM2.buildEMInputJSON(),EM2.buildEMInputJSON()],
    )
    def secondPeriod = new PeriodsDataInput(
        dateOfPeriod: "2016-07-28T11:00:00",
        startFuels: startFuels,
        fuels: fuels,
        fuelEmissionsArray: [[EM1.buildEMInputJSON()],[],[]],
        stationEmissionsArray: [EM1.buildEMInputJSON(),EM1.buildEMInputJSON()],
    )
    def thirdPeriod = new PeriodsDataInput(
        dateOfPeriod: "2016-07-28T12:00:00",
        startFuels: startFuels,
        fuels: fuels,
        fuelEmissionsArray: [[EM2.buildEMInputJSON()],[],[]],
        stationEmissionsArray: [EM2.buildEMInputJSON(),EM2.buildEMInputJSON()],
    )
    def bidLibraryFirst = new BidLibraryFirstPeriod(
        bidLibraryPeriod: "2016-07-28T00:00:00",
        volumeBL: [125,150],
        priceBL: [10,20]
    )
    def bidLibrarySecond = new BidLibraryFirstPeriod(
        bidLibraryPeriod: "2016-07-28T90:00:00",
        volumeBL: [100,200],
        priceBL: [5,15]
    )
    def json = new InputJSON(
        calculationsParameters: calculationsParams,
        unitCharacteristic: unitCharacteristic,
        periodsData: [firstPeriod.buildPRInputJSON(),secondPeriod.buildPRInputJSON(),thirdPeriod.buildPRInputJSON()],
        bidLibraryArray: [bidLibraryFirst.buildBLInputJSON(), bidLibrarySecond.buildBLInputJSON()]
    )

    println json.buildSPInputJSON().toPrettyString()
  }
}
