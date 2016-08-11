package com.abb.e7.model

import groovy.json.JsonBuilder

class BidLibraryPeriodsData {

  def bidLibraryPeriod
  def bidLibraryVolume
  def bidLibraryPrice
  def bidAdderLib = 0.0
  def bidMultiplierLib = 1.0

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder.call(
        [{
          'Period' bidLibraryPeriod
          'Volume' bidLibraryVolume
          'Price' bidLibraryPrice
          'BidAdder' bidAdderLib
          'BidMultiplier' bidMultiplierLib
        }]
    )
  }
}
