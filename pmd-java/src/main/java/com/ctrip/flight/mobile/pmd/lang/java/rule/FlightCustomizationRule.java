package com.ctrip.flight.mobile.pmd.lang.java.rule;

import net.sourceforge.pmd.lang.java.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-10-09
 */
public class FlightCustomizationRule extends FlightJavaRule {

    private static final Pattern TEST_CLASS_PATTERN = Pattern.compile("^Test.*$|^[A-Z][a-zA-Z0-9]*Test(s|Case)?$");
    private static final String TEST_ANNOTATION = "Test";
    protected boolean isTestMethod;
    protected boolean isTestClass;

    public FlightCustomizationRule(){
        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
        addRuleChainVisit(ASTAnnotation.class);
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        isTestClass = false;
        if (TEST_CLASS_PATTERN.matcher(node.getImage()).matches()) {
            isTestClass = true;
        }
        return data;
    }

    @Override
    public Object visit(ASTAnnotation node, Object data) {
        isTestMethod = false;
        if (StringUtils.equalsIgnoreCase(node.getAnnotationName(), TEST_ANNOTATION)) {
            isTestMethod = true;
        }
        return data;
    }
}
