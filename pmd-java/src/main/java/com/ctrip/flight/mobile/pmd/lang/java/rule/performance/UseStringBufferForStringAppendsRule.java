package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.Objects;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class UseStringBufferForStringAppendsRule extends FlightJavaRule {

    public UseStringBufferForStringAppendsRule() {
        addRuleChainVisit(ASTVariableDeclaratorId.class);
    }

    /**
     * This method is used to check whether user appends string directly instead of using StringBuffer or StringBuilder
     *
     * @param node This is the expression of part of java code to be checked.
     * @param data This is the data to return.
     * @return Object This returns the data passed in. If violation happens, violation is added to data.
     */
    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        if (!TypeTestUtil.isA(String.class, node) || node.isArray()
                || node.getNthParent(3) instanceof ASTForStatement) {
            return data;
        }

        // Remember how often we the variable has been used
        int usageCounter = 0;

        for (NameOccurrence no : node.getUsages()) {
            Node name = no.getLocation();
            ASTStatementExpression statement = name.getFirstParentOfType(ASTStatementExpression.class);
            if (statement == null) {
                continue;
            }
            ASTArgumentList argList = name.getFirstParentOfType(ASTArgumentList.class);
            if (argList != null && argList.getFirstParentOfType(ASTStatementExpression.class) == statement) {
                // used in method call
                continue;
            }
            ASTEqualityExpression equality = name.getFirstParentOfType(ASTEqualityExpression.class);
            if (equality != null && equality.getFirstParentOfType(ASTStatementExpression.class) == statement) {
                // used in condition
                continue;
            }
            if ((node.getNthParent(2) instanceof ASTFieldDeclaration
                    || node.getParent() instanceof ASTFormalParameter)
                    && isNotWithinLoop(name)) {
                // ignore if the field or formal parameter is *not* used within loops
                continue;
            }
            ASTConditionalExpression conditional = name.getFirstParentOfType(ASTConditionalExpression.class);

            if (conditional != null) {
                Node thirdParent = name.getNthParent(3);
                Node fourthParent = name.getNthParent(4);
                if ((Objects.equals(thirdParent, conditional) || Objects.equals(fourthParent, conditional))
                        && conditional.getFirstParentOfType(ASTStatementExpression.class) == statement) {
                    // is used in ternary as only option (not appended to other
                    // string)
                    continue;
                }
            }
            if (statement.getNumChildren() > 0 && statement.getChild(0) instanceof ASTPrimaryExpression) {
                ASTName astName = statement.getChild(0).getFirstDescendantOfType(ASTName.class);
                if (astName != null) {
                    ASTAssignmentOperator assignmentOperator = statement
                            .getFirstDescendantOfType(ASTAssignmentOperator.class);
                    if (astName.equals(name)) {
                        if (assignmentOperator != null && assignmentOperator.isCompound()) {
                            if (isWithinLoop(name)) {
                                // always report within a loop
                                addViolationWithPrecisePosition(data, assignmentOperator,
                                        USE_STRING_BUFFER_FOR_STRING_APPEND_VIOLATION_MSG);
                            } else {
                                usageCounter++;
                                if (usageCounter > 1) {
                                    // only report, if it is not the first time
                                    addViolationWithPrecisePosition(data, assignmentOperator,
                                            USE_STRING_BUFFER_FOR_STRING_APPEND_VIOLATION_MSG);
                                }
                            }
                        }
                    } else if (astName.hasImageEqualTo(name.getImage())) {
                        if (assignmentOperator != null && !assignmentOperator.isCompound()) {
                            if (isWithinLoop(name)) {
                                // always report within a loop
                                addViolationWithPrecisePosition(data, assignmentOperator,
                                        USE_STRING_BUFFER_FOR_STRING_APPEND_VIOLATION_MSG);
                            } else {
                                usageCounter++;
                                if (usageCounter > 1) {
                                    // only report, if it is not the first time
                                    addViolationWithPrecisePosition(data, assignmentOperator,
                                            USE_STRING_BUFFER_FOR_STRING_APPEND_VIOLATION_MSG);
                                }
                            }
                        } else if (assignmentOperator != null && assignmentOperator.isCompound()
                                && usageCounter >= 1) {
                            addViolationWithPrecisePosition(data, assignmentOperator,
                                    USE_STRING_BUFFER_FOR_STRING_APPEND_VIOLATION_MSG);
                        }
                    }
                }
            }
        }
        return data;
    }

    private boolean isNotWithinLoop(Node name) {
        return name.getFirstParentOfType(ASTForStatement.class) == null
                && name.getFirstParentOfType(ASTWhileStatement.class) == null
                && name.getFirstParentOfType(ASTDoStatement.class) == null;
    }

    private boolean isWithinLoop(Node name) {
        return !isNotWithinLoop(name);
    }

    private static final String USE_STRING_BUFFER_FOR_STRING_APPEND_VIOLATION_MSG =
            "java.performance.UseStringBufferForStringAppendsRule.violation.msg";
}
