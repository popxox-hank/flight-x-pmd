package com.ctrip.flight.mobile.pmd.lang.java.rule.documentation;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCommentRule;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static net.sourceforge.pmd.properties.PropertyFactory.booleanProperty;
import static net.sourceforge.pmd.properties.PropertyFactory.stringListProperty;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
@Deprecated
public class CommentContentRule extends FlightCommentRule {

    private boolean caseSensitive;
    private List<String> originalBadWords;
    private List<String> currentBadWords;

    // ignored when property above == True
    public static final PropertyDescriptor<Boolean> CASE_SENSITIVE_DESCRIPTOR =
            booleanProperty("caseSensitive").defaultValue(false).desc("Case sensitive").build();

    public static final PropertyDescriptor<List<String>> DISSALLOWED_TERMS_DESCRIPTOR =
            stringListProperty("disallowedTerms")
                    .desc("Illegal terms or phrases")
                    .defaultValues("idiot", "jerk").build(); // TODO make blank property? or add more defaults?

    private static final Set<PropertyDescriptor<?>> NON_REGEX_PROPERTIES;

    static {
        NON_REGEX_PROPERTIES = new HashSet<>(1);
        NON_REGEX_PROPERTIES.add(CASE_SENSITIVE_DESCRIPTOR);
    }

    public CommentContentRule() {
        definePropertyDescriptor(CASE_SENSITIVE_DESCRIPTOR);
        definePropertyDescriptor(DISSALLOWED_TERMS_DESCRIPTOR);
    }

    /**
     * Capture values and perform all the case-conversions once per run
     */
    @Override
    public void start(RuleContext ctx) {
        originalBadWords = getProperty(DISSALLOWED_TERMS_DESCRIPTOR);
        caseSensitive = getProperty(CASE_SENSITIVE_DESCRIPTOR);
        if (caseSensitive) {
            currentBadWords = originalBadWords;
        } else {
            currentBadWords = new ArrayList<>();
            for (String badWord : originalBadWords) {
                currentBadWords.add(badWord.toUpperCase(Locale.ROOT));
            }
        }
    }

    /**
     * .
     *
     * @see Rule#end(RuleContext)
     */
    @Override
    public void end(RuleContext ctx) {
        // Override as needed
    }

    private List<String> illegalTermsIn(Comment comment) {

        if (currentBadWords.isEmpty()) {
            return Collections.emptyList();
        }

        String commentText = filteredCommentIn(comment);
        if (StringUtils.isBlank(commentText)) {
            return Collections.emptyList();
        }

        if (!caseSensitive) {
            commentText = commentText.toUpperCase(Locale.ROOT);
        }

        List<String> foundWords = new ArrayList<>();

        for (int i = 0; i < currentBadWords.size(); i++) {
            if (commentText.contains(currentBadWords.get(i))) {
                foundWords.add(originalBadWords.get(i));
            }
        }

        return foundWords;
    }

    private String errorMsgFor(List<String> badWords) {
        StringBuilder msg = new StringBuilder(this.getMessage()).append(": ");
        if (badWords.size() == 1) {
            msg.append("Invalid term: '").append(badWords.get(0)).append('\'');
        } else {
            msg.append("Invalid terms: '");
            msg.append(badWords.get(0));
            for (int i = 1; i < badWords.size(); i++) {
                msg.append("', '").append(badWords.get(i));
            }
            msg.append('\'');
        }
        return msg.toString();
    }

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {

        // NPE patch: Eclipse plugin doesn't call start() at onset?
        if (currentBadWords == null) {
            start(null);
        }

        for (Comment comment : cUnit.getComments()) {
            List<String> badWords = illegalTermsIn(comment);
            if (badWords.isEmpty()) {
                continue;
            }

            addViolationWithPrecisePosition(data, cUnit, COMMENT_CONTENT_VIOLATION_MSG,
                    errorMsgFor(badWords), comment.getBeginLine(), comment.getEndLine());
        }

        return super.visit(cUnit, data);
    }

    private static final String COMMENT_CONTENT_VIOLATION_MSG =
            "java.documentation.CommentContentRule.violation.msg";
}
