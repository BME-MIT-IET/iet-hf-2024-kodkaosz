Feature: MakeSticky
  Ez a teszteset a csövek ragadóssá tételét teszteli.
  Egy játékos ragadóssá teszi a csövet, és lelép róla.
  Ezután a következő játékos rálép a csőre, és megnézzük, hogy rajta ragad-e.
  Ezen kívül azt is ellenőrizzük, hogy pumpát ragadóssá tud-e tenni a játékos.

  Scenario: Fix pipe
    Given Saboteur is  on a pipe
    When Saboteur makes the element sticky
    Then The pipe is sticky

    When Saboteur moves to pump
    Then Move was successful

    When Saboteur makes the element sticky
    Then The pump is not sticky

    When Plumber moves to sticky pipe
    Then Move was successful

    When Plumber moves to pump
    Then Move was not successful

