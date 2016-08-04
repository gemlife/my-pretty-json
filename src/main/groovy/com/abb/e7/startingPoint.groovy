package com.abb.e7

import groovy.json.JsonBuilder

class StartingPoint {

    def firstName = 'Guillame'
    def lastName = 'Surname'
    def cityOfLiving = 'Paris'
    def country = 'France'
    def zip = 12345
    def married = true
    def conferences = ['JavaOne', 'Gr8conf']

    def builder = new JsonBuilder()

    private def root = builder.people {
        person {
            firstName firstName
            lastName lastName
            address {
                city cityOfLiving
                country country
                zip zip
            }
            married married
            conferences conferences
        }
    }
}

