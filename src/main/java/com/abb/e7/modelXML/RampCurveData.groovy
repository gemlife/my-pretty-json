package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class RampCurveData {


  def writer = new StringWriter()
  def builder = new MarkupBuilder(writer)

  def xmlBuilder() {
    return {
         SEGMENT() {
          SEG_NUM("2")
          RAMP_MW("1")
          RAMP_RATE_UP("21")
          RAMP_RATE_DOWN("22")
          RAMP_RATE_BIDIRECTIONAL("2")
        }
      }
  }
}
