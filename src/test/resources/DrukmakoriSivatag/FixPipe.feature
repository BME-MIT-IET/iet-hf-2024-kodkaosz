Feature: FixPipe
  Ez a teszteset a csövek befoltozását teszteli.
  Egy elromlott csövet próbál befoltozni egy játékos.
  Scenario: Fix pipe
    Given Plumber is  on a damaged pipe
    When Plumber fixes the pipe
    Then The pipe is not damaged
    Then FixedTime is greater than 0