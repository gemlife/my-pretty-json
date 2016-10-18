package com.abb.e7.model

import com.abb.e7.model.CalculationParameters
import com.abb.e7.model.MarketData
import com.abb.e7.model.UnitParameters
import groovy.json.JsonBuilder

class InputJSON {

  def marketCode = "CAISO"
  def marketTypeCode = "DAM"
  def participantId = "036123294152"
  def bidTactics
  def calculationsParameters = new CalculationParameters()
  def marketParameters = new MarketData()
  def unitCharacteristic = new UnitParameters()
  def inputData = []
  def periodsData = []
//  def firstPeriod, secondPeriod, thirdPeriod
  def bidLibraryArray = []

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
             'InputData' inputData
//                 {[
//                 'UnitCharacteristic': unitCharacteristic,
//                 'PeriodsData': periodsData,
//                 'BidLibraryPeriodsData': bidLibraryArray
//             ]}
           }]
    } as JsonBuilder
  }
}
