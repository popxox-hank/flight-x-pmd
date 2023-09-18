package com.intellij.plugins.bodhi.pmd.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

/**
 * @author haoren
 * Create at: 2023/8/28
 */
public class PMDLanguage extends DefaultActionGroup {

    public PMDLanguage() {
        super("Change Language", false);
//        AnAction action = new AnAction() {
//            public void actionPerformed(AnActionEvent e) {
//                System.out.println("haha");
//            }
//        };
//        super.add(action);
    }
}
