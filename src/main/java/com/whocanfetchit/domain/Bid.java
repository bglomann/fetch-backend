package com.whocanfetchit.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Bid {

    /**
     */
    @ManyToOne
    private Participant bidder;

    /**
     */
    @ManyToOne
    private Task task;

    /**
     */
    private double amount;

    /**
     */
    @Enumerated(EnumType.ORDINAL)
    private BidState bidState;
}
