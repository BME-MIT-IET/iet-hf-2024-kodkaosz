# Statikus kódellenőrzés SonarCloud segítségével

A feladat célja a kódbázisban található hibák statikus kódellenőrző eszközzel való felderítése, majd kijavítása volt.

## Kiinduló állapot

A projekt futtatható állapotra hozása után a SonarCloud 162 hibát jelzett a kódbázisban, melyek javítására a becsült idő 2 nap és 6 óra volt, az áttekintés oldalon megjelenített kategóriák szerint B, B és E minősítéseket adott. A kódbázis minden kategóriában és súlyosságban tartalmazott hibákat.

## Ellenőrzés menete

Az eszköz által jelzett hibákat minden esetben ellenőriztük, hogy valós hibák-e, illetve, hogy a projekt kontextusában is olyan hibák-e, melyek javításra szorulnak, vagy esetlegesen valamilyen üzemszerű, egyedi működést vélt hibának. Az esetek túlnyomó többségében valós hibákat jelzett az eszköz, azonban előfordult olyan eset is, amikor a "hiba" a kóddal szemben támasztott eredeti követelményekből (kommentezési forma) fakadt, ezekben az esetekben ezeket elutasítottam.

A kézi ellenőrzés után minden jelzett hibához hibajegy került felvételre, amely hivatkozott a hiba helyére, tartalmazta a hiba okát, illetve a triviális eseteken kívül megoldási javaslatokat is. A hibák megoldása külön branchen történt, majd pull request formájában került véleményezésre, majd elfogadásra a megoldás.

## Eredmény

A javítások után az eszköz már minden kategóriában "A" minősítést ad a kódra. A fennmaradó figyelmeztetések mind alacsony prioritásúak, javításuk nem nélkülözhetetlen a projekt szempontjából, újdonságot nem jelentenek a feladat szempontjából sem.

## Konklúzió

A használt statikus ellenőrző eszköz számos olyan hibát is feltárt, melyek dinamikus tesztelés során nem merültek fel, kézzel, illetve a használt IDE figyelmeztetéseit böngészve nem derültek volna ki. Ezek sok esetben code smellek voltak, melyek az alkalmazás futását jelenlegi állapotában nem veszélyeztette volna, azonban a továbbfejlesztést veszélyeztették volna. Voltak azonban kritikus hibák is, melyek már most is valós veszélyt jelentettek, ezek is a statikus ellenőrzésnek hála kerültek felszámolásra.