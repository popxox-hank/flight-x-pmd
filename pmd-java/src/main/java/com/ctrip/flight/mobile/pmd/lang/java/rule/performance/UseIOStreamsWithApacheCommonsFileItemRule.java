package com.ctrip.flight.mobile.pmd.lang.java.rule.performance;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightXPathRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.xpath.XPathVersion;

/**
 * @author haoren
 * Create at: 2023-07-26
 */
public class UseIOStreamsWithApacheCommonsFileItemRule extends FlightXPathRule {

    private static final String XPATH = "//PrimaryPrefix/Name[ends-with(@Image, '.get') or ends-with(@Image, '" +
            ".getString')][starts-with(@Image, concat(ancestor::MethodDeclaration//FormalParameter/Type/ReferenceType" +
            "/ClassOrInterfaceType[pmd-java:typeIs('org.apache.commons.fileupload.FileItem')]/../../." +
            ".//VariableDeclaratorId/@Name,'.')) or starts-with(@Image, concat" +
            "(ancestor::MethodDeclaration//LocalVariableDeclaration/Type/ReferenceType/ClassOrInterfaceType" +
            "[pmd-java:typeIs('org.apache.commons.fileupload.FileItem')]/../../..//VariableDeclaratorId/@Name,'.')) " +
            "or starts-with(@Image, concat(ancestor::ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration" +
            "/FieldDeclaration/Type" +
            "/ReferenceType/ClassOrInterfaceType[pmd-java:typeIs('org.apache.commons.fileupload.FileItem')]/../../." +
            ".//VariableDeclaratorId/@Name,'.'))]";

    public UseIOStreamsWithApacheCommonsFileItemRule() {
        super(XPathVersion.XPATH_2_0, XPATH);
    }

    @Override
    public void addViolation(Object data, Node node, String arg) {
        addViolationWithPrecisePosition(data, node, USE_IO_STREAM_WITH_APACHE_COMMONS_FILE_ITEM_VIOLATION_MSG);
    }

    private static final String USE_IO_STREAM_WITH_APACHE_COMMONS_FILE_ITEM_VIOLATION_MSG =
            "java.performance.UseIOStreamsWithApacheCommonsFileItemRule.violation.msg";
}
