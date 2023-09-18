package com.intellij.plugins.bodhi.pmd.core;

import net.sourceforge.pmd.Report;

/**
 * Represents the suppressed violation node user data. This will be data for leaf
 * nodes of the tree and encapsulates the PMD Report.SuppressedViolation.
 * Only core package classes are coupled with the PMD Library.
 *
 * @author jborgers
 */
public class PMDSuppressedViolation implements HasPositionInFile {

    private final Report.SuppressedViolation suppressedViolation;
    private final PMDViolation pmdViolation;

    public PMDSuppressedViolation(Report.SuppressedViolation suppressed) {
        this.suppressedViolation = suppressed;
        this.pmdViolation = new PMDViolation(suppressedViolation.getRuleViolation());
    }

    /**
     * Returns <code>true</code> if the violation has been suppressed via a
     * NOPMD comment.
     *
     * @return <code>true</code> if the violation has been suppressed via a
     *         NOPMD comment.
     */
    public boolean suppressedByNOPMD() {
        return suppressedViolation.suppressedByNOPMD();
    }

    /**
     * Returns <code>true</code> if the violation has been suppressed via a
     * annotation.
     *
     * @return <code>true</code> if the violation has been suppressed via a
     *         annotation.
     */
    public boolean suppressedByAnnotation() {
        return suppressedViolation.suppressedByAnnotation();
    }

    /**
     * Returns the PMDViolation which is suppressed.
     * @return the PMDViolation which is suppressed.
     */
    public PMDViolation getPMDViolation() {
        return pmdViolation;
    }

    /**
     * Returns the documented reason, the suppressed code line following //NOPMD, or <code>null</code>.
     * @return the documented reason, the suppressed code line following //NOPMD, or <code>null</code>.
     */
    public String getUserMessage() {
        return suppressedViolation.getUserMessage();
    }

    @Override
    public String getFilename() {
        return pmdViolation.getFilename();
    }

    @Override
    public int getBeginLine() {
        return pmdViolation.getBeginLine();
    }

    @Override
    public int getBeginColumn() {
        return pmdViolation.getBeginColumn();
    }
}
