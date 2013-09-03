package com.whocanfetchit.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findParticipantsByLoginEquals" })
public class Participant {

    /**
     */
    private String login;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bidder")
    private Set<Bid> bids = new HashSet<Bid>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Set<Task> owned_tasks = new HashSet<Task>();
}
