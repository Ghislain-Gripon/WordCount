Feature: Program that counts occurrences of words in text

  @mapreduce
  Scenario Outline: :
    Given there is a configuration file at "<path>"
    And the configuration file is read
    And there is text
    When text is read
    Then text is given to the program
    Then the program returns a list of words and their occurrence figure

  Examples:
    |         path         |
    |                      |
    | src/data/Config.yaml |

