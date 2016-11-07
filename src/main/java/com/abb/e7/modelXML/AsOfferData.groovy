package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class AsOfferData {

  def tp = ["SPIN", "SUPP_ON", "SUPP_OFF"]
  def pr = ["10.1", "11.1", "12.3", "15.0"]
  def smw = ["15.1", "15.1", "16.1", "17.1"]
  def ds = ["ECONOMIC", "SELFSCHEDULE", "EMERGENCY", "NOTQUALIFIED"]

  def builder = new MarkupBuilder()

  def xmlBuilder() {
    return {
      AS_OFFER() {
        [ tp, pr, smw, ds].transpose().collect {
          [TYPE(it[0]), PRICE(it[1]), SELF_SCHEDULE_MW(it[2]), DISPATCH_STATUS(it[3])]
        }
      }
    }
  }
}
