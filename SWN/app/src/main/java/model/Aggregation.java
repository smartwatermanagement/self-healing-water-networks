package model;

/**
 * Created by kempa on 22/10/14.
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Dummy data structure to hold information about the aggregates
 */
public class Aggregation implements IAggregation, Serializable {
    private String name;
    private List<IAggregation> children;
    private int consumption;
    private Aggregation parent;
    private int issueCount = 0;
    private int id;

    public Aggregation(){

    }

    public Aggregation(String name, int consumption, Aggregation parent, int issueCount) {
        this.name = name;
        this.consumption = consumption;
        this.children = new LinkedList<IAggregation>();
        this.parent = parent;

        this.issueCount = issueCount;
    }

    public Aggregation(int id, String name, int consumption) {
        this.id = id;
        this.name = name;
        this.consumption = consumption;
    }

    public void setChildren(List<IAggregation> children) {
        this.children = children;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public void setParent(Aggregation parent) {
        this.parent = parent;
    }

    public void addChild(IAggregation IAggregation) {
        children.add(IAggregation);
    }

    public List<IAggregation> getChildren() {
        return children;
    }

    public int getConsumption() {
        return consumption;
    }

    public Aggregation getParent() {
        return parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {

        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public int getIssueCount() {

        return issueCount;
    }

}