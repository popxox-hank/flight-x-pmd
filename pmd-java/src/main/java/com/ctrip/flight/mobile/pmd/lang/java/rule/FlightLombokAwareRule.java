package com.ctrip.flight.mobile.pmd.lang.java.rule;

import com.ctrip.flight.mobile.pmd.utils.I18nResources;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.rule.AbstractLombokAwareRule;

/**
 * @author haoren
 * Create at: 2023-07-24
 */
public class FlightLombokAwareRule extends AbstractLombokAwareRule {

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
}
