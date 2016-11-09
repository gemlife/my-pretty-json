package com.abb.e7.modelXML

import groovy.xml.StreamingMarkupBuilder

class HourliesData {
  def energyOffersData = new EnergyOfferData()
  def asOfferData = [new AsOfferData()]
  def rampCurveData = new RampCurveData()

  def operatingHour = "2014-01-01T01:00:00-05:00"
  def commitmentStatus = "ECONOMIC"
  def coldStartupTime = "120"
  def interStartupTime = "180"
  def hotStartupTime = "60"
  def coldNotIfTime = "60"
  def interNotIfTime = "60"
  def hotNotIfTime = "60"
  def minEmergencyLimit = "26.6"
  def maxEmergencyLimit = "27.7"
  def minEconomicLimit = "28.2"
  def maxEconomicLimit = "29.1"
  def minRegulationLimit = "30.2"
  def maxRegulationLimit = "30.1"
  def offlineResourceLimit = "30.3"
  def rampRate = "30.4"
  def rampRateBiDirectional = "30.5"
  def rampRateUp = "30.6"
  def rampRateDown = "30.7"
  def rampRateEnableCurveFlg = "YES"
  def maxOfflineSuppLimit = "30.8"
  def useSlopeFlg = "YES"
  def rowID = "1"
  def extID = "0001"
  def userComment = "Default Comment for Test"

  def builder = new StreamingMarkupBuilder()

  def xmlBuilder() {
    builder = {
      HOURLIES() {
        OPERATING_HOUR(operatingHour)
        COMMITMENT_STATUS(commitmentStatus)
        COLD_STARTUP_TIME(coldStartupTime)
        INTER_STARTUP_TIME(interStartupTime)
        HOT_STARTUP_TIME(hotStartupTime)
        COLD_NOTIF_TIME(coldNotIfTime)
        INTER_NOTIF_TIME(interNotIfTime)
        HOT_NOTIF_TIME(hotNotIfTime)
        MIN_EMERGENCY_LIMIT(minEmergencyLimit)
        MAX_EMERGENCY_LIMIT(maxEmergencyLimit)
        MIN_ECONOMIC_LIMIT(minEconomicLimit)
        MAX_ECONOMIC_LIMIT(maxEconomicLimit)
        MIN_REGULATION_LIMIT(minRegulationLimit)
        MAX_REGULATION_LIMIT(maxRegulationLimit)
        OFFLINE_RESOURCE_LIMIT(offlineResourceLimit)
        RAMP_RATE(rampRate)
        RAMP_RATE_BIDIRECTIONAL(rampRateBiDirectional)
        RAMP_RATE_UP(rampRateUp)
        RAMP_RATE_DOWN(rampRateDown)
        RAMP_RATE_ENABLE_CURVE_FLG(rampRateEnableCurveFlg)
        MAX_OFFLINE_SUPP_LIMIT(maxOfflineSuppLimit)
        USE_SLOPE_FLG(useSlopeFlg)
        ENERGY_OFFER(energyOffersData.builder)
        AS_OFFERS(asOfferData.builder)
        RAMP_CURVE(rampCurveData.builder)
        ROW_ID(rowID)
        EXT_ID(extID)
        USER_COMMENTS(userComment)
      }
    }
  }
}