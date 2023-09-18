package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class CodeStyleRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/codestyle/flight-x-code-style.xml";

    @Override
    protected void setUp() {

        addRule(RULE_SET, "BooleanGetMethodNameRule");
        addRule(RULE_SET, "ClassNamingConventionsRule");
        addRule(RULE_SET, "ControlStatementBracesRule");


        addRule(RULE_SET, "FieldNamingConventionsRule");
        addRule(RULE_SET, "FormalParameterNamingConventionsRule");
        addRule(RULE_SET, "GenericsNamingRule");
        addRule(RULE_SET, "LinguisticNamingRule");
        addRule(RULE_SET, "LocalVariableNamingConventionsRule");
        addRule(RULE_SET, "LongVariableRule");
        addRule(RULE_SET, "MethodNamingConventionsRule");
        addRule(RULE_SET, "PackageCaseRule");
        addRule(RULE_SET, "PrematureDeclarationRule");
        addRule(RULE_SET, "UnnecessaryLocalBeforeReturnRule");

        addRule(RULE_SET, "UnnecessaryConstructorRule");
        addRule(RULE_SET, "UnnecessaryModifierRule");

    }
}
