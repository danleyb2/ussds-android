package com.nyaundibrian.ussds.config;

/**
 * Created by ndiek on 8/2/2016.
 */
public class Config {

    private static final String HOSTNAME = "https://ussds.herokuapp.com/api/";
    private static final String USSDS = "ussds";
    private static final String COMPANIES = "companies";


    public static String getHostname() {
        return HOSTNAME;
    }

    public static String Companies() {
        return COMPANIES;
    }
}
