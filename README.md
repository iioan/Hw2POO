# Tema 2 POO - Sistem digitalizare instituție publică

#### Ioan Teodorescu, 323CB

## Introducere

Tema constă în implementarea unui _sistem digital de management_ al unei primării. Programul se axează pe gestionarea
cererilor făcute de cetățeni, precum și pe gestionarea angajaților primăriei. Cetățeanul, mai precis, **Utilizatorul**
poate crea și trimite o cerere către institut. De asemenea, **utilizatorul** poate fi de mai multe tipuri (_elev,
persoană,
pensionar, angajat, entitate juridică(reprezentant)_), iar pentru fiecare tip, în primărie există câte un birou
destinat. Biroul este responsabil de gestionarea cererilor de tipul **utilizatorului** și este format din mai mulți
funcționari publici. Toate acesta sunt coordonate cu ajutorul Managementului primăriei.

În continuare, voi explica în detaliu implementarea sistemului, prin explicarea funcționalității fiecărui concept.

### Clasa Cerere (```Cerere.java```)

Clasa ```Cerere``` este clasa de bază pentru cereri. Aceasta conține
câmpurile ```requestLabel```, ```type```, ```priority```,
```date```, ```templatedText``` și ```formatter```. Funcția ```compareTo()``` este suprascrisă pentru a putea fi
folosită în sortarea cererilor.

### Funcționalitățile utilizatorului (```Utilizator.java```)

Pentru fiecare utilizator avem urmatoarele atribute:

- _nume_
- _parametrii specifici tipului de utilizatori_ (ex: scoala, reprezentant, etc)
- _template_ = textul cererii specifice fiecărui tip
- _requestsList_ = cererile pe care le poate face utilizatorul. Folosesc un ```ArrayList``` a găsii mai ușor cererea
  dorită, cu ajutorul metodei _.contains()_.
- _waitingQueue_ = coada de cereri în așteptare
- _finishedQueue_ = coada de cereri finalizate
- _RequestsType_ = set de date de tip **enum**, în care sunt definite tipurile de cereri pe care le poate face
  utilizatorul.
  avem _numele_ fiecărei constante, alături de un **string** care reprezintă tipul cererii. Mă folosesc de variabila
  label pentru
  a prelua string-urile.
- Fiecare subclasă își va defini proprii parametrii, _template_ și _RequestsType_.
- Folosesc cozi prioritare, pentru a stoca cererile, întrucât cererile sunt comparabile între ele, și doresc să fiu
  poziționate în ordine cronologică.

1. _Scrierea textului unei cereri_
    - Cererea va fi realizată în ```writeRequest```. Se verifică dacă tipul cererii primite ca parametru este valid cu
      ajutorul
      metodei ```isRequestValid```. Dacă este valid, se ia numele clasei care a apelat metoda și dupa fiecare tip de
      utilizator,
      se va returna textul cererii, formatat pentru fiecare utilizator. În cazul în care cererea nu este validă, se va
      arunca o excepție
      de tipul ```NotMyRequestException```.
2. _Crearea unei cereri_
    - Cererea va fi creată în ```createRequest```. Pentru a verifica dacă cererea este validă, vom scrie textul cererii
      și o vom
      atribui variabilei _templated_. Dacă aceasta nu e null, atunci cererea este validă și se va crea o nouă cerere,
      care conține: tipul
      cererii, prioritatea, data și textul cererii. Dacă stringul e null, se
      tratează ```NotMyRequestException```, scriind mesajul în
      fișierul de **output**. De asemenea, cererea este adaugată in coada cererilor în așteptare.
3. _Retragerea unei cereri_
    - Se verifică dacă data primită ca parametru este egală cu data unei cereri din coadă. Daca da, se sterge cererea.
4. _Afișarea cererilor_
    - Se parcurge coada de cereri și se va scrie în fișierul de **output** cererea curentă.

### Funcționalitățile unui Birou (```Birou.java```)

- Pentru fiecare tip de utilizator, avem câte un birou diferit, datorită genericității clasei. Clasa ```BirouItem```
  este
  esențială pentru gestionarea cererilor în birouri, având ca atribute: o variabilă _user_ de tip T(datorită
  genericității)
  și o cerere. Clasa mai are și o funcție ```compareTo()``` pentru a putea fi folosită în sortarea cererilor.
- Biroul are o coadă de cereri în asteptare, care foloseste obiecte de tip ```BirouItem```. De asemenea, biroul are o
  coadă
  auxiliară, care va fi folosită pentru a sorta cererile. Cozile sunt prioritare, pentru a putea fi sortate în
  funcție de prioritate. 
  Biroul are și o listă pentru funcționarii publici destinați
  fiecărui
  birou.

Se va crea o nouă cerere, cu ajutorul constructorului clasei ```Cerere```.

### Funcționalitățile Managementului din Primarie

Clasa preia informațiile preluate din frontend și le trimite către clasele corespunzătoare. În main-ul acesteia, se află
numele fișierului în care vom prelua informațiile și le vom scrie (sunt 2 fișiere, unul pentru **input** și altul
pentru **output**). Pentru început se va face un clean-up al fișierelor de **output** și apoi se vor citi datele din
fișierul de **input**. Fiecare comanda din fișierul de **input** va fi prelucrată și rezolvată cu ajutorul colecțiilor
și a claselor corespunzătoare. Pentru păstrarea utilizatorilor, voi folosi o colecție de tip **HashMap**, în care
cheia va fi numele utilizatorului și valoarea va fi obiectul de tipul utilizatorului. Accesarea unui utilizator cu
ajutorul
numelui are o complexitate de tip O(1), în cele mai bune cazuri.
Putem întâlni urmatoarele comenzi:

1. ```adauga_utilizator; <tip_utilizator>; <nume_utilizator>```
    - Se apeleaza metoda ```addUser2Map```, unde se determina numele și tipul utilizatorul, si se adauga in *
      *HashMap**,
2. ```adauga_functionar; <tip_utilizator/birou>; <nume_functionar>```
    - Se apeleaza metoda ```addEmployee``` și se preia numele și tipul biroului pentru care funcționarul va lucra.
      Întrucât clasa ManagementPrimarie are câte un birou pentru fiecare tip de utilizator, cu ajutorul
      funcției ```getBirou``` (
      verifica tipul de utilizator scris in input și returneaza biroul corespunzator) se va adauga functionarul în
      biroul dorit.
      În funcția ```addEmployee``` din ```Birou.java``` adaug funționarul în colecția de funcționari ai biroului (
      _functionari_).
3. ```cerere_noua; <nume_utilizator>; <tip_cerere>; <data>; <prioritate>```
    - Se apeleaza metoda ```addRequest2User``` și se preia numele utilizatorului, tipul cererii, data și prioritatea.
      Caut utilizatorul în colecția de utilizatori și adaug cererea în colecția de cereri în așteptare a utilizatorului.
      Apoi,
      adaug cererea în colectia de cereri nerezolvate în cadrul biroului corespunzator tipului de utilizator.
4. ```retrage_cerere; <nume_utilizator>; <data>```
    - Se apeleaza metoda ```removeRequest``` și se preia numele utilizatorului și data cererii. Sterg cererea atât din
      coada colectiilor ale utilizatorului (Apelez ```removeRequest``` din ```Utilizator.java```; iar fiecare cerere si
      verific
      daca data ei este egală cu cea primită ca parametru; în caz afirmativ, elimin cererea din coada) cât și din coada
      cererilor
      ale biroului (Apelez ```removeRequest``` din ```Birou.java```, iar fiecare cerere si verific dacă numele și daca
      cererii
      sunt egale cu cele primite ca parametru).
5. ```rezolva_cerere; <tip_utilizator/birou>; <nume_functionar>```
    - Se apeleaza metoda ```resolveRequest``` și se preia tipul biroului și numele funcționarului. Caut biroul de care
      depinde funcționarul și apelez ```resolveRequest``` din ```Birou.java```, unde se va rezolva cererea. În funcție,
      scot primul
      element din coadă, iar pentru deținătorul cererii, îi o scot din listă și o adaug în lista de cereri rezolvate.
      Apoi, sortez
      coada de cereri. Pentru funcționarul care a rezolvat cererea, voi apela ```addToEmployeeList```
      din ```Functionar.java```
      unde voi adauga cererea finalizată în fișierul de tip ```functionar_<nume_functionar>.txt```.
6. ```afiseaza_cereri; <tip_utilizator/birou>```
    - Se apeleaza metoda ```displayRequests``` și se preia tipul biroului. Caut biroul și apelez ```displayRequests```
7. ```afiseaza_cereri_in_asteptare; <nume_utilizator>``` sau ```afiseaza_cereri_finalizate; <nume_utilizator>```
    - Pentru ambele cazuri, se apeleaza ```showWaitingRequests```
      \ ```showResolvedRequests``` și se preia numele utilizatorului.
      Se caută utlizatorul cu acel nume și se apelează aceleași funcții din ```Utilizator.java```