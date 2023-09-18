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
- [最佳实践 BestPractices](#BestPractices)
    - [避免重新分配参数 AvoidReassigningParametersRule](#AvoidReassigningParametersRule)
    - [避免用双大括号初始化 DoubleBraceInitializationRule](#DoubleBraceInitializationRule)
    - [JUnit测试应该包含断言 JUnitTestsShouldIncludeAssertRule](#JUnitTestsShouldIncludeAssertRule)
    - [equals方法避免NPE EqualsAvoidNullRule](#EqualsAvoidNullRule)
    - [未使用的参数 UnusedFormalParameterRule](#UnusedFormalParameterRule)
    - [未使用的局部变量 UnusedLocalVariableRule](#UnusedLocalVariableRule)
    - [未使用的私有字段 UnusedPrivateFieldRule](#UnusedPrivateFieldRule)
    - [未使用的分配 UnusedAssignmentRule](#UnusedAssignmentRule)
- [代码风格 CodeStyle](#CodeStyle)
    - [返回boolean值方法的命名建议 BooleanGetMethodNameRule](#BooleanGetMethodNameRule)
    - [类命名约定 ClassNamingConventionsRule](#ClassNamingConventionsRule)
    - [控制语句包含大括号 ControlStatementBracesRule](#ControlStatementBracesRule)
    - [字段命名约定 FieldNamingConventionsRule](#FieldNamingConventionsRule)
    - [参数命名约定 FormalParameterNamingConventionsRule](#FormalParameterNamingConventionsRule)
    - [泛型命名约定 enericsNamingRule](#GenericsNamingRule)
    - [语言规律检查 LinguisticNamingRule](#LinguisticNamingRule)
    - [局部变量命名约定 LocalVariableNamingConventionsRule](#LocalVariableNamingConventionsRule)
    - [长变量约定 LongVariableRule](#LongVariableRule)
    - [方法名命名约定 MethodNamingConventionsRule](#MethodNamingConventionsRule)
    - [包命名规范 PackageCaseRule](#PackageCaseRule)
    - [过早定义变量 PrematureDeclarationRule](#PrematureDeclarationRule)
    - [避免创建不必要的局部变量 UnnecessaryLocalBeforeReturnRule](#UnnecessaryLocalBeforeReturnRule)
- [设计 Design](#Design)
    - [避免深度嵌套if语句 AvoidDeeplyNestedIfStmtsRule](#AvoidDeeplyNestedIfStmtsRule)
    - [认知复杂度 CognitiveComplexityRule](#CognitiveComplexityRule)
    - [可合并的if语句 CollapsibleIfStatementsRule](#CollapsibleIfStatementsRule)
    - [对象的耦合度较高 CouplingBetweenObjectsRule](#CouplingBetweenObjectsRule)
    - [声明为final的字段可设置为static FinalFieldCouldBeStaticRule](#FinalFieldCouldBeStaticRule)
    - [不可变字段 ImmutableFieldRule](#ImmutableFieldRule)
    - [逻辑反转 LogicInversionRule](#LogicInversionRule)
    - [非私有静态字段声明为final MutableStaticStateRule](#MutableStaticStateRule)
    - [Ncss计数 NcssCountRule](#NcssCountRule)
    - [NPath复杂度 NPathComplexityRule](#NPathComplexityRule)
    - [简化三元表达式 SimplifiedTernaryRule](#SimplifiedTernaryRule)
    - [简化布尔表达式 SimplifyBooleanExpressionsRule](#SimplifyBooleanExpressionsRule)
    - [简化布尔返回 SimplifyBooleanReturnsRule](#SimplifyBooleanReturnsRule)
    - [单字段 SingularFieldRule](#SingularFieldRule)
    - [switch语句密度 SwitchDensityRule](#SwitchDensityRule)
    - [字段过多 TooManyFieldsRule](#TooManyFieldsRule)
    - [方法太多 TooManyMethodsRule](#TooManyMethodsRule)
    - [无用的重写方法 UselessOverridingMethodRule](#UselessOverridingMethodRule)
    - [使用容器对象可以使API更清晰 UseObjectForClearerAPIRule](#UseObjectForClearerAPIRule)
    - [参数过多 ExcessiveParameterListRule](#ExcessiveParameterListRule)
    - [public属性/方法过多 ExcessivePublicCountRule](#ExcessivePublicCountRule)
- [文档 Documentation](#Documentation)
    - [注释必填 CommentRequiredRule](#CommentRequiredRule)
- [容易出错 ErrorProne](#ErrorProne)
    - [操作数赋值 AssignmentInOperandRule](#AssignmentInOperandRule)
    - [给不是final的static字段赋值 AssignmentToNonFinalStaticRule](#AssignmentToNonFinalStaticRule)
    - [避免使用float/double创建BigDecimal AvoidDecimalLiteralsInBigDecimalConstructorRule](#AvoidDecimalLiteralsInBigDecimalConstructorRule)
    - [避免重复文字 AvoidDuplicateLiteralsRule](#AvoidDuplicateLiteralsRule)
    - [避免字段名称和方法名称相同 AvoidFieldNameMatchingMethodNameRule](#AvoidFieldNameMatchingMethodNameRule)
    - [避免字段名称和类型名称相同 AvoidFieldNameMatchingTypeNameRule](#AvoidFieldNameMatchingTypeNameRule)
    - [避免在catch中处理不是本catch捕获的异常类型 AvoidInstanceofChecksInCatchClauseRule](#AvoidInstanceofChecksInCatchClauseRule)
    - [避免在if条件语句下使用硬编码 AvoidLiteralsInIfConditionRule](#AvoidLiteralsInIfConditionRule)
    - [避免多个一元运算符 AvoidMultipleUnaryOperatorsRule](#AvoidMultipleUnaryOperatorsRule)
    - [clone方法必须是public的 CloneMethodMustBePublicRule](#CloneMethodMustBePublicRule)
    - [clone方法的返回类型必须和类名相同 CloneMethodReturnTypeMustMatchClassNameRule](#CloneMethodReturnTypeMustMatchClassNameRule)
    - [比较对象使用equals方法 CompareObjectsWithEqualsRule](#CompareObjectsWithEqualsRule)
    - [在finally中不要抛出异常 DoNotThrowExceptionInFinallyRule](#DoNotThrowExceptionInFinallyRule)
    - [空catch块 EmptyCatchBlockRule](#EmptyCatchBlockRule)
    - [类不应具有与类同名的方法 MethodWithSameNameAsEnclosingClassRule](#MethodWithSameNameAsEnclosingClassRule)
    - [可序列化的类缺失serialVersionUID MissingSerialVersionUIDRule](#MissingSerialVersionUIDRule)
    - [非序列化类 NonSerializableClassRule](#NonSerializableClassRule)
    - [重载应该同时覆盖equals和hashcode OverrideBothEqualsAndHashcodeRule](#OverrideBothEqualsAndHashcodeRule)
    - [适当的logger字段 ProperLoggerRule](#ProperLoggerRule)
    - [返回空的集合取代返回null ReturnEmptyCollectionRatherThanNullRule](#ReturnEmptyCollectionRatherThanNullRule)
    - [从finally块中返回 ReturnFromFinallyBlockRule](#ReturnFromFinallyBlockRule)
- [多线程 MultiThreading](#MultiThreading)
    - [双重检查锁定 DoubleCheckedLockingRule](#DoubleCheckedLockingRule)
    - [应以同步方式访问静态Formatter对象 UnSynchronizedStaticFormatterRule](#UnSynchronizedStaticFormatterRule)
- [表现 Performance](#Performance)
    - [避免添加空字符串 AddEmptyStringRule](#AddEmptyStringRule)
    - [使用StringBuffer或StringBuilder添加单个字符时候避免添加String AppendCharacterWithCharRule](#AppendCharacterWithCharRule)
    - [避免使用Calendar创建日期 AvoidCalendarDateCreationRule](#AvoidCalendarDateCreationRule)
    - [不要创建已存在的BigInteger/BigDecimal的实例  BigIntegerInstantiationRule](#BigIntegerInstantiationRule)
    - [连续的Append应该重用 ConsecutiveAppendsShouldReuseRule](#ConsecutiveAppendsShouldReuseRule)
    - [避免进行默认值初始化字段设定 RedundantFieldInitializerRule](#RedundantFieldInitializerRule)
    - [避免使用String.valueOf() UselessStringValueOfRule](#UselessStringValueOfRule)
    - [使用StringBuffer或StringBuilder来连接字符串 UseStringBufferForStringAppendsRule](#UseStringBufferForStringAppendsRule)
    - [使用StringBuffer.length() UseStringBufferLengthRule](#UseStringBufferLengthRule)
- [自定义规则 Customization](#Customization)
    - [stream表达式规则 StreamExpressionStyleRule](#StreamExpressionStyleRule)
    - [条件语句太长需要换行 ConditionalTooLongNeedChangeLineRule](#ConditionalTooLongNeedChangeLineRule)
    - [避免使用复杂条件语句 AvoidComplexConditionalRule](#AvoidComplexConditionalRule)
    - [stream表达式太长 StreamExpressionTooLongRule](#StreamExpressionTooLongRule)
    - [避免在if语句中使用Stream表达式 AvoidStreamExpressionInIfStmtsRule](#AvoidStreamExpressionInIfStmtsRule)
    - [魔法值 UndefinedMagicConstantRule](#UndefinedMagicConstantRule)
    - [避免在switch语句中使用if语句 AvoidIfStmtsInSwitchStmtsRule](#AvoidIfStmtsInSwitchStmtsRule)
    - [避免在Stream表达式中包含stream表达式 AvoidStreamExpressionInStreamExpressionRule](#AvoidStreamExpressionInStreamExpressionRule)
    - [卫语句 GuardClauseRule](#GuardClauseRule)
    - [Collectors.toMap未使用第三个参数 CollectorsToMapUnUseThirdParamRule](#CollectorsToMapUnUseThirdParamRule)

# TODO
* 1 自定义规则：switch语句中避免使用if-else语句-done
* 2 自定义规则：if条件语句后避免紧跟return
* 3 自定义规则：Collectors.toMap未使用第三个参数
* 4 自定义规则：禁止Optional“转Stream”
* 5 自定义规则：if中的“与或非”只允许出现一种，但可多次
* 6 自定义规则：too many conditions
* 7 Design：LawOfDemeter - deprecated
* 8 自定义规则：禁止在model的set方法中使用流表达式

### <a name="BestPractices" color="green">最佳实践 BestPractices</a>
* 2 <a name="AvoidReassigningParametersRule" /> ``[AvoidReassigningParametersRule]`` Reassigning values to incoming 
parameters of a method or constructor is not recommended, as this can make the code more difficult to understand. 
The code is often read with the assumption that parameter values don’t change and an assignment violates therefore the principle of least astonishment. 
This is especially a problem if the parameter is documented e.g. in the method’s javadoc and the new content differs from the original documented content.
Use temporary local variables instead. This allows you to assign a new name, which makes the code better 
understandable.\
``[避免重新分配参数]``不建议方法或构造函数对传入的参数重新分配值，因为这会使代码更难以理解。阅读代码时通常假设参数值不会改变，因此赋值
违反了最少意外/惊讶原则。请改用临时局部变量。

    ```java
    public class Hello {
      private void greet(String name) {
        // incorrect:
        name = name + "another";
        System.out.println("Hello " + name);
    
        // preferred
        String anotherName = name + "another";
        System.out.println("Hello " + anotherName);
      }
    }
    ```
* 6 <a name="DoubleBraceInitializationRule" /> ``[DoubleBraceInitializationRule]`` Double brace initialisation is a 
pattern to initialise eg collections concisely. 
But it implicitly generates a new .class file, and the object holds a strong reference to the enclosing object. 
For those reasons, it is preferable to initialize the object normally, even though it’s verbose.\
``[避免用双大括号初始化]``双括号初始化是一种简洁地初始化例如集合的模式。但它隐式生成一个新的.class文件，并且该对象持有对封闭对象的强
引用。出于这些原因，最好正常初始化对象，即使它很冗长。

    incorrect:
    
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
* 8 <a name="JUnitTestsShouldIncludeAssertRule" /> ``[JUnitTestsShouldIncludeAssertRule]`` JUnit tests should include
 at least one assertion. This makes the tests more robust, and using assert with messages provide the developer a 
 clearer idea of what the test does.\
 ``[JUnit测试应该包含断言]``Unit测试应至少包含一个断言。这使得测试更加健壮。

    incorrect:
    
    ```java
    public class FooTest {
       @Test
       public void testSomething() {
          Bar b = findBar();
          b.work();
       }
    }
    ```
* 9 <a name="EqualsAvoidNullRule" /> ``[EqualsAvoidNullRule]`` Put constant or valued objects first in all Object 
comparisons, avoiding NPE if the second argument is null.You can also use Apache's StringUtils.equals/equalsIgnoreCase 
or Objects.equals methods.\
``[equals方法避免NPE]``在所有Object比较中将常量或有值的对象放在第一位，如果第二个参数为null，则可以避免NPE。也可以使用
Apache的StringUtils.equals/equalsIgnoreCase或Objects.equals方法

    incorrect:
    
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
* 10 <a name="UnusedFormalParameterRule" /> ``[UnusedFormalParameterRule]`` parameters of methods and constructors 
that are not referenced them in the method body.Parameters whose name starts with `ignored` or `unused` are filtered 
out.\
``[未使用的参数]``方法和构造函数中有未使用的参数。名称以ignored或unused开头的参数会被过滤掉。

    ```java
    public class Foo {
        private void bar(String howdy) {
            // howdy is not used
        }
    }
    ```
* 11 <a name="UnusedLocalVariableRule" /> ``[UnusedLocalVariableRule]`` Detects when a local variable is declared 
and/or assigned, but not used. Variables whose name starts with ignored or unused are filtered out.\
``[未使用的局部变量]``声明和/或分配的局部变量但未使用，名称以ignored或unused开头的变量会被过滤掉。

    ```java
    public class Foo {
        public void doSomething() {
            int i = 5; // Unused
            // doSomething
        }
    }
    ```
* 12 <a name="UnusedPrivateFieldRule" /> ``[UnusedPrivateFieldRule]`` Detects when a private field is declared and/or 
assigned a value, but not used.\
``[未使用的私有字段]``声明的私有字段和/或为其赋值但未使用。
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
* 14 <a name="UnusedAssignmentRule" /> ``[UnusedAssignmentRule]`` The variable is never read after the assignment, or 
The assigned value is always overwritten by other assignments before the next read of the variable.The rule doesn't 
consider assignments to fields except for those of `this` in a constructor,or static fields of the current class in 
static initializers.\
``[未使用的分配]``该变量在赋值后永远不会被读取或者在下次获取变量之前分配的值始终会被其他分配覆盖。此规则不考虑对this构造函数中的字段或
静态初始值设定项中当前类的静态字段之外的字段进行赋值。

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
### <a name="CodeStyle" color="green">代码风格 CodeStyle</a>
* 1 <a name="BooleanGetMethodNameRule" /> ``[BooleanGetMethodNameRule]`` Methods that return boolean results should 
be named as predicate statements to denote this. I.e, ‘isReady()’, ‘hasValues()’, ‘canCommit()’, ‘willFail()’, 
etc. Avoid the use of the ‘get’ prefix for these methods.\
``[返回boolean值方法的命名建议]``返回布尔结果的方法应命名为谓词语句来表示这一点。即“isReady()”、“hasValues()”、“canCommit()”、
“willFail()”等。避免在这些方法中使用“get”前缀。

    ```java
    public class Foo {
      public boolean getFoo();            // bad
      public boolean isFoo();             // ok
    }
    ```
* 2 <a name="ClassNamingConventionsRule" /> ``[ClassNamingConventionsRule]`` The class name uses the UpperCamelCase 
style and must follow the camel case(but the following exceptions: domain model related naming DO / BO / DTO / VO / DAO).
the abstract class must start with Abstract or Base,.and the unit test class must start with Test or end with Test / 
Tests / TestCase.\
``[类命名约定]``类名使用UpperCamelCase风格，必须遵从驼峰形式(但以下情形例外：领域模型的相关命名DO/BO/DTO/VO/DAO)，抽象类必须以
Abstract或Base开头,单元测试类必须以Test开头或Test/Tests/TestCase结尾。

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
* 3 <a name="ControlStatementBracesRule" /> ``[ControlStatementBracesRule]`` Enforce a policy for braces on control 
statements. It is recommended to use braces on ‘if … else’ statements and loop statements, even contains only one statement. 
This usually makes the code clearer, and helps prepare the future when you need to add another statement. \
``[控制语句包含大括号]``对控制语句执行大括号策略。建议在“if … else”语句和循环语句上使用大括号，即使只有一条语句。这通常会使代码更清晰，
并有助于为将来需要添加另一个语句时做好准备。

    ```java
    public class FooBar {
      public void foo() {
        while (true)    // incorrect
          x++;
        
        while (true) {  // preferred approach
          x++;
        }
      }
    }
    ```
* 4 <a name="FieldNamingConventionsRule" /> ``[FieldNamingConventionsRule]`` Public protected static final fields are 
named using the ALL_UPPER naming method that includes underscores.\
``[字段命名约定]``public protected static final字段命名使用包含下划线的ALL_UPPER命名法。

    ```java
    public class Foo {
         private int myField = 1; // This is in camel case, so it's ok
         public int MY_FIELD = 1; // This is ok
         protected int my_Field = 1; // it's not ok 
         final int FINAL_FIELD = 1; // This is ok
         static int FINAL_FIELD = 1; // This is ok
         enum AnEnum {
              ORG, NET, COM; // These use a separate property but are set to ALL_UPPER by default
          }
    }
    ```
* 5 <a name="FormalParameterNamingConventionsRule" /> ``[FormalParameterNamingConventionsRule]`` formal parameter 
convention CamelCase style.\
``[参数命名约定]``参数命名使用首字母小写CamelCase命名法。

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
* 6 <a name="GenericsNamingRule" /> ``[GenericsNamingRule]`` generics values should be limited to a single uppercase 
letter.\
``[泛型命名约定]``泛型命名建议使用单个大写字母。

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
* 7 <a name="LinguisticNamingRule" /> ``[LinguisticNamingRule]`` This rule finds Linguistic Naming Antipatterns. It 
checks for fields, that are named, as if they should be boolean but have a different type. I also checks for methods, 
that according to their name, should return a boolean, but don’t. Further, it checks, that
 getters return something and setters won’t. 
 ``[语言规律检查]``该规则查找语言命名反模式。它检查已命名的字段，就好像它们应该是布尔值但具有不同的类型。它还检查方法，根据它们的名称，
 这些方法应该返回布尔值，但实际上却没有。此外，它还检查getter是否返回某些内容，而setter则不会。

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
* 8 <a name="LocalVariableNamingConventionsRule" /> ``[LocalVariableNamingConventionsRule]`` By default local 
variable uses the standard Java naming convention (Camel case).\
``[局部变量命名约定]`` 局部变量使用首字母小写CamelCase命名法。

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
* 9 <a name="LongVariableRule" /> ``[LongVariableRule]`` Fields, formal arguments, or local variable names that are 
too long can make the code difficult to follow.\
``[长变量约定]``太长的字段、参数或局部变量名称可能会使代码难以理解。(阀值30)
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
* 10 <a name="MethodNamingConventionsRule" /> ``[MethodNamingConventionsRule]`` By default method uses the standard 
Java naming convention(Camel case).\
``[方法名命名约定]``方法名使用首字母小写CamelCase命名法。

    ```java
    public class Foo {
        public void fooStuff() { // it's ok
        }
      
        public void FooStuff() { // violation the first literal needs to be lowercase
          
        }
    }
    ```
* 11 <a name="PackageCaseRule" /> ``[PackageCaseRule]`` Detects when a package definition contains uppercase 
characters.\
``[包命名规范]`` 包命名必须需要全小写字母

    ```java
    package com.MyCompany;  // should be lowercase name
    
    public class SomeClass {
    }
    ```
* 12 <a name="PrematureDeclarationRule" /> ``[PrematureDeclarationRule]`` Checks for variables that are defined before
 they might be used. A reference is deemed to be premature if it is created right before a block of code that doesn’t 
 use it that also has the ability to return or throw an exception.\
 ``[过早定义变量]`` 变量在不使用该变量但也能够返回或引发异常的代码块之前创建的，则该变量被认为是过早定义的。

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
* 13 <a name="UnnecessaryLocalBeforeReturnRule" /> ``[UnnecessaryLocalBeforeReturnRule]`` Avoid the creation of 
unnecessary local variables.\
``[避免创建不必要的局部变量]``避免创建不必要的局部变量

    ```java
    public class Foo {
       public int foo() {
         int x = doSomething();
         return x;  // instead, just 'return doSomething();'
       }
    }
    ```
    
### <a name="Design" color="green">设计 Design</a>
* 1 <a name="AvoidDeeplyNestedIfStmtsRule" /> ``[AvoidDeeplyNestedIfStmtsRule]`` Avoid creating deeply nested if-then
 statements since they are harder to read and error-prone to maintain.\
 ``[避免深度嵌套if语句]``避免创建深层嵌套的if-then语句(阀值3)，因为它们更难以阅读并且易于维护错误。

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
* 2 <a name="CognitiveComplexityRule" /> ``[CognitiveComplexityRule]`` Methods that are highly complex are difficult 
to read and more costly to maintain. If you include too much decisional logic within a single method, you make its 
behavior hard to understand and more difficult to modify.Information about Cognitive complexity can be found in the 
originalpaper here: [https://www.sonarsource.com/docs/CognitiveComplexity.pdf](https://www.sonarsource.com/docs/CognitiveComplexity.pdf)
default report level is 15.\
``[认知复杂度]`` 高度复杂的方法难以阅读并且维护成本更高。如果在单个方法中包含太多决策逻辑，则会使其行为难以理解并且更难以修改。
有关认知复杂性的信息可详见：[https://www.sonarsource.com/docs/CognitiveComplexity.pdf](https://www.sonarsource.com/docs/CognitiveComplexity.pdf)
默认报告阀值是15。
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
* 3 <a name="CollapsibleIfStatementsRule" /> ``[CollapsibleIfStatementsRule]`` Sometimes two consecutive ‘if’ 
statements can be consolidated by separating their conditions with a boolean short-circuit operator.\
``[可合并的if语句]``可以通过用布尔运算符合并两个连续的“if”语句。

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
* 4 <a name="CouplingBetweenObjectsRule" /> ``[CouplingBetweenObjectsRule]`` counts the number of unique attributes, 
local variables, and return types within an object. A number higher than the specified threshold can indicate a high 
degree of coupling.default threshold is 20.\
``[对象的耦合度较高]``计算对象中唯一属性、局部变量和返回类型的数量。高于指定阈值20表示耦合程度较高。
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
* 5 <a name="FinalFieldCouldBeStaticRule" /> ``[FinalFieldCouldBeStaticRule]`` If a final field is assigned to a 
compile-time constant, it could be made static, thus saving overhead in each object at runtime.\
``[声明为final的字段可设置为static]``将final字段分配给编译时的常量，则可以将其设置为静态，从而节省每个对象在运行时的开销。

    ```java
    public class Foo {
      public final int BAR = 42; // this could be static and save some space
    }
    ```
* 6 <a name="ImmutableFieldRule" /> ``[ImmutableFieldRule]`` Identifies private fields whose values never change once
 object initialization ends either in the declaration of the field or by a constructor. This helps in converting 
 existing classes to becoming immutable ones. Note that this rule does not enforce referenced object to be immutable 
 itself. A class can still be mutable, even if all its member fields are declared final. This is referred to as shallow 
 immutability. For more information on mutability, see Effective Java, 3rd Edition, Item 17: Minimize mutability.\
``[不可变字段]`` 标识私有字段，一旦对象初始化在字段声明中或通过构造函数结束，其值就永远不会改变。这有助于将现有类转换为不可变类。
请注意，此规则并不强制引用的对象本身不可变。即使类的所有成员字段都声明为final类，类仍然可以是可变的。这称为浅层不变性。有关可变性的更多
信息，请参阅《Effective Java》，第3版，第17项：最小化可变性。

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
* 7 <a name="LogicInversionRule" /> ``[LogicInversionRule]`` Use opposite operator instead of negating the whole 
expression with a logic complement operator.\
``[逻辑反转]`` 使用相反的运算符，而不是使用逻辑非运算符来否定整个表达式。

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
* 8 <a name="MutableStaticStateRule" /> ``[MutableStaticStateRule]`` Non-private non-final static fields break 
encapsulation and can lead to hard to find bugs, since these fields can be modified from anywhere within the program. 
Callers can trivially access and modify non-private non-final static fields. Neither accesses nor modifications can be 
guarded against, and newly set values cannot be validated.\
``[非私有静态字段声明为final]``非private final static字段会破坏封装，并可能导致难以发现错误，因为可以从程序内的任何位置修改这些
字段。调用者可以轻松访问和修改非private final static字段。无法防止访问或修改，并且无法验证新设置的值。

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
* 9 <a name="NcssCountRule" /> ``[NcssCountRule]`` This rule uses the NCSS (Non-Commenting Source Statements) metric 
to determine the number of lines of code in a class, method or constructor. If the number of lines of code of a class, 
method or constructor exceeds the specified threshold, it is generally considered that the class, method or constructor 
needs to be refactored to make the logic of the code clearer and more reasonable.NCSS ignores comments, blank lines, 
and only counts actual statements. For more details on the calculation, see the documentation of the：
[https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index\.html#non-commenting-source-statements-ncss](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index\.html#non-commenting-source-statements-ncss)
default method report lines is 80,default class report lines is 1500.\
``[Ncss计数]`` 此规则使用Ncss（非注释源语句）指标来确定类、方法或构造函数中的代码行数。如果类、方法或者构造函数的代码行数超过指定阀值一
般认为该类、方法或者构造函数是需要重构使代码的逻辑更加清晰和合理。Ncss忽略注释、空行，只计算实际语句。 NCSS统计逻辑可详见：
[https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index\.html#non-commenting-source-statements-ncss](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index\.html#non-commenting-source-statements-ncss)
默认的告警阀值方法80行，类1500行。

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
* 10 <a name="NPathComplexityRule" /> ``[NPathComplexityRule]`` The NPath complexity of a method is the number of 
acyclic execution paths through that method. NPath counts the number of full paths from the beginning to the end of 
the block of the method. That metric grows exponentially, as it multiplies the complexity of statements in the same 
block.For more details on the calculation, see the documentation of the：
[https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index.html#npath-complexity-npath](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index.html#npath-complexity-npath)
default report level is 200.\
``[NPath复杂度]`` 方法的NPath复杂度是通过该方法的非循环执行路径的数量。NPath计算方法块从开始到结束的完整路径的数量。该指标呈指数级增
长，因为它使同一块中语句的复杂性成倍增加。NPath复杂度可详见：
[https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index.html#npath-complexity-npath](https://docs.pmd-code.org/pmd-doc-6.55.0/pmd_java_metrics_index.html#npath-complexity-npath)
默认告警阀值是200。

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
* 11 <a name="SimplifiedTernaryRule" /> ``[SimplifiedTernaryRule]`` \
condition ? true : expr simplifies to condition || expr.\
condition ? false : expr simplifies to !condition && expr.\
condition ? expr : true simplifies to !condition || expr.\
condition ? expr : false simplifies to condition && expr.\
``[简化三元表达式]`` condition ? true : expr 可以简化为condition || expr \
                condition ? false : expr 可以简化为!condition && expr \
                condition ? expr : true 可以简化为!condition || expr \
                condition ? expr : false 可以简化为condition && expr

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
* 12 <a name="SimplifyBooleanExpressionsRule" /> ``[SimplifyBooleanExpressionsRule]`` Avoid unnecessary comparisons in
 boolean expressions, they serve no purpose and impacts readability.\
 ``[简化布尔表达式]`` 避免在布尔表达式中进行不必要的比较，它们没有任何作用并且会影响可读性。

    ```java
    public class Bar {
      // can be simplified to
      // bar = isFoo();
      private boolean bar = (isFoo() == true);
    
      public isFoo() { return false;}
    } 
    ```
* 13 <a name="SimplifyBooleanReturnsRule" /> ``[SimplifyBooleanReturnsRule]`` Avoid unnecessary if-then-else 
statements when returning a boolean. The result of the conditional test can be returned instead.\
``[简化布尔返回]`` 返回布尔值时避免不必要的if-then-else语句。可以改为返回条件测试的结果。
             
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
* 14 <a name="SingularFieldRule" /> ``[SingularFieldRule]`` Fields whose scopes are limited to just single methods do 
not rely on the containing object to provide them to other methods. They may be better implemented as local variables
 within those methods(lombok related Annotation has been set to ignore).\
``[单字段]`` 如果全局变量只在单个方法中被使用，可以考虑替换为局部变量(lombok相关Annotation已设置忽略)。
    ```java
    public class Foo {
        private int x;  // no reason to exist at the Foo instance level
        public void foo(int y) {
         x = y + 5;
         return x;
        }
    }
  
    @Data
    public class FooModel {
        private int x; // it's ok
    }
    ```
* 15 <a name="SwitchDensityRule" /> ``[SwitchDensityRule]`` A high ratio of statements to labels in a switch statement
 implies that the switch statement is overloaded.Consider moving the statements into new methods or creating subclasses 
 based on the switch variable.defalut report level is 10.\
 ``[switch语句密度]`` switch语句中case标签包含的语句比例较高意味着switch语句超载。考虑将语句移至新方法或基于switch变量创建子类。

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
* 16 <a name="TooManyFieldsRule" /> ``[TooManyFieldsRule]`` Classes that have too many fields can become unwieldy and 
could be redesigned to have fewer fields, possibly through grouping related fields in new objects.now report level is
 30.\
``[字段过多]``具有太多字段的类可能会变得难以使用，可以考虑重新设计以具有更少的字段，可以通过将相关字段分组到新对象中来实现。默认告警阀值
是30。
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
* 17 <a name="TooManyMethodsRule" /> ``[TooManyMethodsRule]`` A class with too many methods is probably a good suspect
 for refactoring, in order to reduce its complexity and find a way to have more fine grained objects.now report level
 is 20.\
``[方法太多]`` 具有太多方法的类可能需要重构，以降低其复杂性并找到一种拥有更细粒度对象的方法。默认告警阀值是20
    ```java
    public class Person {
      public void method() { // +1
             
      }
         
      private void privateMethod() { // +1
                  
      }
    }
    ```
* 18 <a name="UselessOverridingMethodRule" /> ``[UselessOverridingMethodRule]`` The overriding method merely calls the
 same method defined in a superclass.\
``[无用的重写方法]``重写方法仅调用超类中定义的相同方法。
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
* 19 <a name="UseObjectForClearerAPIRule" /> ``[UseObjectForClearerAPIRule]`` When you write a public method, you 
should be thinking in terms of an API. If your method is public, it means other class will use it, therefore, you 
want (or need) to offer a comprehensive and evolutive API. If you pass a lot of information as a simple series of 
Strings, you may think of using an Object to represent all those information. You’ll get a simpler API 
(such as doWork(Workload workload), rather than a tedious series of Strings) and more importantly, if you need at 
some point to pass extra data, you’ll be able to do so by simply modifying or extending Workload without any 
modification to your API.\
``[使用容器对象可以使API更清晰]`` 当您编写公共方法时，您应该从 API 的角度进行思考。如果您的方法是公共的，则意味着其他类将使用它，
因此，您希望（或需要）提供全面且不断发展的 API。如果您将大量信息作为一系列简单的字符串传递，您可能会考虑使用对象来表示所有这些信息。
您将获得一个更简单的API，而不是一系列繁琐的字符，更重要的是如果您在某些时候需要传递额外的数据，您将能够通过简单地修改或扩展API来实现，
而无需对API进行任何修改。（本规则只针对参数是String类型）

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
* 20 <a name="ExcessiveParameterListRule" /> ``[ExcessiveParameterListRule]`` Methods with numerous parameters are a 
challenge to maintain, especially if most of them share the same datatype. These situations usually denote the need 
for new objects to wrap the numerous parameters.now report level is 10.\
``[参数过多]`` 具有大量参数的方法是维护的一个挑战，特别是如果它们中的大多数共享相同的数据类型。这些情况通常表示需要新对象来包装众多参数。
默认告警阀值是10。

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
* 21 <a name="ExcessivePublicCountRule" /> ``[ExcessivePublicCountRule]`` Classes with large numbers of public methods
 and attributes require disproportionate testing efforts since combinational side effects grow rapidly and increase risk. 
Refactoring these classes into smaller ones not only increases testability and reliability.now report level is 45.\
``[public属性/方法过多]`` 具有大量公共方法和属性的类需要不成比例的测试工作，因为组合副作用会迅速增长并增加风险。将这些类重构为更小的类
不仅可以提高可测试性和可靠性。当前告警阀值是45。

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
    
### <a name="Documentation" color="green">文档 Documentation</a>
* 1 <a name="CommentRequiredRule" /> ``[CommentRequiredRule]`` public, protected methods and classes, fields, and 
enumerations need to include comments.\
``[注释必填]`` public、protected方法及类、字段、枚举需要包含注释。
    ```java
    public class Foo { // non comment
        public void doWork() {} // non comment
    }
    ```
    
### <a name="ErrorProne" color="green">容易出错 ErrorProne</a>
* 1 <a name="AssignmentInOperandRule" /> ``[AssignmentInOperandRule]`` Avoid assignments in operands; this can make 
code more complicated and harder to read.\
``[操作数赋值]`` 避免在操作数中赋值，这会使代码更加复杂且难以阅读。
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
* 2 <a name="AssignmentToNonFinalStaticRule" /> ``[AssignmentToNonFinalStaticRule]`` Identifies a possible unsafe 
usage of a static field.\
``[给不是final的static字段赋值]``识别static资源不安全的使用。

    ```java
    public class StaticField {
       static int x;
       public FinalFields(int y) {
        x = y; // unsafe
       }
    }
    ```
* 3 <a name="AvoidDecimalLiteralsInBigDecimalConstructorRule" /> ``[AvoidDecimalLiteralsInBigDecimalConstructorRule]`` 
One might assume that the result of "new BigDecimal(0.1)" is exactly equal to 0.1, but it is actually equal 
to 0.1000000000000000055511151231257827021181583404541015625.This is because 0.1 cannot be represented exactly as a 
double (or as a binary fraction of any finite length). \
``[避免使用float/double创建BigDecimal]`` 一般会认为“new BigDecimal(0.1)”的结果恰好等于0.1，但它实际上等于
0.1000000000000000055511151231257827021181583404541015625。这是因为0.1不能精确地表示为双精度型（或任何有限长度的二进制分数）。

    ```java
    public class Foo {
       public Foo() {
        BigDecimal bd = new BigDecimal(1.123);       // loss of precision, this would trigger the rule
        
        BigDecimal bd = new BigDecimal("1.123");     // preferred approach
        
        BigDecimal bd = new BigDecimal(12);          // preferred approach, ok for integer values
       }
    }
    ```
* 4 <a name="AvoidDuplicateLiteralsRule" /> ``[AvoidDuplicateLiteralsRule]`` Code containing duplicate String literals 
can usually be improved by declaring the String as a constant field.default report level is 3.\
``[避免重复文字]`` 包含重复字符串的代码建议可以将字符串声明为常量字段。默认告警阀值是3。
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
* 5 <a name="AvoidFieldNameMatchingMethodNameRule" /> ``[AvoidFieldNameMatchingMethodNameRule]`` It can be confusing to 
have a field name with the same name as a method. While this is permitted.\
``[避免字段名称和方法名称相同]`` 字段名与方法同名可能会造成混淆。虽然这是允许的。
    ```java
    public class Foo {
        Object bar;
        // bar is data or an action or both?
        void bar() {
        }
    }
    ```
* 6 <a name="AvoidFieldNameMatchingTypeNameRule" /> ``[AvoidFieldNameMatchingTypeNameRule]`` It is somewhat confusing 
to have a field name matching the declaring type name.\
``[避免字段名称和类型名称相同]`` 字段名称与声明类型名称相同会有点令人困惑。
    ```java
    public class Foo extends Bar {
        int foo;    // There is probably a better name that can be used
    }
    public interface Operation {
        int OPERATION = 1; // There is probably a better name that can be used
    }
    ```
* 7 <a name="AvoidInstanceofChecksInCatchClauseRule" /> ``[AvoidInstanceofChecksInCatchClauseRule]`` Each caught 
exception type should be handled in its own catch clause.\
``[避免在catch中处理不是本catch捕获的异常类型]`` 每个捕获的异常类型都应在其自己的catch子句中处理。
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
* 8 <a name="AvoidLiteralsInIfConditionRule" /> ``[AvoidLiteralsInIfConditionRule]`` Avoid using hard-coded literals 
in conditional statements. By declaring them as static variables or private members with descriptive names 
maintainability is enhanced. \
``[避免在if条件语句下使用硬编码]`` 避免在条件语句中使用硬编码。通过将它们声明为静态变量或具有描述性名称的私有成员。

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
* 9 <a name="AvoidMultipleUnaryOperatorsRule" /> ``[AvoidMultipleUnaryOperatorsRule]`` The use of multiple unary 
operators may be problematic, and/or confusing.Ensure that the intended usage is not a bug, or consider simplifying 
the expression.\
``[避免多个一元运算符]`` 使用多个一元运算符可能会出现问题and/or令人困惑。确保预期用途不是错误，或者考虑简化表达式。
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

* 10 <a name="CloneMethodMustBePublicRule" /> ``[CloneMethodMustBePublicRule]`` clone() method must be public if the 
class implements Cloneable.\
``[clone方法必须是public的]`` 如果类实现Cloneable，clone()方法必须是public的。
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
* 11 <a name="CloneMethodReturnTypeMustMatchClassNameRule" /> ``[CloneMethodReturnTypeMustMatchClassNameRule]`` The 
return type of the clone() method must be the class name when implements Cloneable.\
``[clone方法的返回类型必须和类名相同]`` 如果类实现了cloneable，则方法clone()的返回类型必须是类名。
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
* 12 <a name="CompareObjectsWithEqualsRule" /> ``[CompareObjectsWithEqualsRule]`` Use equals() to compare object 
references，avoid use “==” to compare.\
``[比较对象使用equals方法]``使用equals()来比较对象引用，避免使用“==”进行比较。

    ```java
    public class Foo {
      boolean bar(String a, String b) {
        return a == b; // avoid this
      }
    }
    ```
* 13 <a name="DoNotThrowExceptionInFinallyRule" /> ``[DoNotThrowExceptionInFinallyRule]`` Throwing exceptions within a
 'finally' block is confusing since they may mask other exceptions or code defects.\
``[在finally中不要抛出异常]`` 在“finally”块中抛出异常会令人困惑，因为它们可能会掩盖其他异常或代码缺陷。
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
* 14 <a name="EmptyCatchBlockRule" /> ``[EmptyCatchBlockRule]`` Avoid catch exceptions but do nothing. Can be 
configured through the allowExceptionNameRegex attribute to ignore the exception name, the default is ignored and 
expected.\
``[空catch块]`` 避免catch异常但不执行任何操作。可以通过allowExceptionNameRegex属性配置需要忽略的exception名称，默认
是ignored和expected。

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
* 15 <a name="MethodWithSameNameAsEnclosingClassRule" /> ``[MethodWithSameNameAsEnclosingClassRule]`` Non-constructor 
methods should not have the same name as the enclosing class.\
``[类不应具有与类同名的方法]`` 非构造函数方法不应与类同名。

    ```java
    public class MyClass {
    
        public MyClass() {}         // this is OK because it is a constructor
    
        public void myClass() {}    // this is bad because it is a method
    }
    ```
* 16 <a name="MissingSerialVersionUIDRule" /> ``[MissingSerialVersionUIDRule]`` Serializable classes should provide a 
serialVersionUID field.The serialVersionUID field is also needed for abstract base classes. Each individual class in 
the inheritance chain needs an own serialVersionUID field.\
``[可序列化的类缺失serialVersionUID]`` 可序列化类应该提供一个serialVersionUID字段。抽象基类还需要serialVersionUID 字段。继承中的
每个单独的类都需要一个自己的serialVersionUID字段。

    ```java
    public class Foo implements java.io.Serializable {
        String name;
        // Define serialization id to avoid serialization related bugs
        // i.e., public static final long serialVersionUID = 4328743;
    }
    ```
* 17 <a name="NonSerializableClassRule" /> ``[NonSerializableClassRule]`` If a class is marked as `Serializable`, then
 all fields need to be serializable as well. In order to exclude a field, it can be marked as transient. Static 
 fields are not considered.\
``[非序列化类]`` 如果一个类被标记为“可序列化”，那么所有字段也需要可序列化。为了排除一个字段，它可以被标记为transient。不考虑静态字段。

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
* 18 <a name="OverrideBothEqualsAndHashcodeRule" /> ``[OverrideBothEqualsAndHashcodeRule]`` Override both public 
boolean Object.equals(Object other), and public int Object.hashCode(), or override neither. Even if you are inheriting 
a hashCode() from a parent class, consider implementing hashCode and explicitly delegating to your superclass.\
``[重载应该同时覆盖equals和hashcode]``重载public boolean Object.equals(Object other) 和 public int Object.hashCode()，
或者都不覆盖。即使您从父类继承hashCode()，也请考虑实现hashCode并显式委托给您的超类。

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
* 19 <a name="ProperLoggerRule" /> ``[ProperLoggerRule]`` A logger should normally be defined private static final and
 be associated with the correct class.`private final Log log;` is also allowed for rare cases where loggers need to be 
 passed around,with the restriction that the logger needs to be passed into the constructor.\
 ``[适当的logger字段]`` logger通常应定义为private static final并与正确的类关联。在需要传递logger的极少数情况下，也允许使用
 “private final Log log;”，限制是logger需要传递到构造函数中。

    ```java
    public class Foo {
    
        private static final Log LOG = LogFactory.getLog(Foo.class);    // proper way
    
        protected Log LOG = LogFactory.getLog(Testclass.class);         // wrong approach
    }
    ```
* 20 <a name="ReturnEmptyCollectionRatherThanNullRule" /> ``[ReturnEmptyCollectionRatherThanNullRule]`` For any method
 that returns an collection (such as an array, Collection or Map), it is better to return an empty one rather than a 
 null reference. This removes the need for null checking all results and avoids inadvertent NullPointerExceptions.\
 ``[返回空的集合取代返回null]``对于任何返回集合（例如数组、Collection或Map）的方法，最好返回一个空引用而不是一个'null'，避免NPE。

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
* 21 <a name="ReturnFromFinallyBlockRule" /> ``[ReturnFromFinallyBlockRule]`` Avoid returning from a finally block, 
this can discard exceptions.\
``[从finally块中返回]``避免从finally块返回，这会丢弃异常。
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
 
### <a name="MultiThreading" color="green">多线程 MultiThreading</a>
* 2 <a name="DoubleCheckedLockingRule" /> ``[DoubleCheckedLockingRule]`` Double Check locking in Java is not 
thread-safe, and the volatile keyword needs to be added to prohibit instruction rearrangement.\
``[双重检查锁定]`` Java中的双重检查锁定不是线程安全的，需要添加volatile关键字，禁止指令重排。
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
* 3 <a name="UnSynchronizedStaticFormatterRule" /> ``[UnSynchronizedStaticFormatterRule]`` If multiple threads must 
access a static formatter, the formatter must be synchronized on block level.\
``[应以同步方式访问静态Formatter对象]`` 如果多个线程必须访问静态format程序，则该format方法必须添加synchronized关键字。
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
    
### <a name="Performance" color="green">表现 Performance</a>
* 1 <a name="AddEmptyStringRule" /> ``[AddEmptyStringRule]`` The conversion of literals to strings by concatenating 
them with empty strings is inefficient. It is much better to use one of the type-specific `toString()` methods 
instead or `String.valueOf()`.\
``[避免添加空字符串]`` 通过将文字与空字符串连接来将文字转换为字符串的效率很低。最好使用一种特定于类型的toString()方法来代替或String.valueOf()。

    ```java
    public class Foo {
        String s = "" + 123;                // inefficient
        String t = Integer.toString(456);   // preferred approach
    }
    ```
* 2 <a name="AppendCharacterWithCharRule" /> ``[AppendCharacterWithCharRule]`` Avoid concatenating characters as 
strings in StringBuffer/StringBuilder.append methods.\
``[使用StringBuffer或StringBuilder添加单个字符时候避免添加String]`` 避免在StringBuffer/StringBuilder.append方法中将字符连接为字符串。
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
* 3 <a name="AvoidCalendarDateCreationRule" /> ``[AvoidCalendarDateCreationRule]`` A Calendar is a heavyweight object
 and expensive to create.\
``[避免使用Calendar创建日期]`` Calendar是重量级对象，创建成本昂贵。
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
* 5 <a name="BigIntegerInstantiationRule" /> ``[BigIntegerInstantiationRule]`` Don't create instances of already 
existing BigInteger.ZERO, BigInteger.ONE and BigInteger.TEN and BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN.\
``[不要创建已存在的BigInteger/BigDecimal的实例]`` 不要创建已存在的BigInteger.ZERO、BigInteger.ONE、BigInteger.TEN、
BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN的实例。

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
* 6 <a name="ConsecutiveAppendsShouldReuseRule" /> ``[ConsecutiveAppendsShouldReuseRule]`` Consecutive calls to 
StringBuffer/StringBuilder .append should be chained, reusing the target object. This can improve the performance by 
producing a smaller bytecode, reducing overhead and improving inlining. A complete analysis can be found：
[https://github.com/pmd/pmd/issues/202#issuecomment-274349067](https://github.com/pmd/pmd/issues/202#issuecomment-274349067)\
``[连续的Append应该重用]`` 对StringBuffer/StringBuilder的append方法有连续调用应该链接起来，重用目标对象。这可以通过生成更小的字节
码、减少开销和改进内联来提高性能。完整分析可以查看：
[https://github.com/pmd/pmd/issues/202#issuecomment-274349067](https://github.com/pmd/pmd/issues/202#issuecomment-274349067)

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
* 7 <a name="ConsecutiveLiteralAppendsRule" /> ``[ConsecutiveLiteralAppendsRule]`` Consecutively calling 
StringBuffer/StringBuilder.append(...) with literals should be avoided.Since the literals are constants, they can 
already be combined into a single String literal and this String can be appended in a single method call.\
``[避免连续的文字append]`` 避免使用文字连续调用StringBuffer/StringBuilder.append(...)。由于文字是常量，因此它们已经可以组合成单个
字符串文字，并且可以在单个方法调用中附加该字符串。

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
* 11 <a name="RedundantFieldInitializerRule" /> ``[RedundantFieldInitializerRule]`` Java will initialize fields with 
known default values so any explicit initialization of those same defaults is redundant and results in a larger class 
file (approximately three additional bytecode instructions per field).\
``[避免进行默认值初始化字段设定]`` Java将使用已知的默认值初始化字段，因此这些相同默认值的任何显式初始化都是多余的，并会导致更大的类文件
（每个字段大约三个额外的字节码指令）。

    ```java
    public class C {
        boolean b   = false;    // examples of redundant initializers
        byte by     = 0;
        short s     = 0;
        char c      = 0;
        int i       = 0;
        long l      = 0;
    
        float f     = .0f;    // all possible float literals
        double d    = 0d;     // all possible double literals
        Object o    = null;
    
        MyClass mca[] = null;
        int i1 = 0, ia1[] = null;
    
        class Nested {
            boolean b = false;
        }
    }
    ```
* 15 <a name="UselessStringValueOfRule" /> ``[UselessStringValueOfRule]`` No need to call String.valueOf to append to 
a string; just use the valueOf() argument directly.
``[避免使用String.valueOf()]`` 无需调用String.valueOf来添加字符串；只需直接使用valueOf()参数即可。
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
* 16 <a name="UseStringBufferForStringAppendsRule" /> ``[UseStringBufferForStringAppendsRule]`` The use of the '+=' 
operator for appending strings causes the JVM to create and use an internal StringBuffer. If a non-trivial number of 
these concatenations are being used then the explicit use of a StringBuilder or threadsafe StringBuffer is recommended 
to avoid this.\
``[使用StringBuffer或StringBuilder来连接字符串]`` 使用“+=”运算符附加字符串会导致 JVM 创建并使用内部StringBuffer。如果使用的这些
连接数量不少，则建议显式使用StringBuilder或线程安全StringBuffer来避免这种情况。

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
            StringBuilder result = new StringBuilder(16);
            for (int i = 0; i < 10; i++) {
                result.append(getStringFromSomeWhere(i));
            }
            return result.toString();
        }
    }
    ```
* 17 <a name="UseStringBufferLengthRule" /> ``[UseStringBufferLengthRule]`` Use StringBuffer.length() to determine 
StringBuffer length rather than using StringBuffer.toString().equals("") or StringBuffer.toString().length()\
``[使用StringBuffer.length()]`` 使用StringBuffer.length()来确定StringBuffer长度，而不是使用
StringBuffer.toString().equals("")或StringBuffer.toString().length()。

    ```java
    public class Foo {
        void foo() {
            StringBuffer sb = new StringBuffer();
            
            if (sb.toString().equals("")) {}        // inefficient
            
            if (sb.length() == 0) {}                // preferred
        }
    }
    ```
### <a name="Customization" color="green">自定义规则 Customization</a>
* 1 <a name="StreamExpressionStyleRule" /> ``[StreamExpressionStyleRule]`` Each node of a stream expression needs a
 newline, which can make the expression of the stream expression clearer and more intuitive.\
``[stream表达式规则]`` stream表达式采用链式的方式呈现，为了表述的清晰和直观建议每个节点都要进行换行(此规则包含Optional Flux和Mono)。

    ```java
    public class Foo {
      public Foo() {
        // correct
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> Objects.equals("1", x))
                .collect(Collectors.toList());
    
        filterList = stringList.stream()
                .filter(Objects::nonNull)
                .filter(x -> Objects.equals("1", x))
                .collect(Collectors.toList());
    
        // incorrect
        List<String> filterList = stringList.stream().filter(Objects::nonNull)
                .filter(x -> Objects.equals("1", x)).collect(Collectors.toList());
    
    
         // correct
         Mono<String> resultMono = Flux.fromIterable(iterable)
                    .map(value -> {
                        // doSomething();
                        return Mono.just();
                    })
                    .flatMapSequential(x -> x)
                    .collectList()
                    .flatMap(states -> {
                        // doSomething();
                        return Mono.just();
                    });
    
          // correct
          Mono<Boolean> monoResult = getMonoInfo()
                            .map(wrapper -> Objects.equals("test", wrapper));
          monoResult = getMonoInfo().map(wrapper -> Objects.equals("test", wrapper));
          String firstValue = Mono.just(new ArrayList<String>())
                        .flatMap(this::getMonoListInfo)
                        .map(x -> x.get(0))
                        .block();
      }
    }
    ```
    
* 2 <a name="ConditionalTooLongNeedChangeLineRule" /> ``[ConditionalTooLongNeedChangeLineRule]`` If the length of 
the conditional statement of the conditional judgment statement exceeds 50 characters a newline is required and the 
conditional character needs to be change line together.\
``[条件语句太长需要换行]`` 条件判断语句的条件语句长度超过50个字符需要换行，条件符需要跟着一起换行。

    ```java
    // incorrect
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
  
    // correct 
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
conditional statements, which may confuse other readers.\
``[避免使用复杂条件语句]`` 避免在条件语句中使用复杂的表达式，这可能会让其他阅读者感到困惑。

    ```java
    // incorrect
    public class Foo {
      public Foo(List<String> stringList, String input) {
        boolean isTest = (Objects.nonNull(input) 
                  || input.equals("test")) 
                  && StringList.contains("conditionalTooLongNeedChangeLine");
        if((Objects.nonNull(input) && input.equals("test"))
                    || StringList.contains("conditionalTooLongNeedChangeLine")) {
                // doSomething();
        }
         boolean isTest3 = StringList.stream().anyMatch(x-> !Objects.equals(x, "3")
                    && (Objects.equals(x, "1") || Objects.equals(x, "2")));
      }
    }
  
    // correct 
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
 be split.The number of statistical lines does not include comments.\
``[stream表达式太长]`` stream表达式太长会影响可读性和对代码的理解。流表达式超过30行需要进行拆分。统计数量行不包含注释。

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
expression or Optional expression in if statements.\
``[避免在if语句中使用Stream或Optional表达式]`` 避免在if语句中使用Stream或Optional表达式。
    ```java
    public class Foo {
        // incorrect
        public void bar(String input, List<String> inputList) {
            if(Objects.nonNull(input) && !inputList.isEmpty()
                && inputList.stream().anyMatch(x -> Objects.equals("1", x))) {
                // contain stream expression
            } else if(Optional.ofNullable(input).orElse("").equals("test")) {
                // contain Optional expression
            }
        }
      
        // correct
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
constants) are not allowed to appear directly in the code.\
``[魔法值]`` 不允许任何魔法值（即未经定义的常量）直接出现在代码中。

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
* 7 <a name="AvoidIfStmtsInSwitchStmtsRule" /> ``[AvoidIfStmtsInSwitchStmtsRule]`` Both switch and if are conditional 
judgment statements. Mixing the two may increase the complexity of the code. It is recommended that the if statement 
in switch can be refactored into a method for use.\
``[避免在switch语句中使用if语句]`` switch和if都是条件判断语句，两个混用可能造成代码的复杂度上升，建议switch中的if语句可以重构为一个
方法来使用。

    ```java
    public class Foo {
            public void foo(InputModel inputModel) {
                switch (inputModel.getTitle()) {
                    case "Test1":
                        // incorrect
                        if (StringUtils.isNotEmpty(inputModel.getContent())) {
                            //doSomething()
                        } else {
                            //doSomething()
                        }
                        break;
                    case "Unit1":
                        // incorrect
                        if (Objects.equals(inputModel.getContent(), "UnitContent")) {
                            //doSomething()
                        } else {
                            //doSomething()
                        }
                        break;
                     default:
                         //doSomething()
                }
            }
        }
    ```
* 8 <a name="AvoidStreamExpressionInStreamExpressionRule" /> ``[AvoidStreamExpressionInStreamExpressionRule]`` It is 
not recommended to nest the stream expression in the stream expression,which will make the whole stream expression 
extremely complicated and difficult to understand.\
``[避免在Stream表达式中包含stream表达式]`` 不建议在stream表达式中再嵌套stream表达式，这样会使整个stream表达式异常复杂并难以理解。

    ```java
    public class Foo {
      public Foo() {
        // correct
        List<String> stringList = new ArrayList();
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                .filter(x -> Objects.equals("1", x))
                .collect(Collectors.toList());
    
        // incorrect
        List<String> filterList = stringList
                .stream()
                .filter(Objects::nonNull)
                // stream expression contain stream expression
                .filter(x -> Optional.ofNullable(x).orElse("").equals("1"))
                .collect(Collectors.toList());
    
        // incorrect
        InnerClass innerClass = getInnerClass();
        List<InnerClass2> filterList = innerClass.getInnerClass2List()
                .stream()
                .filter(Objects::nonNull)
                // stream expression contain stream expression
                .filter(x -> x.getStringList().stream().anyMatch(j -> Objects.equals(j, "1")))
                .collect(Collectors.toList());
    
        // correct
        boolean result = innerClassList.stream()
            .filter(Objects::nonNull)
            .map(InnerClass::getExtensions)
            .flatMap(Collection::stream)
            .anyMatch(x -> Objects.equals("1", x));
        // incorrect
        boolean result = innerClassList.stream()
            .filter(Objects::nonNull)
            // stream expression contain stream expression
            .flatMap(x -> x.getExtensions().stream())
            .anyMatch(x -> Objects.equals("1", x));
      }
    }
    ```  
* 9 <a name="GuardClauseRule" /> ``[GuardClauseRule]`` The guard statement simplifies the flow of the program by 
logically analyzing the original conditions and prioritizing some key (guard) conditions for judgment.\
For a detailed definition of the guard sentence, see：[https://en.wikipedia.org/wiki/Guard_(computer_science)](https://en.wikipedia.org/wiki/Guard_(computer_science/))\
``[卫语句]`` 卫语句是通过对原条件进行逻辑分析，将某些要害（guard）条件优先作判断，从而简化程序的流程。对于卫语句详细定义可\
见：[https://en.wikipedia.org/wiki/Guard_(computer_science)](https://en.wikipedia.org/wiki/Guard_(computer_science/))

    ```java
    public class Foo {
      public String bar() {
        // incorrect
        if(isOk()){
            doSomething();
            return "ok"; 
        }
        return "not ok"; // or throw new Exception("not ok");
    
        // incorrect
        if(isOk()){
            doSomething();
            return "ok";
        } else {
            return "not ok";// or throw new Exception("not ok");
        }
    
        // correct
        if(!isOk()){
            return "not ok";// or throw new Exception("not ok");
        }
    
        doSomething();
        return "ok";
      
        // correct
        if(isOk()){
          doSomething();
          return "ok";
        } else {
          doSomething();
          return "not ok";// or throw new Exception("not ok");
        }
      }
    }
    ```  
* 10 <a name="CollectorsToMapUnUseThirdParamRule" /> ``[CollectorsToMapUnUseThirdParamRule]`` Collectors
.toMap/toConCurrentMap will throw java.lang.IllegalStateException: Duplicate key error if the third parameter is not used and there is a 
duplicate key.\
``[Collectors.toMap未使用第三个参数]`` Collectors.toMap/toConCurrentMap如果未使用第三个参数碰到有重复key将会抛出
java.lang.IllegalStateException: Duplicate key错误

    ```java
    public class Foo {
        // correct
        private Map<String, String> convertExtensionOptions(List<KeyValuePairType> source) {
            return source.stream()
                    .filter(Objects::nonNull)
                    .filter(keyValue -> StringUtils.hasText(keyValue.getKey())
                        && Objects.nonNull(keyValue.getValue()))
                    .collect(Collectors.toMap(KeyValuePairType::getKey, KeyValuePairType::getValue, (key1, key2) -> key1));
                    //.collect(Collectors.toConcurrentMap(KeyValuePairType::getKey, KeyValuePairType::getValue, (key1, key2) -> key1));
        }
    
        // incorrect
        private Map<String, String> convertExtensionOptions(List<KeyValuePairType> source) {
            return source.stream()
                    .filter(Objects::nonNull)
                    .filter(keyValue -> StringUtils.hasText(keyValue.getKey())
                        && Objects.nonNull(keyValue.getValue()))
                    .collect(Collectors.toMap(KeyValuePairType::getKey, KeyValuePairType::getValue));
        }
    }
    ``` 