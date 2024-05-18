Feature: FixPump
  Ez a teszteset a pumpák javítását teszteli. Egy elromlott pumpát próbál javítani a játékos.

  Scenario: Fix pump
    Given Plumber is  on a damaged pump
    When Plumber fixes the pump
    Then The pump is not damaged