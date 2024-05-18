Feature: PickUpPipe
  Ez a teszteset a csővégek lecsatlakoztatását és felcsatlakoztatását teszteli.
  Egy pumpán lévő játékos megpróbál lecsatlakoztatni egy csővéget, aztán újra felcsatlakoztatja.
  A felcsatlakoztatást követően, a játékos újból felveszi a csővéget, átlép egy másik pumpára,
  és ott is megpróbálja lecsatlakoztatni az egyik csővéget.
  Scenario: Pick up pipe
    Given Plumber is  on a pump
    When Plumber picks up pipe
    Then The pick up was successful
    Then Pipe1 is picked up
    Then Plumber has a pipe

    When Plumber connects pipe
    Then The connection was successful
    Then Pipe1 is not picked up
    Then Plumber has no pipe

    When Plumber picks up pipe
    Then The pick up was successful
    Then Pipe2 is picked up
    Then Plumber has a pipe

    Given Plumber moves to another pump
    When Plumber picks up pipe
    Then The pick up was not successful
    Then Pipe1 is not picked up
    Then Plumber has a pipe