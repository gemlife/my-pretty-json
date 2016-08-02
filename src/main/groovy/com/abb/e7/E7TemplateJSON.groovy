package com.abb.e7

import groovy.json.JsonBuilder

class E7TemplateJSON {

  def calculationsParameters = new CalculationsParameters()
  def unitCharacteristic = new UnitCharacteristic()
  def inputData
  def PeriodsData = new PeriodsData()
  def mwHRPoints = new MWHeatRatePairs()

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      def flags = calculationsParameters.buildInputJSON()
      'CalculationsParameters' flags

      "InputData" inputData =
          [{
             def units = unitCharacteristic.buildInputJSON()
             'UnitCharacteristic' units


             def firstPeriod = PeriodsData.buildInputJSON()
             'PeriodsData' (firstPeriod, "null") //should find how have [] without null value


           }]
    } as JsonBuilder
  }
}
