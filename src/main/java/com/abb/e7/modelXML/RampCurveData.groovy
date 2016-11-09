package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder

class RampCurveData {
  def sn = ["1"]
  def rmw = ["1"]
  def rrup = ["21"]
  def rrdown = ["22"]
  def rrbd = ["2"]

  def builder = new StreamingMarkupBuilder()

  def xmlBuilder() {
    builder = {
        [sn, rmw, rrup, rrdown, rrbd].transpose().collect { offer ->
          SEGMENT {
            SEG_NUM(offer[0])
            RAMP_MW(offer[1])
            RAMP_RATE_UP(offer[2])
            RAMP_RATE_DOWN(offer[3])
            RAMP_RATE_BIDIRECTIONAL(offer[4])
        }
      }
    }
  }
}
