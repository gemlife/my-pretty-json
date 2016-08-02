package com.abb.e7

import groovy.json.JsonBuilder

class UnitCharacteristic {

  def incName = "Copernicus GT"
  def incID = 1
  def minUpTime = 24
  def sCRuntimeFactor = 1
  def sCLoadFactor = 1
  def buildDate = "2000-01-25"
  def retirementDate = "2020-01-25"

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {
      'Name' incName
      'Id' incID
      'MinUpTime' minUpTime
      'SCRuntimeFactor' sCRuntimeFactor
      'SCLoadFactor' sCLoadFactor
      'BuildDate' buildDate
      'RetirementDate' retirementDate
    }
  }
}
