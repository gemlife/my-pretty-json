package com.abb.e7.model

import groovy.json.JsonBuilder

class RegularFuelsData {
    def priceArray = [6.83, 5.5, 4.15]
    def fuelNameArray = ['Fuel NG1', 'Fuel NG2', 'Fuel NG3']

    private def builder = new JsonBuilder()

    def buildInputJSON() {
        return builder.call(
                [
                        {
                            'Id' fuelNameArray[0]
                            'Price' priceArray[0]
                        },
                        {
                            'Id' fuelNameArray[1]
                            'Price' priceArray[1]
                        },
                        {
                            'Id' fuelNameArray[2]
                            'Price' priceArray[2]
                        }
                ]
        )
    }
}
