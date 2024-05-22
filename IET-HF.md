# IET Házi feladat
## Feladat funkcióinak leírása

A játék során a drukmákori sivatag bonyolult vízvezetékrendszerén közlekednek a játékosok. A rendszer vizet szállít a hegyi forrásokból a sivatagon túl elterülő városok ciszternáiba. A vezetékhálózatnak több aktív eleme van (forrás, ciszterna, napelemmel működő vízátemelő pumpa), amelyeket elágazás nélküli (vagyis pontosan két véggel rendelkező) csövek kötnek össze. A pumpák ezzel szemben véges sok cső közös csomópontjaiként szolgálnak, és minden pumpán külön-külön állítható, hogy éppen melyik belekötött csőből melyik másik csőbe pumpáljon, azonban egyszerre csak egy bemenete és egy kimenete lehet, a többi rákötött cső eközben el van zárva. Véletlen sorsolt időközönként egy-egy pumpa, egymástól függetlenül elromlik ilyenkor megszűnik benne a vízáramlás, vagyis a kimenetre kötött csőbe nem juttat el több vizet. Minden pumpa rendelkezik egy víztartállyal, ez egy olyan átmeneti víztározó, amelyet a víz egyik csőből másikba való eljuttatásához használ. A csövekhez tartozik egy kapacitás, amely annak a mértéke, hogy mennyi víz tud egy időben keresztülfolyni rajta. Ha eléri ezt a maximális vízszintet, akkor a pumpa, amelyiknek a kimenetére van kötve nem tud vizet áramoltatni rajta keresztül, egészen addig amíg amíg a cső másik végén lévő pumpa nem szállítja át a víz egy bizonyos részét egy másik csőbe. 
A játékot két csapat játszhatja, mindkét csapatban legalább 2 játékosnak kell lennie. A csapatok két különböző szerepet töltenek be, az egyik csapat nomád szabotőrökből áll, akiknek a célja, hogy a rendszerben a lehető legtöbb csövet kilyukasszák, ezzel minél több vizet elfolyatva. Ha a szabotőr kilyukaszt egy csövet, és abból a lyukon keresztül kifolyik a víz a vízvezetékrendszerből, az azt jelenti, hogy a cső kimenetén lévő pumpához már nem jut el a vízáramlás. A szabotőrök a csöveket csúszossá tudjak tenni, így ha valaki erre a csőre szeretne lépni, helyette az egyik szomszédos mezőre fog átcsúszni. Az ellenfél gárdát pedig vízvezeték-szerelők alkotják, akiknek a dolga ezt megakadályozni a rendszer karbantartásával. Ezt többek között a vízvezetékrendszer elemeinek megszerelésével tehetik meg, vagyis a szabotőrök által kilyukasztott csövek megfoltozásával, illetve az elromlott pumpák helyreállításával. A vízvezeték-szerelők továbbá a ciszternáknál új pumpákat tudnak magukhoz venni, amelyeket a vízvezetékrendszerbe építeni úgy képesek, hogy egy csövet kettévágnak, és az így újonnan kapott két cső közé behelyezik azt. Mind a szabotőrök, mind pedig a szerelők is képesek átállítani a pumpákat, vagyis kiválasztani, hogy a pumpára kötött csövek közül melyikből melyikbe haladjon az áramlás. Ha a víz végigér a nem meghibásodott csöveken keresztül a vízvezetékrendszeren, akkor a ciszternákba folyik, és a vízvezetékszerelők vízkészlete bővül. Mindketten ragadóssá tudják továbbá tenni a csöveket, ekkor a ragadós csőre lépett játékos egy random időre a csövön ragad.



A vízvezetékrendszer kialakítása nem állandó, a játék során lehet különféle elemekkel bővíteni. A csövek kellőképp rugalmasak ahhoz, hogy az egyik végüket le lehessen csatlakoztatni a hozzá tartozó pumpáról, és felcsatlakoztatni egy másik pumpára (Kivéve ahhoz, amelyikre csatlakozik a másik vége, minden cső két különböző pumpát kell, hogy összekössön), erre kizárólag a vízvezeték-szerelők képesek. A játék menete közben folyamatosan készülnek új csövek a ciszternáknál, amelyek egyik vége egyből közvetlenül a ciszternára csatlakozik rá, másik vége pedig szabad, így amíg nincsenek hozzácsatlakoztatva a vízvezetékrendszer egy eleméhez, addig a ciszternákban összegyűjtött víz a sivatagi a homokba folyik. Az ciszternákat ilyen módon elhagyó víz nem kerül pontként felszámolásra a szabotőrök csapatának, csakis a lyukas csöveken kiszivárgó. Azonban, mivel a vízvezetékszerelők pontjai a ciszternákban lévő víz mennyisége határozza meg, ezért a víz kifolyásával egyidőben a szerelő csapat pontjainak a száma csökken.
A lyukas csöveken kifolyó víz a szabotőröknek kerül felszámolásra, a vízvezetékszerelők csapata pedig a ciszternákba befolyó víz alapján kapnak pontokat. A játék végén pedig az a csapat nyer, amelyik neve mellett több víz szerepel. 
Mivel a sivatag veszélyes hely, a szerelők és a szabotőrök csak a vízvezetékrendszer elemein haladhatnak. A pumpáknál kikerülhetik egymást, de a csöveken már nem tudnak elmenni egymás mellett. Egy csövön egyszerre csak egy ember állhat, vagyis amikor egy játékos el szeretne lépni egy csövön keresztül egy másik pumpára, akkor csak azon csövek közül választhat, amelyiken épp nem áll senki.
 
## Választott verzió

A projekt Proto verzióját választottuk a házi feladat elvégzésére, tehát nincsen grafikus megjelenítés, csak konzolos.
A konzolon lehet különböző parancsokat kiadva irányítani a játékot.

### Kiadható parancsok

A dbg prefixű parancsok csak debug módban indított játékokon használhatók.

#### load $path$ $rand$
- Leírás: Betölti a megadott pályát.
- Opciók: 
    - $path$: betöltendő pálya elérési útvonala, nem tartalmazhat szóközt
	- $rand$:	
        - random – a játék pszeudorandom értékeket használ, ahol szükséges
		- debug – a játékban minden döntés a felhasználó által dönthető el, a periodikus események külön paranccsal válthatók ki.

#### save $path$
- Leírás: A bemeneti formátummal megegyező formában kiírja az objektumok aktuális állapotát a kimenetre.
- Opciók: 	—

#### state $object$
- Leírás: Kiírja a megadott elemről rendelkezésre álló adatokat.
- Opciók: 
    - $object$: a kiíradnó objektum

#### exit 
- Leírás: Megszakítja az adott játékmenetet, a load paranccsal lehet új játékot indítani.
- Opciók: —

#### move $player$ $dir$
- Leírás: Megadott játékossal megpróbálunk lépni a megadott irányba.
- Opciók: 	
    - $player$: léptetendő játékos
	- $dir$: léptetés iránya

#### damage $player$
- Leírás: A megadott játékos megpróbálja kilyukasztani az elemet, amin áll. Ha nem csövön áll, nem történik semmi.
- Opciók: 
    - $player$: játékos, aki lyukasztani próbál

#### repair $player$
- Leírás: A megadott játékos megpróbálja megjavítani az elemet, amin áll.
- Opciók: 
    - $player$: játékos, aki javítani próbál
 
 
#### pickUp $type$ $player$ 
- Leírás: Az adott játékos megpróbál felvenni megadott típusú elemet.
- Opciók: 
    - $type$:	
        - pipe: csövet próbáljon felvenni a játékos
	    - pump: pumpát próbáljon meg felvenni a játékos
	- $player$: játékos, aki megpróbálja felvenni az elemet

#### connectPipeEnd <player>
- Leírás: A megadott játékos csatlakoztatja (valamelyik) nála lévő csővéget az elemhez, amin áll, ha az engedi.
- Opciók: 
    - $player$: játékos aki csatlakoztatni próbál

#### addPump $player$ 
- Leírás: A megadott játékos megpróbálja lerakni a nála lévő pumpát (már ha van nála) az elemre, amin áll.
- Opciók: 
    - $player$:  játékos aki megpróbálja lerakni a pumpát

#### redirect $player$ $from$ $to$
- Leírás: A megadott játékos megpróbálja átirányítani a pumpát, amin áll, ha valóban pumpán áll.
- Opciók:	
    - $player$: aki megpróbálja árirányítani a pumpát
	- $from$: melyik elemből pumpáljon a pumpa
	- $to$: melyik elembe pumpáljon a pumpa

#### makeSlippery $player$
- Leírás: A megadott játékos megpróbálja csúszóssá tenni az elemet, amin áll.
- Opciók: 
    - $player$: a kísérletet végző játékos.

#### makeSticky $player$
- Leírás: A megadott játékos megpróbálja ragadóssá tenni az elemet, amin áll.
- Opciók: 
    - $player$: a kísérletet végző játékos.

#### plumberPoints 
- Leírás: Kiírja a szerelők aktuális pontszámát.
- Opciók: —

#### saboteurPoints 
Leírás: Kiírja a szabotőrök aktuális pontszámát.
Opciók: —

#### dbgTick $Tickable$
- Leírás: A megadott objektumon meghívja a tick() függvényt.
- Opciók: 
    - $Tickable$: az a Tickable objektum, amin a tick() függvényt szeretnénk hívni

#### dbgTickAll
- Leírás: Minden Tickable interfészt megvalósító objektumon meghívja a tick() függvényt.
- Opciók: —


#### dbgCreatePipe $WaterTank$
- Leírás: Új csövet hoz létre a megadott ciszternán.
- Opciók: 
    - $WaterTank$: a ciszterna, amin új csövet akarunk létrehozni

#### dbgCreatePump $WaterTank$
- Leírás: Új pumpát hoz létre a megadott ciszternán.
- Opciók: 
    - $WaterTank$: a ciszterna, amin új pumpát akarunk létrehozni

#### dbgDamage $type$ $element$ 
- Leírás: Elrontja a megadott elemet. Abban különbözik a damage utasítástól, hogy itt akkor is elronthatunk egy elemet, ha nem áll rajta senki, illetve, ha az adott elem Pumpa.
- Opciók: 	
    - $type$:
        - Pipe: csövet akarunk elrontani
		- Pump: pumpát akarunk elrontani
	- $element$: maga az elem, amit el akarunk rontani

#### dbgSetLeakable $pipe$ $remainingImmunity$ 
- Leírás: Beállítja, hogy az adott cső meddig ne lehessen kilyukasztható. 0 esetén azonnal kilyukaszthatóvá válik a cső.
- Opciók:	
    - $pipe$: a cső, amit manipulálni szeretnénk
	- $remainingImmunity$: meddig legyen kilyukaszthatatlan a cső
