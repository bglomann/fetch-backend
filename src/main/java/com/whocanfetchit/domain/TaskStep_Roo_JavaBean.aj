// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.whocanfetchit.domain;

import com.whocanfetchit.domain.Location;
import com.whocanfetchit.domain.TaskStep;

privileged aspect TaskStep_Roo_JavaBean {
    
    public Location TaskStep.getLocation() {
        return this.location;
    }
    
    public void TaskStep.setLocation(Location location) {
        this.location = location;
    }
    
}
