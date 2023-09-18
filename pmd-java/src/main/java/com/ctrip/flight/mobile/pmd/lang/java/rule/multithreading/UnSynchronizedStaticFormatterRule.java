package com.ctrip.flight.mobile.pmd.lang.java.rule.multithreading;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.types.TypeTestUtil;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;

import java.text.Format;
import java.util.Arrays;
import java.util.List;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
@Deprecated
public class UnSynchronizedStaticFormatterRule extends FlightJavaRule {
    private Class<?> formatterClassToCheck = Format.class;
    private static final List<String> THREAD_SAFE_FORMATTER = Arrays.asList(
            "org.apache.commons.lang3.time.FastDateFormat"
    );

    private static final PropertyDescriptor<Boolean> ALLOW_METHOD_LEVEL_SYNC =
            PropertyFactory.booleanProperty("allowMethodLevelSynchronization")
                    .desc("If true, method level synchronization is allowed as well as synchronized block. Otherwise"
                            + " only synchronized blocks are allowed.")
                    .defaultValue(false)
                    .build();

    public UnSynchronizedStaticFormatterRule() {
        addRuleChainVisit(ASTFieldDeclaration.class);
        definePropertyDescriptor(ALLOW_METHOD_LEVEL_SYNC);
    }

    UnSynchronizedStaticFormatterRule(Class<?> formatterClassToCheck) {
        this();
        this.formatterClassToCheck = formatterClassToCheck;
    }

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        if (!node.isStatic()) {
            return data;
        }
        ASTClassOrInterfaceType cit = node.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        if (cit == null || !TypeTestUtil.isA(formatterClassToCheck, cit)) {
            return data;
        }

        ASTVariableDeclaratorId var = node.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
        for (String formatter : THREAD_SAFE_FORMATTER) {
            if (TypeTestUtil.isA(formatter, var)) {
                return data;
            }
        }
        for (NameOccurrence occ : var.getUsages()) {
            Node n = occ.getLocation();
            // ignore usages, that don't call a method.
            if (!n.getImage().contains(".")) {
                continue;
            }

            // is there a block-level synch?
            ASTSynchronizedStatement syncStatement = n.getFirstParentOfType(ASTSynchronizedStatement.class);
            if (syncStatement != null) {
                ASTExpression expression = syncStatement.getFirstChildOfType(ASTExpression.class);
                if (expression != null) {
                    ASTName name = expression.getFirstDescendantOfType(ASTName.class);
                    if (name != null && name.hasImageEqualTo(var.getName())) {
                        continue;
                    }
                }
            }

            // method level synch enabled and used?
            if (getProperty(ALLOW_METHOD_LEVEL_SYNC)) {
                ASTMethodDeclaration method = n.getFirstParentOfType(ASTMethodDeclaration.class);
                if (method != null && method.isSynchronized() && method.isStatic()) {
                    continue;
                }
            }
            addViolationWithPrecisePosition(data, n, UN_SYNCHRONIZED_STATIC_FORMATTER_VIOLATION_MSG);
        }
        return data;
    }

    private static final String UN_SYNCHRONIZED_STATIC_FORMATTER_VIOLATION_MSG =
            "java.multithreading.UnSynchronizedStaticFormatterRule.violation.msg";
}
