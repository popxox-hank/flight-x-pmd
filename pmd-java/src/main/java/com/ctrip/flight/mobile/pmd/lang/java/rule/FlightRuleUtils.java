package com.ctrip.flight.mobile.pmd.lang.java.rule;

import com.ctrip.flight.mobile.pmd.utils.I18nResources;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.rule.AbstractRule;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-21
 */
public final class FlightRuleUtils {

    public static void addCustomViolation(AbstractRule rule,
                                          Object data,
                                          Node node,
                                          String message,
                                          Object... args) {
        if (message == null) {
            rule.addViolation(data, node);
            return;
        }

        if (args == null || args.length == 0) {
            rule.addViolationWithMessage(data, node, replaceSpecialLiterals(I18nResources.getMessage(message)));
        } else {
            rule.addViolationWithMessage(data, node, replaceSpecialLiterals(I18nResources.getMessage(message, args)));
        }
    }

    private static String replaceSpecialLiterals(String message) {
        return StringUtils.replace(message, "\"", "")
                .replace("{", "'{")
                .replace("}", "'}");
    }

    public static boolean isStringBuilderOrBuffer(TypeNode node) {
        return TypeTestUtil.isA(StringBuffer.class, node)
                || TypeTestUtil.isA(StringBuilder.class, node);
    }

    public static boolean isInStringBufferOperationChain(Node node, String methodName) {
        ASTPrimaryExpression expr = node.getFirstParentOfType(ASTPrimaryExpression.class);
        MethodCallChain methodCalls = MethodCallChain.wrap(expr);
        while (expr != null && methodCalls == null) {
            expr = expr.getFirstParentOfType(ASTPrimaryExpression.class);
            methodCalls = MethodCallChain.wrap(expr);
        }

        if (methodCalls != null && !methodCalls.isExactlyOfAnyType(StringBuffer.class, StringBuilder.class)) {
            methodCalls = null;
        }

        return methodCalls != null && methodCalls.getMethodNames().contains(methodName);
    }

    public static class MethodCallChain {
        private final ASTPrimaryExpression primary;

        private MethodCallChain(ASTPrimaryExpression primary) {
            this.primary = primary;
        }

        public boolean isExactlyOfAnyType(Class<?> clazz, Class<?>... clazzes) {
            ASTPrimaryPrefix typeNode = getTypeNode();

            if (TypeTestUtil.isExactlyA(clazz, typeNode)) {
                return true;
            }
            if (clazzes != null) {
                for (Class<?> c : clazzes) {
                    if (TypeTestUtil.isExactlyA(c, typeNode)) {
                        return true;
                    }
                }
            }
            return false;
        }

        ASTPrimaryPrefix getTypeNode() {
            return this.primary.getFirstChildOfType(ASTPrimaryPrefix.class);
        }

        public List<String> getMethodNames() {
            List<String> methodNames = new ArrayList<>();

            ASTPrimaryPrefix prefix = getTypeNode();
            ASTName name = prefix.getFirstChildOfType(ASTName.class);
            if (name != null) {
                String firstMethod = name.getImage();
                int dot = firstMethod.lastIndexOf('.');
                if (dot != -1) {
                    firstMethod = firstMethod.substring(dot + 1);
                }
                methodNames.add(firstMethod);
            }

            for (ASTPrimarySuffix suffix : primary.findChildrenOfType(ASTPrimarySuffix.class)) {
                if (suffix.getImage() != null) {
                    methodNames.add(suffix.getImage());
                }
            }
            return methodNames;
        }

        public static MethodCallChain wrap(ASTPrimaryExpression primary) {
            if (primary != null && isMethodCall(primary)) {
                return new MethodCallChain(primary);
            }
            return null;
        }

        private static boolean isMethodCall(ASTPrimaryExpression primary) {
            return primary.getNumChildren() >= 2
                    && primary.getChild(0) instanceof ASTPrimaryPrefix
                    && primary.getChild(1) instanceof ASTPrimarySuffix;
        }
    }
}
