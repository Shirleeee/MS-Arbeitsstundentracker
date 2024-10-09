# MS-Arbeitsstundentracker


## Branches und ihre Nutzung


Branches werden benutzt, um parallel an verschiedenen Teilen eines Projektes zu arbeiten. Dies ist 
nützlich für die Entwicklung neuer Funktionen oder Fehlerbehebung (Bugfixes). 

Dabei ist jeder erstellte Branch vom main-branch isoliert. Änderungen sind nur in den jeweiligen Branches zu sehen 
und können hierbei ausgiebig getestet werden.




`git branch` / `git branch --list`  - führt alle Branches in deinem Repository auf 

`git branch <branch>` - erstellst du einen neuen Branch mit dem Namen

`git branch -d <branch>` - Löscht den angegebenen Branch 

`git branch -D <branch>` - erzwingt das Löschen eines Branches 

`git branch -m <branch>`  - Befehl benennst du den aktuellen Branch 

`git branch -a`  - führt alle Remote-Branches auf

`git checkout` 

### Unterschied lokaler und remote Branch

### Remote Branch erstellen

## Umgang mit Merge-Konflikten

Merge-Konflikte treten auf, wenn zwei Branches zusammengefügt werden sollen und dabei dieselbe Datei oder Codezeile.