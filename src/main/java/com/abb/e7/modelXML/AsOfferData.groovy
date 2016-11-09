package com.abb.e7.modelXML

import groovy.xml.StreamingMarkupBuilder

class AsOfferData {

  def tp = ["SPIN", "SUPP_ON", "SUPP_OFF"]
  def pr = ["10.1", "11.1", "12.3", "15.0"]
  def smw = ["15.1", "15.1", "16.1", "17.1"]
  def ds = ["ECONOMIC", "SELFSCHEDULE", "EMERGENCY", "NOTQUALIFIED"]

  def builder = new StreamingMarkupBuilder()

  def xmlBuilder() {
    builder = {
      [tp, pr, smw, ds].transpose().collect() { offer ->
        AS_OFFER {
          TYPE(offer[0])
          PRICE(offer[1])
          SELF_SCHEDULE_MW(offer[2])
          DISPATCH_STATUS(offer[3])
        }
      }
    }
  }
}