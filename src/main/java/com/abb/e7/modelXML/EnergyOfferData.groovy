package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class EnergyOfferData {

  def builder = new MarkupBuilder()
  def mw = ["50", "100", "150", "300"]
  def sn = ["1", "2", "3", "4"]
  def pr = ["20", "22", "25", "30"]

  def xmlBuilder() {
    return {
      NO_LOAD_COST("55.6")
      DISPATCH_STATUS("ECONOMIC")
      SELF_SCHEDULE_MW("66.6")
      ENERGY_CURVE() {
        ENERGY_SEGMENT() {
          [sn, mw, pr].transpose().collect{[SEG_NUM(it[0]), OFFER_MW(it[1]), PRICE(it[2]) ]}
        }
      }
    }
  }
}
