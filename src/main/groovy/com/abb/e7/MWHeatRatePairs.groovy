package com.abb.e7

import groovy.json.JsonBuilder


class MWHeatRatePairs {

  def mwHRPoints
  def mw1 = 75
  def mw2 = 150
  def mw3 = 225
  def mw4 = 300
  def hr1 = 8000
  def hr2 = 8300
  def hr3 = 8600
  def hr4 = 9200

  private def builder = new JsonBuilder()

  def buildInputJSON() {
    return builder {

      'MWHeatRatePairsOlga' mwHRPoints = [
          {
            'MWPoint' mw1
            'HeatRate' hr1
          },
          {
            'MWPoint' mw2
            'HeatRate' hr2
          },
          {
            'MWPoint' mw3
            'HeatRate' hr3
          },
          {
            'MWPoint' mw4
            'HeatRate' hr4
          }
      ]

      //  def mwPoints = [75,150,225,300]
//  def heatRates = [8000,8300,8600,9200]
//

//      static public void main(String[] args) {
//        List<String> names = Arrays.asList("apple,orange,pear".split(","));
//        List<String> things = Arrays.asList("123,456,789".split(","));
//        Map<String,String> map = combineListsIntoOrderedMap (names, things);
//        System.out.println(map);
//      }

//     def map3 = mwPoints[1]:heatRates[1]
//      println "${map3}"
    }
  }
}