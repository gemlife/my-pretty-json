package com.abb.e7.model

import groovy.json.JsonBuilder

class FuelEmissions {

  def emissionId =1
  def emissionPrice
  def emissionPriceAdder
  def emissionReleaseRate
  def emissionRemovalRate
  def emissionBasis = "lb/MMBtu"

  private def builder = new JsonBuilder()

  def buildEMInputJSON() {
    return builder {
      'Id' emissionId
      'Price' emissionPrice
      'PriceAdder' emissionPriceAdder
      'ReleaseRate' emissionReleaseRate
      'RemovalRate' emissionRemovalRate
      'EmissionBasis' emissionBasis
    }
  }
}
