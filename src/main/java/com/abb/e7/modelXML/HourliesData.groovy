package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class HourliesData {
  def energyOffersData = new EnergyOfferData()
  def asOfferData = new AsOfferData()
  def rampCurveData = new RampCurveData()
  def operatingHour = "2014-01-01T01:00:00-05:00"
  def commitmentStatus = "ECONOMIC"

  def writer = new StringWriter()
  def builder = new MarkupBuilder(writer)

  def xmlBuilder() {
    return {
      OPERATING_HOUR(operatingHour)
      COMMITMENT_STATUS(commitmentStatus)
      COLD_STARTUP_TIME("120")
      INTER_STARTUP_TIME("180")
      HOT_STARTUP_TIME("60")
      COLD_NOTIF_TIME("60")
      INTER_NOTIF_TIME("60")
      HOT_NOTIF_TIME("60")
      MIN_EMERGENCY_LIMIT("26.6")
      MAX_EMERGENCY_LIMIT("27.7")
      MIN_ECONOMIC_LIMIT("28.2")
      MAX_ECONOMIC_LIMIT("29.1")
      MIN_REGULATION_LIMIT("30.2")
      MAX_REGULATION_LIMIT("30.1")
      OFFLINE_RESOURCE_LIMIT("30.3")
      RAMP_RATE("30.4")
      RAMP_RATE_BIDIRECTIONAL("30.5")
      RAMP_RATE_UP("30.6")
      RAMP_RATE_DOWN("30.7")
      RAMP_RATE_ENABLE_CURVE_FLG("YES")
      MAX_OFFLINE_SUPP_LIMIT("30.8")
      USE_SLOPE_FLG("YES")
      ENERGY_OFFER(energyOffersData.xmlBuilder())
      AS_OFFERS(asOfferData.xmlBuilder())
      RAMP_CURVE(rampCurveData.xmlBuilder())
      ROW_ID("1")
      EXT_ID("0001")
      USER_COMMENTS("Comment")
    }
  }
}
