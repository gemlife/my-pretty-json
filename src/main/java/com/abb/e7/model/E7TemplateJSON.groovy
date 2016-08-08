package com.abb.e7.model

import groovy.json.JsonBuilder

class E7TemplateJSON {

  def calculationsParameters = new CalculationsParameters()
  def unitCharacteristic = new UnitCharacteristic()
  def inputData
  def periodsData = new PeriodsData()

  private def builder = new JsonBuilder()

  def buildInputJSON() {
//      println periodsData.startFuels.startFuelIDs
    return builder {
      def flags = calculationsParameters.buildInputJSON()
      'CalculationsParameters' flags

      "InputData" inputData =
          [{
             def units = unitCharacteristic.buildInputJSON()
             'UnitCharacteristic' units
             def firstPeriod = periodsData.buildInputJSON()
             periodsData ([firstPeriod])
           }]
    } as JsonBuilder
  }
}
