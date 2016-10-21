package com.abb.e7.modelJSON

import groovy.json.JsonBuilder

class StartFuelsIDs {
    def startFuelIDs = ["Fuel N1"]
    def quantity = 1000.0
    def startRatio = null

    private def builder = new JsonBuilder()

    def buildInputJSON() {
        return builder {
            'Ids' startFuelIDs
            'Quantity' quantity
            'Ratios' startRatio
        }

    }
}
