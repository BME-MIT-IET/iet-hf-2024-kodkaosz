Feature: MoveToOccupied
  Ez a teszteset a játékosok mozgását teszteli, de itt van foglalt elem is.

  Scenario: Move to occupied
    Given Saboteur is  on a pump
    When Saboteur moves to occupied pipe
    Then Move was not successful