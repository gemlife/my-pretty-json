package com.abb.e7.model.Templates

import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.MarketData
import com.abb.e7.model.PeriodsData.PeriodsDataFirst
import com.abb.e7.model.PeriodsData.PeriodsDataSecond
import com.abb.e7.model.PeriodsData.PeriodsDataThird
import com.abb.e7.model.UnitParameters
import groovy.json.JsonBuilder

class InputJSONWithThreePeriods {

  def marketCode = "CAISO"
  def marketTypeCode = "DAM"
  def participantId = "036123294152"
  def bidTactics
  def calculationsParameters = new CalculationParameters()
  def marketParameters = new MarketData()
  def unitCharacteristic = new UnitParameters()
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