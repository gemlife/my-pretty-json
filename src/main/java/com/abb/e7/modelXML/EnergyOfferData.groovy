package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder

class EnergyOfferData {

  def builder = new StreamingMarkupBuilder()
  def mw = ["50", "100", "150", "300"]
  def sn = ["1", "2", "3", "4"]
  def pr = ["20", "22", "25", "30"]

  def xmlBuilder() {
    builder = {
      NO_LOAD_COST("55.6")
      DISPATCH_STATUS("ECONOMIC")
      SELF_SCHEDULE_MW("66.6")
      ENERGY_CURVE() {
        [sn, mw, pr].transpose().collect { offer ->
          ENERGY_SEGMENT {
            SEG_NUM(offer[0])
            OFFER_MW(offer[1])
            PRICE(offer[2])

          }
        }
      }
    }
  }
}