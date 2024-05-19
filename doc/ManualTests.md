# Manuális tesztek Dokumentációja

## Tesztek

### Cső végének felvétele
#### Leírás:
Ez a teszteset a csővégek lecsatlakoztatását teszteli. Egy ciszternán lévő játékos
megpróbál felvenni egy csővéget.

#### Bemenet
load PickUpPipeFromOtherElement.dat debug\
pickUp Pipe plumber\
state pipe\
state plumber

#### Kimenet

Pálya betöltése sikeres\
Elem felvétele megtörtént. \
Pipe pipe
neighbors:waterTank;isPickedUp:true;capacity:0;waterLevel:0;isDamaged:false;isSlippery:false;fixedTime:0;isSticky:false\
Plumber plumberstuckCounter:0;pickedUpPipe:pipe;pickedUpPump:null;element:waterTank

### Cső mindkét végének felvétele
#### Leírás:
Ez a teszteset mindkét csővég lecsatlakoztatását teszteli egyidőben. Egy szerelő az
egyik cső végén lévő pumpáról felveszi a csővéget amíg egy másik szerelő túloldalt
szintén felveszi a másik csővéget.

#### Bemenet
load PickUpBothPipeEnds.dat debug\
pickUpPipeEnd plumber 0\
pickUpPipeEnd plumber2 0\
state pipe\
state plumber\
state plumber2

#### Kimenet

Pálya betöltése sikeres.\
Csővég felvétele megtörtént.\
Csővég felvétele megtörtént.\
Pipe pipe
neighbors:pump,pump2;isPickedUp:true;capacity:0;waterLevel:0;isDamaged:false;isSlippery:false;fixedTime:0isSticky:false\
Plumber plumber
stuckCounter:0;pickedUpPipe:pipe;pickedUpPump:null;element:pump\
Plumber plumber2
stuckCounter:0;pickedUpPipe:pipe;pickedUpPump:null;element:pump2

### Cső felvétele másik mezőn állva
#### Leírás:
Ez a teszteset a csővégek lecsatlakoztatását teszteli. Egy nem pumpán lévő játékos
megpróbál felvenni egy csövet.

#### Bemenet
load PickUpPipeFromOtherElement.dat debug\
pickUp Pipe plumber\
pickUpPipeEnd plumber 0\
state pipe\
state plumber

#### Kimenet

Pálya betöltése sikeres.\
Elem felvétele sikertelen.\
Csővég felvétele sikertelen.\
Pipe pipe neighbors:waterSource,waterTank;isPickedUp:false;capacity:0;waterLe
vel:0;isDamaged:false;isSlippery:false;fixedTime:0;isSticky:false\
Plumber plumber stuckCounter:0;pickedUpPipe:null;pickedUpPump:null;element:waterTank

### Víz lefolyása
#### Leírás:
Ez a teszteset azt teszteli, hogy egy teljesen ép pályán, ahol létezik út a forrás és a
ciszterna között, a megfelelő módon áramlik-e a víz, valamint, hogy ennek
megfelelően inkrementálódik a szerelők pontszáma.

#### Bemenet
load PropagateWater.dat debug\
dbgTickAll\
state waterTank\
plumberPoints

#### Kimenet

Pálya betöltése sikeres.\
tickAll.\
WaterTank waterTank neighbors:pipe1;isPickedUp:false;waterLevel:5\
A szerelők pontszáma: 5.

### Víz elfolyása nem csatlakozatott csőből
#### Leírás:
Ez a teszteset teszteli, hogy képes-e létrehozni új csövet a ciszterna és hogy ezen
megfelelően kifolyik e a benne lévő víz, ha szabad a vége. Azt is ellenőrzi, hogy
ennek a következményeképp helyesen változik-e a csapatok pontszáma.

#### Bemenet
load SeepWaterFromPipeEnd.dat debug\
dbgCreatePipe waterTank\
state newPipe\
state waterTank\
plumberPoints\
saboteurPoints\
dbgTickAll\
state waterTank\
plumberPoints\
saboteurPoints

#### Kimenet

Pálya betöltése sikeres.\
Cső létrehozva.\
Pipe newPipe neighbors:waterTank,desert;isPickedUp:false;capacity:5;waterLevel:
0;isDamaged:false;isSlippery:false;fixedTime:0;isSticky:false\
WaterTank waterTank neighbors:newPipe;isPickedUp:false;waterLevel:10\
A szerelők pontszáma: 10.\
A szabotőrök pontszáma: 0.\
TickAll.\
WaterTank waterTank neighbors:newPipe;isPickedUp:false;waterLevel:5\
A szerelők pontszáma: 5.\
A szabotőrök pontszáma: 5.

### Víz elfolyása lyukas csőből
#### Leírás:
Ez a teszteset a lyukas csövön történő vízáramoltatáshoz kapcsolódó működéseket
ellenőrzi.

#### Bemenet
load SeepWaterFromPipe.dat debug\
dbgTickAll\
state pipe\
state waterTank\
plumberPoints\
sabouteurPoints

#### Kimenet

Pálya betöltése sikeres.\
TickAll.\
Pipe pipe neighbors:waterSource,waterTank;isPickedUp:false;capacity\
:5;waterLevel:0;isDamaged:true;isSlippery:false;fixedTime:0;isStic\
ky:false\
WaterTank waterTank neighbors:pipe;isPickedUp:false;waterLevel:0\
A szerelők pontszáma: 0.\
A szabotőrök pontszáma 5.

### Pumpa irányának átkapcsolása
#### Leírás:
Ez a teszteset a pumpák átirányítását teszteli. Megpróbál egy megengedett átállítást,
illetve egy nem megengedettet végrehajtani.

#### Bemenet
load RedirectPump.dat debug\
redirect playerOnPump damagedPipe endPipe1\
dbgTickAll\
state waterTank2\
redirect playerOnPump goodPipe endPipe2\
dbgTickAll\
state waterTank1\
state waterTank2\
redirect playerOnPump goodPipe goodPipe

#### Kimenet

Pálya betöltése sikeres.\
Az átirányítás sikeres.\
TickAll.\
WaterTank waterTank1 neighbors:endPipe1;isPickedUp:false;waterLevel:0\
Az átirányítás sikeres.\
TickAll.\
WaterTank waterTank1 neighbors:endPipe1;isPickedUp:false;waterLevel:0\
WaterTank waterTank2 neighbors:endPipe2;isPickedUp:false;waterLevel:5\
Az átirányítás sikertelen.

### Pumpa irányának átkapcsolása
#### Leírás:

Ez a teszteset a csövek csúszóssá tételét teszteli. Egy játékos csúszóssá teszi a csövet,
és lelép róla. Ezután a következő játékos rálép a csőre, és megnézzük, hogy
átcsúszik-e. Ezen kívül azt is ellenőrizzük, hogy pumpát csúszóssá tud-e tenni a
játékos.\
A kiinduló állapotban a saboteur a pipe-on áll, majd onnan lép a pump1-re. A victim a pump2-ről indul. Először megpróbál ennek a 0. szomszédjára (pipe) lépni, ekkor csúszik a pump1-re. Ezután megpróbál a pump1 1. szomszédjára lépni, ami szintén a pipe, ami csúszós, emiatt annak a 0. szomszédjára (mert debug módban vagyunk,
nem randomban) csúszik, tehát marad a pump1-en.

#### Bemenet
load MakeSlippery.dat debug\
makeSlippery saboteur\
state pipe\
move saboteur 0\
move victim 0\
move victim 1\
makeSticky saboteur

#### Kimenet

Pálya betöltése sikeres.\
Csúszóssá tevés sikeres.\
Pipe pipe neighbors:pump1,pump2;isPickedUp:false;capacity:5;waterLevel:0;isDamaged:false;isSlippery:true;fixedTime:0;isSticky:false\
Új pozíció: pump1.\
Új pozíció: pump1.\
Új pozíció: pump1.\
Csúszóssá tevés sikertelen.

