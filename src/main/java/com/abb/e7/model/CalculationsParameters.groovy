package com.abb.e7.model

import groovy.json.JsonBuilder

class CalculationsParameters {

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
  def bidTacticSelfScheduledMW = false

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      'IncludeStartupShutdownCost' includeStartupShutdownCost
      'IncludeDVOM' includeDVOM
      'IncludeEmissionCost' includeEmissionCost
      'IncludeNoLoadCost' includeNoLoadCost
      'IncrementalQuantities' incrementalQuantities
      'IncrementalPrices' incrementalPrices
      'FirstBidHeatRate' firstBidHeatRate
      'SelfScheduledMW' selfScheduledMW
      'PriceZero' priceZero
      'BidTacticSelfScheduledMW' bidTacticSelfScheduledMW
      'ShiftPrices' shiftPrices
    }
  }
}
