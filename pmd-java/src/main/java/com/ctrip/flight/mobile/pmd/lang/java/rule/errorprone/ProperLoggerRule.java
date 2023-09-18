package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-08-04
 */
public class ProperLoggerRule extends FlightXPathRule {

    private static final String XPATH = "//FieldDeclaration[Type/ReferenceType/ClassOrInterfaceType[pmd-java:typeIs" +
            "($loggerClass)]][(@Private = false() or @Final = false()) or (@Static and " +
            "VariableDeclarator/VariableDeclaratorId[@Name != $staticLoggerName]) or (@Static = false() and " +
            "VariableDeclarator/VariableDeclaratorId[@Name != $loggerName]) or " +
            ".//ArgumentList//ClassOrInterfaceType[@Image != " +
            "ancestor::ClassOrInterfaceDeclaration/@SimpleName] or .//ArgumentList//ClassOrInterfaceType[@Image != " +
            "ancestor::EnumDeclaration/@SimpleName]][not(not(VariableDeclarator/VariableInitializer) and @Static = " +
            "false() and ancestor::ClassOrInterfaceBody//ConstructorDeclaration" +
            "//StatementExpression[PrimaryExpression[PrimaryPrefix[@ThisModifier]]/PrimarySuffix[@Image=$loggerName" +
            "]][AssignmentOperator[@Image = '=']][Expression/PrimaryExpression/PrimaryPrefix/Name[@Image = " +
            "ancestor::ConstructorDeclaration//FormalParameter/VariableDeclaratorId/@Name]][not(" +
            ".//AllocationExpression)])]";

    public ProperLoggerRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, PROPER_LOGGER_VIOLATION_MSG);
    }

    private static final String PROPER_LOGGER_VIOLATION_MSG =
            "java.errorprone.ProperLoggerRule.violation.msg";
}
