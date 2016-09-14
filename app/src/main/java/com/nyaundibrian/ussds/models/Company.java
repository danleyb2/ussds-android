package com.nyaundibrian.ussds.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ndiek on 8/2/2016.
 */
public class Company extends RealmObject {
    public String name;

    @PrimaryKey
    private int id;
    private int ussd_count;
    private String ussds_url;
    private String website;
    private String created_at;
    private String icon;
    private RealmList<Ussd> ussds;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUssds_url() {
        return ussds_url;
    }

    public void setUssds_url(String ussds_url) {
        this.ussds_url = ussds_url;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public int getUssd_count() {
        return ussd_count;
    }

    public void setUssd_count(int ussd_count) {
        this.ussd_count = ussd_count;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUssds(RealmList<Ussd> ussds) {
        this.ussds = ussds;
    }


    public RealmList<Ussd> getUssds() {
        return ussds;
    }

    public String getCreated_at() {
        return created_at;
    }


}
