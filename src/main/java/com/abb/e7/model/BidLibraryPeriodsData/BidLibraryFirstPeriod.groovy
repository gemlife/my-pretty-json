package com.abb.e7.model.BidLibraryPeriodsData

import groovy.json.JsonBuilder

class BidLibraryFirstPeriod {

  def bidLibraryPeriod = "2016-03-07T09:00:00.000Z"
  def fixedCommitmentType = "Economic"
  def bidAdderLib = 0.0
  def bidMultiplierLib = 1.0
  def volumeBL
  def priceBL

  private def builderBidLibrary = new JsonBuilder()

  def buildBLInputJSON() {
    return builderBidLibrary {
         'Period' bidLibraryPeriod
         'FixedCommitmentType' fixedCommitmentType
         'Blocks'([volumeBL, priceBL].transpose().collect {
           [Volume: it[0], Price: it[1]]
         })
         'BidAdder' bidAdderLib
         'BidMultiplier' bidMultiplierLib
    }
  }
}


