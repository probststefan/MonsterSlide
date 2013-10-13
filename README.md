MonsterSlide
============
Ich hab hier mal für uns die Basics zu Git aufgeschrieben. Ich hab selbst noch nicht so viel Erfahrung mit Git.
Bin gespannt wie gut das funktioniert ;)

##Branching##

Das repo sollte aus 2 Arten von Branches bestehen. Einmal dem master branch, der nur lauffähigen Code enthält.
Mit diesem Branch sollten nur größere Subbranches gemergt werden. So fügen wir also immer komplette Features 
der Hauptlinie hinzu.
Um die Features zu entwicklen sollten "Feature"-Branches benutzt werden für z.B. Physik, Sound, UI oder Issues.
Es ist daher auch wichtig von welchem Branch aus man einen neuen erstellt.

###Workflow###
Der Hauptunterschied zu SVN ist, dass man neuen Code zuerst lokal merged und dann das Ergebnis wieder zu Github
hochlädt und nicht während des Commits alles nötige merged!

1. Man updatet den lokalen master Branch und erzeugt daraus einen neuen lokalen Branch.
2. Man wechselt zum neu erstellten Branch.
3. Man arbeitet am Feature/Fix etc...
4. Man wechselt zurück zum lokalen master Branch und merged den Feature-Branch.
5. Man checkt ob sich der github master Branch zwischendurch geändert hat und mergt gegebenfalls mit dem lokalen.
6. Man testet lokal ob der Code noch funktioniert.
7. Man pusht den lokalen master Branch zum github master.

Falls man jetzt seine lokale Arbeit jemand anderem zeigen will muss man nur den lokalen Feature-Branch auf den github server pushen.
Dann bekomm jeder beim nächsten fetch diese Branches zu sehen und kann sie auschecken, ändern, committen.

