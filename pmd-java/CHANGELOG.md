<!-- Keep a Changelog guide -> https://keepachangelog.com -->
# pmd-java Changelog

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