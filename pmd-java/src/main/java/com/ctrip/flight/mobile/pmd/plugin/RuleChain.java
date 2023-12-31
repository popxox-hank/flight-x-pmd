/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package com.ctrip.flight.mobile.pmd.plugin;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.annotation.InternalApi;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.ast.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RuleChain is a means by which Rules can participate in a uniform
 * visitation of the AST, and not need perform their own independent visitation.
 * The RuleChain exists as a means to improve the speed of PMD when there are
 * many Rules.
 *
 * @deprecated Internal API, will be removed with PMD 7.0.0.
 */
@Deprecated
@InternalApi
public class RuleChain {
    // Mapping from Language to RuleChainVisitor
    private final Map<Language, RuleChainVisitor> languageToRuleChainVisitor = new HashMap<>();

    /**
     * Add all Rules from the given RuleSet which want to participate in the
     * RuleChain.
     *
     * @param ruleSet The RuleSet to add Rules from.
     */
    public void add(RuleSet ruleSet) {
        for (Rule r : ruleSet.getRules()) {
            add(ruleSet, r);
        }
    }

    /**
     * Add the given Rule if it wants to participate in the RuleChain.
     *
     * @param ruleSet The RuleSet to which the rule belongs.
     * @param rule    The Rule to add.
     */
    private void add(RuleSet ruleSet, Rule rule) {
        RuleChainVisitor visitor = getRuleChainVisitor(rule.getLanguage());
        if (visitor != null) {
            visitor.add(ruleSet, rule);
        }
    }

    /**
     * Apply the RuleChain to the given Nodes using the given RuleContext, for
     * those rules using the given Language.
     *
     * @param nodes    The Nodes.
     * @param ctx      The RuleContext.
     * @param language The Language.
     */
    public void apply(List<Node> nodes, RuleContext ctx, Language language) {
        RuleChainVisitor visitor = getRuleChainVisitor(language);
        if (visitor != null) {
            visitor.visitAll(nodes, ctx);
        }
    }

    // Get the RuleChainVisitor for the appropriate Language.
    private RuleChainVisitor getRuleChainVisitor(Language language) {
        RuleChainVisitor visitor = languageToRuleChainVisitor.get(language);
        if (visitor == null) {
            if (language.getRuleChainVisitorClass() != null) {
                try {
                    visitor = (RuleChainVisitor) language.getRuleChainVisitorClass().getConstructor().newInstance();
                } catch (ReflectiveOperationException | SecurityException | IllegalArgumentException e) {
                    throw new IllegalStateException(
                            "Failure to created RuleChainVisitor: " + language.getRuleChainVisitorClass(), e);
                }
                languageToRuleChainVisitor.put(language, visitor);
            } else {
                throw new IllegalArgumentException("Language does not have a RuleChainVisitor: " + language);
            }
        }
        return visitor;
    }
}
