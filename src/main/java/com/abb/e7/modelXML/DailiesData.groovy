package com.abb.e7.modelXML

import groovy.xml.MarkupBuilder

class DailiesData {
  def coldStartupCost = "1.11"
  def interStartupCost = "2.11"
  def hotStartupCost = "3.11"
  def minRunTime = "60"
  def maxRunTime = "120"
  def minDownTime = "180"
  def hotToColdTime = "240"
  def hotToInterTime = "300"
  def maxDailyEnergy = "99"
  def maxWeeklyStarts = "14"
  def maxWeeklyEnergy = "10.88"
  def condenseAvailFlg = "NO"
  def condenseNotifyTime = "700"
  def condenseStartupCost = "12.11"
  def condenseHourlyCost = "13.11"
  def condensePower = "14.11"
  def userComments = "Daily comment"


  def builder = new MarkupBuilder()

  def xmlBuilder() {
    return {
      COLD_STARTUP_COST(coldStartupCost)
      INTER_STARTUP_COST(interStartupCost)
      HOT_STARTUP_COST(hotStartupCost)
      MIN_RUN_TIME(minRunTime)
      MAX_RUN_TIME(maxRunTime)
      MIN_DOWN_TIME(minDownTime)
      HOT_TO_COLD_TIME(hotToColdTime)
      HOT_TO_INTER_TIME(hotToInterTime)
      MAX_DAILY_STARTS(maxDailyEnergy)
      MAX_DAILY_ENERGY(maxDailyEnergy)
      MAX_WEEKLY_STARTS(maxWeeklyStarts)
      MAX_WEEKLY_ENERGY(maxWeeklyEnergy)
      CONDENSE_AVAIL_FLG(condenseAvailFlg)
      CONDENSE_NOTIFY_TIME(condenseNotifyTime)
      CONDENSE_STARTUP_COST(condenseStartupCost)
      CONDENSE_HOURLY_COST(condenseHourlyCost)
      CONDENSE_POWER(condensePower)
      USER_COMMENTS(userComments)
    }
  }
}
