<!-- Keep a Changelog guide -> https://keepachangelog.com -->
# pmd-java Changelog

## [1.0.8]
### Added
- 增加自定义规则
  - 叙事化段落规则(NarrativeParagraphRule)
### Changed
- 修改流表达式判断基类FlightStreamExpressionRule中对于参数、方法和内部变量是否是流表达式时候
  - 从只通过name判断变更为用className_name来判断
  - 原先通过全部toLowerCase和equalsIgnoreCase来判断是否相等，修改为不转为全小写和使用equals


## [1.0.7]
### Added
- 增加自定义规则
  - 避免在stream的forEach语句中操作外部变量(AvoidOperateExternalVariInStreamForeachStmtsRule)
  
### Changed
- 所有自定义规则去除对于Test类(需要Test开头或Test、Tests和TestCase结尾的类)或带有@Test标识方法的违规检查
  - 增加基类FlightCustomizationRule用来识别和处理Test类和@Test标识方法 
- 避免在get方法中进行set操作(AvoidUseSetFuncInGetMethodRule)增加对xxxx.XXX().setXXX()或xxxx.XXX().XXX()...setXXX()的违规拦截
- 重构基类FlightStreamExpressionRule中对于是否是stream表达式的判断逻辑
- 重构AvoidStreamExpressionInIfStmtsRule的实现
- 重构AvoidUseComplexStreamExpressionInSetMethodRule的部分实现
- 注释必填(CommentRequiredRule)对于Spring bean注入的字段(包含构造函数注入)没有注释时不提示违规

### BugFix
- 修复"避免在get方法中进行set操作(AvoidUseSetFuncInGetMethodRule)"规则在判断lambda表达式中存在禁止的方法时，会把其他所有需要检查的方法都判定为违规
  - ps:如果同一个类中的重载方法中的一个方法存在违规会把所有重载方法都判定为违规
- 修复"流表达式式样规则(StreamExpressionStyleRule)"当同一目录下某个类存在流表达式式样违规的场景下会把该目录下其他类中正确的流表示式样也判断为违规
- 修复"避免三元表达式嵌套三元表达式(AvoidTernaryNestedTernaryRule)"当嵌套的三元表达式带有括号的场景下未识别为违规

## [1.0.6]
### Changed
- 流表达式式样规则(StreamExpressionStyleRule)对于Optional.get().getXXX()做特殊处理，不换行也不会判定为违规
- 避免在if语句中使用流表达式规则(AvoidStreamExpressionInIfStmtsRule)对于流对象的isPresent或get().getXXX，例如if(Optional.isPresent()),不会判定违规
- 避免在set方法中传入复杂的流表达式(AvoidUseComplexStreamExpressionInSetMethodRule)增加判断复杂流表达式的阀值参数'complexStreamLayerNum'(默认阀值1）,
  - 只有当流表达式的层级超过配置值才会判定为是复杂流表达式，例如：model.setHasTicket(listStream.anyMatch(...)),该场景下流表达式的层级只有1.
  - 同时对于整个流表达式也增加了所用行数判定，如果所用行数超过该配置也会被判定为复杂流表达式(比如anyMatch中的条件判断语句有换行)。
- 条件语句太长需要换行(ConditionalTooLongNeedChangeLineRule)的阀值修改为80
### Added
- 增加自定义规则
  - 三元表达式换行规则(TernaryChangeLineRule)
  - 避免三元表达式中嵌套三元表达式(AvoidTernaryNestedTernaryRule)
  - 避免使用尤达条件表达式(AvoidYodaConditionRule)
  - 避免在get方法中进行set操作(AvoidUseSetFuncInGetMethodRule)
  - 避免在枚举中通过for语句获取枚举值(AvoidGetEnumUseForStatementRule)
  
## [1.0.5]
### Changed
- 避免复杂表达式规则(AvoidComplexConditionalRule)范围增加，包含位或(|)、位与(&)、异或(^)、右移(>>>、>>)、左移(<<)、非(!、~)。
- 卫语句规则(GuardClausesRule)增加语句复杂度判断，if后直接跟return(throw)语句，会判断if语句中的复杂度和return(throw)语句的复杂度，
只有return(throw)语句的复杂度小于if语句的复杂度才会报告违规。
### Added
- 增加自定义规则
  - 避免条件过多(AvoidTooManyConditionRule)
  - 避免在set方法中传入复杂的流表达式(AvoidUseComplexStreamExpressionInSetMethodRule)
  - 避免使用没有Synchronized关键字的Format类(AvoidUseUnSynchronizedFormatRule)

## [1.0.4]
### BugFix
- add deprecated="true" in config xml,but PMD will continue to be performed.

## [1.0.3]
### Changed
- add deprecated="true" in config xml contain rules
  - AvoidDuplicateLiteralsRule
  - AvoidLiteralsInIfConditionRule
  - UnSynchronizedStaticFormatterRule
  - UseObjectForClearerAPIRule
- update example contain rules
  - EqualsAvoidNullRule
  - UnusedFormalParameterRule
  - CognitiveComplexityRule
  - AssignmentToNonFinalStaticRule
  - NPathComplexityRule
- update threshold or level
  - TooManyMethodsRule update threshold to 45.
  - ExcessivePublicCountRule update threshold to 25.
  - AvoidCalendarDateCreationRule update level to 5.
- double brace initialization is possible in methods starting with test or ending with Test/Tests/TestCase.
- GenericsNamingRule is recommended to use uppercase letters for generic naming.no letters length limited.
- SimplifiedTernaryRule is only handle scene  "condition ? true : false".

## [1.0.2]
### Changed
- Refactored the implementation of the AvoidIfStmtsInSwitchStmtsRule
- ClassNamingConventionsRule Add naming support for X products，for example: XProductClassName OneXProductClassName 
- LocalVariableCouldBeFinalRule add deprecated="true" in config xml
### Added
- Add Customization Rules
  - GuardClauseRule
  - CollectorsToMapUnUseThirdParamRule
  
## [1.0.1]
### BugFix
- Fixed AvoidStreamExpressionInIfStmtsRule the problem that in some scenarios, local variables are decided to be 
stream expressions even if they are not stream expressions.
### Changed
- Remove the rule in StreamExpressionStyleRule that determines that a stream expression contains a stream expression
- add deprecated="true" in config xml contain rules
  - AbstractClassWithoutAbstractMethodRule
  - AvoidReassigningCatchVariablesRule
  - MethodReturnsInternalArrayRule
  - PrimitiveWrapperInstantiationRule
  - UseCollectionIsEmptyRule
  - UseStandardCharsetsRule
  - UseTryWithResourcesRule
  - UnnecessaryCastRule
  - UnnecessaryConstructorRule
  - UnnecessaryModifierRule
  - AvoidProtectedFieldInFinalClassRule
  - AvoidProtectedMethodInFinalClassNotExtendingRule
  - EmptyControlStatementRule
  - EmptyMethodInAbstractClassShouldBeAbstractRule
  - AvoidRethrowingExceptionRule
  - AvoidThrowingNewInstanceOfSameExceptionRule
  - AvoidThrowingNullPointerExceptionRule
  - ClassWithOnlyPrivateConstructorsShouldBeFinalRule
  - CommentContentRule
  - ClassCastExceptionWithToArrayRule
  - ImplicitSwitchFallThroughRule
  - InstantiationToGetClassRule
  - UnconditionalIfStatementRule
  - StringInstantiationRule
  - StringToStringRule
### Added
- Add Customization Rules
  - AvoidStreamExpressionInStreamExpressionRule

## [1.0.0]
### Changed 
- Published 1.0.0 version

## [0.0.3]
### Added
- Added Customization Rules And UnitTest
  - StreamExpressionStyleRule
  - ConditionalTooLongNeedChangeLineRule
  - AvoidComplexConditionalRule
  - StreamExpressionTooLongRule
  - AvoidStreamExpressionInIfStmtsRule
  - UndefinedMagicConstantRule
  - AvoidIfStmtsInSwitchStmtsRule
  
## [0.0.2]
### Changed 
- Support customization Intellij IDEA Plugin for PMD detection

## [0.0.1]
### Added
- Added zh-cn and en-en language support
### Changed
- Base on PMD 6.55.0 develop [https://github.com/pmd/pmd/tree/pmd_releases/6.55.0](https://github.com/pmd/pmd/tree/pmd_releases/6.55.0)
- Filtered some rules (see: readme.md for details) 