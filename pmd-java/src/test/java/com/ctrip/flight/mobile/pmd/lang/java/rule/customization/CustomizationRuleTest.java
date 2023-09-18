package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-08-29
 */
public class CustomizationRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/customization/flight-x-customization.xml";

    @Override
    protected void setUp() {
        addRule(RULE_SET, "StreamExpressionStyleRule");
        addRule(RULE_SET, "ConditionalTooLongNeedChangeLineRule");
        addRule(RULE_SET, "AvoidComplexConditionalRule");
        addRule(RULE_SET, "StreamExpressionTooLongRule");
        addRule(RULE_SET, "AvoidStreamExpressionInIfStmtsRule");
        addRule(RULE_SET, "UndefinedMagicConstantRule");
        addRule(RULE_SET, "AvoidIfStmtsInSwitchStmtsRule");
        addRule(RULE_SET, "AvoidStreamExpressionInStreamExpressionRule");
        addRule(RULE_SET, "GuardClausesRule");
        addRule(RULE_SET, "CollectorsToMapUnUseThirdParamRule");
        addRule(RULE_SET, "AvoidTooManyConditionRule");
        addRule(RULE_SET, "AvoidUseComplexStreamExpressionInSetMethodRule");
        addRule(RULE_SET, "AvoidUseUnSynchronizedFormatRule");
    }
}
