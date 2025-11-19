Feature: DB

  Background: Database connection
    * Database connection established
  @DB
  @US1  # Calculate the total meeting time in minutes for the user email=oske.work@gmail.com in the users table.
  Scenario: Calculate the total meeting time in minutes for the user

    * Calculate the total meeting time in minutes for the user "email" in the  "users" table.
    * Verify the information Results are obtained.
    * Database connection is closed

  @US2  #List the total number and ratio of Active (Open),  pending and finished  meetings in the reserve_meetings table
  Scenario Outline: List the total number and rate of meetings in the reserve_meetings table by status
    Given Preparation of query grouping according to statuses in the reserved meeting table
    Then The status should be "<status>"
    And The total_meetings should be <total_meetings>
    And The percentage should be <percentage>

    Examples:
      | status   | total_meetings | percentage |
      | open     | 1              | 0.9091     |
      | pending  | 84             | 76.3636    |
      | finished | 25             | 22.7273    |