package com.abb.e7.modelJSON

import groovy.json.JsonBuilder

class FuelsInputData {
    def fuelIDs = ["Fuel N1", "Fuel N2", "Fuel N3"]
    def regularRatio = null
    def useMinCostFuel = true
    def handlingCost = 0.0
    def dfcm = 1.0

    private def builder = new JsonBuilder()

    def buildInputJSON() {
        return builder {
            "Ids" fuelIDs
            'Ratios' regularRatio
            'UseMinCostFuel' useMinCostFuel
            'HandlingCost' handlingCost
            'DFCM' dfcm
        }
    }
}
