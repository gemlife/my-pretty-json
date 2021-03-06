package com.abb.e7.modelJSON

import groovy.json.JsonBuilder

class PeriodsDataInput {

  def dateOfPeriod = "2016-07-28T08:00:00"
  def fixedCommitmentType = 'Economic'
  def incMinCap = 75.0
  def incMaxCap = 300.0
  def generationPoint = null
  def mw = [75.0, 150.0, 225.0, 300.0]
  def hr = [8000.0, 8300.0, 8600.0, 9200.0]
  def noLoadHeatRate = 0.0
  def noLoadCostAdder = 0.0
  def noLoadCostMultiplier = 1.0
  def isAverageHeatRate = false
  def isPolynomialCoefficients = false
  def numberOfBlocks = 4
  def coefficients = null
  def bidAdder = 0.0
  def bidMultiplier = 1.0
  def startUpCost = 5000.0
  def startupCostAdder = 0.0
  def startupCostMultiplier = 1.0
  def shutDownCost = 0.0
  def startHours = null
  def startFuels = new StartFuelsIDs()
  def vomCost = 3.0
  def dvom = 1.0
  def fuels = new FuelsInputData()
  def priceArray = [4.5, 5.5, 6.5]
  def fuelNameArray = ['Fuel N1', 'Fuel N2', 'Fuel N3']
  def fuelEmissionsArray = [[], [], []]
  def stationEmissionsArray = []

  private def builder = new JsonBuilder()

  def buildPRInputJSON() {
    return builder {
      'Period' dateOfPeriod
      'FixedCommitmentType' fixedCommitmentType
      'MinCapacity' incMinCap
      'MaxCapacity' incMaxCap
      'GenerationMWPoint' generationPoint
      'MWHeatRatePairs'([mw, hr].transpose().collect { [MWPoint: it[0], HeatRate: it[1]] })
      'NoLoadHeat' noLoadHeatRate
      'NoLoadCostAdder' noLoadCostAdder
      'NoLoadCostMultiplier' noLoadCostMultiplier
      'IsAverageHeatRate' isAverageHeatRate
      'IsPolynomialCoefficients' isPolynomialCoefficients
      'NumberOfBlocks' numberOfBlocks
      'Coefficients' coefficients
      'BidAdder' bidAdder
      'BidMultiplier' bidMultiplier
      'StartupCost' startUpCost
      'StartupCostAdder' startupCostAdder
      'StartupCostMultiplier' startupCostMultiplier
      'ShutDownCost' shutDownCost
      'StartHour' startHours
      'StartFuels' startFuels.buildInputJSON()
      'VomCost' vomCost
      'Dvom' dvom
      'Fuels' fuels.buildInputJSON()
      'FuelsData'([fuelNameArray, priceArray, fuelEmissionsArray].transpose().collect {
        [Id: it[0], Price: it[1], Emissions: it[2]]
      })
      'Emissions' stationEmissionsArray
    }
  }
}
