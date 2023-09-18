# FLIGHT-MOBILE-PMD

## <font color="green">Build requirements</font>
- JDK 1.8+
- Maven 3

## <font color="green">Use as dependency</font>

### <font color="green">Maven</font>
```xml
<dependency>
    <groupId>com.ctrip.flight.mobile.pmd</groupId>
    <artifactId>pmd-java</artifactId>
    <version>0.0.1</version>
</dependency>
```

## <font color="green">Rules</font>
Base on PMD Version 6.55.0：([https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_rules_java.html](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_rules_java.html))

## Catalog
- [BestPractices](#BestPractices)
    - [AbstractClassWithoutAbstractMethod](#AbstractClassWithoutAbstractMethod)
    - [AvoidReassigningCatchVariables](#AvoidReassigningCatchVariables)
    - [AvoidReassigningLoopVariablesRule](#AvoidReassigningLoopVariablesRule)
    - [AvoidReassigningParametersRule](#AvoidReassigningParametersRule)
    - [AvoidStringBufferFieldRule](#AvoidStringBufferFieldRule)
    - [ConstantInterfaceRule](#ConstantInterfaceRule)
    - [DefaultLabelNotLastInSwitchStmtRule](#DefaultLabelNotLastInSwitchStmtRule)
    - [DoubleBraceInitializationRule](#DoubleBraceInitializationRule)
    - [ForLoopVariableCountRule](#ForLoopVariableCountRule)
    - [JUnitTestsShouldIncludeAssertRule](#JUnitTestsShouldIncludeAssertRule)
    - [EqualsAvoidNullRule](#EqualsAvoidNullRule)
    - [MethodReturnsInternalArrayRule](#MethodReturnsInternalArrayRule)
    - [PrimitiveWrapperInstantiationRule](#PrimitiveWrapperInstantiationRule)
    - [UnusedFormalParameterRule](#UnusedFormalParameterRule)
    - [UnusedLocalVariableRule](#UnusedLocalVariableRule)
    - [UnusedPrivateFieldRule](#UnusedPrivateFieldRule)
    - [UnusedPrivateMethodRule](#UnusedPrivateMethodRule)
    - [UseCollectionIsEmptyRule](#UseCollectionIsEmptyRule)
    - [UseStandardCharsetsRule](#UseStandardCharsetsRule)
    - [UnusedAssignmentRule](#UnusedAssignmentRule)
    - [UseTryWithResourcesRule](#UseTryWithResourcesRule)
- [CodeStyle](#CodeStyle)
    - [AvoidProtectedFieldInFinalClassRule](#AvoidProtectedFieldInFinalClassRule)
    - [AvoidProtectedMethodInFinalClassNotExtendingRule](#AvoidProtectedMethodInFinalClassNotExtendingRule)
    - [BooleanGetMethodNameRule](#BooleanGetMethodNameRule)
    - [ClassNamingConventionsRule](#ClassNamingConventionsRule)
    - [ControlStatementBracesRule](#ControlStatementBracesRule)
    - [EmptyControlStatementRule](#EmptyControlStatementRule)
    - [EmptyMethodInAbstractClassShouldBeAbstractRule](#EmptyMethodInAbstractClassShouldBeAbstractRule)
    - [FieldNamingConventionsRule](#FieldNamingConventionsRule)
    - [FormalParameterNamingConventionsRule](#FormalParameterNamingConventionsRule)
    - [GenericsNamingRule](#GenericsNamingRule)
    - [LinguisticNamingRule](#LinguisticNamingRule)
    - [LocalVariableCouldBeFinalRule](#LocalVariableCouldBeFinalRule)
    - [LocalVariableNamingConventionsRule](#LocalVariableNamingConventionsRule)
    - [LongVariableRule](#LongVariableRule)
    - [MethodNamingConventionsRule](#MethodNamingConventionsRule)
    - [PackageCaseRule](#PackageCaseRule)
    - [PrematureDeclarationRule](#PrematureDeclarationRule)
    - [UnnecessaryCastRule](#UnnecessaryCastRule)
    - [UnnecessaryLocalBeforeReturnRule](#UnnecessaryLocalBeforeReturnRule)
    - [UnnecessaryConstructorRule](#UnnecessaryConstructorRule)
    - [UnnecessaryModifierRule](#UnnecessaryModifierRule)
- [Design](#Design)
    - [AvoidDeeplyNestedIfStmtsRule](#AvoidDeeplyNestedIfStmtsRule)
    - [AvoidRethrowingExceptionRule](#AvoidRethrowingExceptionRule)
    - [AvoidThrowingNewInstanceOfSameExceptionRule](#AvoidThrowingNewInstanceOfSameExceptionRule)
    - [AvoidThrowingNullPointerExceptionRule](#AvoidThrowingNullPointerExceptionRule)
    - [ClassWithOnlyPrivateConstructorsShouldBeFinalRule](#ClassWithOnlyPrivateConstructorsShouldBeFinalRule)
    - [CognitiveComplexityRule](#CognitiveComplexityRule)
    - [CollapsibleIfStatementsRule](#CollapsibleIfStatementsRule)
    - [CouplingBetweenObjectsRule](#CouplingBetweenObjectsRule)
    - [FinalFieldCouldBeStaticRule](#FinalFieldCouldBeStaticRule)
    - [ImmutableFieldRule](#ImmutableFieldRule)
    - [LogicInversionRule](#LogicInversionRule)
    - [MutableStaticStateRule](#MutableStaticStateRule)
    - [NcssCountRule](#NcssCountRule)
    - [NPathComplexityRule](#NPathComplexityRule)
    - [SimplifiedTernaryRule](#SimplifiedTernaryRule)
    - [SimplifyBooleanExpressionsRule](#SimplifyBooleanExpressionsRule)
    - [SimplifyBooleanReturnsRule](#SimplifyBooleanReturnsRule)
    - [SimplifyConditionalRule](#SimplifyConditionalRule)
    - [SingularFieldRule](#SingularFieldRule)
    - [SwitchDensityRule](#SwitchDensityRule)
    - [TooManyFieldsRule](#TooManyFieldsRule)
    - [TooManyMethodsRule](#TooManyMethodsRule)
    - [UselessOverridingMethodRule](#UselessOverridingMethodRule)
    - [UseObjectForClearerAPIRule](#UseObjectForClearerAPIRule)
    - [ExcessiveParameterListRule](#ExcessiveParameterListRule)
    - [ExcessivePublicCountRule](#ExcessivePublicCountRule)
- [Documentation](#Documentation)
    - [CommentContentRule](#CommentContentRule)
    - [CommentRequiredRule](#CommentRequiredRule)
- [ErrorProne](#ErrorProne)
    - [AssignmentInOperandRule](#AssignmentInOperandRule)
    - [AssignmentToNonFinalStaticRule](#AssignmentToNonFinalStaticRule)
    - [AvoidDecimalLiteralsInBigDecimalConstructorRule](#AvoidDecimalLiteralsInBigDecimalConstructorRule)
    - [AvoidDuplicateLiteralsRule](#AvoidDuplicateLiteralsRule)
    - [AvoidFieldNameMatchingMethodNameRule](#AvoidFieldNameMatchingMethodNameRule)
    - [AvoidFieldNameMatchingTypeNameRule](#AvoidFieldNameMatchingTypeNameRule)
    - [AvoidInstanceofChecksInCatchClauseRule](#AvoidInstanceofChecksInCatchClauseRule)
    - [AvoidLiteralsInIfConditionRule](#AvoidLiteralsInIfConditionRule)
    - [AvoidMultipleUnaryOperatorsRule](#AvoidMultipleUnaryOperatorsRule)
    - [ClassCastExceptionWithToArrayRule](#ClassCastExceptionWithToArrayRule)
    - [CloneMethodMustBePublicRule](#CloneMethodMustBePublicRule)
    - [CloneMethodReturnTypeMustMatchClassNameRule](#CloneMethodReturnTypeMustMatchClassNameRule)
    - [CompareObjectsWithEqualsRule](#CompareObjectsWithEqualsRule)
    - [DoNotThrowExceptionInFinallyRule](#DoNotThrowExceptionInFinallyRule)
    - [EmptyCatchBlockRule](#EmptyCatchBlockRule)
    - [ImplicitSwitchFallThroughRule](#ImplicitSwitchFallThroughRule)
    - [InstantiationToGetClassRule](#InstantiationToGetClassRule)
    - [MethodWithSameNameAsEnclosingClassRule](#MethodWithSameNameAsEnclosingClassRule)
    - [MissingSerialVersionUIDRule](#MissingSerialVersionUIDRule)
    - [NonSerializableClassRule](#NonSerializableClassRule)
    - [OverrideBothEqualsAndHashcodeRule](#OverrideBothEqualsAndHashcodeRule)
    - [ProperLoggerRule](#ProperLoggerRule)
    - [ReturnEmptyCollectionRatherThanNullRule](#ReturnEmptyCollectionRatherThanNullRule)
    - [ReturnFromFinallyBlockRule](#ReturnFromFinallyBlockRule)
    - [UnconditionalIfStatementRule](#UnconditionalIfStatementRule)
- [MultiThreading](#MultiThreading)
    - [AvoidThreadGroupRule](#AvoidThreadGroupRule)
    - [DoubleCheckedLockingRule](#DoubleCheckedLockingRule)
    - [UnSynchronizedStaticFormatterRule](#UnSynchronizedStaticFormatterRule)
- [Performance](#Performance)
    - [AddEmptyStringRule](#AddEmptyStringRule)
    - [AppendCharacterWithCharRule](#AppendCharacterWithCharRule)
    - [AvoidCalendarDateCreationRule](#AvoidCalendarDateCreationRule)
    - [AvoidFileStreamRule](#AvoidFileStreamRule)
    - [AvoidInstantiatingObjectsInLoopsRule](#AvoidInstantiatingObjectsInLoopsRule)
    - [BigIntegerInstantiationRule](#BigIntegerInstantiationRule)
    - [ConsecutiveAppendsShouldReuseRule](#ConsecutiveAppendsShouldReuseRule)
    - [ConsecutiveLiteralAppendsRule](#ConsecutiveLiteralAppendsRule)
    - [InefficientStringBufferingRule](#InefficientStringBufferingRule)
    - [InsufficientStringBufferDeclarationRule](#InsufficientStringBufferDeclarationRule)
    - [OptimizableToArrayCallRule](#OptimizableToArrayCallRule)
    - [RedundantFieldInitializerRule](#RedundantFieldInitializerRule)
    - [StringInstantiationRule](#StringInstantiationRule)
    - [StringToStringRule](#StringToStringRule)
    - [TooFewBranchesForASwitchStatementRule](#TooFewBranchesForASwitchStatementRule)
    - [UseIndexOfCharRule](#UseIndexOfCharRule)
    - [UseIOStreamsWithApacheCommonsFileItemRule](#UseIOStreamsWithApacheCommonsFileItemRule)
    - [UselessStringValueOfRule](#UselessStringValueOfRule)
    - [UseStringBufferForStringAppendsRule](#UseStringBufferForStringAppendsRule)
    - [UseStringBufferLengthRule](#UseStringBufferLengthRule)
- [Customization](#Customization)
    - [StreamExpressionStyleRule](#StreamExpressionStyleRule)
    - [ConditionalTooLongNeedChangeLineRule](#ConditionalTooLongNeedChangeLineRule)
    - [AvoidComplexConditionalRule](#AvoidComplexConditionalRule)
    - [StreamExpressionTooLongRule](#StreamExpressionTooLongRule)
    - [AvoidStreamExpressionInIfStmtsRule](#AvoidStreamExpressionInIfStmtsRule)
    - [UndefinedMagicConstantRule](#UndefinedMagicConstantRule)

# TODO
* 1 自定义规则：switch语句中避免使用if-else语句
* 2 自定义规则：if条件语句后避免紧跟return
* 3 自定义规则：Collectors.toMap未使用第三个参数

### <a name="BestPractices" color="green">BestPractices</a>
* 1 <a name="AbstractClassWithoutAbstractMethod"/> ``[AbstractClassWithoutAbstractMethod]`` The abstract class does not contain any abstract methods. 
An abstract class suggests an incomplete implementation, which is to be completed by subclasses implementing the abstract methods. 
If the class is intended to be used as a base class only (not to be instantiated directly) a protected constructor can be provided to prevent direct instantiation.

    correct example:
    
    ```java
    public abstract class Foo {
          int method1() { 
            // ... 
          }
          /**
          * xxx desc
          */
          protected abstract int method2();
      }
    ```
* 2 <a name="AvoidReassigningCatchVariables" /> ``[AvoidReassigningCatchVariables]`` Reassigning exception variables 
caught in a catch statement should be avoided because of:
    1) If it is needed, multi catch can be easily added and code will still compile.
    2) Following the principle of least surprise we want to make sure that a variable caught in a catch statement is always the one thrown in a try block.
    
    not recommend:
    
    ```java
    public class Foo {
        public void foo() {
            try {
                // do something
            } catch (Exception e) {
                e = new NullPointerException(); // not recommended
            }
        }
    }
    ```
* 3 <a name="AvoidReassigningLoopVariablesRule" /> ``[AvoidReassigningLoopVariablesRule]`` Reassigning loop variables can lead to hard-to-find bugs. 
Prevent or limit how these variables can be changed.

    not recommend:

    ```java
    public class Foo {
      public void foo() {
        for (int i = 0; i < 10; i++) {
              if (check(i)) {
                i++; // not recommend
              }
              doSomethingWith(i);
        }
      }
    }
    ```
* 4 <a name="AvoidReassigningParametersRule" /> ``[AvoidReassigningParametersRule]`` Reassigning values to incoming parameters of a method or constructor is not recommended, as this can make the code more difficult to understand. 
The code is often read with the assumption that parameter values don’t change and an assignment violates therefore the principle of least astonishment. 
This is especially a problem if the parameter is documented e.g. in the method’s javadoc and the new content differs from the original documented content.
Use temporary local variables instead. This allows you to assign a new name, which makes the code better understandable.

    ```java
    public class Hello {
      private void greet(String name) {
        // not recommend:
        name = name + "another";
        System.out.println("Hello " + name);
    
        // preferred
        String anotherName = name + "another";
        System.out.println("Hello " + anotherName);
      }
    }
    ```
* 5 <a name="AvoidStringBufferFieldRule" /> ``[AvoidStringBufferFieldRule]`` StringBuffers/StringBuilders can grow considerably, and so may become a source 
of memory leaks if held within objects with long lifetimes.

    not recommend:
    
    ```java
    public class Foo {
        private StringBuffer buffer;    // potential memory leak as an instance variable;
        public void foo(){
          buffer = new StringBuffer(16);
          buffer.append("append");
      }
    }
    ```
* 6 <a name="ConstantInterfaceRule" /> ``[ConstantInterfaceRule]`` Using constants in interfaces is a bad practice. 
Interfaces define types, constants are implementation details better placed in classes or enums.
See Effective Java’s ‘Use interfaces only to define types’.

    not recommend:
    
    ```java
    public interface ConstantInterface {
        public static final int CONST1 = 1; // violation, no fields allowed in interface!
        static final int CONST2 = 1;        // violation, no fields allowed in interface!
        final int CONST3 = 1;               // violation, no fields allowed in interface!
        int CONST4 = 1;                     // violation, no fields allowed in interface!
    }
    ```
* 7 <a name="DefaultLabelNotLastInSwitchStmtRule" /> ``[DefaultLabelNotLastInSwitchStmtRule]`` By convention, the default label should be the last label in a switch statement.

    not recommend:
    
    ```java
    public class Foo {
      void bar(int a) {
       switch (a) {
        case 1:  // do something
           break;
        default:  // the default case should be last, by convention
           break;
        case 2:
           break;
       }
      }
    }
    ```
* 8 <a name="DoubleBraceInitializationRule" /> ``[DoubleBraceInitializationRule]`` Double brace initialisation is a pattern to initialise eg collections concisely. 
But it implicitly generates a new .class file, and the object holds a strong reference to the enclosing object. 
For those reasons, it is preferable to initialize the object normally, even though it’s verbose.

    not recommend:
    
    ```java
    public class Foo {
      List<String> getList() {
       return new ArrayList<String>(){{
           add("a");
           add("b");
           add("c");
       }};
      }
    }
    ```
* 9 <a name="ForLoopVariableCountRule" /> ``[ForLoopVariableCountRule]`` Having a lot of control variables in a ‘for’ loop makes it harder to see what range of values the loop iterates over. 
By default this rule allows a regular ‘for’ loop with only one variable.

    not recommend:
    
    ```java
    public class Foo {
      void foo() {
       for (int i = 0, j = 0; i < 10; i++, j += 2) {
          foo1();
       }
      }
    }
    ```
* 10 <a name="JUnitTestsShouldIncludeAssertRule" /> ``[JUnitTestsShouldIncludeAssertRule]`` JUnit tests should include at least one assertion. 
This makes the tests more robust, and using assert with messages provide the developer a clearer idea of what the test does.

    not recommend:
    
    ```java
    public class FooTest {
       @Test
       public void testSomething() {
          Bar b = findBar();
          b.work();
       }
    }
    ```
* 11 <a name="EqualsAvoidNullRule" /> ``[EqualsAvoidNullRule]`` Put constant or valued objects first in all Object comparisons, avoiding NPE if the second argument is null.
You can also use Apache's StringUtils.equals/equalsIgnoreCase or Objects.equals methods.

    not recommend:
    
    ```java
    public class Foo {
        boolean bar(String x) {
            return x.equals("2"); // should be "2".equals(x)
        }
        boolean bar(String x) {
            return x.equalsIgnoreCase("2"); // should be "2".equalsIgnoreCase(x)
        }
        boolean bar(Integer x) {
            return x.equals(2); // should be: 2.equals(x)
        }
        boolean bar(Integer x) {
            return x.equalsIgnoreCase(2); // should be: 2.equalsIgnoreCase(x)
        }
    }
    ```
* 12 <a name="MethodReturnsInternalArrayRule" /> ``[MethodReturnsInternalArrayRule]`` Exposing internal arrays to the caller violates object encapsulation since 
elements can be removed or replaced outside of the object that owns it. It is safer to return a copy of the array.

    ```java
    public class SecureSystem {
        UserData [] ud;
        public UserData [] getUserData() {
            // Don't return directly the internal array, return a copy
            return ud;
            // suggest return this
            return Arrays.copyOf(ud, ud.length);
        }
    }
    ```
* 13 <a name="PrimitiveWrapperInstantiationRule" /> ``[PrimitiveWrapperInstantiationRule]`` usages of primitive wrapper constructors. They are deprecated since Java 9 and should not be used. 
Even before Java 9, they can be replaced with usage of the corresponding static valueOf factory method (which may be automatically inserted by the compiler since Java 1.5). 
This has the advantage that it may reuse common instances instead of creating a new instance each time.

    ```java
    public class Foo {
        private Integer ZERO = new Integer(0);      // violation
        private Integer ZERO1 = Integer.valueOf(0); // better
        private Integer ZERO1 = 0;                  // even better
    }
    ```
* 14 <a name="UnusedFormalParameterRule" /> ``[UnusedFormalParameterRule]`` parameters of methods and constructors that are not referenced them in the method body.
Parameters whose name starts with `ignored` or `unused` are filtered out.

    ```java
    public class Foo {
        private void bar(String howdy) {
            // howdy is not used
        }
    }
    ```
* 15 <a name="UnusedLocalVariableRule" /> ``[UnusedLocalVariableRule]`` Detects when a local variable is declared and/or assigned, but not used. 
Variables whose name starts with ignored or unused are filtered out.

    ```java
    public class Foo {
        public void doSomething() {
            int i = 5; // Unused
            // doSomething
        }
    }
    ```
* 16 <a name="UnusedPrivateFieldRule" /> ``[UnusedPrivateFieldRule]`` Detects when a private field is declared and/or assigned a value, but not used.

    ```java
    public class Something {
        private static int FOO = 2; // Unused
        private int i = 5; // Unused
        private int j = 6;
        public int addOne() {
            return j++;
        }
    }
    ```
* 17 <a name="UnusedPrivateMethodRule" /> ``[UnusedPrivateMethodRule]`` Unused Private Method detects when a private method is declared but is unused.

    ```java
    public class Something {
        private void foo() {} // unused
    }
    ```
* 18 <a name="UseCollectionIsEmptyRule" /> ``[UseCollectionIsEmptyRule]`` The isEmpty() method on java.util.Collection is provided to determine if a collection has any elements. 
Comparing the value of size() to 0 does not convey intent as well as the isEmpty() method.

    ```java
    public class Foo {
        void good() {
            List foo = getList();
            if (foo.isEmpty()) {
                // doSomething
            }
        }
    
        void bad() {
            List foo = getList();
            if (foo.size() == 0) {
                // doSomething
            }
        }
    }
    ```
* 19 <a name="UseStandardCharsetsRule" /> ``[UseStandardCharsetsRule]`` StandardCharsets provides constants for common Charset objects, such as UTF-8. 
Using the constants is less error prone, and can provide a small performance advantage compared to Charset.forName(...) since no scan across the internal Charset caches is needed.

    ```java
    public class UseStandardCharsets {
        public void run() {  
            // looking up the charset dynamically
            try (OutputStreamWriter osw = new OutputStreamWriter(out, Charset.forName("UTF-8"))) {
                osw.write("test");
            }
    
            // best to use StandardCharsets
            try (OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
                osw.write("test");
            }
        }
    }
    ```
* 20 <a name="UnusedAssignmentRule" /> ``[UnusedAssignmentRule]`` The variable is never read after the assignment, or The assigned value is always overwritten by other assignments before the next read of the variable.
The rule doesn't consider assignments to fields except for those of `this` in a constructor,or static fields of the current class in static initializers.

    ```java
    class A {
        // this field initializer is redundant,
        // it is always overwritten in the constructor
        int f = 1;
    
        A(int f) {
           this.f = f;
        }
    }
    ```
* 21 <a name="UseTryWithResourcesRule" /> ``[UseTryWithResourcesRule]`` try-with-resources statement. This statement ensures that each resource is closed at the end of the statement. 
It avoids the need of explicitly closing the resources in a finally block. Additionally exceptions are better handled: If an exception occurred both in the try block and finally block, then the exception from the try block was suppressed. 
With the try-with-resources statement, the exception thrown from the try-block is preserved.

    ```java
    public class TryWithResources {
        public void run() {
            // not recommend
            InputStream in = null;
            try {
                in = openInputStream();
                int i = in.read();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                      in.close();
                    }
                } catch (IOException ignored) {
                    // ignored
                }
            }
    
            // better use try-with-resources
            try (InputStream in2 = openInputStream()) {
                int i = in2.read();
            }
        }
    }
    ```

### <a name="CodeStyle" color="green">CodeStyle</a>
* 1 <a name="AvoidProtectedFieldInFinalClassRule" /> ``[AvoidProtectedFieldInFinalClassRule]`` Do not use protected 
fields in final classes since they cannot be subclassed. Clarify your intent by using private or package access modifiers instead.

    ```java
    public final class Bar {
       private int x;
       protected int y;  // bar cannot be subclassed, so is y really private or package visible?
       Bar() {}
    }
    ```
* 2 <a name="AvoidProtectedMethodInFinalClassNotExtendingRule" /> ``[AvoidProtectedMethodInFinalClassNotExtendingRule]`` Do not use protected methods in most final classes since they cannot be subclassed. 
This should only be allowed in final classes that extend other classes with protected methods (whose visibility cannot be reduced). Clarify your intent by using private or package access modifiers instead.

    ```java
    public final class Foo {
      private int bar() {}
      protected int baz() {} // Foo cannot be subclassed, and doesn't extend anything, so is baz() really private or package visible?
    }
    ```
* 3 <a name="BooleanGetMethodNameRule" /> ``[BooleanGetMethodNameRule]`` Methods that return boolean results should be named as predicate statements to denote this. 
I.e, ‘isReady()’, ‘hasValues()’, ‘canCommit()’, ‘willFail()’, etc. Avoid the use of the ‘get’ prefix for these methods.

    ```java
    public class Foo {
      public boolean getFoo();            // bad
      public boolean isFoo();             // ok
    }
    ```
* 4 <a name="ClassNamingConventionsRule" /> ``[ClassNamingConventionsRule]`` The class name uses the UpperCamelCase style and must follow the camel case(but the following exceptions: domain model related naming DO / BO / DTO / VO / DAO).
the abstract class must start with Abstract or Base,.and the unit test class must start with Test or end with Test / Tests / TestCase.

    ```java
    // This is Pascal case, the recommended naming convention in Java
    // Note that the default values of this rule don't allow underscores
    // or accented characters in type names
    public class FooBar {}
    
    // You may want abstract classes to be named 'AbstractXXX',
    // in which case you can customize the regex for abstract
    // classes to '^(Abstract|Base)+[A-Z][a-zA-Z0-9]*'
    public abstract class Thing {}
    
    // This class doesn't respect the convention, and will be flagged
    public class Éléphant {}
    ```
* 5 <a name="ControlStatementBracesRule" /> ``[ControlStatementBracesRule]`` Enforce a policy for braces on control statements. 
It is recommended to use braces on ‘if … else’ statements and loop statements, even contains only one statement. 
This usually makes the code clearer, and helps prepare the future when you need to add another statement. 

    ```java
    public class FooBar {
      public void foo() {
        while (true)    // not recommended
          x++;
        
        while (true) {  // preferred approach
          x++;
        }
      }
    }
    ```
* 6 <a name="EmptyControlStatementRule" /> ``[EmptyControlStatementRule]`` control statements whose body is empty, as well as empty initializers.

    ```java
    public class Foo {
        public void foo() {
            if (true); // empty if statement
            if (true) { // empty as well
            }
        }
    }
    ```
* 7 <a name="EmptyMethodInAbstractClassShouldBeAbstractRule" /> ``[EmptyMethodInAbstractClassShouldBeAbstractRule]`` Empty or auto-generated methods in an abstract class should be tagged as abstract. 
This helps to remove their inapproprate usage by developers who should be implementing their own versions in the concrete subclasses.

    ```java
    public abstract class ShouldBeAbstract {
        public Object couldBeAbstract() {
            // Should be abstract method ?
            return null;
        }
    
        public void couldBeAbstract() {
        }
    }
    ```
* 8 <a name="FieldNamingConventionsRule" /> ``[FieldNamingConventionsRule]`` field naming convention ALL_UPPER style.

    ```java
    public class Foo {
         int MY_FIELD = 1; // This is ok
         int my_Field = 1; // it's not ok 
         final int FINAL_FIELD = 1; // This is ok
         enum AnEnum {
              ORG, NET, COM; // These use a separate property but are set to ALL_UPPER by default
          }
    }
    ```
* 9 <a name="FormalParameterNamingConventionsRule" /> ``[FormalParameterNamingConventionsRule]`` formal parameter convention CamelCase style.

    ```java
    class Foo {
      abstract void bar(int myInt); // This is Camel case, so it's ok
        void bar(int my_i) { // it's not ok 
        
        }
        
        void lambdas() {
        
          // it's not ok 
          Consumer<String> lambda1 = s_str -> { };
        
          // it's ok 
          Consumer<String> lambda1 = (String str) -> { };
        
         }
    
      }
    ```
* 10 <a name="GenericsNamingRule" /> ``[GenericsNamingRule]`` generics values should be limited to a single uppercase letter.

    ```java
    public interface GenericDao<E extends BaseModel, K extends Serializable> extends BaseDao {
        // This is ok...
    }
    
    public interface GenericDao<E extends BaseModel, K extends Serializable> {
        // Also this
    }
    
    public interface GenericDao<e extends BaseModel, K extends Serializable> {
        // 'e' should be an 'E'
    }
    
    public interface GenericDao<EF extends BaseModel, K extends Serializable> {
       // 'EF' is not ok.
    }
    ```
* 11 <a name="LinguisticNamingRule" /> ``[LinguisticNamingRule]`` This rule finds Linguistic Naming Antipatterns. It checks for fields, that are named, as if they should be boolean but have a different type. 
It also checks for methods, that according to their name, should return a boolean, but don’t. Further, it checks, that getters return something and setters won’t. 

    ```java
    public class LinguisticNaming {
         int isValid;    // the field name indicates a boolean, but it is an int.
         boolean isTrue; // correct type of the field
     
         void myMethod() {
             int hasMoneyLocal;      // the local variable name indicates a boolean, but it is an int.
             boolean hasSalaryLocal; // correct naming and type
         }
     
         // the name of the method indicates, it is a boolean, but the method returns an int.
         int isValid() {
             return 1;
         }
         // correct naming and return type
         boolean isSmall() {
             return true;
         }
     
         // the name indicates, this is a setter, but it returns something
         int setName() {
             return 1;
         }
     
         // the name indicates, this is a getter, but it doesn't return anything
         void getName() {
             // nothing to return?
         }
     
         // the name indicates, it transforms an object and should return the result
         void toDataType() {
             // nothing to return?
         }
         // the name indicates, it transforms an object and should return the result
         void grapeToWine() {
             // nothing to return?
         }
     }
    ```
* 12 <a name="LocalVariableCouldBeFinalRule" /> ``[LocalVariableCouldBeFinalRule]`` A local variable assigned only once can be declared final.

    ```java
    public class Bar {
        public void foo () {
            String txtA = "a";          // if txtA will not be assigned again it is better to do this:
            final String txtB = "b";
        }
    }
    ```
* 13 <a name="LocalVariableNamingConventionsRule" /> ``[LocalVariableNamingConventionsRule]`` By default local variable uses the standard Java naming convention (Camel case).

    ```java
    public class Foo {
        void bar() {
             int localVariable = 1; // This is in camel case, so it's ok
             int local_variable = 1; // This will be reported unless you change the regex
           
             final int i_var = 1; // This will be reported unless you change the regex
           
             try {
                 foo();
             } catch (IllegalArgumentException e_illegal) {
                 // This will be reported unless you change the regex
             }
           
        }
    }
    ```
* 14 <a name="LongVariableRule" /> ``[LongVariableRule]`` Fields, formal arguments, or local variable names that are too long can make the code difficult to follow.

    ```java
    public class Something {
        int reallyLongIntName = -3;             // OK - Field
        public static void main(String argumentsList[]) { // OK - Formal
            int otherReallyLongName = -5;       // OK - Local
            for (int interestingIntIndex = 0;   // OK - For
                 interestingIntIndex < 10;
                 interestingIntIndex ++ ) {
            }
            int thisVariableFromAnotherParameter = 0; // VIOLATION - Local above 30 length(default is 17,now set 30)
        }
    }
    ```
* 15 <a name="MethodNamingConventionsRule" /> ``[MethodNamingConventionsRule]`` By default method uses the standard Java naming convention(Camel case).

    ```java
    public class Foo {
        public void fooStuff() { // it's ok
        }
      
        public void FooStuff() { // violation the first literal needs to be lowercase
          
        }
    }
    ```
* 16 <a name="PackageCaseRule" /> ``[PackageCaseRule]`` Detects when a package definition contains uppercase characters.

    ```java
    package com.MyCompany;  // should be lowercase name
    
    public class SomeClass {
    }
    ```
* 17 <a name="PrematureDeclarationRule" /> ``[PrematureDeclarationRule]`` Checks for variables that are defined before they might be used. 
A reference is deemed to be premature if it is created right before a block of code that doesn’t use it that also has the ability to return or throw an exception.

    ```java
    public class Foo {
    public int getLength(String[] strings) {  
        int length = 0; // declared prematurely
    
        if (strings == null || strings.length == 0) {
          return 0;
        }
    
        for (String str : strings) {
            length += str.length();
        }
    
        return length;
      }
    }
    ```
* 18 <a name="UnnecessaryCastRule" /> ``[UnnecessaryCastRule]`` This rule detects when a cast is unnecessary while accessing collection elements. 

    ```java
    public class UnnecessaryCastSample {
        public void method() {
            List<String> stringList = Arrays.asList("a", "b");
            String element = (String) stringList.get(0); // this cast is unnecessary
            String element2 = stringList.get(0);
        }
    }
    ```
* 19 <a name="UnnecessaryLocalBeforeReturnRule" /> ``[UnnecessaryLocalBeforeReturnRule]`` Avoid the creation of unnecessary local variables.

    ```java
    public class Foo {
       public int foo() {
         int x = doSomething();
         return x;  // instead, just 'return doSomething();'
       }
    }
    ```
* 20 <a name="UnnecessaryConstructorRule" /> ``[UnnecessaryConstructorRule]`` This rule detects when a constructor is not necessary; 
i.e., when there is only one constructor and the constructor is identical to the default constructor. 
The default constructor should has same access modifier as the declaring class.

    ```java
    public class Foo {
      public Foo() {} // UnnecessaryConstructor
    }
    ```
* 21 <a name="UnnecessaryModifierRule" /> ``[UnnecessaryModifierRule]`` Fields in interfaces and annotations are automatically `public static final`, and methods are `public abstract`. 
Classes, interfaces or annotations nested in an interface or annotation are automatically `public static`(all nested interfaces and annotations are automatically static). 
Nested enums are automatically `static`.For historical reasons, modifiers which are implied by the context are accepted by the compiler, but are superfluous.

    ```java
    public @interface Annotation {
        public abstract void bar();     // both abstract and public are ignored by the compiler
        public static final int X = 0;  // public, static, and final all ignored
        public static class Bar {}      // public, static ignored
        public static interface Baz {}  // ditto
    }
    public interface Foo {
        public abstract void bar();     // both abstract and public are ignored by the compiler
        public static final int X = 0;  // public, static, and final all ignored
        public static class Bar {}      // public, static ignored
        public static interface Baz {}  // ditto
    }
    public class Bar {
        public static interface Baz {}  // static ignored
        public static enum FoorBar {    // static ignored
            FOO;
        }
    }
    public class FooClass {
        static record BarRecord() {}     // static ignored
    }
    public interface FooInterface {
        static record BarRecord() {}     // static ignored
    }
    ```
    
### <a name="Design" color="green">Design</a>
* 1 <a name="AvoidDeeplyNestedIfStmtsRule" /> ``[AvoidDeeplyNestedIfStmtsRule]`` Avoid creating deeply nested if-then statements since they are harder to read and error-prone to maintain.

    ```java
    public class Foo {
      public void bar(int x, int y, int z) {
        if (x > y) {
          if (y > z) {
            if (z == x) { 
             // !! too deep default is 3
            }
          }
        }
      }
    }
    ```
* 2 <a name="AvoidRethrowingExceptionRule" /> ``[AvoidRethrowingExceptionRule]`` Catch blocks that merely rethrow a caught exception only add to code size and runtime complexity.

    ```java
    public class Foo {
      public void bar() {
        try {
            // do something
        }  catch (SomeException se) {
           throw se;
        }
      }
    }
    ```
* 3 <a name="AvoidThrowingNewInstanceOfSameExceptionRule" /> ``[AvoidThrowingNewInstanceOfSameExceptionRule]`` Catch blocks that merely rethrow a caught exception wrapped inside a new instance of the same type only add to code size and runtime complexity.

    ```java
    public class Foo {
      public void bar() {
          try {
              // do something
          } catch (SomeException se) {
              // harmless comment
              throw new SomeException(se);
          }
      }
    }
    ```
* 4 <a name="AvoidThrowingNullPointerExceptionRule" /> ``[AvoidThrowingNullPointerExceptionRule]`` Avoid throwing NullPointerExceptions manually. 
These are confusing because most people will assume that the virtual machine threw it. 
To avoid a method being called with a null parameter, you may consider using an IllegalArgumentException instead, 
making it clearly seen as a programmer-initiated exception.

    ```java
    public class Foo {
        void bar() {
            throw new NullPointerException();
        }
    }
    ```
* 5 <a name="ClassWithOnlyPrivateConstructorsShouldBeFinalRule" /> ``[ClassWithOnlyPrivateConstructorsShouldBeFinalRule]`` A class with only private constructors should be final, unless the private constructor is invoked by an inner class.

    ```java
    public class Foo {  //Should be final
        private Foo() { }
    }
    ```
* 6 <a name="CognitiveComplexityRule" /> ``[CognitiveComplexityRule]`` Methods that are highly complex are difficult to read and more costly to maintain. 
If you include too much decisional logic within a single method, you make its behavior hard to understand and more difficult to modify.
Information about Cognitive complexity can be found in the originalpaper here: [https://www.sonarsource.com/docs/CognitiveComplexity.pdf](https://www.sonarsource.com/docs/CognitiveComplexity.pdf)
default report level is 15

    ```java
    public class Foo {
      // Has a cognitive complexity of 0
      public void createAccount() {
        Account account = new Account("PMD");
        // save account
      }
    
      // Has a cognitive complexity of 1
      public Boolean setPhoneNumberIfNotExisting(Account a, String phone) {
        if (a.phone == null) {                          // +1
          a.phone = phone;
          return true;
        }
    
        return false;
      }
    
      // Has a cognitive complexity of 4
      public void updateContacts(List<Contact> contacts) {
        List<Contact> contactsToUpdate = new ArrayList<Contact>();
    
        for (Contact contact : contacts) {                           // +1
          if (contact.department.equals("Finance")) {                // +2 (nesting = 1)
            contact.title = "Finance Specialist";
            contactsToUpdate.add(contact);
          } else if (contact.department.equals("Sales")) {           // +1
            contact.title = "Sales Specialist";
            contactsToUpdate.add(contact);
          }
        }
        // save contacts
      }
    }
    ```
* 7 <a name="CollapsibleIfStatementsRule" /> ``[CollapsibleIfStatementsRule]`` Sometimes two consecutive ‘if’ statements can be consolidated by separating their conditions with a boolean short-circuit operator.

    ```java
    public class Foo {
        void bar() {
            if (x) {            // original implementation
                if (y) {
                    // do stuff
                }
            }
        }
        
        void bar() {
            if (x && y) {        // optimized implementation
                // do stuff
            }
        }
    }
    ```
* 8 <a name="CouplingBetweenObjectsRule" /> ``[CouplingBetweenObjectsRule]`` counts the number of unique attributes, local variables, and return types within an object. 
A number higher than the specified threshold can indicate a high degree of coupling.default threshold is 20.

    ```java
    import com.Blah;
    import org.Bar;
    import org.Bardo;
    
    public class Foo {
        private Blah var1;
        private Bar var2;
    
        //followed by many imports of unique objects
        ObjectC doWork() {
            Bardo var55;
            ObjectA var44;
            ObjectZ var93;
            return something();
        }
    }
    ```
* 9 <a name="FinalFieldCouldBeStaticRule" /> ``[FinalFieldCouldBeStaticRule]`` If a final field is assigned to a compile-time constant, it could be made static, thus saving overhead in each object at runtime.

    ```java
    public class Foo {
      public final int BAR = 42; // this could be static and save some space
    }
    ```
* 10 <a name="ImmutableFieldRule" /> ``[ImmutableFieldRule]`` Identifies private fields whose values never change once object initialization ends either in the declaration of the field or by a constructor. 
This helps in converting existing classes to becoming immutable ones. Note that this rule does not enforce referenced object to be immutable itself. 
A class can still be mutable, even if all its member fields are declared final. 
This is referred to as shallow immutability. For more information on mutability, see Effective Java, 3rd Edition, Item 17: Minimize mutability.

    ```java
    public class Foo {
      private int x; // could be final
      public Foo() {
          x = 7;
      }
      public void foo() {
         int a = x + 2;
      }
    }
    ```
* 11 <a name="LogicInversionRule" /> ``[LogicInversionRule]`` Use opposite operator instead of negating the whole expression with a logic complement operator.

    ```java
    public class Foo {
      public boolean bar(int a, int b) {
          if (!(a == b)) { // use !=
               return false;
           }
      
          if (!(a < b)) { // use >=
               return false;
          }
      
          return true;
      }
    }
    ```
* 12 <a name="MutableStaticStateRule" /> ``[MutableStaticStateRule]`` Non-private non-final static fields break encapsulation and can lead to hard to find bugs, since these fields can be modified from anywhere within the program. 
Callers can trivially access and modify non-private non-final static fields. Neither accesses nor modifications can be guarded against, and newly set values cannot be validated.

    ```java
    public class Greeter { 
      public static Foo foo = new Foo();  // avoid this
      // ... 
    }      
    public class Greeter { 
      public static final Foo FOO = new Foo(); // use this instead
      // ... 
    } 
    ```
* 13 <a name="NcssCountRule" /> ``[NcssCountRule]`` This rule uses the NCSS (Non-Commenting Source Statements) metric to determine the number of lines of code in a class, method or constructor. 
If the number of lines of code of a class, method or constructor exceeds the specified threshold, it is generally considered that the class, method or constructor needs to be refactored to make the logic of the code clearer and more reasonable.
NCSS ignores comments, blank lines, and only counts actual statements. For more details on the calculation, see the documentation of the：
[https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index\.html#non-commenting-source-statements-ncss](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index\.html#non-commenting-source-statements-ncss)
default method report lines is 80,default class report lines is 1500.

    ```java
    import java.util.Collections;       // +0
    import java.io.IOException;         // +0
    
    class Foo {                         // +1, total Ncss = 12
    
      public void bigMethod()           // +1
          throws IOException {
        int x = 0, y = 2;               // +1
        boolean a = false, b = true;    // +1
    
        if (a || b) {                   // +1
          try {                         // +1
            do {                        // +1
              x += 2;                   // +1
            } while (x < 12);
    
            System.exit(0);             // +1
          } catch (IOException ioe) {   // +1
            throw new PatheticFailException(ioe); // +1
          }
        } else {
          assert false;                 // +1
        }
      }
    } 
    ```
* 14 <a name="NPathComplexityRule" /> ``[NPathComplexityRule]`` The NPath complexity of a method is the number of acyclic execution paths through that method. 
NPath counts the number of full paths from the beginning to the end of the block of the method. That metric grows exponentially, as it multiplies the complexity of statements in the same block.
For more details on the calculation, see the documentation of the：
[https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index.html#npath-complexity-npath](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index.html#npath-complexity-npath)
default report level is 200.

    ```java
    public class Foo {
      public static void bar() { // Ncss = 252: reported!
        boolean a, b = true;
        try { // 2 * 2 + 2 = 6
          if (true) { // 2
            List buz = new ArrayList();
          }
    
          for(int i = 0; i < 19; i++) { // * 2
            List buz = new ArrayList();
          }
        } catch(Exception e) {
          if (true) { // 2
            e.printStackTrace();
          }
        }
    
        while (j++ < 20) { //  * 2
          List buz = new ArrayList();
        }
    
        switch(j) { // * 7
          case 1:
          case 2: break;
          case 3: j = 5; break;
          case 4: if (b && a) { bar(); } break;
          default: break;
        }
    
        do { // * 3
            List buz = new ArrayList();
        } while (a && j++ < 30);
      }
    } 
    ```
* 15 <a name="SimplifiedTernaryRule" /> ``[SimplifiedTernaryRule]`` condition ? true : expr simplifies to condition || expr.
condition ? false : expr simplifies to !condition && expr.
condition ? expr : true simplifies to !condition || expr.
condition ? expr : false simplifies to condition && expr.

    ```java
    public class Foo {
        public boolean test() {
            return condition ? true : something(); // can be as simple as return condition || something();
        }
    
        public void test2() {
            final boolean value = condition ? false : something(); // can be as simple as value = !condition && something();
        }
    
        public boolean test3() {
            return condition ? something() : true; // can be as simple as return !condition || something();
        }
    
        public void test4() {
            final boolean otherValue = condition ? something() : false; // can be as simple as condition && something();
        }
    
        public boolean test5() {
            return condition ? true : false; // can be as simple as return condition;
        }
    }
    ```
* 16 <a name="SimplifyBooleanExpressionsRule" /> ``[SimplifyBooleanExpressionsRule]`` Avoid unnecessary comparisons in boolean expressions, they serve no purpose and impacts readability.

    ```java
    public class Bar {
      // can be simplified to
      // bar = isFoo();
      private boolean bar = (isFoo() == true);
    
      public isFoo() { return false;}
    } 
    ```
* 17 <a name="SimplifyBooleanReturnsRule" /> ``[SimplifyBooleanReturnsRule]`` Avoid unnecessary if-then-else statements when returning a boolean. The result of the conditional test can be returned instead.

    ```java
    public class Bar {
      public boolean isBarEqualTo(int x) {
          if (bar == x) {      // this bit of code...
              return true;
          } else {
              return false;
          }
      }
      
      public boolean isBarEqualTo(int x) {
          return bar == x;    // can be replaced with this
      }
    } 
    ```
* 18 <a name="SimplifyConditionalRule" /> ``[SimplifyConditionalRule]`` No need to check for null before an instanceof; the instanceof keyword returns false when given a null argument.

    ```java
    public class Foo {
       void bar(Object x) {
         if (x != null && x instanceof Bar) {
           // just drop the "x != null" check
         }
       }
    }
    ```
* 19 <a name="SingularFieldRule" /> ``[SingularFieldRule]`` Fields whose scopes are limited to just single methods do not rely on the containing object to provide them to other methods. 
They may be better implemented as local variables within those methods.

    ```java
    public class Foo {
        private int x;  // no reason to exist at the Foo instance level
        public void foo(int y) {
         x = y + 5;
         return x;
        }
    }
    ```
* 20 <a name="SwitchDensityRule" /> ``[SwitchDensityRule]`` A high ratio of statements to labels in a switch statement implies that the switch statement is overloaded.
Consider moving the statements into new methods or creating subclasses based on the switch variable.defalut report 
level is 10.

    ```java
    public class Foo {
      public void bar(int x) {
        switch (x) {
          case 1: {
            // lots of statements
            break;
          } case 2: {
            // lots of statements
            break;
          }
        }
      }
    }
    ```
* 21 <a name="TooManyFieldsRule" /> ``[TooManyFieldsRule]`` Classes that have too many fields can become unwieldy and could be redesigned to have fewer fields, possibly through grouping related fields in new objects.
now report level is 30.

    ```java
    public class Person {   // too many separate fields
       int birthYear;
       int birthMonth;
       int birthDate;
       float height;
       float weight;
    }
    
    public class Person {   // this is more manageable
       Date birthDate;
       BodyMeasurements measurements;
    }
    ```
* 22 <a name="TooManyMethodsRule" /> ``[TooManyMethodsRule]`` A class with too many methods is probably a good suspect for refactoring, in 
                              order to reduce its complexity and find a way to have more fine grained objects.
                              now report level is 20.
    ```java
    public class Person {
      public void method() { // +1
             
      }
         
      private void privateMethod() { // +1
                  
      }
    }
    ```
* 23 <a name="UselessOverridingMethodRule" /> ``[UselessOverridingMethodRule]`` The overriding method merely calls the same method defined in a superclass.

    ```java
    public class Person extends Foo {
      public void foo(String bar) {
          super.foo(bar);      // why bother overriding?
      }
           
      public String foo() {
          return super.foo();  // why bother overriding?
      }
    }
    ```
* 24 <a name="UseObjectForClearerAPIRule" /> ``[UseObjectForClearerAPIRule]`` When you write a public method, you should be thinking in terms of an API. 
If your method is public, it means other class will use it, therefore, you want (or need) to offer a comprehensive and evolutive API. 
If you pass a lot of information as a simple series of Strings, you may think of using an Object to represent all those information. 
You’ll get a simpler API (such as doWork(Workload workload), 
rather than a tedious series of Strings) and more importantly, 
if you need at some point to pass extra data, you’ll be able to do so by simply modifying or extending Workload without any modification to your API.

    ```java
    public class MyClass {
        public void connect(String username,
            String pssd,
            String databaseName,
            String databaseAdress)
            // Instead of those parameters object
            // would ensure a cleaner API and permit
            // to add extra data transparently (no code change):
            // void connect(UserData data);
        {
    
        }
    
        // suggest
        public void connect(UserData userData){
            doSomething();
        }
    }
    ```
* 25 <a name="ExcessiveParameterListRule" /> ``[ExcessiveParameterListRule]`` Methods with numerous parameters are a challenge to maintain, 
especially if most of them share the same datatype. These situations usually denote the need for new objects to wrap the numerous parameters.
now report level is 10.

    ```java
    public class Foo {
        public void addPerson(// too many arguments liable to be mixed up
            int birthYear, int birthMonth, int birthDate, int height, int weight, int ssn) {
            // . . . .
        }
        
        public void addPerson(// preferred approach
            Date birthdate, BodyMeasurements measurements, int ssn) { 
            // . . . .
        }
    }
    ```
* 26 <a name="ExcessivePublicCountRule" /> ``[ExcessivePublicCountRule]`` Classes with large numbers of public methods and attributes require disproportionate testing efforts since combinational side effects grow rapidly and increase risk. 
Refactoring these classes into smaller ones not only increases testability and reliability.now report level is 45.

    ```java
    public class Foo {
        public String value;
        public Bar something;
        public Variable var;
        // [... more more public attributes ...]
    
        public void doWork() {}
        public void doMoreWork() {}
        public void doWorkAgain() {}
        // [... more more public methods ...]
    }
    ```
    
### <a name="Documentation" color="green">Documentation</a>
* 1 <a name="CommentContentRule" /> ``[CommentContentRule]`` Do not include discriminatory language in comments.now set discriminatory language is `idiot|jerk`.

    ```java
    public class Foo {
        //OMG, this is horrible, xxx is an idiot !!!
        public Variable var;
    }
    ```
* 2 <a name="CommentRequiredRule" /> ``[CommentRequiredRule]`` Public, protected methods and classes, fields, and enumerations need to include comments

    ```java
    public class Foo { // non comment
        public void doWork() {} // non comment
    }
    ```
    
### <a name="ErrorProne" color="green">ErrorProne</a>
* 1 <a name="AssignmentInOperandRule" /> ``[AssignmentInOperandRule]`` Avoid assignments in operands; this can make code more complicated and harder to read.

    ```java
    public class Foo {
        public void bar() {
            int x = 2;
            if ((x = getX()) == 3) {
              System.out.println("3!");
            }
        }
    }
    ```
* 2 <a name="AssignmentToNonFinalStaticRule" /> ``[AssignmentToNonFinalStaticRule]`` Identifies a possible unsafe usage of a static field.

    ```java
    public class StaticField {
       static int x;
       public FinalFields(int y) {
        x = y; // unsafe
       }
    }
    ```
* 3 <a name="AvoidDecimalLiteralsInBigDecimalConstructorRule" /> ``[AvoidDecimalLiteralsInBigDecimalConstructorRule]`` One might assume that the result of "new BigDecimal(0.1)" 
is exactly equal to 0.1, but it is actually equal to 0.1000000000000000055511151231257827021181583404541015625.This 
is because 0.1 cannot be represented exactly as a double (or as a binary fraction of any finite length). 

    ```java
    public class Foo {
       public Foo() {
        BigDecimal bd = new BigDecimal(1.123);       // loss of precision, this would trigger the rule
        
        BigDecimal bd = new BigDecimal("1.123");     // preferred approach
        
        BigDecimal bd = new BigDecimal(12);          // preferred approach, ok for integer values
       }
    }
    ```
* 4 <a name="AvoidDuplicateLiteralsRule" /> ``[AvoidDuplicateLiteralsRule]`` Code containing duplicate String literals can usually be improved by declaring 
the String as a constant field.default report level is 3.

    ```java
    public class Foo {
       private void bar() {
            buz("Howdy");
            buz("Howdy");
            buz("Howdy");
            buz("Howdy");
       }
       private void buz(String x) {}
    }
    ```
* 5 <a name="AvoidFieldNameMatchingMethodNameRule" /> ``[AvoidFieldNameMatchingMethodNameRule]`` It can be confusing to have a field name with the same name as a 
method. While this is permitted.

    ```java
    public class Foo {
        Object bar;
        // bar is data or an action or both?
        void bar() {
        }
    }
    ```
* 6 <a name="AvoidFieldNameMatchingTypeNameRule" /> ``[AvoidFieldNameMatchingTypeNameRule]`` It is somewhat confusing to have a field name matching the declaring type name.

    ```java
    public class Foo extends Bar {
        int foo;    // There is probably a better name that can be used
    }
    public interface Operation {
        int OPERATION = 1; // There is probably a better name that can be used
    }
    ```
* 7 <a name="AvoidInstanceofChecksInCatchClauseRule" /> ``[AvoidInstanceofChecksInCatchClauseRule]`` Each caught exception type should be handled in its own catch clause.

    ```java
    public class Foo {
      void foo() {
        try {// Avoid this
            // do something
        } catch (Exception ee) {
            if (ee instanceof IOException) {
                cleanup();
            }
        }
        
        try {// Prefer this:
            // do something
        } catch (IOException ee) {
            cleanup();
        }
      }
    }
    ```
* 8 <a name="AvoidLiteralsInIfConditionRule" /> ``[AvoidLiteralsInIfConditionRule]`` Avoid using hard-coded literals in conditional statements. By declaring them
 as static variables or private members with descriptive names maintainability is enhanced. 

    ```java
    public class Foo{
        private static final int MAX_NUMBER_OF_REQUESTS = 10;
        public void checkRequests() {
            if (i == 10) {                        // magic number, buried in a method
              doSomething();
            }
        
            if (i == MAX_NUMBER_OF_REQUESTS) {    // preferred approach
              doSomething();
            }
        
            if (aString.indexOf('.') != -2) {}     // magic number -2, by default ignored
            if (aString.indexOf('.') >= 0) { }     // alternative approach
        
            if (aDouble > 0.1) {}                  // magic number 0.1
            if (aDouble >= Double.MIN_VALUE) {}    // preferred approach
        
            // with rule property "ignoreExpressions" set to "false"
            if (i == pos + 5) {}  // violation: magic number 5 within an (additive) expression
            if (i == pos + SUFFIX_LENGTH) {} // preferred approach
        }
    }
    ```
* 9 <a name="AvoidMultipleUnaryOperatorsRule" /> ``[AvoidMultipleUnaryOperatorsRule]`` The use of multiple unary operators may be problematic, and/or confusing
.Ensure that the intended usage is not a bug, or consider simplifying the expression.

    ```java
    public class Foo{
        public void foo() {
            int i = - -1;
            int j = + - +1;
            int z = ~~2;
            boolean b = !!true;
            boolean c = !!!true;
            
            // These are better:
            int i = 1;
            int j = -1;
            int z = 2;
            boolean b = true;
            boolean c = false;
            
            // And these just make your brain hurt:
            int i = ~-2;
            int j = -~7;
        }
    }
    ```
* 10 <a name="ClassCastExceptionWithToArrayRule" /> ``[ClassCastExceptionWithToArrayRule]`` When deriving an array of a specific class from your Collection, one 
should provide an array of the same class as the parameter of the toArray() method. Doing otherwise you will will 
result in a ClassCastException.

    ```java
    public class Foo{
        public void foo() {
            Collection c = new ArrayList();
            Integer obj = new Integer(1);
            c.add(obj);
            
            // this would trigger the rule (and throw a ClassCastException if executed)
            Integer[] a = (Integer [])c.toArray();
            
            // this is fine and will not trigger the rule
            Integer[] b = (Integer [])c.toArray(new Integer[c.size()]);
        }
    }
    ```
* 11 <a name="CloneMethodMustBePublicRule" /> ``[CloneMethodMustBePublicRule]`` clone() method must be public if the class implements Cloneable.

    ```java
    public class Foo implements Cloneable {
        @Override
        protected Object clone() throws CloneNotSupportedException { // Violation, must be public
        }
    }
    
    public class Foo implements Cloneable {
        @Override
        protected Foo clone() { // Violation, must be public
        }
    }
    
    public class Foo implements Cloneable {
        @Override
        public Object clone() {} // Ok
    }
    ```
* 12 <a name="CloneMethodReturnTypeMustMatchClassNameRule" /> ``[CloneMethodReturnTypeMustMatchClassNameRule]`` The return type of the clone() method must be the class name 
when implements Cloneable.

    ```java
    public class Foo implements Cloneable {
        @Override
        protected Object clone() { // Violation, Object must be Foo
        }
    }
    
    public class Foo implements Cloneable {
        @Override
        public Foo clone() { //Ok
        }
    }
    ```
* 13 <a name="CompareObjectsWithEqualsRule" /> ``[CompareObjectsWithEqualsRule]`` Use equals() to compare object references，avoid use “==” to compare.

    ```java
    public class Foo {
      boolean bar(String a, String b) {
        return a == b; // avoid this
      }
    }
    ```
* 14 <a name="DoNotThrowExceptionInFinallyRule" /> ``[DoNotThrowExceptionInFinallyRule]`` Throwing exceptions within a 'finally' block is confusing since they may 
mask other exceptions or code defects.

    ```java
    public class Foo {
        public void bar() {
            try {
                // Here do some stuff
            } catch( Exception e) {
                // Handling the issue
            } finally {
                // is this really a good idea ?
                throw new Exception();
            }
        }
    }
    ```
* 15 <a name="EmptyCatchBlockRule" /> ``[EmptyCatchBlockRule]`` Avoid catch exceptions but do nothing. Can be configured through the 
allowExceptionNameRegex attribute to ignore the exception name, the default is ignored and expected.

    ```java
    public class Foo {
        public void doSomething() {
            try {
                FileInputStream fis = new FileInputStream("/tmp/bugger");
            } catch (IOException ioe) {
                // not good
            }
        }
    }
    ```
* 16 <a name="ImplicitSwitchFallThroughRule" /> ``[ImplicitSwitchFallThroughRule]`` Switch statements without break or return statements for each case option may
 indicate problematic behaviour.

    ```java
    public class Foo {
        public void bar(int status) {
            switch(status) {
              case CANCELLED:
                doCancelled();
                // break; hm, should this be commented out?
              case NEW:
                doNew();
                // is this really a fall-through?
              case REMOVED:
                doRemoved();
                // what happens if you add another case after this one?
              case OTHER: // empty case - this is interpreted as an intentional fall-through
              case ERROR:
                doErrorHandling();
                break;
            }
        }
    }
    ```
* 17 <a name="InstantiationToGetClassRule" /> ``[InstantiationToGetClassRule]`` Avoid instantiating an object just to call getClass() on it; use the .class 
public member instead.

    ```java
    public class Foo {
        public void bar() {
            // replace this
            Class c = new String().getClass();
            
            // with this:
            Class c = String.class;
        }
    }
    ```
* 18 <a name="MethodWithSameNameAsEnclosingClassRule" /> ``[MethodWithSameNameAsEnclosingClassRule]`` Non-constructor methods should not have the same name as the 
enclosing class.

    ```java
    public class MyClass {
    
        public MyClass() {}         // this is OK because it is a constructor
    
        public void MyClass() {}    // this is bad because it is a method
    }
    ```
* 19 <a name="MissingSerialVersionUIDRule" /> ``[MissingSerialVersionUIDRule]`` Serializable classes should provide a serialVersionUID field.The 
serialVersionUID field is also needed for abstract base classes. Each individual class in the inheritance chain needs
 an own serialVersionUID field.

    ```java
    public class Foo implements java.io.Serializable {
        String name;
        // Define serialization id to avoid serialization related bugs
        // i.e., public static final long serialVersionUID = 4328743;
    }
    ```
* 20 <a name="NonSerializableClassRule" /> ``[NonSerializableClassRule]`` If a class is marked as `Serializable`, then all fields need to be serializable 
as well. In order to exclude a field, it can be marked as transient. Static fields are not considered.

    ```java
    public class Buzz implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
    
        private transient int someFoo;          // good, it's transient
        private static int otherFoo;            // also OK, it's static
        private java.io.FileInputStream stream; // bad - FileInputStream is not serializable
    
        public void setStream(FileInputStream stream) {
            this.stream = stream;
        }
    
        public int getSomeFoo() {
              return this.someFoo;
        }
    }
    ```
* 21 <a name="OverrideBothEqualsAndHashcodeRule" /> ``[OverrideBothEqualsAndHashcodeRule]`` Override both public boolean Object.equals(Object other), and public int
 Object.hashCode(), or override neither.  Even if you are inheriting a hashCode() from a parent class, consider 
 implementing hashCode and explicitly delegating to your superclass.

    ```java
    public class Bar {        // poor, missing a hashcode() method
        public boolean equals(Object o) {
          // do some comparison
        }
    }
    
    public class Baz {        // poor, missing an equals() method
        public int hashCode() {
          // return some hash value
        }
    }
    
    public class Foo {        // perfect, both methods provided
        public boolean equals(Object other) {
          // do some comparison
        }
        public int hashCode() {
          // return some hash value
        }
    }
    ```
* 22 <a name="ProperLoggerRule" /> ``[ProperLoggerRule]`` A logger should normally be defined private static final and be associated with the 
correct class.`private final Log log;` is also allowed for rare cases where loggers need to be passed around,with 
the restriction that the logger needs to be passed into the constructor.

    ```java
    public class Foo {
    
        private static final Log LOG = LogFactory.getLog(Foo.class);    // proper way
    
        protected Log LOG = LogFactory.getLog(Testclass.class);         // wrong approach
    }
    ```
* 23 <a name="ReturnEmptyCollectionRatherThanNullRule" /> ``[ReturnEmptyCollectionRatherThanNullRule]`` For any method that returns an collection (such as an array, 
Collection or Map), it is better to return an empty one rather than a null reference. This removes the need for null 
checking all results and avoids inadvertent NullPointerExceptions.

    ```java
    public class Example {
        // Not a good idea...
        public int[] badBehavior() {
            // ...
            return null;
        }
    
        // Good behavior
        public String[] bonnePratique() {
            //...
            return new String[0];
        }
    }
    ```
* 24 <a name="ReturnFromFinallyBlockRule" /> ``[ReturnFromFinallyBlockRule]`` Avoid returning from a finally block, this can discard exceptions.

    ```java
    public class Bar {
        public String foo() {
            try {
                throw new Exception( "My Exception" );
            } catch (Exception e) {
                throw e;
            } finally {
                return "A. O. K."; // return not recommended here
            }
        }
    }
    ```
* 25 <a name="UnconditionalIfStatementRule" /> ``[UnconditionalIfStatementRule]`` Do not use `if` statements that are always true or always false.

    ```java
    public class Foo {
        public void close() {
            if (true) {        // fixed conditional, not recommended
                // ...
            }
        }
    }
    ```
    
### <a name="MultiThreading" color="green">MultiThreading</a>
* 1 <a name="AvoidThreadGroupRule" /> ``[AvoidThreadGroupRule]`` Avoid using java.lang.ThreadGroup; although it is intended to beused in a threaded environment it contains methods that are not thread-safe.

    ```java
    public class Bar {
        void buz() {
            ThreadGroup tg = new ThreadGroup("My threadgroup");
            tg = new ThreadGroup(tg, "my thread group");
            tg = Thread.currentThread().getThreadGroup();
            tg = System.getSecurityManager().getThreadGroup();
        }
    }
    ```
* 2 <a name="DoubleCheckedLockingRule" /> ``[DoubleCheckedLockingRule]`` Double Check locking in Java is not thread-safe, and the volatile keyword needs to be added to prohibit instruction rearrangement.

    ```java
    public class Foo {
        /*volatile */ Object baz = null; // fix for Java5 and later: volatile
        Object bar() {
            if (baz == null) { // baz may be non-null yet not fully created
                synchronized(this) {
                    if (baz == null) {
                        baz = new Object();
                    }
                  }
            }
            return baz;
        }
    }
    ```    
* 3 <a name="UnSynchronizedStaticFormatterRule" /> ``[UnSynchronizedStaticFormatterRule]`` If multiple threads must access a static formatter, the formatter must be synchronized on block level.

    ```java
    public class Foo {
        private static final SimpleDateFormat sdf = new SimpleDateFormat();
        void bar() {
            sdf.format(); // poor, no thread-safety
        }
        void foo() {
            synchronized (sdf) { // preferred
                sdf.format();
            }
        }
    }
    ```
    
### <a name="Performance" color="green">Performance</a>
* 1 <a name="AddEmptyStringRule" /> ``[AddEmptyStringRule]`` The conversion of literals to strings by concatenating them with empty strings is inefficient. 
It is much better to use one of the type-specific `toString()` methods instead or `String.valueOf()`.

    ```java
    public class Foo {
        String s = "" + 123;                // inefficient
        String t = Integer.toString(456);   // preferred approach
    }
    ```
* 2 <a name="AppendCharacterWithCharRule" /> ``[AppendCharacterWithCharRule]`` Avoid concatenating characters as strings in StringBuffer/StringBuilder.append methods.

    ```java
    public class Foo {
      void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("a");     // avoid this
        
        StringBuffer sb = new StringBuffer();
        sb.append('a');     // use this instead
      }
    }
    ```
* 3 <a name="AvoidCalendarDateCreationRule" /> ``[AvoidCalendarDateCreationRule]`` A Calendar is a heavyweight object and expensive to create.

    ```java
    import java.time.LocalDateTime;
    import java.util.Calendar;
    import java.util.Date;
    
    public class DateStuff {
        private Date bad() {
            return Calendar.getInstance().getTime(); // now
        }
        private Date good() {
            return new Date(); // now
        }
        private LocalDateTime good1() {
            return LocalDateTime.now();
        }
        private long bad2() {
            return Calendar.getInstance().getTimeInMillis();
        }
        private long good2() {
            return System.currentTimeMillis();
        }
    }
    ```
* 4 <a name="AvoidFileStreamRule" /> ``[AvoidFileStreamRule]`` he FileInputStream and FileOutputStream classes contain a finalizer method that will cause a GC pause. 
This problem has been fixed in JDK10 version, see:[https://bugs.openjdk.org/browse/JDK-8080225](https://bugs.openjdk.org/browse/JDK-8080225).
Please note, that the java.nio API does not throw a FileNotFoundException anymore, instead it throws a NoSuchFileException. 
If your code dealt explicitly with a FileNotFoundException, then this needs to be adjusted. 

    ```java
    import java.time.LocalDateTime;
    import java.util.Calendar;
    import java.util.Date;
    
    public class FileStreamExample {
        // these instantiations cause garbage collection pauses, even if properly closed
        FileInputStream fis = new FileInputStream(fileName);
        FileOutputStream fos = new FileOutputStream(fileName);
        FileReader fr = new FileReader(fileName);
        FileWriter fw = new FileWriter(fileName);
      
        void streamExample() {
          // the following instantiations help prevent Garbage Collection pauses, no finalization
          try(InputStream is = Files.newInputStream(Paths.get(fileName))) {
          }
          try(OutputStream os = Files.newOutputStream(Paths.get(fileName))) {
          }
          try(BufferedReader br = Files.newBufferedReader(Paths.get(fileName), StandardCharsets.UTF_8)) {
          }
          try(BufferedWriter wr = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
          }
        }
    }
    ```
* 5 <a name="AvoidInstantiatingObjectsInLoopsRule" /> ``[AvoidInstantiatingObjectsInLoopsRule]`` New objects created within loops should be checked to see if they can created outside them and reused.

    ```java
    public class Something {
        public void main() {
            for (int i = 0; i < 10; i++) {
                Foo f = new Foo(); // Avoid this whenever you can it's really expensive
            }
        }
    }
    ```
* 6 <a name="BigIntegerInstantiationRule" /> ``[BigIntegerInstantiationRule]`` Don't create instances of already existing BigInteger.ZERO, BigInteger.ONE and 
BigInteger.TEN and BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN.

    ```java
    public class Something {
        public void main() {
            BigInteger bi = new BigInteger(1);       // reference BigInteger.ONE instead
            BigInteger bi2 = new BigInteger("0");    // reference BigInteger.ZERO instead
            BigInteger bi3 = new BigInteger(0.0);    // reference BigInteger.ZERO instead
            BigInteger bi4;
            bi4 = new BigInteger(0);                 // reference BigInteger.ZERO instead
        }
    }
    ```
* 7 <a name="ConsecutiveAppendsShouldReuseRule" /> ``[ConsecutiveAppendsShouldReuseRule]`` Consecutive calls to StringBuffer/StringBuilder .append should be 
chained, reusing the target object. This can improve the performance by producing a smaller bytecode, reducing overhead and improving inlining. 
A complete analysis can be found：[https://github.com/pmd/pmd/issues/202#issuecomment-274349067](https://github.com/pmd/pmd/issues/202#issuecomment-274349067)

    ```java
    public class Something {
        public void main() {
            String foo = " ";
            
            StringBuffer buf = new StringBuffer();
            buf.append("Hello"); // poor
            buf.append(foo);
            buf.append("World");
            
            StringBuffer buf = new StringBuffer();
            buf.append("Hello").append(foo).append("World"); // good
        }
    }
    ```
* 8 <a name="ConsecutiveLiteralAppendsRule" /> ``[ConsecutiveLiteralAppendsRule]`` Consecutively calling StringBuffer/StringBuilder.append(...) with literals 
should be avoided.
Since the literals are constants, they can already be combined into a single String literal and this String can be appended in a single method call.

    ```java
    public class Something {
        public void main() {
            StringBuilder buf = new StringBuilder();
            buf.append("Hello").append(" ").append("World");    // poor
            buf.append("Hello World");                          // good
            
            buf.append('h').append('e').append('l').append('l').append('o'); // poor
            buf.append("hello");                                             // good
            
            buf.append(1).append('m');  // poor
            buf.append("1m");           // good
        }
    }
    ```
* 9 <a name="InefficientStringBufferingRule" /> ``[InefficientStringBufferingRule]`` Avoid concatenating non-literals in a StringBuffer constructor or append() 
since intermediate buffers will need to be be created and destroyed by the JVM.

    ```java
    public class Something {
        public void main() {
            // Avoid this, two buffers are actually being created here
            StringBuffer sb = new StringBuffer("tmp = "+System.getProperty("java.io.tmpdir"));
            
            // do this instead
            StringBuffer sb = new StringBuffer("tmp = ");
            sb.append(System.getProperty("java.io.tmpdir"));
        }
    }
    ```
* 10 <a name="InsufficientStringBufferDeclarationRule" /> ``[InsufficientStringBufferDeclarationRule]`` Failing to pre-size a StringBuffer or StringBuilder properly could 
cause it to re-size many times during runtime. An empty StringBuffer/StringBuilder constructor initializes the object to 16 characters. 
This default is assumed if the length of the constructor can not be determined.

    ```java
    public class Something {
        public void main() {
            StringBuilder bad = new StringBuilder();
            bad.append("This is a long string that will exceed the default 16 characters");
            
            StringBuilder good = new StringBuilder(41);
            good.append("This is a long string, which is pre-sized");
        }
    }
    ```
* 11 <a name="OptimizableToArrayCallRule" /> ``[OptimizableToArrayCallRule]`` Calls to a collection's `toArray(E[])` method should specify a target array of 
zero size. This allows the JVM to optimize the memory allocation and copying as much as possible.

    ```java
    public class Something {
        public void main() {
            List<Foo> foos = getFoos();
            
            // much better; this one allows the jvm to allocate an array of the correct size and effectively skip
            // the zeroing, since each array element will be overridden anyways
            Foo[] fooArray = foos.toArray(new Foo[0]);
            
            // inefficient, the array needs to be zeroed out by the jvm before it is handed over to the toArray method
            Foo[] fooArray = foos.toArray(new Foo[foos.size()]);
        }
    }
    ```
* 12 <a name="RedundantFieldInitializerRule" /> ``[RedundantFieldInitializerRule]`` Java will initialize fields with known default values so any 
explicit initialization of those same defaults is redundant and results in a larger class file (approximately three 
additional bytecode instructions per field).

    ```java
    public class C {
        boolean b   = false;    // examples of redundant initializers
        byte by     = 0;
        short s     = 0;
        char c      = 0;
        int i       = 0;
        long l      = 0;
    
        float f     = .0f;    // all possible float literals
        doable d    = 0d;     // all possible double literals
        Object o    = null;
    
        MyClass mca[] = null;
        int i1 = 0, ia1[] = null;
    
        class Nested {
            boolean b = false;
        }
    }
    ```
* 13 <a name="StringInstantiationRule" /> ``[StringInstantiationRule]`` Avoid instantiating String objects; this is usually unnecessary since they are 
immutable and can be safely shared.

    ```java
    public class C {
        // just do a String bar = "bar";
        private String bar = new String("bar"); 
    }
    ```
* 14 <a name="StringToStringRule" /> ``[StringToStringRule]`` Avoid calling toString() on objects already known to be string instances; this is unnecessary.

    ```java
    public class C {
        private String baz() {
            String bar = "howdy";
            return bar.toString();
        }
    }
    ```
* 15 <a name="TooFewBranchesForASwitchStatementRule" /> ``[TooFewBranchesForASwitchStatementRule]`` Switch statements are intended to be used to support complex 
branching behaviour. Using a switch for only a few cases is ill-advised, since switches are not as easy to 
understand as if-else statements. In these cases use the if-else statement to increase code readability.
now report level is 3.

    ```java
    public class Foo {
        public void bar() {
            switch (condition) {
                case ONE:
                    instruction;
                    break;
                default:
                    break; // not enough for a 'switch' stmt, a simple 'if' stmt would have been more appropriate
            }
        }
    }
    ```
* 16 <a name="UseIndexOfCharRule" /> ``[UseIndexOfCharRule]`` Use String.indexOf(char) when checking for the index of a single character; it executes faster.

    ```java
    public class Foo {
        public void bar() {
            String s = "hello world";
            // avoid this
            if (s.indexOf("d")) {}
            // instead do this
            if (s.indexOf('d')) {}
        }
    }
    ```
* 17 <a name="UseIOStreamsWithApacheCommonsFileItemRule" /> ``[UseIOStreamsWithApacheCommonsFileItemRule]`` Use of FileItem.get() and FileItem.getString() could exhaust 
memory since they load the entire file into memory.Solution: Use FileItem.getInputStream() and buffering.

    ```java
    import org.apache.commons.fileupload.FileItem;
    
    public class FileStuff {
       private String bad(FileItem fileItem) {
            return fileItem.getString();
       }
    
       private InputStream good(FileItem fileItem) {
            return fileItem.getInputStream();
       }
    }
    ```
* 18 <a name="UselessStringValueOfRule" /> ``[UselessStringValueOfRule]`` No need to call String.valueOf to append to a string; just use the valueOf() argument directly.

    ```java
    public class Foo {
       public String convert(int i) {
           String s;
           s = "a" + String.valueOf(i);    // not required
           s = "a" + i;                    // preferred approach
           return s;
       }
    }
    ```
* 19 <a name="UseStringBufferForStringAppendsRule" /> ``[UseStringBufferForStringAppendsRule]`` The use of the '+=' operator for appending strings causes the JVM to 
create and use an internal StringBuffer. If a non-trivial number of these concatenations are being used then the 
explicit use of a StringBuilder or threadsafe StringBuffer is recommended to avoid this.

    ```java
    public class Foo {
        String inefficientConcatenation() {
            String result = "";
            for (int i = 0; i < 10; i++) {
                // warning: this concatenation will create one new StringBuilder per iteration
                result += getStringFromSomeWhere(i);
            }
            return result;
        }
    
        String efficientConcatenation() {
            // better would be to use one StringBuilder for the entire loop
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                result.append(getStringFromSomeWhere(i));
            }
            return result.toString();
        }
    }
    ```
* 20 <a name="UseStringBufferLengthRule" /> ``[UseStringBufferLengthRule]`` Use StringBuffer.length() to determine StringBuffer length rather than using 
StringBuffer.toString().equals("") or StringBuffer.toString().length()

    ```java
    public class Foo {
        void foo() {
            StringBuffer sb = new StringBuffer();
            
            if (sb.toString().equals("")) {}        // inefficient
            
            if (sb.length() == 0) {}                // preferred
        }
    }
    ```
### <a name="Customization" color="green">Customization</a>
* 1 <a name="StreamExpressionStyleRule" /> ``[StreamExpressionStyleRule]`` Each node of a stream expression needs a
 newline, which can make the expression of the stream expression clearer and more intuitive.At the same time, 
 the conditional judgment statement of the Stream expression cannot exceed 4 and cannot embed another stream expression.

    ```java
    public class Foo {
      public Foo() {
        // recommend
        List<String> stringList = new ArrayList();
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> Objects.equals("1", x))
                .collect(Collectors.toList());
    
        // not recommend
        List<String> filterList = stringList.stream().filter(Objects::nonNull)
                .filter(x -> Objects.equals("1", x)).collect(Collectors.toList());
    
        // recommend
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> x.equals("1") || x.equals("2") || x.equals("3") || x.equals("4"))
                .collect(Collectors.toList());
    
        // not recommend
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> x.equals("1") || x.equals("2") || x.equals("3") || x.equals("4") || x.equals("5"))
                .collect(Collectors.toList());
    
        // not recommend
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> Optional.ofNullable(x)
                                .orElse("")
                                .equals("1"))
                .collect(Collectors.toList());
    
        // not recommend
        InnerClass innerClass = getInnerClass();
        List<InnerClass2> filterList = innerClass.getInnerClass2List()
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> x.getStringList()
                    .stream()
                    .anyMatch(j -> Optional.ofNullable(j).orElse("").equals("1")))
                .collect(Collectors.toList());
      }
    }
    ```
    
* 2 <a name="ConditionalTooLongNeedChangeLineRule" /> ``[ConditionalTooLongNeedChangeLineRule]`` If the length of 
the conditional statement of the conditional judgment statement exceeds 50 characters a newline is required and the 
conditional character needs to be change line together.

    ```java
    // not recommend
    public class Foo {
      public Foo(List<String> stringList, String input) {
        boolean isTest = Objects.nonNull(input) && input.equals("test") && StringList.contains("conditionalTooLongNeedChangeLine");
        boolean isTest2 = Objects.nonNull(input) && input.equals("test") // change line
                && StringList.contains("conditionalTooLongNeedChangeLine");
      
        if(Objects.nonNull(input) && input.equals("test") && StringList.contains("conditionalTooLongNeedChangeLine")) {
                // doSomething();
        }
      }
    }
  
    // recommend 
    public class Foo {
      public Foo(List<String> stringList, String input) {
        boolean isTest = Objects.nonNull(input)
                              && input.equals("test")
                              && StringList.contains("conditionalTooLongNeedChangeLine");
        boolean isTest2 = Objects.nonNull(input) && input.equals("test"); // conditional statements can be no longer than 50 characters
      
        if(Objects.nonNull(input)
                && input.equals("test")
                && StringList.contains("conditionalTooLongNeedChangeLine")) {
                // doSomething();
        }
      }
    }
    ```

* 3 <a name="AvoidComplexConditionalRule" /> ``[AvoidComplexConditionalRule]`` Avoid using complex expressions in 
conditional statements, which may confuse other readers.

    ```java
    // not recommend
    public class Foo {
      public Foo(List<String> stringList, String input) {
        boolean isTest = Objects.nonNull(input) || input.equals("test") && StringList.contains("conditionalTooLongNeedChangeLine");
        if((Objects.nonNull(input) && input.equals("test"))
                    || StringList.contains("conditionalTooLongNeedChangeLine")) {
                // doSomething();
        }
         boolean isTest3 = StringList.stream().anyMatch(x-> !Objects.equals(x, "3")
                    && (Objects.equals(x, "1") || Objects.equals(x, "2")));
      }
    }
  
    // recommend 
    public class Foo {
      public Foo(List<String> stringList, String input) {
        boolean isTest = Objects.nonNull(input)
                    || input.equals("test")
                    || StringList.contains("conditionalTooLongNeedChangeLine");
        
        if(Objects.nonNull(input)
                && input.equals("test")
                && StringList.contains("conditionalTooLongNeedChangeLine")) {
                // doSomething();
        }
        
        boolean isTest3 = StringList
                .stream()
                .anyMatch(x-> !Objects.equals(x, "3")
                    && Objects.equals(x, "1")
                    && Objects.equals(x, "2"));
      }
    }
    ```
    
* 4 <a name="StreamExpressionTooLongRule" /> ``[StreamExpressionTooLongRule]`` If the stream expression is too long,
 it will affect the readability and comprehension of the code. If the stream expression exceeds 30 lines, it needs to 
 be split.The number of statistical lines does not include comments.

    ```java
    public class Foo {
        public void foo() {
           List<String> stringList = new ArrayList();
           stringList.add("1");
           // stream expression over 30 lines
           List<String> filterList = stringList
              .stream()
              .filter(Objects::nonNull)
              .filter(x -> Objects.equals("1", x))
              // comment (don't statistical)
              .map(x-> {
                  // [....more line function ...]
                  return x;
              })
              .collect(Collectors.toList());
        }
    }
    ```
* 5 <a name="AvoidStreamExpressionInIfStmtsRule" /> ``[AvoidStreamExpressionInIfStmtsRule]`` Avoid including stream 
expression or Optional expression in if statements.

    ```java
    public class Foo {
        // not recommend
        public void bar(String input, List<String> inputList) {
            if(Objects.nonNull(input) && !inputList.isEmpty()
                && inputList.stream().anyMatch(x -> Objects.equals("1", x))) {
                // contain stream expression
            } else if(Optional.ofNullable(input).orElse("").equals("test")) {
                // contain Optional expression
            }
        }
      
        // recommend
        public void barOk(String input, List<String> inputList) {
            if(!inputList.isEmpty() && isTestValue(input)) {
                  // ok
            }
        }
      
        private boolean isTestValue(String input) {
            return Optional.ofNullable(input).orElse("").equals("test");
        }
    }
    ```   
* 6 <a name="UndefinedMagicConstantRule" /> ``[UndefinedMagicConstantRule]`` Any magic constant (i.e. undefined 
constants) are not allowed to appear directly in the code.

    ```java
    public class Foo {
        public void foo(String key) {
          // Magic values, except for predefined, are forbidden in coding.
          if ("ctrip".equals(key)) {
              //...
          }
          // correct example
          String KEY_PRE = "ctrip";
          if (KEY_PRE.equals(key)) {
             //...
          }
        }
    }
    ```
