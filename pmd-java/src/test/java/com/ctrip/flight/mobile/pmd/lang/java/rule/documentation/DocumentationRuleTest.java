package com.ctrip.flight.mobile.pmd.lang.java.rule.documentation;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class DocumentationRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/documentation/flight-x-documentation.xml";

    @Override
    protected void setUp() {
        addRule(RULE_SET, "CommentRequiredRule");
    }
}
