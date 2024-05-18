Feature: DamagePipeNotDamageable
  Ez a teszteset a csövek kilyukasztását teszteli.
  Egy már nemrég megjavított csövet próbál kilyukasztani a játékos.

  Scenario: Damage not damageable pipe
    Given Saboteur is  on a not damageable pipe
    When Saboteur damages the pipe
    Then FixedTime is greater than 0
    Then The pipe is not damaged