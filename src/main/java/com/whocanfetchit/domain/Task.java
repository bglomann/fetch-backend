package com.whocanfetchit.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ManyToMany;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findTasksByTaskState" })
public class Task {

    /**
     */
    private String description;

    /**
     */
    private double expenses;

    /**
     */
    @ManyToOne
    private Participant owner;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dueDate;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<TaskStep> steps = new ArrayList<TaskStep>();

    /**
     */
    @Enumerated(EnumType.ORDINAL)
    private TaskState taskState;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private Set<Bid> bids = new HashSet<Bid>();
}
