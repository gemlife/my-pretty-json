package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class AsOfferData {

  def writer = new StringWriter()
  def builder = new MarkupBuilder(writer)

  def xmlBuilder() {
    return {
      AS_OFFER() {
        TYPE("REG")
        PRICE("10.1")
        MILEAGE_PRICE("100.11")
        SELF_SCHEDULE_MW("15.1")
        DISPATCH_STATUS("ECONOMIC")
      }
      AS_OFFER() {
        TYPE("SPIN")
        PRICE("11.1")
        SELF_SCHEDULE_MW("15.1")
        DISPATCH_STATUS("SELFSCHEDULE")
      }
      AS_OFFER() {
        TYPE("SUPP_ON")
        PRICE("12.1")
        SELF_SCHEDULE_MW("16.1")
        DISPATCH_STATUS("EMERGENCY")
      }
      AS_OFFER() {
        TYPE("SUPP_OFF")
        PRICE("13.1")
        SELF_SCHEDULE_MW("17.1")
        DISPATCH_STATUS("NOTQUALIFIED")
      }
    }
  }
}
