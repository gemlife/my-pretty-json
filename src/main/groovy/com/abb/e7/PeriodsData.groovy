package com.abb.e7

import groovy.json.JsonBuilder

class PeriodsData {

  def mwHRPoints = new MWHeatRatePairs()
  def mw1 = 75
  def mw2 = 150
  def mw3 = 225
  def mw4 = 300
  def hr1 = 8000
  def hr2 = 8300
  def hr3 = 8600
  def hr4 = 9200
  def data = "2016-03-07T08:00:00.000Z"
  def incMinCap = 75
  def incMaxCap = 300
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
  def fuelsData

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      'Period' data
      'MinCapacity' incMinCap
      'MaxCapacity' incMaxCap

      def HRP = mwHRPoints.buildInputJSON()
      'MWHeatRatePairs' HRP

//      'MWHeatRatePairs' mwHRPoints = [
//          {
//            'MWPoint' mw1
//            'HeatRate' hr1
//          },
//          {
//            'MWPoint' mw2
//            'HeatRate' hr2
//          },
//          {
//            'MWPoint' mw3
//            'HeatRate' hr3
//          },
//          {
//            'MWPoint' mw4
//            'HeatRate' hr4
//          }
//      ]
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
  }
}
