package com.abb.e7.modelXML

import groovy.xml.*

class InputXML {
  def genOfferData = new GenOfferData()

  def builder = new StreamingMarkupBuilder()
  def xml = builder.bind() {
    GEN_OFFERS() {
      GEN_OFFER(genOfferData.xmlBuilder())
    }
  }
}




