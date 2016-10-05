package com.abb.e7.model.Emissions

import groovy.json.JsonBuilder

class RegularFuelsDataWithEmissions {
  def priceArray = [4.5, 5.5, 6.5]
  def fuelNameArray = ['Fuel N1', 'Fuel N2', 'Fuel N3']
  def fuelEmission
  def fuelEmission2 = new FuelEmissions()
  def fuelEmission3 = new FuelEmissions()
  def fuelEmission4 = new FuelEmissions()

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder.call(
        [
            {
              'Id' fuelNameArray[0]
              'Price' priceArray[0]
              'Emissions' fuelEmission.buildEMInputJSON()
            },
            {
              'Id' fuelNameArray[1]
              'Price' priceArray[1]
              'Emissions'
            },
            {
              'Id' fuelNameArray[2]
              'Price' priceArray[2]
              'Emissions' (fuelEmission.buildEMInputJSON(),fuelEmission3.buildEMInputJSON())
            }
        ]
    )
  }
}