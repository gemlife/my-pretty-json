package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class InputXML {
  def genOfferData = new GenOfferData()

  def writer = new StringWriter()
  def xml = new MarkupBuilder(writer).GEN_OFFERS() {
    GEN_OFFER (genOfferData.xmlBuilder())
  }
}


