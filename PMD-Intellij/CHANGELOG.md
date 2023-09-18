<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# PMDPlugin Changelog

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