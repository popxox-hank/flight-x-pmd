package com.intellij.plugins.bodhi.pmd;

import com.ctrip.flight.mobile.pmd.config.AppResourceBundle;
import com.intellij.AbstractBundle;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.plugins.bodhi.pmd.actions.PMDCustom;
import com.intellij.plugins.bodhi.pmd.actions.PMDLanguage;
import com.intellij.plugins.bodhi.pmd.actions.PreDefinedMenuGroup;
import com.intellij.plugins.bodhi.pmd.core.PMDResultCollector;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * This is the Project Component of the PMD Plugin.
 *
 * @author bodhi
 * @version 1.0
 */

@State(
        name = "PDMPlugin",
        storages = {
                @Storage(
                        value = "$PROJECT_FILE$"
                )}
)
public class PMDProjectComponent implements ProjectComponent, PersistentStateComponent<PersistentData>, Disposable {

    /**
     * The Tool ID of the results panel.
     */
    public static final String TOOL_ID = "Ctrip PMD";

    private static final String COMPONENT_NAME = "PMDProjectComponent";
    private static final String CHANGE_LANGUAGE = "Change Language → %s";
    private static final String CHINESE_LANGUAGE = "chinese";
    private static final String ENGLISH_LANGUAGE = "english";
    private final Project currentProject;
    private static final AtomicInteger numProjectsOpen = new AtomicInteger();
    private PMDResultPanel resultPanel;
    private ToolWindow resultWindow;
    private String lastRunRuleSetPaths;
    private boolean lastRunRulesCustom;
    private AnActionEvent lastRunActionEvent;
    private Set<String> customRuleSetPaths = new LinkedHashSet<>(); // avoid duplicates, maintain order
    private Map<String, String> options = new HashMap<>();
    private final ToolWindowManager toolWindowManager;
    private boolean skipTestSources;
    private boolean scanFilesBeforeCheckin;
    private static final NotificationGroup BALLOON =
            NotificationGroupManager.getInstance().getNotificationGroup("PMDChangeLanguageBalloon");
    private static final String BUNDLE = "messages.PMD-Intellij";

    /**
     * Creates a PMD Project component based on the project given.
     *
     * @param project The project on which to create the component.
     */
    public PMDProjectComponent(Project project) {
        this.currentProject = project;
        toolWindowManager = ToolWindowManager.getInstance(currentProject);
        numProjectsOpen.incrementAndGet();
    }

    public void initComponent() {
        //Add custom rules as menu items if defined.
        updateCustomRulesMenu();
        updateChangeLanguageMenu();
        ActionGroup actionGroup = registerActions("PMDPredefined");
        if (actionGroup != null)
            ((PreDefinedMenuGroup) actionGroup).setComponent(this);
        registerActions("PMDCustom");
        registerActions("PMDLanguage");
    }

    private ActionGroup registerActions(String actionName) {
        ActionManager actionMgr = ActionManager.getInstance();
        ActionGroup actionGroup = (ActionGroup) actionMgr.getAction(actionName);
        if (actionGroup != null) {
            for (AnAction act : actionGroup.getChildren(null)) {
                String actName = "PMD" + act.getTemplatePresentation().getText();
                if (actionMgr.getAction(actName) == null)
                    actionMgr.registerAction(actName, act);
            }
        }
        return actionGroup;
    }

    void updateChangeLanguageMenu() {
        PMDLanguage actionGroup = (PMDLanguage) ActionManager.getInstance().getAction("PMDLanguage");
        String defaultChangeLanguage = isEnglishLanguage() ? CHINESE_LANGUAGE : ENGLISH_LANGUAGE;

        AnAction action = new AnAction(String.format(CHANGE_LANGUAGE, defaultChangeLanguage)) {
            public void actionPerformed(AnActionEvent e) {
                Locale changeLocale = isEnglishLanguage() ? Locale.CHINESE : Locale.ENGLISH;
                AppResourceBundle.changeLanguage(changeLocale);
                Notifications.Bus.notify(
                        BALLOON.createNotification(getBalloonMessage(changeLocale), NotificationType.INFORMATION));
            }

            public void update(AnActionEvent event) {
                String languageText = isEnglishLanguage() ? CHINESE_LANGUAGE : ENGLISH_LANGUAGE;
                event.getPresentation().setText(String.format(CHANGE_LANGUAGE, languageText));
            }
        };
        actionGroup.removeAll();
        actionGroup.add(action);
    }

    private boolean isEnglishLanguage() {
        return Objects.equals(AppResourceBundle.getLanguage(), Locale.ENGLISH);
    }

    private String getBalloonMessage(Locale locale) {
        return Objects.equals(locale, Locale.ENGLISH)
                ? AbstractBundle.message(ResourceBundle.getBundle(BUNDLE), "notification.balloon.english.message")
                : AbstractBundle.message(ResourceBundle.getBundle(BUNDLE), "notification.balloon.chinese.message");
    }

    private boolean hasDuplicateBareFileName(Iterable<String> paths) {
        boolean duplicate = false;
        List<String> fileNames = new ArrayList<>();
        for (String path : paths) {
            String fileName = PMDUtil.getBareFileNameFromPath(path);
            if (fileNames.contains(fileName)) {
                duplicate = true;
                break;
            }
            fileNames.add(fileName);
        }
        return duplicate;
    }

    /**
     * Reflect customRuleSetPaths into actionGroup (ActionManager singleton instance)
     * Better solution is an ActionManager for each project and
     * one shared configuration/settings for all projects, as expected by user
     * Now for > 1 projects open, merge the rule sets of shared actions (menu) and current project
     */
    void updateCustomRulesMenu() {
        PMDCustom actionGroup = (PMDCustom) ActionManager.getInstance().getAction("PMDCustom");
        if (numProjectsOpen.get() != 1) {
            // merge actions from menu and from settings to not lose any when switching between projects
            AnAction[] currentActions = actionGroup.getChildren(null);
            Set<String> ruleSetPathsFromMenu = new HashSet<>();
            for (AnAction action : currentActions) {
                if (action.getSynonyms().size() == 1) {
                    String ruleSetPath = action.getSynonyms().get(0).get();
                    ruleSetPathsFromMenu.add(ruleSetPath);
                }
            }
            customRuleSetPaths.addAll(ruleSetPathsFromMenu);
        }
        List<AnAction> newActionList = new ArrayList<>();
        boolean hasDuplicate = hasDuplicateBareFileName(customRuleSetPaths);
        for (final String ruleSetPath : customRuleSetPaths) {
            String ruleSetName = PMDResultCollector.getRuleSetName(ruleSetPath);
            String extFileName = PMDUtil.getExtendedFileNameFromPath(ruleSetPath);
            String bareFileName = PMDUtil.getBareFileNameFromPath(ruleSetPath);
            String actionText = ruleSetName;
            if (!ruleSetName.equals(bareFileName) || hasDuplicate) {
                actionText += " (" + extFileName + ")";
            }
            AnAction action = new AnAction(actionText) {
                public void actionPerformed(AnActionEvent e) {
                    PMDInvoker.getInstance().runPMD(e, ruleSetPath, true);
                    setLastRunActionAndRules(e, ruleSetPath, true);
                }
            };
            action.addSynonym(() -> ruleSetPath);
            newActionList.add(action);
        }
        actionGroup.removeAll();
        actionGroup.addAll(newActionList);
    }

    public void dispose() {
        numProjectsOpen.decrementAndGet();
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    public void projectOpened() {
        resultPanel = new PMDResultPanel(this);
    }

    /**
     * Registers a tool window for showing PMD results.
     */
    private void registerToolWindow() {
        if (toolWindowManager.getToolWindow(TOOL_ID) == null) {
            resultWindow = toolWindowManager.registerToolWindow(TOOL_ID, true, ToolWindowAnchor.BOTTOM);
            Content content = ContentFactory.SERVICE.getInstance().createContent(resultPanel, "", false);
            resultWindow.getContentManager().addContent(content);
            resultWindow.setType(ToolWindowType.DOCKED, null);
        }
    }

    /**
     * Gets the result panel where the PMD results are shown.
     *
     * @return The panel where results are shown.
     */
    public PMDResultPanel getResultPanel() {
        return resultPanel;
    }

    /**
     * Set up the tool window and initializes the result tree.
     */
    public void setupToolWindow() {
        registerToolWindow();
        resultPanel.initializeTree();
    }

    /**
     * Close the result panel and unregister the tool window.
     */
    public void closeResultWindow() {
        resultWindow.hide(null);
        resultPanel.initializeTree();
        if (toolWindowManager.getToolWindow(TOOL_ID) != null)
            toolWindowManager.unregisterToolWindow(TOOL_ID);
    }

    /**
     * Get the current project.
     *
     * @return the current project
     */
    public Project getCurrentProject() {
        return currentProject;
    }

    /**
     * Get the last run PMD rule set paths on this project.
     *
     * @return the last run rule set paths.
     */
    public String getLastRunRuleSetPaths() {
        return lastRunRuleSetPaths;
    }

    /**
     * Return whether the last run PMD rules on this project are custom rules.
     *
     * @return whether the last run rules are custom rules.
     */
    public boolean isLastRunRulesCustom() {
        return lastRunRulesCustom;
    }

    /**
     * Get the last run action event on this project.
     *
     * @return the last run action.
     */
    public AnActionEvent getLastRunAction() {
        return lastRunActionEvent;
    }

    /**
     * Set the last run action event and PMD rule(s). Multiple rules should be delimited by
     * PMDInvoker.RULE_DELIMITER.
     *
     * @param lastActionEvent     the last run action event
     * @param lastRunRuleSetPaths The last run rule set paths
     * @param isCustom            whether the last run rules are custom rules
     */
    public void setLastRunActionAndRules(AnActionEvent lastActionEvent, String lastRunRuleSetPaths, boolean isCustom) {
        this.lastRunRuleSetPaths = lastRunRuleSetPaths;
        this.lastRunActionEvent = lastActionEvent;
        this.lastRunRulesCustom = isCustom;
    }

    public List<String> getCustomRuleSetPaths() {
        return new ArrayList<>(customRuleSetPaths);
    }

    public void setCustomRuleSets(List<String> customRuleSetPaths) {
        this.customRuleSetPaths = new LinkedHashSet<>(customRuleSetPaths);
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    /**
     * Return fields in a PersistentData object
     *
     * @return
     */
    @NotNull
    public PersistentData getState() {
        final PersistentData pd = new PersistentData();
        for (String item : customRuleSetPaths) {
            pd.getCustomRuleSets().add(item);
        }
        for (String key : options.keySet()) {
            pd.getOptions().put(key, options.get(key));
        }
        pd.setSkipTestSources(skipTestSources);
        pd.setScanFilesBeforeCheckin(scanFilesBeforeCheckin);
        return pd;
    }

    /**
     * load state into fields
     *
     * @param state
     */
    public void loadState(PersistentData state) {
        customRuleSetPaths.clear();
        options.clear();
        customRuleSetPaths.addAll(state.getCustomRuleSets());
        for (String key : state.getOptions().keySet()) {
            options.put(key, state.getOptions().get(key));
        }
        // replace unused 'Encoding' by 'Statistics URL'
        if (options.remove("Encoding") != null) {
            options.put(PMDConfigurationForm.STATISTICS_URL, "");
        }

        this.skipTestSources = state.isSkipTestSources();
        this.scanFilesBeforeCheckin = state.isScanFilesBeforeCheckin();
    }

    public void skipTestSources(boolean skipTestSources) {
        this.skipTestSources = skipTestSources;
    }

    public boolean isSkipTestSources() {
        return skipTestSources;
    }

    public void setScanFilesBeforeCheckin(boolean scanFilesBeforeCheckin) {
        this.scanFilesBeforeCheckin = scanFilesBeforeCheckin;
    }

    public boolean isScanFilesBeforeCheckin() {
        return scanFilesBeforeCheckin;
    }

}
