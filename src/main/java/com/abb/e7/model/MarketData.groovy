package com.abb.e7.model

import groovy.json.JsonBuilder

class MarketData {

  def marketProductType = "MP type"
  def marketSelfScheduleType = "M SS type"

  private def builder = new JsonBuilder()

  def buildMPInputJSON() {
    return builder {
      'MarketProductType' marketProductType
      'MarketSelfScheduleType' marketSelfScheduleType
    }
  }
}
