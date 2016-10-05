package com.abb.e7.model.Emissions

import groovy.json.JsonBuilder

class FuelEmissions2 {

  def emissionId
  def emissionPrice
  def emissionPriceAdder
  def emissionReleaseRate
  def emissionRemovalRate

  private def builder = new JsonBuilder()

  def buildEMInputJSON() {
    return builder {
      'Id' emissionId
      'Price' emissionPrice
      'PriceAdder' emissionPriceAdder
      'ReleaseRate' emissionReleaseRate
      'RemovalRate' emissionRemovalRate
    }
  }
}
