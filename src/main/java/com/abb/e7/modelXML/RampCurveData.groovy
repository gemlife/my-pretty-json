package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class RampCurveData {
  def sn = ["1"]
  def rmw = ["1"]
  def rrup = ["21"]
  def rrdown = ["22"]
  def rrbd = ["2"]

  def builder = new MarkupBuilder()

  def xmlBuilder() {
    return {
      SEGMENT() {
        [sn, rmw, rrup, rrdown, rrbd].transpose().collect {
          [SEG_NUM(it[0]), RAMP_MW(it[1]), RAMP_RATE_UP(it[2]), RAMP_RATE_DOWN(it[3]), RAMP_RATE_BIDIRECTIONAL(it[4])]
        }
      }
    }
  }
}
