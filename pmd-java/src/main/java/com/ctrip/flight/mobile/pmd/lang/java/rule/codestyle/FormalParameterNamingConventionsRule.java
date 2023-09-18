package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.AbstractFlightNamingConventionRule;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public class FormalParameterNamingConventionsRule extends AbstractFlightNamingConventionRule<ASTVariableDeclaratorId> {


    private final PropertyDescriptor<Pattern> formalParamRegex =
            defaultProp("methodParameter", "formal parameter")
                    .build();
    private final PropertyDescriptor<Pattern> finalFormalParamRegex =
            defaultProp("finalMethodParameter", "final formal parameter")
                    .build();
    private final PropertyDescriptor<Pattern> lambdaParamRegex =
            defaultProp("lambdaParameter", "inferred-type lambda parameter")
                    .build();
    private final PropertyDescriptor<Pattern> explicitLambdaParamRegex =
            defaultProp("explicitLambdaParameter", "explicitly-typed lambda parameter")
                    .build();


    public FormalParameterNamingConventionsRule() {
        definePropertyDescriptor(formalParamRegex);
        definePropertyDescriptor(finalFormalParamRegex);
        definePropertyDescriptor(lambdaParamRegex);
        definePropertyDescriptor(explicitLambdaParamRegex);

        addRuleChainVisit(ASTVariableDeclaratorId.class);
    }


    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {

        if (node.isLambdaParameter()) {
            checkMatches(node, node.isTypeInferred() ? lambdaParamRegex : explicitLambdaParamRegex, data);
        } else if (node.isFormalParameter()) {
            checkMatches(node, node.isFinal() ? finalFormalParamRegex : formalParamRegex, data);
        }

        return data;
    }


    @Override
    protected String defaultConvention() {
        return CAMEL_CASE;
    }


    @Override
    protected String kindDisplayName(ASTVariableDeclaratorId node, PropertyDescriptor<Pattern> descriptor) {
        if (node.isLambdaParameter()) {
            return node.isTypeInferred() ? "lambda parameter" : "explicitly-typed lambda parameter";
        } else if (node.isFormalParameter()) { // necessarily a method parameter here
            return node.isFinal() ? "final method parameter" : "method parameter";
        }

        throw new UnsupportedOperationException("This rule doesn't handle this case");
    }

    @Override
    protected void checkMatches(ASTVariableDeclaratorId node, PropertyDescriptor<Pattern> regex, Object data) {
        String name = nameExtractor(node);
        if (!getProperty(regex).matcher(name).matches()) {
            addViolationWithPrecisePosition(data, node, FORMAL_PARAMETER_NAMING_CONVENTIONS_VIOLATION_MSG,
                    kindDisplayName(node, regex), name, getProperty(regex).toString());
        }
    }

    private static final String FORMAL_PARAMETER_NAMING_CONVENTIONS_VIOLATION_MSG =
            "java.code.style.FormalParameterNamingConventionsRule.violation.msg";
}
