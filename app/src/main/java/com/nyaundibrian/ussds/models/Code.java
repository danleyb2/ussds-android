package com.nyaundibrian.ussds.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ndiek on 8/2/2016.
 */
public class Code extends RealmObject {
    @PrimaryKey
    private int id;

    private String value;
    private boolean template;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public boolean isTemplate() {
        return template;
    }
}
