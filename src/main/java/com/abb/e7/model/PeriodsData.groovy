package com.abb.e7.model

import groovy.json.JsonBuilder

class PeriodsData {

  def dateOfPeriod = "2016-07-28T01:00:00"
  def fixedCommitmentType = 'Economic'
  def incMinCap = 75.0
  def incMaxCap = 300.0
  def generationPoint = null
  def mwHRPoints = new MWHeatRatePoints()
  def isAverageHeatRate = false
  def isPolynomialCoefficients = false
  def numberOfBlocks = 4
  def coefficients = null
  def bidAdder = 0.0
  def bidMultiplier = 1.0
  def startUpCost = 5000.0
  def shutDownCost = 300.0
  def startFuels = new StartFuelsIDs()
  def vomCost = 3.0
  def dvom = 1.0
  def fuels = new FuelsInputData()
  def fuelsData = new RegularFuelsData()

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      'Period' dateOfPeriod
      'FixedCommitmentType' fixedCommitmentType
      'MinCapacity' incMinCap
      'MaxCapacity' incMaxCap
      'GenerationMWPoint' generationPoint
      'MWHeatRatePairs' mwHRPoints.buildInputJSON()
      'IsAverageHeatRate' isAverageHeatRate
      'IsPolynomialCoefficients' isPolynomialCoefficients
      'NumberOfBlocks' numberOfBlocks
      'Coefficients' coefficients
      'BidAdder' bidAdder
      'BidMultiplier' bidMultiplier
      'StartupCost' startUpCost
      'ShutDownCost' shutDownCost
      'StartFuels' startFuels.buildInputJSON()
      'VomCost' vomCost
      'Dvom' dvom
      'Fuels' fuels.buildInputJSON()
      'FuelsData' fuelsData.buildInputJSON()
    }
  }
}