package com.ctrip.flight.mobile.pmd.lang.java.rule.errorprone;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightJavaRule;
import net.sourceforge.pmd.lang.java.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.properties.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

import static net.sourceforge.pmd.properties.PropertyFactory.booleanProperty;
import static net.sourceforge.pmd.properties.constraints.NumericConstraints.positive;

/**
 * @author haoren
 * Create at: 2023-08-03
 */
@Deprecated
public class AvoidDuplicateLiteralsRule extends FlightJavaRule {
    private static final Logger LOG = Logger.getLogger(AvoidDuplicateLiteralsRule.class.getName());

    public static final PropertyDescriptor<Integer> THRESHOLD_DESCRIPTOR
            = PropertyFactory.intProperty("maxDuplicateLiterals")
            .desc("Max duplicate literals")
            .require(positive()).defaultValue(4).build();

    public static final PropertyDescriptor<Integer> MINIMUM_LENGTH_DESCRIPTOR = PropertyFactory.intProperty(
            "minimumLength").desc("Minimum string length to check").require(positive()).defaultValue(3).build();

    public static final PropertyDescriptor<Boolean> SKIP_ANNOTATIONS_DESCRIPTOR =
            booleanProperty("skipAnnotations")
                    .desc("Skip literals within annotations").defaultValue(false).build();


    public static final StringProperty EXCEPTION_LIST_DESCRIPTOR
            = StringProperty.named("exceptionList")
            .desc("List of literals to ignore. "
                    + "A literal is ignored if its image can be found in this list. "
                    + "Components of this list should not be surrounded by double quotes.")
            .defaultValue(null)
            .build();

    public static final CharacterProperty SEPARATOR_DESCRIPTOR = new CharacterProperty("separator",
            "Ignore list separator", ',', 4.0f);

    @Deprecated
    public static final FileProperty EXCEPTION_FILE_DESCRIPTOR = new FileProperty("exceptionfile",
            "deprecated!(Use 'exceptionList' property) File containing strings to skip (one string per line), only " +
                    "used if ignore list is not set. "
                    + "File must be UTF-8 encoded.", null, 5.0f);

    /**
     * @deprecated This ad-hoc solution will be integrated into the global properties framework somehow
     */
    @Deprecated
    public static class ExceptionParser {

        private static final char ESCAPE_CHAR = '\\';
        private char delimiter;

        public ExceptionParser(char delimiter) {
            this.delimiter = delimiter;
        }

        public Set<String> parse(String s) {
            Set<String> result = new HashSet<>();
            StringBuilder currentToken = new StringBuilder();
            boolean inEscapeMode = false;
            for (int i = 0; i < s.length(); i++) {
                if (inEscapeMode) {
                    inEscapeMode = false;
                    currentToken.append(s.charAt(i));
                    continue;
                }
                if (s.charAt(i) == ESCAPE_CHAR) {
                    inEscapeMode = true;
                    continue;
                }
                if (s.charAt(i) == delimiter) {
                    result.add(currentToken.toString());
                    currentToken = new StringBuilder();
                } else {
                    currentToken.append(s.charAt(i));
                }
            }
            if (currentToken.length() > 0) {
                result.add(currentToken.toString());
            }
            return result;
        }
    }

    private Map<String, List<ASTLiteral>> literals = new HashMap<>();
    private Set<String> exceptions = new HashSet<>();
    private int minLength;

    public AvoidDuplicateLiteralsRule() {
        definePropertyDescriptor(THRESHOLD_DESCRIPTOR);
        definePropertyDescriptor(MINIMUM_LENGTH_DESCRIPTOR);
        definePropertyDescriptor(SKIP_ANNOTATIONS_DESCRIPTOR);
        definePropertyDescriptor(EXCEPTION_LIST_DESCRIPTOR);
        definePropertyDescriptor(SEPARATOR_DESCRIPTOR);
        definePropertyDescriptor(EXCEPTION_FILE_DESCRIPTOR);
    }

    private LineNumberReader getLineReader() throws IOException {
        return new LineNumberReader(Files.newBufferedReader(getProperty(EXCEPTION_FILE_DESCRIPTOR).toPath(),
                StandardCharsets.UTF_8));
    }

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        literals.clear();

        if (getProperty(EXCEPTION_LIST_DESCRIPTOR) != null) {
            if (isPropertyOverridden(SEPARATOR_DESCRIPTOR)) {
                LOG.warning("Rule AvoidDuplicateLiterals uses deprecated property 'separator'. "
                        + "Future versions of PMD will remove support for this property. "
                        + "Please use the default separator (',') and avoid setting this property instead.");
            }
            AvoidDuplicateLiteralsRule.ExceptionParser p =
                    new AvoidDuplicateLiteralsRule.ExceptionParser(getProperty(SEPARATOR_DESCRIPTOR));
            exceptions = p.parse(getProperty(EXCEPTION_LIST_DESCRIPTOR));
        } else if (getProperty(EXCEPTION_FILE_DESCRIPTOR) != null) {
            exceptions = new HashSet<>();
            LOG.warning("Rule AvoidDuplicateLiterals uses deprecated property 'exceptionFile'. "
                    + "Future versions of PMD will remove support for this property. "
                    + "Please use 'exceptionList' instead.");
            try (LineNumberReader reader = getLineReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    exceptions.add(line);
                }
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        minLength = 2 + getProperty(MINIMUM_LENGTH_DESCRIPTOR);

        super.visit(node, data);

        processResults(data);

        return data;
    }

    private void processResults(Object data) {

        int threshold = getProperty(THRESHOLD_DESCRIPTOR);

        for (Map.Entry<String, List<ASTLiteral>> entry : literals.entrySet()) {
            List<ASTLiteral> occurrences = entry.getValue();
            if (occurrences.size() >= threshold) {
                ASTLiteral first = occurrences.get(0);
                String rawImage = first.getEscapedStringLiteral();
                addViolationWithPrecisePosition(data, first, AVOID_DUPLICATE_LITERALS_VIOLATION_MSG, rawImage,
                        occurrences.size(), first.getBeginLine());
            }
        }
    }

    @Override
    public Object visit(ASTLiteral node, Object data) {
        if (!node.isStringLiteral()) {
            return data;
        }
        String image = node.getImage();

        if (image.length() < minLength) {
            return data;
        }

        if (exceptions.contains(image.substring(1, image.length() - 1))) {
            return data;
        }

        if (getProperty(SKIP_ANNOTATIONS_DESCRIPTOR) && node.getFirstParentOfType(ASTAnnotation.class) != null) {
            return data;
        }

        if (literals.containsKey(image)) {
            List<ASTLiteral> occurrences = literals.get(image);
            occurrences.add(node);
        } else {
            List<ASTLiteral> occurrences = new ArrayList<>();
            occurrences.add(node);
            literals.put(image, occurrences);
        }

        return data;
    }

    private static String checkFile(File file) {

        if (!file.exists()) {
            return "File '" + file.getName() + "' does not exist";
        }
        if (!file.canRead()) {
            return "File '" + file.getName() + "' cannot be read";
        }
        if (file.length() == 0) {
            return "File '" + file.getName() + "' is empty";
        }

        return null;
    }

    /**
     * @see PropertySource#dysfunctionReason()
     */
    @Override
    public String dysfunctionReason() {
        File file = getProperty(EXCEPTION_FILE_DESCRIPTOR);
        if (file != null) {
            String issue = checkFile(file);
            if (issue != null) {
                return issue;
            }

            String ignores = getProperty(EXCEPTION_LIST_DESCRIPTOR);
            if (StringUtils.isNotBlank(ignores)) {
                return "Cannot reference external file AND local values";
            }
        }

        return null;
    }

    private static final String AVOID_DUPLICATE_LITERALS_VIOLATION_MSG =
            "java.errorprone.AvoidDuplicateLiteralsRule.violation.msg";
}
