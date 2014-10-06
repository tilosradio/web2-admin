var oabnurl = '../msq_getonairbanner_chat.php';

function msg(x) {
switch (x)
{
case 0:
	alert("Köszöntünk a Tilos Rádió chat oldalán!\n\nA chat főablakának tetején található a téma mező, itt információk lesznek alkalmanként elhelyezve. Jobb oldalon helyezkedik el a felhasználók listája, a lista tetején az operátorokkal (ezt jelzi a @ jel a nevük előtt). A főablak alján található a beviteli mező. Fontos megemlíteni, hogy a Tab billentyűvel vissza tudod hozni az előző parancsokat, illetve ha beírod az egyik felhasználó nevének kezdőbetűjét, és nyomsz egy Tab billentyűt, akkor a nevet kiegészíti. Ha több hasonló név lenne, akkor vált a nevek között.\n\nHa kérdésetek van, az operátorok szívesen segítenek.\n\nKellemes időtöltés kívánunk:\na Tilos Rádió csapata");
	break;
case 1:
	alert("Névcsere\n\nParancs: /nick <név>\n\nEzzel lehet a nevedet kicserélni egy másikra.");
	break;
case 2:
	alert("Név regisztrálása\n\nNick kiválasztásához és arra váltáshoz, majd annak egy jelszó segítségével való regisztrálásához az alábbi két sort kell beírnod a bal fenti "Status" fülön, szóköz nélkül az elején, azaz a "/"-jellel kezdve a sorodat:\n\n/nick FELHASZNÁLÓNEVED\n\n/msg nickserv register JELSZAVAD");
	break;
case 3:
	alert("Csoport használata\n\nParancs: /msg NickServ GROUP <név> <jelszó>\n\nEzzel a paranccsal a saját nevedet összekapcsolhatod a <név> csoportjával. A <jelszó> annak a névnek a jelszava, amelyhez kapcsolódni szeretnél.\n\nHa csatlakozol egy csoporthoz, akkor a csoporthoz tartozó neveknek közösek lesznek a beállításai, naplói és hozzáférési jogai.\n\nA csoport addig létezik, amíg tagjai vannak. Ez azt jelenti, hogy ha egy név el lesz távolítva a csoportból, akkor nem veszted el az előbb említett közös dolgokat, ha legalább egy név van még a csoportban.\n\nEzt a parancsot akkor is használhatod, ha a nevedet még nem regisztráltad. Ha már regisztráltad, akkor azonosítanod kell magadat, mielőtt használnád. További információkért lásd a /msg NickServ HELP IDENTIFY parancsot.\n\nAjánlott nem regisztrált név esetén használni ezt a parancsot, mert a név automatikusan regisztrálva lesz a folyamat során.\n\nEgyszerre csak egy csoportban lehetsz. A csoportok egyesítése nem lehetséges.\n\nFIGYELEM: az összes névhez ugyanaz a jelszó tartozik.");
	break;
case 4:
	alert("Privát üzenet\n\nLehetőség van a többi felhasználóval külön beszélgetést folytatni.\nA jobb oldali névlistában bal klikk a névre, akivel privát beszélgetést szeretnél.\nEkkor a név alatt megnyílik egy menü, abban bal klikk a \"QUERY\" szóra.\nEkkor megnyílik egy külön fülön a felhasználó nevével egy privát szoba, ahol privát beszélgetést folytathattok.");
	break;
case 5:
	alert("Nyelv váltás\n\nParancs: /msg NickServ SET LANGUAGE 12\n\nEzzel a paranccsak lehet a rendszerüzenetek nyelvét magyarra váltani (például az általod adott parancsok visszajelzéseit).\n\nFIGYELEM: ez a parancs csak a név regisztrálása után adható ki.");
	break;
case 6:
	alert("IRC kliens\n\nLehetőség van a Tilos chat IRC kliensből való elérésére is.\nSzerver: irc.tilos.hu\nPort: 6667\nCsatorna: #tilos\n\nHa regisztráltál nevet, belépés után ne felejtsd el azonosítani!\nEzt a következő paranccsal teheted meg:\n\n/msg NickServ IDENTIFY <jelszó>\n\nA kliensekben lehetőség van induláskor automatikusan parancsokat futtatni, így ha ezt beállítod oda, akkor nem kell minden belépésnél kiadni ezt a parancsot.\n\nChatZilla - lépésről-lépésre\n\nMozilla Firefox böngészőből\nEszközök -> Kiegészítők -> Kiterjesztések -> és rákeresni -> ChatZilla\nmajd telepíteni.\n\nIndítása:\nEszközök -> ChatZilla\n\nBeállítása:\nbal felső menü -> ChatZilla\nPreferences...\nMegjelenő ablak, bal oldali sáv -> Global Settings\njobb oldalon, felül katt a fülre -> Startup\nEzután a lentebbi terület -> Locations\nüres fehér mező, mellette a legfelső gomb -> Add...\nEzt írd a begjelenő beíró mezőbe ->  irc://irc.tilos.hu:6667\nezután gomb -> OK\n\nmég mielőtt bezárnád az ablakot, kattints felül a General fülre\nNick név beállítás: Nickname mezőbe írd be a nick neved.\n\nmajd ablak alján gomb -> Apply, és OK (bezáródik az ablak)\n\nChat indítása:\nChatZilla menü -> Open Auto-connect\n\nszobába való belépéshez: /join #tilos");
	break;
case 7:
	alert("TrollKontroll\n\nA jobb oldali névlistában bal klikk annak a nevére, akinek a szövegét nem akarod olvasni.\nEkkor a név alatt megnyílik egy menü, abban bal klikk a \"SILENCE\" szóra.\nEttől kezdve egészen addig nem látod, hogy mit ír, amíg hasonló elven a \"DESILENCE\" szóra nem klikkelsz.\n\nTANÁCS: A troll előre megfontoltan aknázza ki az online közösség normáinak gyenge pontjait, hogy feldühítse az embereket.\nNe etesd a trollt! A trollnak a veszekedés, a zavar és a felfordulás a lételeme: ha vitába szállsz vele, csak a kedvére teszel. Ahogy a mondás tartja: ne kezdj birkózásba egy disznóval. Mindketten sárosak lesztek, de a disznó még élvezi is.");
	break;
}
}

function msqshowminitv() {
	wcw=window.open('http://tilos.hu/minitv.php','','toolbar=no,titlebar=no,status=no,scrollbars=no,resizable=yes,menubar=no,location=no,width=260,height=380');
	wcw.focus();
}

function msqshowminiradio() {
	wcmr=window.open('http://tilos.hu/miniradio.php','','toolbar=no,titlebar=no,status=no,scrollbars=no,resizable=no,menubar=no,location=no,width=260,height=220');
	wcmr.focus();
}

function calcSecondsUntilNextHalfHour () {
  var nowDate = new Date();
  var minutes = nowDate.getMinutes();
  var nextMinutes = 30;
  if (minutes >= 30)
    nextMinutes = 60;
  var newDate = new Date(1900 + nowDate.getYear(), nowDate.getMonth(), nowDate.getDate(), nowDate.getHours(), nextMinutes, 0);
  var seconds = parseInt(Math.abs(newDate - nowDate) / 1000);
  return seconds + 5; // Add 5 seconds for poorly implemented timers
}

function msqgetbanner() {
  $.ajax({
    url: '../msq_getonairbanner_chat.php',
    success: function (data) {
      $('#OnAirBanner').html(data);
      setTimeout(function () {
        msqgetbanner();
      }, calcSecondsUntilNextHalfHour() * 1000);
    },
  });
}
