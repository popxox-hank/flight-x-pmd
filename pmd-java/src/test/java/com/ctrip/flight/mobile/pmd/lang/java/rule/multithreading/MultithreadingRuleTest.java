package com.ctrip.flight.mobile.pmd.lang.java.rule.multithreading;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class MultithreadingRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/multithreading/flight-x-multithreading.xml";

    @Override
    protected void setUp() {
        addRule(RULE_SET, "AvoidThreadGroupRule");
        addRule(RULE_SET, "DoubleCheckedLockingRule");
    }
}
