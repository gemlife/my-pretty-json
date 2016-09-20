package com.abb.e7.model

import groovy.json.JsonBuilder

class StartFuelsIDs {
    def startFuelIDs = ["Fuel N1", "Fuel N2", "Fuel N3"]
    def quantity = 1000.0
    def startRatio

    private def builder = new JsonBuilder()

    def buildInputJSON() {
        return builder {
            'Ids' startFuelIDs
            'Quantity' quantity
            'Ratio' startRatio
        }

    }
}
