package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class PerformanceRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/performance/flight-x-performance.xml";

    @Override
    protected void setUp() {
        addRule(RULE_SET, "AddEmptyStringRule");
        addRule(RULE_SET, "AppendCharacterWithCharRule");
        addRule(RULE_SET, "AvoidCalendarDateCreationRule");
        addRule(RULE_SET, "AvoidFileStreamRule");
        addRule(RULE_SET, "AvoidInstantiatingObjectsInLoopsRule");
        addRule(RULE_SET, "BigIntegerInstantiationRule");
        addRule(RULE_SET, "ConsecutiveAppendsShouldReuseRule");
        addRule(RULE_SET, "ConsecutiveLiteralAppendsRule");
        addRule(RULE_SET, "InefficientStringBufferingRule");
        addRule(RULE_SET, "InsufficientStringBufferDeclarationRule");
        addRule(RULE_SET, "OptimizableToArrayCallRule");
        addRule(RULE_SET, "RedundantFieldInitializerRule");

        addRule(RULE_SET, "TooFewBranchesForASwitchStatementRule");
        addRule(RULE_SET, "UseIndexOfCharRule");
        addRule(RULE_SET, "UseIOStreamsWithApacheCommonsFileItemRule");
        addRule(RULE_SET, "UselessStringValueOfRule");
        addRule(RULE_SET, "UseStringBufferForStringAppendsRule");
        addRule(RULE_SET, "UseStringBufferLengthRule");
    }
}
