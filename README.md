# MS-Arbeitsstundentracker


## Was ist Git und warum sollte es verwendet werden?

Git ist ein Versionskontrollsystem, das hauptsächlich in der Softwareentwicklung verwendet wird, um Änderungen an Dateien, insbesondere Quellcode, zu verfolgen und zu verwalten.

Git sollte wegen folgender Punkte verwendet werden:

- Versionskontrolle: Git speichert den Verlauf aller Änderungen an Dateien, sodass man jederzeit zu früheren Versionen zurückkehren kann.
- Unabhängiges Arbeiten: Entwickler können unabhängig voneinander arbeiten, da jeder eine vollständige Kopie des Projekts auf seinem Computer hat.
- Kollaboratives Arbeiten: Git erleichtert das Zusammenführen von Änderungen, auch wenn mehrere Entwickler gleichzeitig am selben Projekt arbeiten.

## Grundlegende Git-Befehle (z. B. git init, git add, git commit, git push)

- git init: Erstellt ein neues Git-Repository.
- git add: Fügt Änderungen zur "Staging Area" hinzu, um sie für den nächsten Commit vorzubereiten.
- git commit: Speichert (commitet) die Änderungen aus der Staging Area dauerhaft im lokalen Repository.
- git push: Schickt die lokalen Commits zu einem Remote-Repository (z. B. GitHub).
- git pull: Holt Änderungen vom Remote-Repository und integriert sie in deinen lokalen Branch.
- git clone: Klont ein Remote-Repository auf deinen Computer.




## Branches und ihre Nutzung


Branches werden benutzt, um parallel an verschiedenen Teilen eines Projektes zu arbeiten. Dies ist 
nützlich für die Entwicklung neuer Funktionen oder Fehlerbehebung (Bugfixes). 

Dabei ist jeder erstellte Branch vom main-branch isoliert. Änderungen sind nur in den jeweiligen Branches zu sehen 
und können hierbei ausgiebig getestet werden.


`git branch` / `git branch --list`  - führt alle lokalen Branches auf 

`git branch -a`  - führt alle Remote-Branches auf


`git branch <branch>` - erstellst du einen neuen lokalen Branch mit dem Namen

`git checkout -b <branch-name>` - erstellt neuen lokalen Branch und wechselt gleich zu diesem



`git branch -d <branch>` - löscht den angegebenen lokalen Branch 

`git branch -D <branch>` - erzwingt das Löschen des lokalen Branches 

`git push origin --delete <branch-name>` - entfernt den Remote-Branch


`git switch <branch>`/ `git checkout <branch>` - Branch wechseln

`git push -u origin <new-branch-name>` - neuer Branch wird zum Remote-Repository hinzugefügt


`git fetch` / `git pull` - lädt den Remote-Branch runter

`git clone -b <branch-name> <repository-url>` - klont den angegebenen Branch direkt aus dem Remote-Repository



### Unterschied lokaler und remote Branch

Ein lokaler Branch existiert nur auf dem eigenen Rechner und 
ist nur für einem selbst sichtbar, während ein Remote Branch im Remote-Repository gespeichert ist 
und für alle Teammitglieder zugänglich ist.


## Umgang mit Merge-Konflikten

Merge-Konflikte treten auf, wenn zwei Branches zusammengefügt werden sollen, die Änderungen an derselben Datei oder denselben Codezeilen beinhalten. 
Git kann diese Änderungen nicht automatisch zusammenführen, da unklar ist, welche Änderung die gewünschte ist.

`git status` - auf Mergkonflikte überprüfen

`git merge <branch-name>` - führt den angegebenen Branch <branch-name> mit dem aktuellen Branch zusammen



### Problemlösung

Betroffene Datei in der IDE öffnen und damit Konflikte auflösen.


### Konflikte vorbeugen

- Häufige Updates und Merges durchführen, um Konflikte zu minimieren.

- Gute Kommunikation im Team, um gleichzeitige Änderungen an denselben Dateien zu vermeiden.


Quelle: [Git Branching - Basic Branching and Merging](https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging) 


## Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository

### Remote Repository

Ein Remote Repository ist ein Git-Repository, welches auf einem Server gespeichert wird. Das Remote Repository dient als
zentrale Anlaufstelle und enthält den aktuellsten Stand des Projektes.

### Lokales Repository

Ein lokales Repository ist ein Verzeichnis, das lokal auf dem Computer gespeichert wird und eine Kopie der Inhalte des
Remote Repositories enthält. Das lokale Repository sollte regelmäßig mit dem Remote Repository synchronisiert werden, um
immer auf dem aktuellsten Stand zu sein.

### Lokales Repository mit IntelliJ/PyCharm anlegen

Das Remote Repository kann ganz leicht mithilfe von IntelliJ/PyCharm geklont und als lokales Repository eingerichtet werden:

* im Menü **New** > **Project from Version Control...** auswählen

Das Repository kann entweder über die Repository-URL oder über den jeweiligen Dienst, wie zum Beispiel GitHub geklont
werden.

### Git-Kommandos in IntelliJ/PyCharm

In IntelliJ/PyCharm lassen sich alle Git-Kommandos über die grafische Benutzeroberfläche ausführen. Ein praktisches
Werkzeug dafür ist das Commit-Tool.

Das Commit-Tool zeigt alle vorgenommenen Änderungen an. Hier können die Änderungen in den ausgewählten Dateien direkt
committet oder committet und gepusht werden. Dazu gehört es auch, eine Commit-Message zu verfassen, welche die
Änderungen kurz zusammenfasst.

### Zusätzliche Funktionen in IntelliJ/PyCharm

Eine Funktion, welche nicht zu Git gehört, ist das **Shelven** von Änderungen. Dieses Feature ist vor allem hilfreich, wenn
man zwischen verschiedenen Branches, an denen man arbeitet, wechseln möchte.

* alle Änderungen markieren, die geshelvt werden sollen
* Rechtsklick auf die markierten Änderungen
* **Shelve Changes...** auswählen

Um geshelvte Änderungen wiederherzustellen, geht man folgendermaßen vor:

* im Reiter **Shelf** einen Rechtsklick auf die zu unshelvenden Änderungen machen
* **Unshelve...** auswählen

Außerdem kann man in IntelliJ/PyCharm leicht neue Branches erstellen und zwischen bestehenden Branches wechseln. Um einen
neuen Branch zu erstellen, klickt man mit Rechtsklick auf den Branch, auf dem der neue Branch basieren soll und wählt
**New Branch from ' '...** aus.

Um zwischen Branches zu wechseln, macht man einen Rechtsklick auf den Branch, zu dem man wechseln möchte und wählt
**Checkout** aus. Idealerweise shelvt man vorher seine Änderungen, um sie nicht unbeabsichtigt mit in den neuen Branch
zu übernehmen.


## Nützliche Git-Tools und Plattformen

Im Folgenden werden einige Tools und Plattformen vorgestellt,
die bei der Nutzung von Git behilflich sein können.

### Git-Tools

- GitHub Desktop
- GitKraken
- IntelliJ IDEA
- SmartGit
- Tower


### Plattformen

- Amazon AWS CodeCommit
- Github
- GitLab
- Microsoft Azure DevOps
- SourceForge


Wer hat welchen Beitrag geleistet?

Sarina Schoknecht: 
o Was ist Git und warum sollte es verwendet werden?
o Grundlegende Git-Befehle (z. B. git init, git add, git commit, git push)

Shirley Lein: 
o Branches und ihre Nutzung, Umgang mit Merge-Konflikten

Shirley: 
o Branches und ihre Nutzung, Umgang mit Merge-Konflikten

Josephine Hoegen: 
o Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository

Philip Wehling: 
o Nützliche Git-Tools und Plabormen (z. B. GitHub)
