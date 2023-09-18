package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.AbstractFlightNamingConventionRule;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class LocalVariableNamingConventionsRule extends AbstractFlightNamingConventionRule<ASTVariableDeclaratorId> {


    private final PropertyDescriptor<Pattern> localVarRegex =
            defaultProp("localVar", "non-final local variable").build();
    private final PropertyDescriptor<Pattern> finalVarRegex =
            defaultProp("finalVar", "final local variable").build();
    private final PropertyDescriptor<Pattern> exceptionBlockParameterRegex =
            defaultProp("catchParameter", "exception block parameter").build();


    public LocalVariableNamingConventionsRule() {
        definePropertyDescriptor(localVarRegex);
        definePropertyDescriptor(finalVarRegex);
        definePropertyDescriptor(exceptionBlockParameterRegex);

        addRuleChainVisit(ASTVariableDeclaratorId.class);
    }


    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {

        if (node.isExceptionBlockParameter()) {
            checkMatches(node, exceptionBlockParameterRegex, data);
        } else if (node.isLocalVariable()) {
            checkMatches(node, node.isFinal() ? finalVarRegex : localVarRegex, data);
        }

        return data;
    }


    @Override
    protected String defaultConvention() {
        return CAMEL_CASE;
    }


    @Override
    protected String kindDisplayName(ASTVariableDeclaratorId node, PropertyDescriptor<Pattern> descriptor) {
        if (node.isExceptionBlockParameter()) {
            return "exception block parameter";
        } else if (node.isLocalVariable()) {
            return node.isFinal() ? "final local variable" : "local variable";
        }

        throw new UnsupportedOperationException("This rule doesn't handle this case");
    }

    @Override
    protected void checkMatches(ASTVariableDeclaratorId node, PropertyDescriptor<Pattern> regex, Object data) {
        String name = nameExtractor(node);
        if (!getProperty(regex).matcher(name).matches()) {
            addViolationWithPrecisePosition(data, node, LOCAL_VARIABLE_NAMING_CONVENTIONS_VIOLATION_MSG,
                    kindDisplayName(node, regex), name, getProperty(regex).toString());
        }
    }

    private static final String LOCAL_VARIABLE_NAMING_CONVENTIONS_VIOLATION_MSG =
            "java.code.style.LocalVariableNamingConventionsRule.violation.msg";
}
