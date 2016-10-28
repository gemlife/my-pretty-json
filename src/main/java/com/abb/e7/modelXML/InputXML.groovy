package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class InputXML {

  def marketCode = "IRELAND"
  def type = "IREGEN"
  def action = "INSERT"
  def rowID = 1
  def name = "Generator_IRE1"
  def isoID = "GU_000001"
  def startDate = "01/01/2015"
  def parentType = "MKT"
  def parentAsset = "IRELAND"
  def participantID = "IRE_TEST"
  def percentage = "100"
  def endDate = "01/01/2016"
  def meterNameFirst = "IREMETER1"
  def meterNameSecond = "IREMETER2"
  def meterNameThird = "IREMETER3"

  def writer = new StringWriter()

  def xml = new MarkupBuilder(writer).CROSS_MARKET () {
    MKT_CD(marketCode)
      TRANSACTION_POINTS() {
        TRANSACTION_POINT() {
          TYPE(type)
          ORIG_ISO_ID()
            ACTION(action)
            ROW_ID(rowID)
            INTERVALS() {
              INTERVAL() {
                TX_PT_NAME(name)
                ISO_ID(isoID)
                START_DATE(startDate)
                PARENTS() {
                  PARENT() {
                    PARENT_TYPE(parentType)
                    PARENT_TX_PT(parentAsset)
                  }
                }
              }
              OWNERSHIP() {
                OWNER() {
                  PTCPT_CD(participantID)
                  PERCENTAGE(percentage)
                  START_DATE(startDate)
                  END_DATE(endDate)
                }
              }
              METERS() {
                METER() {
                  NAME (meterNameFirst)
                }
                METER() {
                  NAME (meterNameSecond)
                }
                METER() {
                  NAME (meterNameThird)
                }
              }
            }
          }
        }
    return writer.toString ()
  }
}