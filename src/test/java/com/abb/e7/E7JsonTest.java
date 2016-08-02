package com.abb.e7;

import com.abb.e7.core.E7Json;
import org.json.JSONObject;
import org.junit.Test;

import static com.abb.e7.core.E7Json.emptyJsonTemplate;
import static org.json.JSONObject.NULL;
import static org.junit.Assert.assertEquals;

public class E7JsonTest {

    @Test
    public void shouldCreateCorrectValues() {
        E7Json e7Json = emptyJsonTemplate()
                .withName("Olyasha")
                .withBook("Tur Gambit")
                .withRequiredValue("hoho")
                .build();

        JSONObject builtJson = e7Json.getJson();

        assertEquals("Olyasha", builtJson.get("name"));
        assertEquals("Tur Gambit", builtJson.get("bookModel"));
        assertEquals("hoho", builtJson.get("requiredValue"));
    }

    @Test
    public void shouldNotModifyJson() {
        E7Json e7Json = emptyJsonTemplate()
                .withName("Olyasha")
                .build();
        e7Json.getJson().put("name", "Tolyaniy");

        JSONObject builtJson = e7Json.getJson();

        assertEquals("Olyasha", builtJson.get("name"));
    }

    @Test
    public void shouldCloneEmptyJson() {
        E7Json e7Json = emptyJsonTemplate();

        JSONObject builtJson = e7Json.getJson();

        assertEquals(0, builtJson.length());
    }

    @Test
    public void shouldPopulateNullableValues() {
        E7Json e7Json = emptyJsonTemplate()
                .build();

        JSONObject builtJson = e7Json.getJson();

        assertEquals(NULL, builtJson.get("requiredValue"));
    }

    @Test
    public void shouldConvertToStringAsPrettyJson() {
        E7Json e7Json = emptyJsonTemplate().build();

        String result = e7Json.toString();

        assertEquals(e7Json.getJson().toString(4), result);
    }
}
