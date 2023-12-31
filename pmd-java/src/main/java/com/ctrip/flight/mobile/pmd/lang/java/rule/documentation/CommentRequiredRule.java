package com.ctrip.flight.mobile.pmd.lang.java.rule.documentation;

import com.ctrip.flight.mobile.pmd.lang.java.rule.FlightCommentRule;
import com.google.common.collect.ImmutableSet;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.multifile.signature.JavaOperationSignature;
import net.sourceforge.pmd.properties.PropertyBuilder;
import net.sourceforge.pmd.properties.PropertyDescriptor;
import net.sourceforge.pmd.properties.PropertyFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author haoren
 * Create at: 2023-07-25
 */
public class CommentRequiredRule extends FlightCommentRule {
    private static final Logger LOG = Logger.getLogger(CommentRequiredRule.class.getName());

    // Used to pretty print a message
    private static final Map<String, String> DESCRIPTOR_NAME_TO_COMMENT_TYPE = new HashMap<>();

    private static final PropertyDescriptor<CommentRequirement> ACCESSOR_CMT_DESCRIPTOR
            = requirementPropertyBuilder("accessorCommentRequirement", "Comments on getters and setters\"")
            .defaultValue(CommentRequirement.Ignored).build();
    private static final PropertyDescriptor<CommentRequirement> OVERRIDE_CMT_DESCRIPTOR
            = requirementPropertyBuilder("methodWithOverrideCommentRequirement", "Comments on @Override methods")
            .defaultValue(CommentRequirement.Ignored).build();
    private static final PropertyDescriptor<CommentRequirement> HEADER_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("headerCommentRequirement", "Deprecated! Header comments. Please use the " +
            "property \"classCommentRequired\" instead.").build();
    private static final PropertyDescriptor<CommentRequirement> CLASS_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("classCommentRequirement", "Class comments").build();
    private static final PropertyDescriptor<CommentRequirement> FIELD_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("fieldCommentRequirement", "Field comments").build();
    private static final PropertyDescriptor<CommentRequirement> PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR
            =
            requirementPropertyBuilder("publicMethodCommentRequirement", "Public method and constructor comments").build();
    private static final PropertyDescriptor<CommentRequirement> PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("protectedMethodCommentRequirement",
            "Protected method constructor comments").build();
    private static final PropertyDescriptor<CommentRequirement> ENUM_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("enumCommentRequirement", "Enum comments").build();
    private static final PropertyDescriptor<CommentRequirement> SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("serialVersionUIDCommentRequired", "Serial version UID comments")
            .defaultValue(CommentRequirement.Ignored).build();
    private static final PropertyDescriptor<CommentRequirement> SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR
            = requirementPropertyBuilder("serialPersistentFieldsCommentRequired", "Serial persistent fields comments")
            .defaultValue(CommentRequirement.Ignored).build();
    private static final Set<String> UN_CHECK_FIELD_ANNOTATION = ImmutableSet.of("Autowired", "Inject", "Resource",
            "InjectMock");
    /**
     * stores the resolved property values. This is necessary in order to transparently use deprecated properties.
     */
    private final Map<PropertyDescriptor<CommentRequirement>, CommentRequirement> propertyValues = new HashMap<>();

    public CommentRequiredRule() {
        definePropertyDescriptor(OVERRIDE_CMT_DESCRIPTOR);
        definePropertyDescriptor(ACCESSOR_CMT_DESCRIPTOR);
        definePropertyDescriptor(CLASS_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(HEADER_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(FIELD_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(ENUM_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR);
        definePropertyDescriptor(SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR);

//        addRuleChainVisit(ASTCompilationUnit.class);
//        addRuleChainVisit(ASTEnumDeclaration.class);
//        addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
//        addRuleChainVisit(ASTConstructorDeclaration.class);
//        addRuleChainVisit(ASTMethodDeclaration.class);
//        addRuleChainVisit(ASTFieldDeclaration.class);
    }

    @Override
    public void start(RuleContext ctx) {
        propertyValues.put(ACCESSOR_CMT_DESCRIPTOR, getProperty(ACCESSOR_CMT_DESCRIPTOR));
        propertyValues.put(OVERRIDE_CMT_DESCRIPTOR, getProperty(OVERRIDE_CMT_DESCRIPTOR));
        propertyValues.put(FIELD_CMT_REQUIREMENT_DESCRIPTOR, getProperty(FIELD_CMT_REQUIREMENT_DESCRIPTOR));
        propertyValues.put(PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR, getProperty(PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR));
        propertyValues.put(PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR, getProperty(PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR));
        propertyValues.put(ENUM_CMT_REQUIREMENT_DESCRIPTOR, getProperty(ENUM_CMT_REQUIREMENT_DESCRIPTOR));
        propertyValues.put(SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR,
                getProperty(SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR));
        propertyValues.put(SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR,
                getProperty(SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR));

        CommentRequirement headerCommentRequirementValue = getProperty(HEADER_CMT_REQUIREMENT_DESCRIPTOR);
        boolean headerCommentRequirementValueOverridden = headerCommentRequirementValue != CommentRequirement.Required;
        CommentRequirement classCommentRequirementValue = getProperty(CLASS_CMT_REQUIREMENT_DESCRIPTOR);
        boolean classCommentRequirementValueOverridden = classCommentRequirementValue != CommentRequirement.Required;

        if (headerCommentRequirementValueOverridden && !classCommentRequirementValueOverridden) {
            LOG.warning("Rule CommentRequired uses deprecated property 'headerCommentRequirement'. "
                    + "Future versions of PMD will remove support for this property. "
                    + "Please use 'classCommentRequirement' instead!");
            propertyValues.put(CLASS_CMT_REQUIREMENT_DESCRIPTOR, headerCommentRequirementValue);
        } else {
            propertyValues.put(CLASS_CMT_REQUIREMENT_DESCRIPTOR, classCommentRequirementValue);
        }
    }

    private void checkCommentMeetsRequirement(Object data, AbstractJavaNode node,
                                              PropertyDescriptor<CommentRequirement> descriptor) {
        switch (propertyValues.get(descriptor)) {
            case Ignored:
                break;
            case Required:
                if (node.comment() == null) {
                    commentRequiredViolation(data, node, descriptor);
                }
                break;
            case Unwanted:
                if (node.comment() != null) {
                    commentRequiredViolation(data, node, descriptor);
                }
                break;
            default:
                break;
        }
    }


    // Adds a violation
    private void commentRequiredViolation(Object data, AbstractJavaNode node,
                                          PropertyDescriptor<CommentRequirement> descriptor) {
        addViolationWithPrecisePosition(data, node, COMMENT_REQUIRED_VIOLATION_MSG,
                DESCRIPTOR_NAME_TO_COMMENT_TYPE.get(descriptor.name())
                        .replace(" comments", "")
                        .replace("Comments on ", "")
                        .replace("\"", ""));
    }


    @Override
    public Object visit(ASTClassOrInterfaceDeclaration decl, Object data) {
        checkCommentMeetsRequirement(data, decl, CLASS_CMT_REQUIREMENT_DESCRIPTOR);
        return super.visit(decl, data);
    }


    @Override
    public Object visit(ASTConstructorDeclaration decl, Object data) {
        checkMethodOrConstructorComment(decl, data);
        return super.visit(decl, data);
    }


    @Override
    public Object visit(ASTMethodDeclaration decl, Object data) {
        if (isAnnotatedOverride(decl)) {
            checkCommentMeetsRequirement(data, decl, OVERRIDE_CMT_DESCRIPTOR);
        } else if (decl.getSignature().role == JavaOperationSignature.Role.GETTER_OR_SETTER) {
            checkCommentMeetsRequirement(data, decl, ACCESSOR_CMT_DESCRIPTOR);
        } else {
            checkMethodOrConstructorComment(decl, data);
        }
        return super.visit(decl, data);
    }


    private void checkMethodOrConstructorComment(AbstractJavaAccessNode decl, Object data) {
        if (decl.isPublic()) {
            checkCommentMeetsRequirement(data, decl, PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR);
        } else if (decl.isProtected()) {
            checkCommentMeetsRequirement(data, decl, PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR);
        }
    }


    private boolean isAnnotatedOverride(ASTMethodDeclaration decl) {
        List<ASTMarkerAnnotation> annotations = decl.getParent().findDescendantsOfType(ASTMarkerAnnotation.class);
        for (ASTMarkerAnnotation ann : annotations) { // TODO consider making a method to get the annotations of a
            // method
            if ("Override".equals(ann.getFirstChildOfType(ASTName.class).getImage())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Object visit(ASTFieldDeclaration decl, Object data) {
        if (isUnCheckAnnotation(decl) || isUnCheckConstructorAnnotation(decl)) {
            return super.visit(decl, data);
        }
        if (isSerialVersionUID(decl)) {
            checkCommentMeetsRequirement(data, decl, SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR);
        } else if (isSerialPersistentFields(decl)) {
            checkCommentMeetsRequirement(data, decl, SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR);
        } else {
            checkCommentMeetsRequirement(data, decl, FIELD_CMT_REQUIREMENT_DESCRIPTOR);
        }

        return super.visit(decl, data);
    }

    private boolean isUnCheckConstructorAnnotation(ASTFieldDeclaration decl) {
        List<ASTConstructorDeclaration> constructorList =
                Optional.ofNullable(decl.getParentsOfType(ASTClassOrInterfaceDeclaration.class))
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(Objects::nonNull)
                        .map(x -> x.findDescendantsOfType(ASTConstructorDeclaration.class))
                        .flatMap(List::stream)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        if (constructorList.isEmpty()) {
            return false;
        }

        List<String> annotationNameList = constructorList.stream()
                .map(constructor -> constructor.getParent().findChildrenOfType(ASTAnnotation.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(ASTAnnotation::getAnnotationName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());
        if (!isMatchUnCheckAnnotation(annotationNameList)) {
            return false;
        }

        return isMatchConstructorAnnotation(decl, constructorList);
    }

    private boolean isMatchConstructorAnnotation(ASTFieldDeclaration decl,
                                                 List<ASTConstructorDeclaration> constructorList) {
        Set<String> parameterTypeNameSet = constructorList.stream()
                .filter(Objects::nonNull)
                .map(constructor -> constructor.findChildrenOfType(ASTFormalParameters.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(parameters -> parameters.findChildrenOfType(ASTFormalParameter.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(formalParameter -> formalParameter.findChildrenOfType(ASTType.class))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(ASTType::getTypeImage)
                .collect(Collectors.toSet());
        ASTType astType = decl.getFirstChildOfType(ASTType.class);
        if (parameterTypeNameSet.isEmpty()
                || Objects.isNull(astType)
                || StringUtils.isEmpty(astType.getTypeImage())) {
            return false;
        }

        return parameterTypeNameSet.contains(astType.getTypeImage());

    }


    private boolean isUnCheckAnnotation(ASTFieldDeclaration decl) {
        List<String> annotationNameList = Optional.ofNullable(decl.getParent())
                .map(parent -> parent.findChildrenOfType(ASTAnnotation.class))
                .orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .filter(astAnnotation -> StringUtils.isNotEmpty(astAnnotation.getAnnotationName()))
                .map(ASTAnnotation::getAnnotationName)
                .collect(Collectors.toList());

        return isMatchUnCheckAnnotation(annotationNameList);
    }

    private boolean isMatchUnCheckAnnotation(List<String> annotationNameList) {
        return Optional.ofNullable(annotationNameList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(StringUtils::isNotEmpty)
                .anyMatch(UN_CHECK_FIELD_ANNOTATION::contains);
    }


    private boolean isSerialVersionUID(ASTFieldDeclaration field) {
        return "serialVersionUID".equals(field.getVariableName())
                && field.isStatic()
                && field.isFinal()
                && field.getType() == long.class;
    }

    /**
     * Whether the given field is a serialPersistentFields variable.
     * <p/>
     * This field must be initialized with an array of ObjectStreamField objects.
     * The modifiers for the field are required to be private, static, and final.
     *
     * @param field the field, must not be null
     * @return true if the field is a serialPersistentFields variable, otherwise false
     * @see
     * <a href="https://docs.oracle.com/javase/7/docs/platform/serialization/spec/serial-arch.html#6250">Oracle docs</a>
     */
    private boolean isSerialPersistentFields(final ASTFieldDeclaration field) {
        return "serialPersistentFields".equals(field.getVariableName())
                && field.isPrivate()
                && field.isStatic()
                && field.isFinal()
                && field.isArray()
                && "ObjectStreamField".equals(field.jjtGetFirstToken().getImage()); // .getType() returns null
    }

    @Override
    public Object visit(ASTEnumDeclaration decl, Object data) {
        checkCommentMeetsRequirement(data, decl, ENUM_CMT_REQUIREMENT_DESCRIPTOR);
        return super.visit(decl, data);
    }

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        assignCommentsToDeclarations(cUnit);
        return super.visit(cUnit, data);
    }

    private boolean allCommentsAreIgnored() {

        return getProperty(OVERRIDE_CMT_DESCRIPTOR) == CommentRequirement.Ignored
                && getProperty(ACCESSOR_CMT_DESCRIPTOR) == CommentRequirement.Ignored
                && (getProperty(CLASS_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored
                || getProperty(HEADER_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored)
                && getProperty(FIELD_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored
                && getProperty(PUB_METHOD_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored
                && getProperty(PROT_METHOD_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored
                && getProperty(ENUM_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored
                && getProperty(SERIAL_VERSION_UID_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored
                && getProperty(SERIAL_PERSISTENT_FIELDS_CMT_REQUIREMENT_DESCRIPTOR) == CommentRequirement.Ignored;
    }

    @Override
    public String dysfunctionReason() {
        return allCommentsAreIgnored() ? "All comment types are ignored" : null;
    }

    private enum CommentRequirement {
        /**
         *
         */
        Required("Required"),
        /**
         *
         */
        Ignored("Ignored"),
        /**
         *
         */
        Unwanted("Unwanted");

        private static final List<String> LABELS = buildValueLabels();
        private static final Map<String, CommentRequirement> MAPPINGS;
        private final String label;

        static {
            Map<String, CommentRequirement> tmp = new HashMap<>();
            for (CommentRequirement r : values()) {
                tmp.put(r.label, r);
            }
            MAPPINGS = Collections.unmodifiableMap(tmp);
        }

        CommentRequirement(String theLabel) {
            label = theLabel;
        }


        private static List<String> buildValueLabels() {
            List<String> labels = new ArrayList<>(values().length);
            for (CommentRequirement r : values()) {
                labels.add(r.label);
            }
            return Collections.unmodifiableList(labels);
        }


        public static List<String> labels() {
            return LABELS;
        }


        public static Map<String, CommentRequirement> mappings() {
            return MAPPINGS;
        }
    }


    private static PropertyBuilder.GenericPropertyBuilder<CommentRequirement>
    requirementPropertyBuilder(String name, String commentType) {
        DESCRIPTOR_NAME_TO_COMMENT_TYPE.put(name, commentType);
        return PropertyFactory.enumProperty(name,
                CommentRequirement.mappings())
                .desc(commentType + ". Possible values: " + CommentRequirement.labels())
                .defaultValue(CommentRequirement.Required);
    }

    private static final String COMMENT_REQUIRED_VIOLATION_MSG =
            "java.documentation.CommentRequiredRule.violation.msg";
}
