package com.ctrip.flight.mobile.pmd.lang.java.rule;

import com.ctrip.flight.mobile.pmd.utils.I18nResources;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.rule.XPathRule;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-18
 */
public class FlightXPathRule extends XPathRule {

    public FlightXPathRule(XPathVersion version, String expression) {
        super(version, expression);
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(I18nResources.getMessage(message));
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(I18nResources.getMessage(description));
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node) {
        addViolationWithPrecisePosition(data, node, null, null);
    }


    protected final void addViolationWithPrecisePosition(Object data, Node node, String message) {
        addViolationWithPrecisePosition(data, node, message, null);
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node, String message, Object... args) {
        if (node instanceof ASTFieldDeclaration) {
            ASTVariableDeclaratorId variableDeclaratorId = node.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
            FlightRuleUtils.addCustomViolation(this, data, variableDeclaratorId, message, args);
            return;
        }
        if (node instanceof ASTMethodDeclaration) {
            ASTMethodDeclarator declarator = node.getFirstChildOfType(ASTMethodDeclarator.class);
            FlightRuleUtils.addCustomViolation(this, data, declarator, message, args);
            return;
        }
        FlightRuleUtils.addCustomViolation(this, data, node, message, args);
    }
}
