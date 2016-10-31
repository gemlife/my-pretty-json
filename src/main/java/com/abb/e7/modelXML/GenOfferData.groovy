package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class GenOfferData {
  def action = "INSERT"
  def participantCode = "MCGO"
  def asset = "TESTGEN"
  def operatingDay = "2014-01-01"
  def clearingCode = "BOTH"
  def dailiesData = new DailiesData()
  def hourliesData = new HourliesData()

  def writer = new StringWriter()
  def xml = new MarkupBuilder(writer)

  def xmlBuilder() {
    return {
        ACTION (action)
        PTCPT_CD(participantCode)
        TX_PT(asset)
        OPERATING_DAY(operatingDay)
        CLRG_CD(clearingCode)
        DAILIES(dailiesData.xmlBuilder())
        HOURLIES(hourliesData.xmlBuilder())
    }
  }
}