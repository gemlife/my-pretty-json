package com.abb.e7.modelJSON

import groovy.json.JsonBuilder

class UnitData {

  def unitCharacteristic = new UnitParameters()
  def periodsData = []
  def bidLibraryArray = []

  private def builder = new JsonBuilder()

  def buildSPInputJSON() {
    return builder {
      'UnitCharacteristic' unitCharacteristic
      'PeriodsData' periodsData
      'BidLibraryPeriodsData' bidLibraryArray
    }
  }
}
