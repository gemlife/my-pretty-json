package com.abb.e7.model

import groovy.json.JsonBuilder

class InputJSONWithThreePeriods {

  def marketCode = "CAISO"
  def marketTypeCode = "DAM"
  def participantId = "036123294152"
  def bidTactics
  def calculationsParameters = new CalculationsParameters()
  def marketParameters = new MarketParameters()
  def unitCharacteristic = new UnitCharacteristic()
  def inputData
  def periodsDataFirst = new PeriodsDataFirst()
  def periodsDataSecond = new PeriodsDataSecond()
  def periodsDataThird = new PeriodsDataThird()

  private def builder = new JsonBuilder()

  def buildSPInputJSON() {
    return builder {
      'MarketCode' marketCode
      'MarketTypeCode' marketTypeCode
      'ParticipantId' participantId
      'BidTactics' bidTactics =
          [{
             def flags = calculationsParameters.buildCPInputJSON()
             'CalculationsParameters' flags
             def market = marketParameters.buildMPInputJSON()
             'MarketParameters' market

             "InputData" inputData =
                 [{
                    def units = unitCharacteristic.buildUCInputJSON()
                    'UnitCharacteristic' units
                    def firstPeriod = periodsDataFirst.buildPRInputJSON()
                    def secondPeriod = periodsDataSecond.buildPRInputJSON()
                    def thirdPeriod = periodsDataThird.buildPRInputJSON()
                    'PeriodsData' ([firstPeriod, secondPeriod, thirdPeriod])
                  }]
           }]
    } as JsonBuilder
  }
}
