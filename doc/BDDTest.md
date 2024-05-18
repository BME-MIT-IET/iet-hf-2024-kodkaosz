# BDD Test Dokumentáció

## DamagePipeDamageable test
- Leírás:
Ez a teszteset a csövek kilyukasztását teszteli. Egy normális működőképes csövet próbál kilyukasztani.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A cső lyukas állapotát jelző tagváltozó át kell legyen állítva (isDamaged = true)

## DamagePipeNotDamageable test
- Leírás:
Ez a teszteset a csövek kilyukasztását teszteli. Egy már nemrég megjavított csövet próbál kilyukasztani a játékos.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A cső lyukas állapotát leíró tagváltozó false kell maradjon (isDamaged = false)
    - A cső fixedTime tagváltozója nem nulla (fixedTime > 0)

## FixPump test
- Leírás:
Ez a teszteset a pumpák javítását teszteli. Egy elromlott pumpát próbál javítani a játékos.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A pumpa romlott állapotát leíró tagváltozó értéke false a javítást követően (isDamaged = false)


