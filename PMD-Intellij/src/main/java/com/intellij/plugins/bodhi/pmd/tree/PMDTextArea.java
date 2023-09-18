package com.intellij.plugins.bodhi.pmd.tree;

import com.ctrip.flight.mobile.pmd.config.AppResourceBundle;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;


/**
 * @author haoren
 * Create at: 2023/8/21
 */
public class PMDTextArea {
    private JTextArea jTextArea;

    public void setTextArea(PMDBranchNode pmdBranchNode) {
        jTextArea.setText("");
        if (Objects.isNull(pmdBranchNode)) {
            return;
        }
        jTextArea.append(String.format("%s%s", AppResourceBundle.getProperty(RULE_NAME, ""),
                Optional.ofNullable(pmdBranchNode.getRuleName()).orElse("")));
        jTextArea.append(CHANGE_LINE);
        setDesc(pmdBranchNode.getToolTip());
        setPmdUrl(pmdBranchNode);
        jTextArea.append(CHANGE_LINE);
        jTextArea.append(AppResourceBundle.getProperty(PMD_EXAMPLE, ""));
        jTextArea.append(CHANGE_LINE);
        if (Objects.nonNull(pmdBranchNode.getExamples())
                && !pmdBranchNode.getExamples().isEmpty()) {
            for (String example : pmdBranchNode.getExamples()) {
                example = example.replace("\n            \n", "");
                jTextArea.append(example);
                jTextArea.append(CHANGE_LINE);
            }
        }
    }

    public JTextArea getTextArea() {
        if (Objects.isNull(this.jTextArea)) {
            jTextArea = new JTextArea();
            jTextArea.setText("");
            jTextArea.setAutoscrolls(true);
            jTextArea.setWrapStyleWord(true);
            jTextArea.setEditable(false);
        }
        return this.jTextArea;
    }

    private void setDesc(String desc) {
        jTextArea.append(AppResourceBundle.getProperty(RULE_DESC, ""));
        if (Objects.isNull(desc)) {
            return;
        }
        if (desc.length() <= DEFAULT_CHANGE_LINE_MAX_LENGTH) {
            jTextArea.append(desc);
            jTextArea.append(CHANGE_LINE);
            return;
        }

        int startIndex;
        int endIndex;
        for (int i = 0; i <= Math.floorDiv(desc.length(), DEFAULT_CHANGE_LINE_MAX_LENGTH); i++) {
            startIndex = Math.multiplyExact(i, DEFAULT_CHANGE_LINE_MAX_LENGTH);
            endIndex = Math.multiplyExact(i + 1, DEFAULT_CHANGE_LINE_MAX_LENGTH);
            jTextArea.append(desc.substring(startIndex, Math.min(endIndex, desc.length())));
            jTextArea.append(CHANGE_LINE);
        }
    }

    private void setPmdUrl(PMDBranchNode pmdBranchNode) {
        jTextArea.append(AppResourceBundle.getProperty(PMD_RULE_URL, ""));
        if (Objects.isNull(pmdBranchNode.getRuleSetName())) {
            jTextArea.append(DEFAULT_PMD_URL);
            return;
        }
        String jumpUrl = getJumpUrl(pmdBranchNode);
        if (jumpUrl.length() <= DEFAULT_CHANGE_LINE_MAX_LENGTH) {
            jTextArea.append(jumpUrl);
            return;
        }
        int startIndex;
        int endIndex;
        for (int i = 0; i <= Math.floorDiv(jumpUrl.length(), DEFAULT_CHANGE_LINE_MAX_LENGTH); i++) {
            startIndex = Math.multiplyExact(i, DEFAULT_CHANGE_LINE_MAX_LENGTH);
            endIndex = Math.multiplyExact(i + 1, DEFAULT_CHANGE_LINE_MAX_LENGTH);
            jTextArea.append(jumpUrl.substring(startIndex, Math.min(endIndex, jumpUrl.length())));
            jTextArea.append(CHANGE_LINE);
        }
    }

    private String getJumpUrl(PMDBranchNode pmdBranchNode) {
        if (StringUtils.equalsIgnoreCase(pmdBranchNode.getRuleSetName(), CUSTOMIZATION_RULE)) {
            return CUSTOMIZATION_RULE_URL;
        } else {
            return String.format(DEFAULT_PMD_FORMAT_URL, pmdBranchNode.getRuleSetName(),
                    getRuleName(pmdBranchNode.getNodeName()));
        }
    }

    private String getRuleName(String ruleName) {
        if (StringUtils.isEmpty(ruleName)) {
            return "";
        }
        return ruleName.replace(DEFAULT_PMD_CHANGE_RULE_NAME, "");
    }

    private static final String RULE_NAME = "java.pmd.intellij.plugin.textarea.rule.name";
    private static final String RULE_DESC = "java.pmd.intellij.plugin.textarea.rule.desc";
    private static final String PMD_RULE_URL = "java.pmd.intellij.plugin.textarea.pmd.rule.url";
    private static final String PMD_EXAMPLE = "java.pmd.intellij.plugin.textarea.pmd.example";
    private static final Integer DEFAULT_CHANGE_LINE_MAX_LENGTH = 45;
    private static final String DEFAULT_PMD_FORMAT_URL = "https://docs.pmd-code.org/latest/pmd_rules_java_%s.html#%s";
    private static final String DEFAULT_PMD_URL = "https://docs.pmd-code.org/latest/pmd_rules_java.html";
    private static final String DEFAULT_PMD_CHANGE_RULE_NAME = "Rule";
    private static final String CHANGE_LINE = "\n";
    private static final String CUSTOMIZATION_RULE = "customization";
    private static final String CUSTOMIZATION_RULE_URL =
            "http://git.dev.sh.ctripcorp.com/flight-mobile/flight-mobile-new-pmd/-/blob/release/pmd-java/README.md";
}
