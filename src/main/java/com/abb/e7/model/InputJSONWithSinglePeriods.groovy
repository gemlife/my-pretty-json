package com.abb.e7.model

import groovy.json.JsonBuilder

class InputJSONWithSinglePeriods {

  def marketCode = "CAISO"
  def marketTypeCode = "DAM"
  def participantId = "036123294152"
  def bidTactics
  def calculationsParameters = new CalculationsParameters()
  def marketParameters = new MarketParameters()
  def unitCharacteristic = new UnitCharacteristic()
  def inputData
  def periodsData = new PeriodsDataFirst()

  private def builder = new JsonBuilder()

  def buildInputJSON() {
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
                    def firstPeriod = periodsData.buildPRInputJSON()
                    'PeriodsData'([firstPeriod])

                  }]
           }]
    } as JsonBuilder
  }
}
