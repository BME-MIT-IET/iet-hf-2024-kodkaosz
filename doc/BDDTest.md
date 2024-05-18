# BDD Test Dokumentáció

## Tesztek

### DamagePipeDamageable test
- Leírás:
Ez a teszteset a csövek kilyukasztását teszteli. Egy normális működőképes csövet próbál kilyukasztani.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A cső lyukas állapotát jelző tagváltozó át kell legyen állítva (isDamaged = true)

### DamagePipeNotDamageable test
- Leírás:
Ez a teszteset a csövek kilyukasztását teszteli. Egy már nemrég megjavított csövet próbál kilyukasztani a játékos.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A cső lyukas állapotát leíró tagváltozó false kell maradjon (isDamaged = false)
    - A cső fixedTime tagváltozója nem nulla (fixedTime > 0)

### FixPump test
- Leírás:
Ez a teszteset a pumpák javítását teszteli. Egy elromlott pumpát próbál javítani a játékos.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A pumpa romlott állapotát leíró tagváltozó értéke false a javítást követően (isDamaged = false)

### FixPipe test
- Leírás:
Ez a teszteset a csövek befoltozását teszteli. Egy elromlott csövet próbál befoltozni egy játékos.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A cső lyukas állapotát leíró tagváltozó értéke false a befoltozást követően (isDamaged = false)
    - A cső fixedTime tagváltozó értéke nem nulla (fixedTime > 0)

### PickUpPipe test
- Leírás:
Ez a teszteset a csővégek lecsatlakoztatását és felcsatlakoztatását teszteli. Egy pumpán lévő játékos megpróbál lecsatlakoztatni egy csővéget, aztán újra felcsatlakoztatja. A felcsatlakoztatást követően, a játékos újból felveszi a csővéget, átlép egy másik pumpára, és ott is megpróbálja lecsatlakoztatni az egyik csővéget.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A cső felvett állapotát leíró tagváltozó értéke true a lecsatlakoztatást követően (isPickedUp = true)
    - A játékos pickedUpPipe referenciája nem null (pickedUpPipe != null)
    - A felcsatlakoztatást követően, a játékos pickedUpPipe referenciája null és a felcsatlakoztatott cső isPickedUp tagváltozója false
    - Ha már van egy felvett csővég a játékosnál, nem tud felvenni újat

### PlaceNewPump test
- Leírás:
Ez a teszteset a ciszternák által generált pumpák felvevését, illetve lerakását teszteli. Egy ciszternán lévő játékos megpróbál egy új pumpát felvenni és lerakni egy csőre. Ez a teszteset azt is teszteli, hogy ha nincs a játékosnál egy felvett pumpa, akkor természetesen nem sikerül a letevés, illetve, ha már felvett a játékos egy pumpát, nem tud felvenni még egy pumpát.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - Sikeresen generált egy pumpát a ciszterna
    - A generált pumpa isPickedUp tagváltozója megfelelően változik a felvevést követően (isPickedUp = true)
    - A játékos pickedUpPump referenciája nem null (pickedUpPump != null)
    - Ha a játékosnak nincs pumpája (a letevést követően), akkor nem sikerül letenni egy pumpát

### MoveToFree test
- Leírás:
Ez a teszteset a játékosok mozgását teszteli. A teszteset minden elemre való lépést teszteli, de egyelőre csak szabad elemek vannak.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - Sivatagra meg forrásra nem tud lépni a játékos
    - Minden más elemre sikeres a move függvény hívás
    - Minden lépéskor a játékos element referenciája megváltozik 

### MoveToOccupied test
- Leírás:
Ez a teszteset a játékosok mozgását teszteli, de itt van foglalt elem is.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - Foglalt csőre való lépéssel próbálkozás esetén sikertelen a move függvény hívás
    - A játékos element referenciája nem változhat


### MakeSticky test
- Leírás:
Ez a teszteset a csövek ragadóssá tételét teszteli. Egy játékos ragadóssá teszi a csövet, és lelép róla. Ezután a következő játékos rálép a csőre, és megnézzük, hogy rajta ragad-e. Ezen kívül azt is ellenőrizzük, hogy pumpát ragadóssá tud-e tenni a játékos.

- Ellenőrzött funkcionalitás, várható hibahelyek:
    - A játékos, aki ragadóssá tette a csövet, el tud-e lépni a csőről (azt várjuk, hogy igen)
    - A  következő játékos rajta ragad-e a csövön (azt várjuk, hogy igen)
    - A cső isSticky változója true lesz-e (azt várjuk, hogy igen)
    - A pumpát ragadóssá lehet-e tenni (azt várjuk, hogy nem)

## Összefoglalás
A feladata során megismerkedtem a Behaviour Driven Development (BDD) tesztek használatával és hasznosságával.
A BDD megkönnyíti a kommunikációt a fejlesztő és a nem műszaki vagy üzleti résztvevők között egy szoftverkészítő projektben, a mindenki számára könnyen érthető angolszerű mondatok használatával. 3 fő kulcsszava: Given, When, Then.










