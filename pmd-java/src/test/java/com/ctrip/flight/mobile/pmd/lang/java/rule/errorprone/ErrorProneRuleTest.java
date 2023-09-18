package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author haoren
 * Create at: 2023-08-02
 */
public class ErrorProneRuleTest extends SimpleAggregatorTst {

    private static final String RULE_SET =
            "src/main/resources/rulesets/java/errorprone/flight-x-error-prone.xml";

    @Override
    protected void setUp() {
        addRule(RULE_SET, "AssignmentInOperandRule");
        addRule(RULE_SET, "AssignmentToNonFinalStaticRule");
        addRule(RULE_SET, "AvoidDecimalLiteralsInBigDecimalConstructorRule");

        addRule(RULE_SET, "AvoidFieldNameMatchingMethodNameRule");
        addRule(RULE_SET, "AvoidFieldNameMatchingTypeNameRule");
        addRule(RULE_SET, "AvoidInstanceofChecksInCatchClauseRule");
        addRule(RULE_SET, "AvoidMultipleUnaryOperatorsRule");
        addRule(RULE_SET, "CloneMethodMustBePublicRule");
        addRule(RULE_SET, "CloneMethodReturnTypeMustMatchClassNameRule");
        addRule(RULE_SET, "CompareObjectsWithEqualsRule");
        addRule(RULE_SET, "DoNotThrowExceptionInFinallyRule");
        addRule(RULE_SET, "EmptyCatchBlockRule");


        addRule(RULE_SET, "MethodWithSameNameAsEnclosingClassRule");
        addRule(RULE_SET, "MissingSerialVersionUIDRule");
        addRule(RULE_SET, "NonSerializableClassRule");
        addRule(RULE_SET, "OverrideBothEqualsAndHashcodeRule");
        addRule(RULE_SET, "ProperLoggerRule");
        addRule(RULE_SET, "ReturnEmptyCollectionRatherThanNullRule");
        addRule(RULE_SET, "ReturnFromFinallyBlockRule");


    }
}
