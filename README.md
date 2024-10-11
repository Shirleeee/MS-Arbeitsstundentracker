# MS-Arbeitsstundentracker


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