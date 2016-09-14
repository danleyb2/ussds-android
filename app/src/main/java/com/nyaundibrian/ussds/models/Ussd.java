package com.nyaundibrian.ussds.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ndiek on 8/2/2016.
 */
public class Ussd extends RealmObject {
    @PrimaryKey
    private int id;
    private String description;
    private Code code;
    private boolean confirmed;
    private String last_confirmed;

    private String invalidation_count;
    private String invalidation_list_url;

    private String created_at;
    private String updated_at;

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLast_confirmed() {
        return last_confirmed;
    }

    public void setLast_confirmed(String last_confirmed) {
        this.last_confirmed = last_confirmed;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }


}
