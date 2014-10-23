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
public class AggregationImpl implements Aggregation, Serializable {
    private String name;
    private List<Aggregation> children;
    private int consumption;
    private AggregationImpl parent;
    private int issueCount = 0;


    public AggregationImpl(String name, int consumption, AggregationImpl parent, int issueCount) {
        this.name = name;
        this.consumption = consumption;
        this.children = new LinkedList<Aggregation>();
        this.parent = parent;

        this.issueCount = issueCount;
    }
    public AggregationImpl(String name, int consumption, AggregationImpl parent) {
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

    public AggregationImpl getParent() {
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