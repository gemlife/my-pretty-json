package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class RampCurveData {
  def segmentNumber
  def rampMW = "1"
  def rampRateUp = "21"
  def rampRateDown = "22"
  def rampRateBiDirectional = "2"

  def builder = new MarkupBuilder()

  def xmlBuilder() {
    return {
         SEGMENT() {
          SEG_NUM(segmentNumber)
          RAMP_MW(rampMW)
          RAMP_RATE_UP(rampRateUp)
          RAMP_RATE_DOWN(rampRateDown)
          RAMP_RATE_BIDIRECTIONAL(rampRateBiDirectional)
        }
      }
  }
}
