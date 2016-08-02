package com.abb.e7.core;

import org.json.JSONObject;

import static com.abb.e7.utils.E7ModelUtils.requiredNullableValueOf;
import static org.json.JSONObject.NULL;

public class E7Json {

    private JSONObject myJsonBuilder;

    private String name;
    private String bookModel;
    private String myRequiredValue;
    private String notRequiredValue;

    private E7Json() {
        myJsonBuilder = new JSONObject();
    }

    public static E7Json emptyJsonTemplate() {
//        should contain default initial minimal JSONObject configuration.
//        build() method will override all the passed by "with...()" values
//        if required more templates - a separate method for each E7JsonTemplate should be created,
//        and will return any of the required templates with further possibility to use
//        with...() methods.
        return new E7Json();
    }

    public E7Json withName(String name) {
        this.name = name;
        return this;
    }

    public E7Json withBook(String bookModel) {
        this.bookModel = bookModel;
        return this;
    }

    public E7Json withRequiredValue(String myRequiredValue) {
        this.myRequiredValue = myRequiredValue;
        return this;
    }

    public E7Json build() {
        myJsonBuilder
                .put("name", name)
                .put("bookModel", bookModel)
                .put("requiredValue", requiredNullableValueOf(myRequiredValue))
                .put("ololo",
                new JSONObject()
                        .put("putin", "plohoi chelovek")
                        .put("Dzigurda", "man"))
                .put("ktoeto", NULL)
                .put("hzKto", notRequiredValue);
        return this;
    }

    /**
     * Returns reference to the underlying json without possibility to modify it
     * @return JSONObject
     */
    public JSONObject getJson() {
        return new JSONObject(myJsonBuilder.toString());
    }

    @Override
    public String toString() {
        return myJsonBuilder.toString(4);
    }
}
