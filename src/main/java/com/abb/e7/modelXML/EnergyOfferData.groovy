package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class EnergyOfferData {

  def writer = new StringWriter()
  def builder = new MarkupBuilder(writer)

  def xmlBuilder() {
    return {
      NO_LOAD_COST("55.6")
      DISPATCH_STATUS("ECONOMIC")
      SELF_SCHEDULE_MW("66.6")
      ENERGY_CURVE() {
        ENERGY_SEGMENT() {
          SEG_NUM("1")
          OFFER_MW("2")
          PRICE("3")
        }
        ENERGY_SEGMENT() {
          SEG_NUM("2")
          OFFER_MW("3")
          PRICE("4")
        }
      }
    }
  }
}
