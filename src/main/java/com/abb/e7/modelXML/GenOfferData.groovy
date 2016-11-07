package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class GenOfferData {
  def action = "11111"
  def participantCode = "MCGO"
  def asset = "TESTGEN"
  def operatingDay = "2014-01-01"
  def clearingCode = "BOTH"
  def dailiesData = new DailiesData()
  def hourliesData = [new HourliesData()]

  def xml = new MarkupBuilder()

  def xmlBuilder() {
    xml = {
      ACTION(action)
      PTCPT_CD(participantCode)
      TX_PT(asset)
      OPERATING_DAY(operatingDay)
      CLRG_CD(clearingCode)
      DAILIES([dailiesData.xmlBuilder()])
      hoursData (hourliesData.builder)
    }
  }
}
