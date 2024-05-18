Feature: MoveToFree
  Ez a teszteset a játékosok mozgását teszteli.
  A teszteset minden elemre való lépést teszteli, de egyelőre csak szabad elemek vannak.
  Scenario: Move to free
    Given Saboteur is  on the watertank
    When Saboteur moves to pipe1
    Then Move was successful

    When Saboteur moves to pump
    Then Move was successful

    When Saboteur moves to pipe2
    Then Move was successful

    When Saboteur moves to desert
    Then Move was not successful

    When Saboteur moves to pump
    Then Move was successful

    When Saboteur moves to pipe3
    Then Move was successful

    When Saboteur moves to watersource
    Then Move was not successful
