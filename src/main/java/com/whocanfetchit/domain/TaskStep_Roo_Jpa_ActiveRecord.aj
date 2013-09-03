// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.whocanfetchit.domain;

import com.whocanfetchit.domain.TaskStep;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect TaskStep_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager TaskStep.entityManager;
    
    public static final EntityManager TaskStep.entityManager() {
        EntityManager em = new TaskStep().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long TaskStep.countTaskSteps() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TaskStep o", Long.class).getSingleResult();
    }
    
    public static List<TaskStep> TaskStep.findAllTaskSteps() {
        return entityManager().createQuery("SELECT o FROM TaskStep o", TaskStep.class).getResultList();
    }
    
    public static TaskStep TaskStep.findTaskStep(Long id) {
        if (id == null) return null;
        return entityManager().find(TaskStep.class, id);
    }
    
    public static List<TaskStep> TaskStep.findTaskStepEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TaskStep o", TaskStep.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void TaskStep.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void TaskStep.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TaskStep attached = TaskStep.findTaskStep(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void TaskStep.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void TaskStep.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public TaskStep TaskStep.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TaskStep merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}