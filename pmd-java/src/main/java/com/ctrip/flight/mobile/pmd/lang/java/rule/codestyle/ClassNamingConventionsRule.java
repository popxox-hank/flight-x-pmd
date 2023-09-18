package com.ctrip.flight.mobile.pmd.lang.java.rule.codestyle;

import com.ctrip.flight.mobile.pmd.lang.java.rule.AbstractFlightNamingConventionRule;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.ast.internal.PrettyPrintingUtil;
import net.sourceforge.pmd.lang.java.rule.AbstractJUnitRule;
import net.sourceforge.pmd.properties.PropertyDescriptor;

import java.util.regex.Pattern;

/**
 * @author haoren
 * Create at: 2023-07-20
 */
public class ClassNamingConventionsRule extends AbstractFlightNamingConventionRule<ASTAnyTypeDeclaration> {

    private final PropertyDescriptor<Pattern> classRegex = defaultProp("class", "concrete class").build();
    private final PropertyDescriptor<Pattern> abstractClassRegex = defaultProp("abstract class").build();
    private final PropertyDescriptor<Pattern> interfaceRegex = defaultProp("interface").build();
    private final PropertyDescriptor<Pattern> enumerationRegex = defaultProp("enum").build();
    private final PropertyDescriptor<Pattern> annotationRegex = defaultProp("annotation").build();
    private final PropertyDescriptor<Pattern> utilityClassRegex = defaultProp("utility class").build();
    private final PropertyDescriptor<Pattern> testClassRegex = defaultProp("test class")
            .desc("Regex which applies to test class names. Since PMD 6.52.0.")
            .defaultValue("^Test.*$|^[A-Z][a-zA-Z0-9]*Test(s|Case)?$").build();


    public ClassNamingConventionsRule() {
        definePropertyDescriptor(classRegex);
        definePropertyDescriptor(abstractClassRegex);
        definePropertyDescriptor(interfaceRegex);
        definePropertyDescriptor(enumerationRegex);
        definePropertyDescriptor(annotationRegex);
        definePropertyDescriptor(utilityClassRegex);
        definePropertyDescriptor(testClassRegex);

        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
        addRuleChainVisit(ASTEnumDeclaration.class);
        addRuleChainVisit(ASTAnnotationTypeDeclaration.class);
    }


    // This could probably be moved to ClassOrInterfaceDeclaration
    // to share the implementation and be used from XPath
    private boolean isUtilityClass(ASTAnyTypeDeclaration node) {
        if (node.getTypeKind() != ASTAnyTypeDeclaration.TypeKind.CLASS) {
            return false;
        }

        ASTClassOrInterfaceDeclaration classNode = (ASTClassOrInterfaceDeclaration) node;

        // A class with a superclass or interfaces should not be considered
        if (classNode.getSuperClassTypeNode() != null
                || !classNode.getSuperInterfacesTypeNodes().isEmpty()) {
            return false;
        }

        // A class without declarations shouldn't be reported
        boolean hasAny = false;

        for (ASTAnyTypeBodyDeclaration decl : classNode.getDeclarations()) {
            switch (decl.getKind()) {
                case FIELD:
                case METHOD:
                    hasAny = isNonPrivate(decl) && !isMainMethod(decl);
                    if (!((AccessNode) decl.getDeclarationNode()).isStatic()) {
                        return false;
                    }
                    break;

                case INITIALIZER:
                    if (!((ASTInitializer) decl.getDeclarationNode()).isStatic()) {
                        return false;
                    }
                    break;

                default:
                    break;
            }
        }

        return hasAny;
    }

    private boolean isNonPrivate(ASTAnyTypeBodyDeclaration decl) {
        return !((AccessNode) decl.getDeclarationNode()).isPrivate();
    }


    private boolean isMainMethod(ASTAnyTypeBodyDeclaration bodyDeclaration) {
        if (ASTAnyTypeBodyDeclaration.DeclarationKind.METHOD != bodyDeclaration.getKind()) {
            return false;
        }

        ASTMethodDeclaration decl = (ASTMethodDeclaration) bodyDeclaration.getDeclarationNode();

        return decl.isStatic()
                && "main" .equals(decl.getName())
                && decl.getResultType().isVoid()
                && decl.getFormalParameters().size() == 1
                && String[].class.equals(decl.getFormalParameters().iterator().next().getType());
    }

    private boolean isTestClass(ASTClassOrInterfaceDeclaration node) {
        return !node.isNested() && AbstractJUnitRule.isTestClass(node.getFirstChildOfType(ASTClassOrInterfaceBody.class));
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {

        if (node.isAbstract()) {
            checkMatches(node, abstractClassRegex, data);
        } else if (isTestClass(node)) {
            checkMatches(node, testClassRegex, data);
        } else if (isUtilityClass(node)) {
            checkMatches(node, utilityClassRegex, data);
        } else if (node.isInterface()) {
            checkMatches(node, interfaceRegex, data);
        } else {
            checkMatches(node, classRegex, data);
        }

        return data;
    }


    @Override
    public Object visit(ASTEnumDeclaration node, Object data) {
        checkMatches(node, enumerationRegex, data);
        return data;
    }


    @Override
    public Object visit(ASTAnnotationTypeDeclaration node, Object data) {
        checkMatches(node, annotationRegex, data);
        return data;
    }


    @Override
    protected String defaultConvention() {
        return PASCAL_CASE;
    }


    @Override
    protected String kindDisplayName(ASTAnyTypeDeclaration node, PropertyDescriptor<Pattern> descriptor) {
        return isUtilityClass(node) ? "utility class" : PrettyPrintingUtil.kindName(node);
    }

    @Override
    protected void checkMatches(ASTAnyTypeDeclaration node, PropertyDescriptor<Pattern> regex, Object data) {
        String name = nameExtractor(node);
        if (!getProperty(regex).matcher(name).matches()) {
            addViolationWithPrecisePosition(data, node, CLASS_NAMING_CONVENTIONS_VIOLATION_MSG,
                    kindDisplayName(node, regex), name, getProperty(regex).toString());
        }
    }

    private static final String CLASS_NAMING_CONVENTIONS_VIOLATION_MSG =
            "java.code.style.ClassNamingConventionsRule.violation.msg";
}
