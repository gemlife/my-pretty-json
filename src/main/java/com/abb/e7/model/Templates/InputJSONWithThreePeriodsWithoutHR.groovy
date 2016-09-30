package com.abb.e7.model.Templates

import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.MarketData
import com.abb.e7.model.PeriodsData.PeriodsDataFirstWithoutHR
import com.abb.e7.model.PeriodsData.PeriodsDataSecondWithoutHR
import com.abb.e7.model.PeriodsData.PeriodsDataThirdWithoutHR
import com.abb.e7.model.UnitParameters
import groovy.json.JsonBuilder

class InputJSONWithThreePeriodsWithoutHR {

  def marketCode = "CAISO"
  def marketTypeCode = "DAM"
  def participantId = "036123294152"
  def bidTactics
  def calculationsParameters = new CalculationParameters()
  def marketParameters = new MarketData()
  def unitCharacteristic = new UnitParameters()
  def inputData
  def periodsDataFirst = new PeriodsDataFirstWithoutHR()
  def periodsDataSecond = new PeriodsDataSecondWithoutHR()
  def periodsDataThird = new PeriodsDataThirdWithoutHR()

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
