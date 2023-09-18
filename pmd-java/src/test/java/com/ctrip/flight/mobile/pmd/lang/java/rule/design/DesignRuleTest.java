package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class DesignRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/design/flight-x-design.xml";

    @Override
    protected void setUp() {
        addRule(RULE_SET, "AvoidDeeplyNestedIfStmtsRule");
        addRule(RULE_SET, "CognitiveComplexityRule");
        addRule(RULE_SET, "CollapsibleIfStatementsRule");
        addRule(RULE_SET, "CouplingBetweenObjectsRule");
        addRule(RULE_SET, "FinalFieldCouldBeStaticRule");
        addRule(RULE_SET, "ImmutableFieldRule");
        addRule(RULE_SET, "LogicInversionRule");
        addRule(RULE_SET, "MutableStaticStateRule");
        addRule(RULE_SET, "NcssCountRule");
        addRule(RULE_SET, "NPathComplexityRule");
        addRule(RULE_SET, "SimplifiedTernaryRule");
        addRule(RULE_SET, "SimplifyBooleanExpressionsRule");
        addRule(RULE_SET, "SimplifyBooleanReturnsRule");
        addRule(RULE_SET, "SimplifyConditionalRule");
        addRule(RULE_SET, "SingularFieldRule");
        addRule(RULE_SET, "SwitchDensityRule");
        addRule(RULE_SET, "TooManyFieldsRule");
        addRule(RULE_SET, "TooManyMethodsRule");
        addRule(RULE_SET, "UselessOverridingMethodRule");
        addRule(RULE_SET, "ExcessiveParameterListRule");
        addRule(RULE_SET, "ExcessivePublicCountRule");
    }
}
