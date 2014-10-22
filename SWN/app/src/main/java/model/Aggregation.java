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
public class Aggregation implements Serializable {
    private String name;
    private List<Aggregation> children;
    private int consumption;
    private Aggregation parent;
    private int issueCount = 0;


    public Aggregation(String name, int consumption, Aggregation parent, int issueCount) {
        this.name = name;
        this.consumption = consumption;
        this.children = new LinkedList<Aggregation>();
        this.parent = parent;

        this.issueCount = issueCount;
    }
    public Aggregation(String name, int consumption, Aggregation parent) {
        this.name = name;
        this.consumption = consumption;
        this.children = new LinkedList<Aggregation>();
        this.parent = parent;
    }

    public void addChild(Aggregation aggregation) {
        children.add(aggregation);
    }

    public List<Aggregation> getChildren() {
        return children;
    }

    public int getConsumption() {
        return consumption;
    }

    public Aggregation getParent() {
        return parent;
    }

    public String getName() {

        return name;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public int getIssueCount() {

        return issueCount;
    }

}