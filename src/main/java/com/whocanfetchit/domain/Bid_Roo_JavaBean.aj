// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.whocanfetchit.domain;

import com.whocanfetchit.domain.Bid;
import com.whocanfetchit.domain.BidState;
import com.whocanfetchit.domain.Participant;
import com.whocanfetchit.domain.Task;

privileged aspect Bid_Roo_JavaBean {
    
    public Participant Bid.getBidder() {
        return this.bidder;
    }
    
    public void Bid.setBidder(Participant bidder) {
        this.bidder = bidder;
    }
    
    public Task Bid.getTask() {
        return this.task;
    }
    
    public void Bid.setTask(Task task) {
        this.task = task;
    }
    
    public double Bid.getAmount() {
        return this.amount;
    }
    
    public void Bid.setAmount(double amount) {
        this.amount = amount;
    }
    
    public BidState Bid.getBidState() {
        return this.bidState;
    }
    
    public void Bid.setBidState(BidState bidState) {
        this.bidState = bidState;
    }
    
}
