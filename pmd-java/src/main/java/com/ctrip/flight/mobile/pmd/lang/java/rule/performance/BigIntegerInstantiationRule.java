package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class BigIntegerInstantiationRule extends FlightJavaRule {

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        Node type = node.getChild(0);

        if (!(type instanceof ASTClassOrInterfaceType)) {
            return super.visit(node, data);
        }

        boolean jdk15 = ((RuleContext) data).getLanguageVersion().compareToVersion("1.5") >= 0;
        if ((TypeTestUtil.isA(BigInteger.class, (ASTClassOrInterfaceType) type)
                || jdk15 && TypeTestUtil.isA(BigDecimal.class, (ASTClassOrInterfaceType) type))
                && !node.hasDescendantOfType(ASTArrayDimsAndInits.class)) {
            ASTArguments args = node.getFirstChildOfType(ASTArguments.class);
            if (args.size() == 1) {
                ASTLiteral literal = node.getFirstDescendantOfType(ASTLiteral.class);
                if (literal == null
                        || literal.getParent().getParent().getParent().getParent().getParent() != args) {
                    return super.visit(node, data);
                }

                String img = literal.getImage();
                if (literal.isStringLiteral()) {
                    img = img.substring(1, img.length() - 1);
                }

                if ("0".equals(img) || "1".equals(img) || jdk15 && "10".equals(img)) {
                    addViolationWithPrecisePosition(data, node, BIG_INTEGER_INSTANTIATION_VIOLATION_MSG);
                    return data;
                }
            }
        }
        return super.visit(node, data);
    }

    private static final String BIG_INTEGER_INSTANTIATION_VIOLATION_MSG =
            "java.performance.BigIntegerInstantiationRule.violation.msg";
}
