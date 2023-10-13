<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# PMDPlugin Changelog

## [1.0.7]
### Added
-Add Customization Rules(see detail readme.md)
  - AvoidOperateExternalVariInStreamForeachStmtsRule
  
### Changed
- All Customization rules remove violation checks for Test classes (classes that require the beginning of Test or the 
end of Test, Tests and TestCase) or methods marked with @Test
  - add base class FlightCustomizationRule to identify and process Test class and @Test identification method
- AvoidUseSetFuncInGetMethodRule adds violation interception for xxxx.XXX().setXXX() or xxxx.XXX().XXX()...setXXX()
- Reconstruct the judgment logic of whether it is a stream expression in the base class FlightStreamExpressionRule
- Refactor the implementation of AvoidStreamExpressionInIfStmtsRule
- Refactor partial implementation of AvoidUseComplexStreamExpressionInSetMethodRule
- CommentRequiredRule No violation will be prompted when there is no comment for fields injected by Spring beans (including constructor injection)

### BugFix
- Fixed the problem that when the 'AvoidUseSetFuncInGetMethodRule' rule determines that there is a prohibited method in 
a lambda expression, all other methods that need to be checked will be judged as violations.
- Fix 'StreamExpressionStyleRule' When a class in the same directory has a stream expression style violation, 
the correct stream expression style in other classes in the directory will also be judged as a violation.
- Fixed 'AvoidTernaryNestedTernaryRule' not being recognized as a violation when nested ternary expressions have parentheses.

## [1.0.6]
### Changed
- PMD dependencies to com.ctrip.flight.mobile.pmd:pmd-java:1.0.6
- The version and the dependent pmd-java version are 
- StreamExpressionStyleRule special processing is done for Optional.get().getXXX(), and it will not be judged as a 
violation without line breaks.
- AvoidStreamExpressionInIfStmtsRule for the isPresent or get().getXXX of the stream object, such as if(Optional
.isPresent()), no violation will be determined
- AvoidUseComplexStreamExpressionInSetMethodRule Add the threshold parameter 'complexStreamLayerNum' for judging complex
 stream expressions (default threshold 1).
  - Only when the level of the stream expression exceeds the configured value will it be determined to be a complex 
 stream expression.for example: model.setHasTicket(listStream.anyMatch(...)), the level of the stream expression in 
 this scenario is only 1.
  - At the same time, the judgment of the number of lines used is also added to the entire flow expression. If the 
 number of lines used exceeds the configuration, it will also be judged as a complex flow expression.for example: the 
 conditional judgment statement in anyMatch has line breaks.
- ConditionalTooLongNeedChangeLineRule change threshold to 80.
### Added
- Add Customization Rules(see detail readme.md)
  - TernaryChangeLineRule
  - AvoidTernaryNestedTernaryRule
  - AvoidYodaConditionRule
  - AvoidUseSetFuncInGetMethodRule
  - AvoidGetEnumUseForStatementRule
  
## [1.0.4]
### Changed
- PMD dependencies to com.ctrip.flight.mobile.pmd:pmd-java:1.0.5
- The range of AvoidComplexConditionalRule has been increased, including bitwise OR (|), bitwise AND (&),  exclusive 
OR (^), right shift (>>>, >>), left shift (<<), not (!, ~)
- GuardClausesRule add statement complexity judgment. 'if' is followed directly by a 'return'('throw') statement, the 
complexity of the statement in the 'if' and the complexity of the 'return'('throw') statement will be judged.Only 'if' the 
complexity of the 'return'('throw') statement is less than the complexity of the statement in the 'if' statement will a 
violation be issued.
### Added
- Add Customization Rules
  - AvoidTooManyConditionRule
  - AvoidUseComplexStreamExpressionInSetMethodRule
  - AvoidUseUnSynchronizedFormatRule
  
## [1.0.3]
### Changed
- PMD dependencies to com.ctrip.flight.mobile.pmd:pmd-java:1.0.4
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
### BugFix
- add deprecated="true" in config xml,but PMD will continue to be performed.

  
## [1.0.2]
### Changed 
- PMD dependencies to com.ctrip.flight.mobile.pmd:pmd-java:1.0.2
- Refactored the implementation of the AvoidIfStmtsInSwitchStmtsRule
- ClassNamingConventionsRule add naming support for X products，for example: XProductClassName OneXProductClassName 
- LocalVariableCouldBeFinalRule add deprecated="true" in config xml
### Added
- Add Customization Rules
  - GuardClauseRule
  - CollectorsToMapUnUseThirdParamRule
  
## [1.0.1]
### Changed 
- PMD dependencies to com.ctrip.flight.mobile.pmd:pmd-java:1.0.1

## [1.0.0]
### Changed 
- Published 1.0.0 version
- PMD dependencies to com.ctrip.flight.mobile.pmd:pmd-java:1.0.0

## [0.0.3]
### Added
- Add Change Language Menu(Support switch zh-cn and en-en)

## [0.0.2]
### Added 
- Add violation rule's description area

## [0.0.1]
### Changed
- Base on https://github.com/amitdev/PMD-Intellij/blob/master/CHANGELOG.md develop
- Change PMD dependencies to customization com.ctrip.flight.mobile.pmd:pmd-java
- Change Plugin Name to "CodeQualityAnalysis"