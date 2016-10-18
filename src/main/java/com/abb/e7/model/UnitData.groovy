package com.abb.e7.model

import groovy.json.JsonBuilder

class UnitData {

  def unitCharacteristic = new UnitParameters()
//  def inputData
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
