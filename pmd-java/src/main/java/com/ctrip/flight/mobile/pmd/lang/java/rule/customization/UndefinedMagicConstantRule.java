package com.ctrip.flight.mobile.pmd.lang.java.rule.customization;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightStreamExpressionRule;
import com.google.common.collect.Lists;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;
import org.jaxen.JaxenException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haoren
 * Create at: 2023-08-04
 */
public class UndefinedMagicConstantRule extends FlightStreamExpressionRule {

    private static final PropertyDescriptor<List<String>> LITERAL_WHITE_LIST
            = PropertyFactory.stringListProperty("literalWhiteList")
            .desc("this is magic constant white literal")
            .defaultValue(Lists.newArrayList("1", "-1", "0"))
            .build();
    private final static String XPATH = "//Literal/../../../../..[not(VariableInitializer)]";

    public UndefinedMagicConstantRule() {
        super();
        definePropertyDescriptor(LITERAL_WHITE_LIST);
        addRuleChainVisit(ASTCompilationUnit.class);
    }

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        if (isTestClass || isTestMethod) {
            return data;
        }

        // removed repeat magic value , to prevent the parent class to find sub-variable nodes when there is a repeat
        List<ASTLiteral> currentLiterals = new ArrayList<ASTLiteral>();
        try {
            // Find the parent node of the undefined variable
            List<Node> parentNodes = node.findChildNodesWithXPath(XPATH);

            for (Node parentItem : parentNodes) {
                List<ASTLiteral> literals = parentItem.findDescendantsOfType(ASTLiteral.class);
                for (ASTLiteral literal : literals) {
                    if (inBlackList(literal) && !currentLiterals.contains(literal)) {
                        currentLiterals.add(literal);
                        addViolationWithPrecisePosition(data, literal, UNDEFINED_MAGIC_CONSTANT_VIOLATION_MSG,
                                literal.getImage());
                    }
                }
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }

    /**
     * Undefined variables are in the blacklist
     *
     * @param literal
     * @return
     */
    private boolean inBlackList(ASTLiteral literal) {
        String name = literal.getImage();
        int lineNum = literal.getBeginLine();
        // name is null,bool literal，belongs to white list
        if (name == null) {
            return false;
        }
        List<String> whiteList = getProperty(LITERAL_WHITE_LIST);
        // filter white list
        for (String whiteItem : whiteList) {
            if (whiteItem.equals(name) || name.equals("\"\"")) {
                return false;
            }
        }
        ASTIfStatement ifStatement = literal.getFirstParentOfType(ASTIfStatement.class);
        if (ifStatement != null && lineNum == ifStatement.getBeginLine()) {
            ASTForStatement forStatement = ifStatement.getFirstParentOfType(ASTForStatement.class);
            ASTWhileStatement whileStatement = ifStatement.getFirstParentOfType(ASTWhileStatement.class);
            return forStatement == null && whileStatement == null;
        }

        // judge magic value belongs to  for statement
        ASTForStatement blackForStatement = literal.getFirstParentOfType(ASTForStatement.class);
        if (blackForStatement != null && lineNum == blackForStatement.getBeginLine()) {
            return true;
        }

        // judge magic value belongs to while statement
        ASTWhileStatement blackWhileStatement = literal.getFirstParentOfType(ASTWhileStatement.class);
        return blackWhileStatement != null && lineNum == blackWhileStatement.getBeginLine();
    }

    private static final String UNDEFINED_MAGIC_CONSTANT_VIOLATION_MSG =
            "java.customization.UndefinedMagicConstantRule.violation.msg";
}
