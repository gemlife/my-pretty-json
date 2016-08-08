package com.abb.e7.model

import groovy.json.JsonBuilder

class MWHeatRatePoints {
    def mwHRPoint
    def mw = [75.0, 150.0, 225.0, 300.0]
    def hr = [8000.0, 8300.0, 8600.0, 9200.0]

    private def builder = new JsonBuilder()

    def buildInputJSON() {
        return builder.call (
            [
                    {
                        'MWPoint' mw[0]
                        'HeatRate' hr[0]
                    },
                    {
                        'MWPoint' mw[1]
                        'HeatRate' hr[1]
                    },
                    {
                        'MWPoint' mw[2]
                        'HeatRate' hr[2]
                    },
                    {
                        'MWPoint' mw[3]
                        'HeatRate' hr[3]
                    }
            ]
        )
    }
}
