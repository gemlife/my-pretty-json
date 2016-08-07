package com.abb.e7.model

import groovy.json.JsonBuilder

class PeriodsData {

  def mwHRPoint
  def mw = [75, 150, 225, 300]
  def hr = [8000, 8300, 8600, 9200]
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
      'MWHeatRatePairs' mwHRPoint = [
          {
            'MWPoint' mw[0]
            'HeatRate' hr[0]
          },
          {
            'MWPoint' mw[1]
            'HeatRate' hr[1]
          },
          {
            'MWPoint' mw[2]
            'HeatRate' hr[2]
          },
          {
            'MWPoint' mw[3]
            'HeatRate' hr[3]
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
  }
}