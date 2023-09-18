package com.ctrip.flight.mobile.pmd.lang.java.rule;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.AbstractRule;
import net.sourceforge.pmd.lang.rule.stat.StatisticalRule;
import net.sourceforge.pmd.stat.DataPoint;
import net.sourceforge.pmd.stat.Metric;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class FlightStatisticalJavaRule extends AbstractJavaRule implements StatisticalRule {


    private static final double DELTA = 0.000005;

    private AbstractRule rule;

    private SortedSet<DataPoint> dataPoints = new TreeSet<>();

    private int count = 0;
    private double total = 0.0;

    public FlightStatisticalJavaRule() {
        this.rule = this;
        rule.definePropertyDescriptor(SIGMA_DESCRIPTOR);
        rule.definePropertyDescriptor(MINIMUM_DESCRIPTOR);
        rule.definePropertyDescriptor(TOP_SCORE_DESCRIPTOR);
    }

    @Override
    public void addDataPoint(DataPoint point) {
        addCustomDataPoint(point);
    }

    @Override
    public Object[] getViolationParameters(DataPoint point) {
        return new Object[0];
    }

    @Override
    public void apply(List<? extends Node> nodes, RuleContext ctx) {
        super.apply(nodes, ctx);
        apply(ctx);
    }


    private void addCustomDataPoint(DataPoint point) {
        count++;
        total += point.getScore();
        dataPoints.add(point);
    }

    private void apply(RuleContext ctx) {

        double deviation;
        double minimum = 0.0;

        if (rule.getProperty(SIGMA_DESCRIPTOR) != null) { // TODO - need to come
            deviation = getStdDev();
            double sigma = rule.getProperty(SIGMA_DESCRIPTOR);
            minimum = getMean() + (sigma * deviation);
        }

        if (rule.getProperty(MINIMUM_DESCRIPTOR) != null) { // TODO - need to
            double mMin = rule.getProperty(MINIMUM_DESCRIPTOR);
            if (mMin > minimum) {
                minimum = mMin;
            }
        }

        SortedSet<DataPoint> newPoints = applyMinimumValue(dataPoints, minimum);

        if (rule.getProperty(TOP_SCORE_DESCRIPTOR) != null) { // TODO - need to
            int topScore = rule.getProperty(TOP_SCORE_DESCRIPTOR);
            if (newPoints.size() >= topScore) {
                newPoints = applyTopScore(newPoints, topScore);
            }
        }

        makeViolations(ctx, newPoints);

        double low = 0.0d;
        double high = 0.0d;
        if (!dataPoints.isEmpty()) {
            low = dataPoints.first().getScore();
            high = dataPoints.last().getScore();
        }

        ctx.getReport().addMetric(new Metric(rule.getName(), count, total, low, high, getMean(), getStdDev()));

        dataPoints.clear();
    }

    private double getMean() {
        return total / count;
    }

    private double getStdDev() {
        if (dataPoints.size() < 2) {
            return Double.NaN;
        }

        double mean = getMean();
        double deltaSq = 0.0;
        double scoreMinusMean;

        for (DataPoint point : dataPoints) {
            scoreMinusMean = point.getScore() - mean;
            deltaSq += scoreMinusMean * scoreMinusMean;
        }

        return Math.sqrt(deltaSq / (dataPoints.size() - 1));
    }

    private SortedSet<DataPoint> applyMinimumValue(SortedSet<DataPoint> pointSet, double minValue) {
        SortedSet<DataPoint> rc = new TreeSet<>();
        double threshold = minValue - DELTA;

        for (DataPoint point : pointSet) {
            if (point.getScore() > threshold) {
                rc.add(point);
            }
        }
        return rc;
    }

    private SortedSet<DataPoint> applyTopScore(SortedSet<DataPoint> points, int topScore) {
        SortedSet<DataPoint> s = new TreeSet<>();
        DataPoint[] arr = points.toArray(new DataPoint[]{});
        for (int i = arr.length - 1; i >= (arr.length - topScore); i--) {
            s.add(arr[i]);
        }
        return s;
    }

    private void makeViolations(RuleContext ctx, Set<DataPoint> p) {
        for (DataPoint point : p) {
            FlightRuleUtils.addCustomViolation(rule, ctx, point.getNode(), point.getMessage(), null);
        }
    }
}
