package com.abb.e7.model.Templates

import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.MarketData
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.UnitParameters
import groovy.json.JsonBuilder

class InputJSONWithSinglePeriods {

  def marketCode = "Market"
  def marketTypeCode = "DAM"
  def participantId = "036123294152"
  def bidTactics
  def calculationsParameters = new CalculationParameters()
  def marketParameters = new MarketData()
  def unitCharacteristic = new UnitParameters()
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
