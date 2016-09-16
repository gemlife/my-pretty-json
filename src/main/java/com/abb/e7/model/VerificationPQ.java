package com.abb.e7.model

import com.abb.e7.core.SupplyCurveCalculationService
import com.abb.e7.model.*
import io.restassured.path.json.JsonPath
import org.junit.Test


public class VerificationPQ {

    def inputJson = json.buildSPInputJSON()

    List<Pattern> pricePatterns = null;
    List<Pattern> quantityPatterns = null;

    String body = SupplyCurveCalculationService.postWithLogging()
    def priceArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Price")
    List<?> quantityArray = JsonPath.from(body).get("Results.PQPairs.Blocks.Quantity")
    priceArray = extractUnderlyingList(priceArray)

    quantityArray = extractUnderlyingList(quantityArray)

    println priceArray
    println quantityArray

            for(def i = 0; i<quantityPatterns.size()-1;i++)

    {
        List<String> currentQuantityBlock = quantityArray.get(i)
        for (def j = 0; j < currentQuantityBlock.size(); j++) {
            def appropriateQuantity = quantityPatterns.get(j)
            def currentQuantity = currentQuantityBlock.get(j).toString()
            assert currentQuantity.matches(appropriateQuantity)
        }
    }

    private static List<String> extractUnderlyingList(def price) {
        while (price.size() == 1) {
            price = price.get(0)
        }
        return price
    }
}


