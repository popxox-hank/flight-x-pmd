package com.ctrip.flight.mobile.pmd.lang.java.rule.design;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.symboltable.*;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;
import net.sourceforge.pmd.lang.symboltable.Scope;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.util.*;

import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-09-05
 */
@Deprecated
public class LawOfDemeterRule extends FlightJavaRule {


    private static final PropertyDescriptor<Integer> TRUST_RADIUS =
            PropertyFactory.intProperty("trustRadius")
                    .desc("Maximum degree of trusted data. The default of 1 is the most restrictive.")
                    .require(positive())
                    .defaultValue(1)
                    .build();

    private static final String REASON_METHOD_CHAIN_CALLS = "method chain calls";
    private static final String REASON_OBJECT_NOT_CREATED_LOCALLY = "object not created locally";
    private static final String REASON_STATIC_ACCESS = "static property access";

    public LawOfDemeterRule() {
        definePropertyDescriptor(TRUST_RADIUS);
        addRuleChainVisit(ASTMethodDeclaration.class);
    }

    /**
     * That's a new method. We are going to check each method call inside the
     * method.
     *
     * @return <code>null</code>.
     */
    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        List<ASTPrimaryExpression> primaryExpressions = node.findDescendantsOfType(ASTPrimaryExpression.class);
        final int trustRadius = getProperty(TRUST_RADIUS);
        for (ASTPrimaryExpression expression : primaryExpressions) {
            List<MethodCall> calls =
                    MethodCall.createMethodCalls(expression, trustRadius);
            addViolations(calls, (RuleContext) data);
        }
        return null;
    }

    private void addViolations(List<MethodCall> calls, RuleContext ctx) {
        for (MethodCall method : calls) {
            if (method.isViolation()) {
                addViolationWithMessage(ctx, method.getExpression(),
                        getMessage() + " (" + method.getViolationReason() + ")");
            }
        }
    }

    /**
     * Collects the information of one identified method call. The method call
     * might be a violation of the Law of Demeter or not.
     */
    private static class MethodCall {
        private static final String METHOD_CALL_CHAIN = "result from previous method call";
        private static final String SIMPLE_ASSIGNMENT_OPERATOR = "=";
        private static final String SCOPE_METHOD_CHAINING = "method-chaining";
        private static final String SCOPE_CLASS = "class";
        private static final String SCOPE_METHOD = "method";
        private static final String SCOPE_LOCAL = "local";
        private static final String SCOPE_STATIC_CHAIN = "static-chain";
        private static final String SUPER = "super";
        private static final String THIS = "this";
        private static final String PREFIX_EXCLUSION_PATTERN = "^.*(b|B)uilder$";

        private ASTPrimaryExpression expression;
        private String baseName;
        private String methodName;
        private String baseScope;
        private String baseTypeName;
        private Class<?> baseType;
        private boolean violation;
        private boolean baseNameInWhitelist;
        private String violationReason;

        /**
         * Create a new method call for the prefix expression part of the
         * primary expression.
         */
        private MethodCall(ASTPrimaryExpression expression, ASTPrimaryPrefix prefix) {
            this.expression = expression;
            analyze(prefix);
            determineType();
            checkViolation();
        }

        /**
         * Create a new method call for the given suffix expression part of the
         * primary expression. This is used for method chains.
         */
        private MethodCall(ASTPrimaryExpression expression, ASTPrimarySuffix suffix) {
            this.expression = expression;
            analyze(suffix);
            determineType();
            checkViolation();
        }

        /**
         * Create a new method call for the given suffix expression part of the
         * primary expression. This is used for method chains.
         */
        private MethodCall(ASTPrimaryExpression expression, ASTPrimarySuffix suffix, int index, int trustRadius) {
            this.expression = expression;
            analyze(suffix, index, trustRadius);
            determineType();
            checkViolation();
        }

        /**
         * Factory method to convert a given primary expression into
         * MethodCalls. In case the primary expression represents a method chain
         * call, then multiple MethodCalls are returned.
         *
         * @return a list of MethodCalls, might be empty.
         */
        public static List<MethodCall> createMethodCalls(ASTPrimaryExpression expression, int trustRadius) {
            List<MethodCall> result = new ArrayList<>();

            if (isNotAConstructorCall(expression) && isNotLiteral(expression) && hasSuffixesWithArguments(expression)) {
                ASTPrimaryPrefix prefixNode = expression.getFirstChildOfType(ASTPrimaryPrefix.class);
                ASTPrimarySuffix followingSuffix = getFollowingSuffix(prefixNode);
                boolean firstExpressionIsMethodCall = followingSuffix != null && followingSuffix.isArguments();
                MethodCall firstMethodCallInChain = null;

                if (firstExpressionIsMethodCall) {
                    firstMethodCallInChain = new MethodCall(expression, prefixNode);
                    result.add(firstMethodCallInChain);
                }

                if (firstMethodCallInChain == null || firstMethodCallInChain.isNotBuilder()) {
                    List<ASTPrimarySuffix> suffixes = findSuffixesWithoutArguments(expression);
                    for (int i = 0; i < suffixes.size(); i++) {
                        if (!expression.hasDescendantOfType(ASTCastExpression.class)) {
                            result.add(new MethodCall(expression, suffixes.get(i), i + 1, trustRadius));
                        }
                    }
                }
            }

//            final Integer trustRadius = getProperty(TRUST_RADIUS);
            // when method chaining is detected, it must consist of multiple method calls
            if (result.size() == 1) {
                MethodCall m = result.get(0);
                if (m.isViolation() && SCOPE_METHOD_CHAINING.equals(m.baseScope)) {
                    result.clear();
                }
            }

            return result;
        }

        private static ASTPrimarySuffix getFollowingSuffix(JavaNode node) {
            int current = node.getIndexInParent();
            if (node.getParent().getNumChildren() > current + 1) {
                JavaNode following = node.getParent().getChild(current + 1);
                if (following instanceof ASTPrimarySuffix) {
                    return (ASTPrimarySuffix) following;
                }
            }
            return null;
        }

        private static boolean isNotAConstructorCall(ASTPrimaryExpression expression) {
            return !expression.hasDescendantOfType(ASTAllocationExpression.class);
        }

        private static boolean isNotLiteral(ASTPrimaryExpression expression) {
            ASTPrimaryPrefix prefix = expression.getFirstDescendantOfType(ASTPrimaryPrefix.class);
            if (prefix != null) {
                return !prefix.hasDescendantOfType(ASTLiteral.class);
            }
            return true;
        }

        private boolean isNotBuilder() {
            return baseType != StringBuffer.class && baseType != StringBuilder.class
                    && !"StringBuilder".equals(baseTypeName) && !"StringBuffer".equals(baseTypeName)
                    && !methodName.endsWith("Builder");
        }

        private static List<ASTPrimarySuffix> findSuffixesWithoutArguments(ASTPrimaryExpression expr) {
            List<ASTPrimarySuffix> result = new ArrayList<>();
            if (hasRealPrefix(expr)) {
                List<ASTPrimarySuffix> suffixes = expr.findChildrenOfType(ASTPrimarySuffix.class);
                for (ASTPrimarySuffix suffix : suffixes) {
                    if (!suffix.isArguments() && !suffix.isArrayDereference()) {
                        result.add(suffix);
                    }
                }
            }
            return result;
        }

        private static boolean hasRealPrefix(ASTPrimaryExpression expr) {
            ASTPrimaryPrefix prefix = expr.getFirstDescendantOfType(ASTPrimaryPrefix.class);
            return !prefix.usesThisModifier() && !prefix.usesSuperModifier();
        }

        private static boolean hasSuffixesWithArguments(ASTPrimaryExpression expr) {
            boolean result = false;
            if (hasRealPrefix(expr)) {
                List<ASTPrimarySuffix> suffixes = expr.findDescendantsOfType(ASTPrimarySuffix.class);
                for (ASTPrimarySuffix suffix : suffixes) {
                    if (suffix.isArguments()) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }

        private void analyze(ASTPrimaryPrefix prefixNode) {
            List<ASTName> names = prefixNode.findDescendantsOfType(ASTName.class);

            baseName = "unknown";
            methodName = "unknown";

            if (!names.isEmpty()) {
                baseName = names.get(0).getImage();

                int dot = baseName.lastIndexOf('.');
                if (dot == -1) {
                    methodName = baseName;
                    baseName = THIS;
                } else {
                    methodName = baseName.substring(dot + 1);
                    baseName = baseName.substring(0, dot);
                    baseNameInWhitelist = baseName.matches(PREFIX_EXCLUSION_PATTERN);
                }

            } else {
                if (prefixNode.usesThisModifier()) {
                    baseName = THIS;
                } else if (prefixNode.usesSuperModifier()) {
                    baseName = SUPER;
                }
            }
        }

        private void analyze(ASTPrimarySuffix suffix) {
            baseName = METHOD_CALL_CHAIN;
            methodName = suffix.getImage();
        }

        private void analyze(ASTPrimarySuffix suffix, int index, int trustRadius) {
            baseName = "unknown";
            if (index >= trustRadius) {
                baseName = METHOD_CALL_CHAIN;
            }
            methodName = suffix.getImage();
        }

        private void checkViolation() {
            violation = false;
            violationReason = null;

            if (baseNameInWhitelist) {
                return;
            } else if (SCOPE_LOCAL.equals(baseScope)) {
                Assignment lastAssignment = determineLastAssignment();
                if (lastAssignment != null && !lastAssignment.allocation && !lastAssignment.iterator
                        && !lastAssignment.forLoop) {
                    violation = true;
                    violationReason = REASON_OBJECT_NOT_CREATED_LOCALLY;
                }
            } else if (SCOPE_METHOD_CHAINING.equals(baseScope)) {
                violation = true;
                violationReason = REASON_METHOD_CHAIN_CALLS;
            } else if (SCOPE_STATIC_CHAIN.equals(baseScope)) {
                violation = true;
                violationReason = REASON_STATIC_ACCESS;
            }
        }

        private void determineType() {
            NameDeclaration var = null;
            Scope scope = expression.getScope();

            baseScope = SCOPE_LOCAL;
            var = findInLocalScope(baseName, scope);
            if (var == null) {
                baseScope = SCOPE_METHOD;
                var = determineTypeOfVariable(baseName,
                        scope.getEnclosingScope(MethodScope.class).getVariableDeclarations().keySet());
            }
            if (var == null) {
                baseScope = SCOPE_CLASS;
                var = determineTypeOfVariable(baseName,
                        scope.getEnclosingScope(ClassScope.class).getVariableDeclarations().keySet());
            }
            if (var == null) {
                baseScope = SCOPE_METHOD_CHAINING;
            }
            if (var == null && (THIS.equals(baseName) || SUPER.equals(baseName))) {
                baseScope = SCOPE_CLASS;
            }

            if (var instanceof TypedNameDeclaration) {
                baseTypeName = ((TypedNameDeclaration) var).getTypeImage();
                baseType = ((TypedNameDeclaration) var).getType();
            } else if (METHOD_CALL_CHAIN.equals(baseName)) {
                baseScope = SCOPE_METHOD_CHAINING;
            } else if (baseName.contains(".") && !baseName.startsWith("System.")) {
                baseScope = SCOPE_STATIC_CHAIN;
            } else {
                // everything else is no violation - probably a static method
                // call.
                baseScope = null;
            }
        }

        private VariableNameDeclaration findInLocalScope(String name, Scope scope) {
            VariableNameDeclaration result = null;

            result = determineTypeOfVariable(name, scope.getDeclarations(VariableNameDeclaration.class).keySet());
            if (result == null && scope.getParent() instanceof LocalScope) {
                result = findInLocalScope(name, scope.getParent());
            }

            return result;
        }

        private VariableNameDeclaration determineTypeOfVariable(String variableName,
                                                                Set<VariableNameDeclaration> declarations) {
            VariableNameDeclaration result = null;
            for (VariableNameDeclaration var : declarations) {
                if (variableName.equals(var.getImage())) {
                    result = var;
                    break;
                }
            }
            return result;
        }

        private Assignment determineLastAssignment() {
            List<Assignment> assignments = new ArrayList<>();

            ASTBlock block = expression.getFirstParentOfType(ASTMethodDeclaration.class)
                    .getFirstChildOfType(ASTBlock.class);
            //get all variableDeclarators within this block
            List<ASTVariableDeclarator> variableDeclarators = block.findDescendantsOfType(ASTVariableDeclarator.class);
            for (ASTVariableDeclarator declarator : variableDeclarators) {
                ASTVariableDeclaratorId variableDeclaratorId = declarator
                        .getFirstChildOfType(ASTVariableDeclaratorId.class);
                //we only care about it if the image name matches the current baseName
                if (variableDeclaratorId.hasImageEqualTo(baseName)) {
                    boolean allocationFound = declarator
                            .getFirstDescendantOfType(ASTAllocationExpression.class) != null;
                    boolean iterator = isIterator() || isFactory(declarator);
                    boolean forLoop = isForLoop(declarator);
                    assignments.add(new Assignment(declarator.getBeginLine(), allocationFound, iterator, forLoop));
                }
            }

            //get all AssignmentOperators within this block
            List<ASTAssignmentOperator> assignmentStmts = block.findDescendantsOfType(ASTAssignmentOperator.class);
            for (ASTAssignmentOperator stmt : assignmentStmts) {
                //we only care about it if it occurs prior to (or on) the beginLine of the current expression
                //and if it is a simple_assignment_operator
                if (stmt.getBeginLine() <= expression.getBeginLine()
                        && stmt.hasImageEqualTo(SIMPLE_ASSIGNMENT_OPERATOR)) {
                    //now we need to make sure it has the right image name
                    ASTPrimaryPrefix primaryPrefix = stmt.getParent()
                            .getFirstDescendantOfType(ASTPrimaryPrefix.class);
                    if (primaryPrefix != null) {
                        ASTName prefixName = primaryPrefix.getFirstChildOfType(ASTName.class);
                        if (prefixName != null && prefixName.hasImageEqualTo(baseName)) {
                            //this is an assignment related to the baseName we are working with
                            boolean allocationFound = stmt.getParent()
                                    .getFirstDescendantOfType(ASTAllocationExpression.class) != null;
                            boolean iterator = isIterator();
                            assignments
                                    .add(new Assignment(stmt.getBeginLine(), allocationFound, iterator, false));
                        }
                    }
                }
            }

            Assignment result = null;
            if (!assignments.isEmpty()) {
                //sort them in reverse order and return the first one
                Collections.sort(assignments);
                result = assignments.get(0);
            }
            return result;
        }

        private boolean isIterator() {
            boolean iterator = false;
            if (baseType != null && baseType == Iterator.class
                    || baseTypeName != null && baseTypeName.endsWith("Iterator")) {
                iterator = true;
            }
            return iterator;
        }

        private boolean isFactory(ASTVariableDeclarator declarator) {
            boolean factory = false;
            List<ASTName> names = declarator.findDescendantsOfType(ASTName.class);
            for (ASTName name : names) {
                if (name.getImage().toLowerCase(Locale.ROOT).contains("factory")) {
                    factory = true;
                    break;
                }
            }
            return factory;
        }

        private boolean isForLoop(ASTVariableDeclarator declarator) {
            return declarator.getParent().getParent() instanceof ASTForStatement;
        }

        public ASTPrimaryExpression getExpression() {
            return expression;
        }

        public boolean isViolation() {
            return violation;
        }

        public String getViolationReason() {
            return violationReason;
        }

        @Override
        public String toString() {
            return "MethodCall on line " + expression.getBeginLine() + ":\n" + "  " + baseName + " name: " + methodName
                    + "\n" + "  type: " + baseTypeName + " (" + baseType + "), \n" + "  scope: " + baseScope + "\n"
                    + "  violation: " + violation + " (" + violationReason + ")\n";
        }

    }

    /**
     * Stores the assignment of a variable and whether the variable's value is
     * allocated locally (new constructor call). The class is comparable, so
     * that the last assignment can be determined.
     */
    private static class Assignment implements Comparable<Assignment> {
        private int line;
        private boolean allocation;
        private boolean iterator;
        private boolean forLoop;

        Assignment(int line, boolean allocation, boolean iterator, boolean forLoop) {
            this.line = line;
            this.allocation = allocation;
            this.iterator = iterator;
            this.forLoop = forLoop;
        }

        @Override
        public String toString() {
            return "assignment: line=" + line + " allocation:" + allocation + " iterator:" + iterator + " forLoop: "
                    + forLoop;
        }

        @Override
        public int compareTo(Assignment o) {
            return o.line - line;
        }
    }

}
