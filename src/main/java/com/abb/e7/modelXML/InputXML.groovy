package com.abb.e7.modelXML

import groovy.xml.*

class InputXML {
  def genOfferData = new GenOfferData()
  def hourliesData = []

  def xmlBuilder = new StreamingMarkupBuilder()
  def xml = xmlBuilder.bind() {
    GEN_OFFERS() {
      GEN_OFFER(genOfferData.xmlBuilder())
    }
  }
}




