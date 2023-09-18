package com.ctrip.flight.mobile.pmd.lang.java.rule;


import com.ctrip.flight.mobile.pmd.utils.I18nResources;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.symboltable.JavaNameOccurrence;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;

import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-18
 */
public class FlightJavaRule extends AbstractJavaRule {

    @Override
    public void setMessage(String message) {
        super.setMessage(I18nResources.getMessage(message));
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(I18nResources.getMessage(description));
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node, String message, Object... args) {
        FlightRuleUtils.addCustomViolation(this, data, node, message, args);
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node, String message) {
        addViolationWithPrecisePosition(data, node, message, null);
    }

    protected final void addViolationWithPrecisePosition(Object data, Node node) {
        addViolationWithPrecisePosition(data, node, null, null);
    }

    protected boolean assigned(List<NameOccurrence> usages) {
        for (NameOccurrence occ : usages) {
            JavaNameOccurrence jocc = (JavaNameOccurrence) occ;
            if (jocc.isOnLeftHandSide() || jocc.isSelfAssignment()) {
                return true;
            }
        }
        return false;
    }
}
