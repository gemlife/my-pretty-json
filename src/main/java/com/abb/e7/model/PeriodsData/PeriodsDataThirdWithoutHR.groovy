package com.abb.e7.model.PeriodsData

import com.abb.e7.model.FuelsInputData
import com.abb.e7.model.MWHeatRatePoints
import com.abb.e7.model.RegularFuelsData
import com.abb.e7.model.StartFuelsIDs
import groovy.json.JsonBuilder

class PeriodsDataThirdWithoutHR {

  def dateOfPeriod = "2016-03-07T10:00:00.000Z"
  def fixedCommitmentType = 'Economic'
  def incMinCap = 75.0
  def incMaxCap = 300.0
  def generationPoint = null
  def mwHRPoints = null
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
  def startFuels = new StartFuelsIDs()
  def vomCost = 3.0
  def dvom = 1.0
  def fuels = new FuelsInputData()
  def fuelsData = new RegularFuelsData()

  private def builder = new JsonBuilder()

  def buildPRInputJSON() {
    return builder {
      'Period' dateOfPeriod
      'FixedCommitmentType' fixedCommitmentType
      'MinCapacity' incMinCap
      'MaxCapacity' incMaxCap
      'GenerationMWPoint' generationPoint
      'MWHeatRatePairs' mwHRPoints
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
      'StartFuels' startFuels.buildInputJSON()
      'VomCost' vomCost
      'Dvom' dvom
      'Fuels' fuels.buildInputJSON()
      'FuelsData' fuelsData.buildInputJSON()
    }
  }
}