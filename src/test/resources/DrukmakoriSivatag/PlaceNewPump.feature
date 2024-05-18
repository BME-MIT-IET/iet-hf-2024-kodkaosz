Feature: PlaceNewPump
  Ez a teszteset a ciszternák által generált pumpák felvevését, illetve lerakását teszteli.
  Egy ciszternán lévő játékos megpróbál egy új pumpát felvenni és lerakni egy csőre.
  Ez a teszteset azt is teszteli, hogy ha nincs a játékosnál egy felvett pumpa,
  akkor természetesen nem sikerül a letevés, illetve, ha már felvett a játékos egy pumpát,
  nem tud felvenni még egy pumpát.
  Scenario: Place new pump
    Given Plumber is  on the watertank
    When Plumber picks up pump
    Then The pump pick up was successful
    Then Pump is picked up
    Then Plumber has a pump

    When Plumber picks up pump
    Then The pump pick up was not successful
    Then Pump is picked up
    Then Plumber has a pump

    Given Plumber moves to a pipe
    When Plumber connects pump
    Then The pump connection was successful
    Then Pump is not picked up
    Then Plumber has no pump

    When Plumber connects pump
    Then The pump connection was not successful