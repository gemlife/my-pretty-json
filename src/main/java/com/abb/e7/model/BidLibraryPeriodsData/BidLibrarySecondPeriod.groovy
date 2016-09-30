package com.abb.e7.model.BidLibraryPeriodsData

import groovy.json.JsonBuilder

class BidLibrarySecondPeriod {

  def bidLibraryPeriod
  def bidLibraryVolume = 0
  def bidLibraryPrice = 0
  def bidAdderLib = 0.0
  def bidMultiplierLib = 1.0

  private def builderBidLibrary = new JsonBuilder()

  def buildBLInputJSON() {
    return builderBidLibrary.call (
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

