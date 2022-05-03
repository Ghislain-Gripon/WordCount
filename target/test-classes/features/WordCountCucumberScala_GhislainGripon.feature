Feature: Program that counts occurrences of words in text
  Background:
    Given there is a configuration file.
    And there is a mapreducer
    And there is text

  Scenario: Program gets text
    When text is given to the program
    Then the program returns a list of all the words and the number of occurrences
