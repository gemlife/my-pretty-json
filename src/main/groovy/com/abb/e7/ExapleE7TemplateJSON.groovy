package com.abb.e7

import groovy.json.JsonBuilder

class ExapleE7TemplateJSON {

  def includeStartupShutdownCost = false
  def includeDVOM = true
  def includeEmissionCost = false
  def includeNoLoadCost = false
  def incrementalQuantities = false
  def incrementalPrices = true
  def firstBidHeatRate = false
  def selfScheduledMW = false
  def priceZero = false
  def shiftPrices = true
  def inputData
  def incName = "Copernicus GT"
  def incID = 1
  def minUpTime = 24
  def sCRuntimeFactor = 1
  def sCLoadFactor = 1
  def buildDate = "2000-01-25"
  def retirementDate = "2020-01-25"
  def periodsData
  def period = "2016-03-07T08:00:00.000Z"
  def incMinCap = 75
  def incMaxCap = 300
  def mWHeatRatePairs
//    def mwPoints
//    def heatRates = [8000,8300,8600,9200]
  def isAverageHeatRate = false
  def startUpCost = 5000
  def startFuelIDs = ["Fuel NG3"]
  def quantity = 1000
  def vomCost = 3
  def dvom = 1
  def fuelIDs = null
  def regularRatio = null
  def useMinCostFuel = true
  def handlingCost = 0
  def dfcm = 1.0
  def mw1 = 75
  def mw2 = 150
  def mw3 = 225
  def mw4 = 300
  def hr1 = 8000
  def hr2 = 8300
  def hr3 = 8600
  def hr4 = 9200
  def fuelsData

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      CalculationsParameters {
        'IncludeStartupShutdownCost' includeStartupShutdownCost
        'IncludeDVOM' includeDVOM
        'IncludeEmissionCost' includeEmissionCost
        'IncludeNoLoadCost' includeNoLoadCost
        'IncrementalQuantities' incrementalQuantities
        'IncrementalPrices' incrementalPrices
        'FirstBidHeatRate' firstBidHeatRate
        'SelfScheduledMW' selfScheduledMW
        'PriceZero' priceZero
        'ShiftPrices' shiftPrices
      }

  UnitCharacteristic inputData =
        [{
           UnitCharacteristic {
             'Name' incName
             'Id' incID
             'MinUpTime' minUpTime
             'SCRuntimeFactor' sCRuntimeFactor
             'SCLoadFactor' sCLoadFactor
             'BuildDate' buildDate
             'RetirementDate' retirementDate
           }
           PeriodsData periodsData =
               [{
                  PeriodsData period
                  'MinCapacity' incMinCap
                  'MaxCapacity' incMaxCap
                  MWHeatRatePair mWHeatRatePairs = [
                      {
                        'MWPoint' mw1
                        'HeatRate' hr1
                      },
                      {
                        'MWPoint' mw2
                        'HeatRate' hr2
                      },
                      {
                        'MWPoint' mw3
                        'HeatRate' hr3
                      },
                      {
                        'MWPoint' mw4
                        'HeatRate' hr4
                      }
                  ]
                  'IsAverageHeatRate' isAverageHeatRate
                  'StartupCost' startUpCost
                  'StartFuels' {
                    'Ids' startFuelIDs
                    'Quantity' quantity
                  }
                  'VomCost' vomCost
                  'Dvom' dvom
                  'Fuels' {
                    "Ids" fuelIDs = ["Fuel NG1", "Fuel NG2", "Fuel NG3"]
                    'Ratios' regularRatio = [0.1, 0.5, 0.4]
                    'UseMinCostFuel' useMinCostFuel = false
                    'HandlingCost' handlingCost = 2.0
                    'DFCM' dfcm = 1.1
                  }
                  'FuelsData' fuelsData = [
                      {
                        'Id' "Fuel NG1"
                        'Price' 6.83
                      },
                      {
                        'Id' "Fuel NG2"
                        'Price' 5.5
                      },
                      {
                        'Id' "Fuel NG3"
                        'Price' 4.15
                      }
                  ]
                }
               ]
         }
        ]
    } as JsonBuilder
  }
}
