package com.abb.e7.model

import groovy.json.JsonBuilder

class E7TemplateJSONBidLibrary {

  def calculationsParameters = new CalculationsParameters()
  def unitCharacteristic = new UnitCharacteristic()
  def inputData
  def periodsData = new PeriodsDataFirst()
  def bidLibrary = new BidLibraryPeriodsData()

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      def flags = calculationsParameters.buildCPInputJSON()
      'CalculationsParameters' flags

      "InputData" inputData =
          [{
             def units = unitCharacteristic.buildUCInputJSON()
             'UnitCharacteristic' units
             def firstPeriod = periodsData.buildPRInputJSON()
             PeriodsDataFirst([firstPeriod])
             def bidLibraryPeriod = bidLibrary.buildInputJSON()
             'BidLibraryPeriodsData' bidLibraryPeriod
           }]
    } as JsonBuilder
  }
}
