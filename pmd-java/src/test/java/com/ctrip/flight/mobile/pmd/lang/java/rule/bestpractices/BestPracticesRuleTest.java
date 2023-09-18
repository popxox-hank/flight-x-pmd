package com.ctrip.flight.mobile.pmd.lang.java.rule.bestpractices;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-07-17
 */
public class BestPracticesRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET = "src/main/resources/rulesets/java/bestpractices/flight-x-best-practices.xml";

    @Override
    protected void setUp() {


        addRule(RULE_SET, "AvoidReassigningLoopVariablesRule");
        addRule(RULE_SET, "AvoidReassigningParametersRule");
        addRule(RULE_SET, "AvoidStringBufferFieldRule");
        addRule(RULE_SET, "ConstantInterfaceRule");
        addRule(RULE_SET, "DefaultLabelNotLastInSwitchStmtRule");
        addRule(RULE_SET, "DoubleBraceInitializationRule");
        addRule(RULE_SET, "ForLoopVariableCountRule");
        addRule(RULE_SET, "JUnitTestsShouldIncludeAssertRule");
        addRule(RULE_SET, "EqualsAvoidNullRule");
        addRule(RULE_SET, "UnusedFormalParameterRule");
        addRule(RULE_SET, "UnusedLocalVariableRule");
        addRule(RULE_SET, "UnusedPrivateFieldRule");
        addRule(RULE_SET, "UnusedPrivateMethodRule");
        addRule(RULE_SET, "UnusedAssignmentRule");
    }


}
