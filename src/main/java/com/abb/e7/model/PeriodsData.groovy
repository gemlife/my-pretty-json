package com.abb.e7.model

import groovy.json.JsonBuilder

class PeriodsData {

  def mwHRPoint
  def mw = [75.0, 150.0, 225.0, 300.0]
  def hr = [8000.0, 8300.0, 8600.0, 9200.0]
  def data = "2016-03-07T08:00:00.000Z"
  def incMinCap = 75.0
  def incMaxCap = 300.0
  def isAverageHeatRate = false
  def startUpCost = 5000.0
  def startFuelIDs = ["Fuel NG3"]
  def quantity = 1000.0
  def vomCost = 3.0
  def dvom = 1.0
  def fuelIDs = null
  def regularRatio = null
  def useMinCostFuel = true
  def handlingCost = 0.0
  def dfcm = 1.0
  def priceArray = [6.83, 5.5, 4.15]
  def fuelNameArray = ['Fuel NG1', 'Fuel NG2', 'Fuel NG3']
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
            'Id' fuelNameArray[0]
            'Price' priceArray[0]
          },
          {
            'Id' fuelNameArray[1]
            'Price' priceArray[1]
          },
          {
            'Id' fuelNameArray[2]
            'Price' priceArray[2]
          }
      ]
    }
  }
}