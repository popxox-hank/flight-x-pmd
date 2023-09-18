package com.intellij.plugins.bodhi.pmd.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * PMD branch tree node for processing errors. It has a Severity.BLOCKER icon.
 * It registers files with errors in order to limit the number of errors per file to 1.
 *
 * @author jborgers
 */
public class PMDErrorBranchNode extends PMDBranchNode {

    private final Set<String> filesWithError = new HashSet<>();

    public PMDErrorBranchNode(String name) {
        super(name);
    }

    public boolean hasFile(String file) {
        return filesWithError.contains(file);
    }

    public void registerFile(String file) {
        filesWithError.add(file);
    }
    @Override
    public synchronized void render(PMDCellRenderer cellRenderer, boolean expanded) {
        cellRenderer.setIcon(Severity.BLOCKER.getIcon());
        super.render(cellRenderer, expanded);
    }
}
