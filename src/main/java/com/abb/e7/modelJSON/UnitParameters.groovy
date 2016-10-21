package com.abb.e7.modelJSON

import groovy.json.JsonBuilder

class UnitParameters {

  def incName = "Copernicus GT"
  def marketID = '03245443124542523'
  def incID = 1
  def minUpTime = 24
  def sCRuntimeFactor = 1
  def sCLoadFactor = 1
  def buildDate = "2000-01-25"
  def retirementDate = "2020-01-25"

  private def builder = new JsonBuilder()

  def buildUCInputJSON() {
    return builder {
      'Name' incName
      'MarketId' marketID
      'Id' incID
      'MinUpTime' minUpTime
      'SCRuntimeFactor' sCRuntimeFactor
      'SCLoadFactor' sCLoadFactor
      'BuildDate' buildDate
      'RetirementDate' retirementDate
    }
  }
}
