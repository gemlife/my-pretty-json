package com.abb.e7.model

import groovy.json.JsonBuilder

class StartFuelsIDs {
    def startFuelIDs = ["Fuel NG1", "Fuel NG2", "Fuel NG3"]
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
