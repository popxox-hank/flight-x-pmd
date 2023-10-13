package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCustomizationRule;
import net.sourceforge.pmd.lang.java.ast.*;

import java.util.*;

/**
 * @author haoren
 * Create at: 2023/9/7
 */
public class GuardClausesRule extends FlightCustomizationRule {

    private boolean isDirectReturnOrThrowStatement;

    public GuardClausesRule() {
        super();
        addRuleChainVisit(ASTMethodDeclaration.class);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        List<ASTBlockStatement> blockStatementList = Optional.ofNullable(node.getBody())
                .map(astBlock -> astBlock.findChildrenOfType(ASTBlockStatement.class))
                .orElse(Collections.emptyList());
        if (blockStatementList.isEmpty()) {
            return data;
        }

        if (isViolation(blockStatementList)) {
            addViolationWithPrecisePosition(data, node, GUARD_CLAUSES_VIOLATION_MSG);
        }

        return data;
    }


    private boolean isViolation(List<ASTBlockStatement> blockStatementList) {
        for (int i = 0; i < blockStatementList.size(); i++) {
            List<ASTIfStatement> ifStatementList =
                    blockStatementList.get(i).getChild(0).findChildrenOfType(ASTIfStatement.class);
            if (ifStatementList.isEmpty()) {
                continue;
            }
            ASTBlockStatement nextBlockStatement = null;
            if (i < blockStatementList.size() - 1) {
                nextBlockStatement = blockStatementList.get(i + 1);
            }
            isDirectReturnOrThrowStatement = false;
            loopCheckIfStatement(ifStatementList, nextBlockStatement);
            if (isDirectReturnOrThrowStatement) {
                return true;
            }
        }
        return false;
    }

    private void loopCheckIfStatement(List<ASTIfStatement> ifStatementList,
                                      ASTBlockStatement nextBlockStatement) {
        if (isDirectReturnOrThrowStatement) {
            return;
        }
        for (int i = 0; i < ifStatementList.size(); i++) {
            ASTStatement thenStatement = ifStatementList.get(i).getThenBranch();
            ASTStatement elseStatement = ifStatementList.get(i).getElseBranch();
            if (isDirectReturnOrThrowStatement(thenStatement, elseStatement, nextBlockStatement)) {
                isDirectReturnOrThrowStatement = true;
                break;
            }
            List<ASTBlockStatement> blockStatementList =
                    thenStatement.getChild(0).findChildrenOfType(ASTBlockStatement.class);
            for (int j = 0; j < blockStatementList.size(); j++) {
                List<ASTIfStatement> loopIfStatementList =
                        blockStatementList.get(j).getChild(0).findChildrenOfType(ASTIfStatement.class);
                if (loopIfStatementList.isEmpty()) {
                    continue;
                }
                ASTBlockStatement loopNextBlockStatement = null;
                if (j < blockStatementList.size() - 1) {
                    loopNextBlockStatement = blockStatementList.get(j + 1);
                }
                loopCheckIfStatement(loopIfStatementList, loopNextBlockStatement);
            }
        }
    }


    /**
     * 确认if语句之后的是否是直接return、throw语句或者else语句是直接return、throw
     *
     * @param blockStatement
     * @return
     */
    private boolean isDirectReturnOrThrowStatement(ASTStatement thenStatement,
                                                   ASTStatement elseStatement,
                                                   ASTBlockStatement blockStatement) {
        if (Objects.nonNull(thenStatement)
                && thenStatement.getChild(0).findChildrenOfType(ASTBlockStatement.class).size() > 1
                && Objects.nonNull(blockStatement)
                && hasReturnOrThrowStatement(blockStatement)
                && isReturnComplexLessThanThenStatement(thenStatement, blockStatement)) {
            return true;
        }

        if (Objects.nonNull(elseStatement)
                && elseStatement.getChild(0).findChildrenOfType(ASTBlockStatement.class).size() == 1
                && hasReturnOrThrowStatement(
                elseStatement.getChild(0).getFirstChildOfType(ASTBlockStatement.class))
                && thenStatement.getChild(0).findChildrenOfType(ASTBlockStatement.class).size() > 1
                && isReturnComplexLessThanThenStatement(thenStatement, elseStatement)) {
            return true;
        }
        return false;
    }

    private boolean isReturnComplexLessThanThenStatement(ASTStatement thenStatement,
                                                         ASTStatement elseStatement) {
        int thenStatementComplexCount = getThenStatementComplexCount(thenStatement);
        int elseStatementComplexCount = getElseStatementComplexCount(elseStatement);
        if (thenStatementComplexCount == 0
                || elseStatementComplexCount == 0) {
            return false;
        }
        return elseStatementComplexCount < thenStatementComplexCount;
    }

    private boolean isReturnComplexLessThanThenStatement(ASTStatement thenStatement,
                                                         ASTBlockStatement blockStatement) {
        int thenStatementComplexCount = getThenStatementComplexCount(thenStatement);
        int nextBlockStatementComplexCount = getNextBlockStatementComplexCount(blockStatement);
        if (thenStatementComplexCount == 0
                || nextBlockStatementComplexCount == 0) {
            return false;
        }
        return nextBlockStatementComplexCount < thenStatementComplexCount;
    }

    private int getThenStatementComplexCount(ASTStatement thenStatement) {
        ASTBlock astBlock = thenStatement.getFirstChildOfType(ASTBlock.class);
        if (Objects.isNull(astBlock)) {
            return 0;
        }
        return astBlock.getNumChildren();
    }

    private int getElseStatementComplexCount(ASTStatement elseStatement) {
        List<ASTReturnStatement> astReturnStatementList =
                elseStatement.findDescendantsOfType(ASTReturnStatement.class);
        List<ASTThrowStatement> astThrowStatementList =
                elseStatement.findDescendantsOfType(ASTThrowStatement.class);
        if (astReturnStatementList.isEmpty()
                && astThrowStatementList.isEmpty()) {
            return 0;
        }
        ASTPrimaryExpression astPrimaryExpression;
        if (!astReturnStatementList.isEmpty()) {
            astPrimaryExpression =
                    astReturnStatementList.get(0).getFirstDescendantOfType(ASTPrimaryExpression.class);
        } else {
            astPrimaryExpression =
                    astThrowStatementList.get(0).getFirstDescendantOfType(ASTPrimaryExpression.class);
        }
        if (Objects.isNull(astPrimaryExpression)) {
            return 0;
        }
        return getRealPrimaryExpressionChildNum(astPrimaryExpression);
    }

    private int getNextBlockStatementComplexCount(ASTBlockStatement blockStatement) {
        List<ASTReturnStatement> astReturnStatementList =
                blockStatement.findDescendantsOfType(ASTReturnStatement.class);
        List<ASTThrowStatement> astThrowStatementList =
                blockStatement.findDescendantsOfType(ASTThrowStatement.class);
        if (astReturnStatementList.isEmpty()
                && astThrowStatementList.isEmpty()) {
            return 0;
        }
        ASTPrimaryExpression astPrimaryExpression;
        if (!astReturnStatementList.isEmpty()) {
            astPrimaryExpression =
                    astReturnStatementList.get(0).getFirstDescendantOfType(ASTPrimaryExpression.class);
        } else {
            astPrimaryExpression =
                    astThrowStatementList.get(0).getFirstDescendantOfType(ASTPrimaryExpression.class);
        }
        if (Objects.isNull(astPrimaryExpression)) {
            return 0;
        }
        return getRealPrimaryExpressionChildNum(astPrimaryExpression);
    }

    private boolean hasReturnOrThrowStatement(ASTBlockStatement blockStatement) {
        if (Objects.isNull(blockStatement)
                || blockStatement.getNumChildren() == 0
                || !(blockStatement.getChild(0) instanceof ASTStatement)) {
            return false;
        }
        ASTReturnStatement astReturnStatement =
                blockStatement.getChild(0).getFirstChildOfType(ASTReturnStatement.class);
        ASTThrowStatement throwStatement =
                blockStatement.getChild(0).getFirstChildOfType(ASTThrowStatement.class);
        return Objects.nonNull(astReturnStatement) || Objects.nonNull(throwStatement);
    }

    private int getRealPrimaryExpressionChildNum(ASTPrimaryExpression astPrimaryExpression) {
        List<JavaNode> javaNodeList = new ArrayList<>();
        ASTPrimaryPrefix prefix = astPrimaryExpression.getFirstDescendantOfType(ASTPrimaryPrefix.class);
        if (!prefix.usesThisModifier() && !prefix.usesSuperModifier()) {
            javaNodeList.add(prefix);
        }

        List<ASTPrimarySuffix> suffixes = astPrimaryExpression.findChildrenOfType(ASTPrimarySuffix.class);
        for (ASTPrimarySuffix suffix : suffixes) {
            if (!suffix.isArguments() && !suffix.isArrayDereference()) {
                javaNodeList.add(suffix);
            }
        }
        return javaNodeList.size();
    }


    private static final String GUARD_CLAUSES_VIOLATION_MSG = "java.customization.GuardClausesRule.violation.msg";
}
