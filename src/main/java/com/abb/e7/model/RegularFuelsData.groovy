package com.abb.e7.model

import com.sun.javafx.collections.MappingChange
import groovy.json.JsonBuilder

import java.awt.List

class RegularFuelsData {
  def priceArray = [4.5, 5.5, 6.5]
  def fuelNameArray = ['Fuel N1', 'Fuel N2', 'Fuel N3']

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder.call(
        [
            {
              'Id' fuelNameArray[0]
              'Price' priceArray[0]
            },
            {
              'Id' fuelNameArray[1]
              'Price' priceArray[1]
            },
            {
              'Id' fuelNameArray[2]
              'Price' priceArray[2]
            }
        ]
    )
  }
}