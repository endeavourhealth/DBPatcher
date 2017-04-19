package org.endeavourhealth.dbpatcher.dataLayer.model;

public class Trigger {
    private String triggerName;
    private String type;
    private String event;
    private String table;
    private String level;
    private String shortDescription;
    private String description;

    public String getTriggerName() {
        return triggerName;
    }

    public Trigger setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public String getType() {
        return type;
    }

    public Trigger setType(String type) {
        this.type = type;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public Trigger setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getTable() {
        return table;
    }

    public Trigger setTable(String table) {
        this.table = table;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public Trigger setLevel(String level) {
        this.level = level;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Trigger setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Trigger setDescription(String description) {
        this.description = description;
        return this;
    }
}
