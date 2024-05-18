Feature: DamagePipeDamageable
  Ez a teszteset a csövek kilyukasztását teszteli. Egy normális működőképes csövet próbál kilyukasztani.

  Scenario: Damage damageable pipe
    Given Saboteur is  on a damageable pipe
    When Saboteur damages the pipe
    Then The pipe is damaged