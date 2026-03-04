# ATLAS

Aplikazio hau Atlas-entzako bezero bezala funtzionatzen duen Android aplikazio mugikorra da, non erabiltzaile baten kredentzialekin beste erabiltzaile/lagunekin bidaiak partekatu daitezke; argazkiak, kontuak, bideak...

- [Erabilera](#erabilera)
- [Zehaztapenak](#zehaztepenak)

## Erabilera

<table>
    <tr><td><h3>Saio hasi</h3></td></tr>
    <tr>
        <td>
        Aplikazio hasterakoan, lehenengo horri honetan zerbitzariaren helbidea eta erabiltzaile kredentzialak sartu beharko dira Atlas zerbitzarian saioa hasi al izateko.
        Eta beheko bi botoietatik bigarrena sakatuz kontu berri bat sortu al izango da.
        </td>
        <td><img src="/img/img1.png" width="800"/></td>
    </tr>
    <tr><td><h3>Saio berria sortu</h3></td></tr>
    <tr>
        <td>
        Kontu berri bat sortzeko horri honetan, erabiltzaile izena, korreo elektronikoa eta psahitza bi aldiz sartuz kontu berria bat sortzen saiatu daiteke.
        </td>
        <td><img src="/img/img2.png" width="800"/></td>
    </tr>
    <tr><td><h3>Bidai zerrenda</h3></td></tr>
    <tr>
        <td>
        Saioa hasterakoan sartuta zauden bidaien zerrenda ikusi daiteke, zein beste batean sartu zein berri bat sortu.
        </td>
        <td><img src="/img/img3.png" width="800"/></td>
    </tr>
    <tr><td><h3>Bidaia</h3></td></tr>
    <tr>
        <td>
        Bidai bateri ematerakoan honen barruan sartu eta zenbait datu erakusten dira:
        <ul>
            <li>Mezuak, argazkiak, kontuak eta mapa</li>
            <li>Partaidak</li>
            <li>Estatistika orokoarrak</li>
        </ul>
        </td>
        <td><img src="/img/img4.png" width="800"/></td>
    </tr>
    <tr><td><h3>Bidai batean sartu</h3></td></tr>
    <tr>
        <td>
        Bidaien zerrendatik, beste bidai batean sartu daiteke honen kodea sartuz.
        </td>
        <td><img src="/img/img5.png" width="800"/></td>
    </tr>
    <tr><td><h3>Bidai berria sortu</h3></td></tr>
    <tr>
        <td>
        Bidaien zerrendatik, bidai berri bat sortzen saiatzerakoan, bidai bat sortzeko aukera ematen duen orria irekiko da, non, besteak beste, bidaiaren izena, epeak, helmuga eta lagunak gonbidatu eta bidaia sortu daiteke.
        </td>
        <td><img src="/img/img6.png" width="800"/></td>
    </tr>
    <tr><td><h3>Profila</h3></td></tr>
    <tr>
        <td>
        Erabiltzailearen profila ikusi daiteke hau aukeratzerakoan, non honen estatistika ezberdinak erakusten diren.
        </td>
        <td><img src="/img/img7.png" width="800"/></td>
    </tr>
</table>

## Zehaztepenak

[Hasieran](#atlas) esan bezala, proiektu hau Android aplikazioa da eta JetPack Compose erabiltzen du ikuspegi ezberdinak sortzeko. Hontaz aparte, TCP konexioak erabiltzen ditu zerbitzariarekin konektatzeko, eta Supabase irudiak igo eta jaisteko.