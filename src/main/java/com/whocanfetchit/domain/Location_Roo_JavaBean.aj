// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.whocanfetchit.domain;

import com.whocanfetchit.domain.Location;

privileged aspect Location_Roo_JavaBean {
    
    public String Location.getAddress() {
        return this.address;
    }
    
    public void Location.setAddress(String address) {
        this.address = address;
    }
    
    public double Location.getLatitude() {
        return this.latitude;
    }
    
    public void Location.setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double Location.getLongitude() {
        return this.longitude;
    }
    
    public void Location.setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
}