package com.abb.e7.model.BidLibraryPeriodsData

import groovy.json.JsonBuilder

class BidLibraryFirstPeriod {

  def bidLibraryPeriod = "2016-03-07T09:00:00.000Z"
  def fixedCommitmentType = "Economic"
  def bidAdderLib = 0.0
  def bidMultiplierLib = 1.0
  def volume = [150, 200]
  def price = [30, 20]
//  def value = [
//      [getVolume: { volume[0] }, getPrice: { price[0] }],
//      [getVolume: { volume[1] }, getPrice: { price[1] }]
//  ]
  def value = getValue(volume, price)

  private def builderBidLibrary = new JsonBuilder()

  def buildBLInputJSON() {
    return builderBidLibrary.call(
        [{
           'Period' bidLibraryPeriod
           'FixedCommitmentType' fixedCommitmentType
           'Blocks' value
//           'Blocks' value.collect { ['Volume': it.getVolume(), 'Price': it.getPrice()] }
           'BidAdder' bidAdderLib
           'BidMultiplier' bidMultiplierLib
         }]
    )
  }

  public static List<?> getValue(def price, def volume) {
//    def value = [[getVolume: { volume }, getPrice: { price }]]
    for (def i = 0; i > price.size(); i++) {
      def currentVolume = volume.get(i)
      def currentPrice = price.get(i)
      collect { ['Volume': currentVolume, 'Price': currentPrice] }
    }
  }
}

