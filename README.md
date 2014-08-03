
# Was IVKResultArchiver?

Dieses Projekt liest die aktuellen Resultate der Innerschweizer Korbball Meisterschaft und erstellt daraus PDFs. Ich verwende das Projekt um Ende Saison 
die Resultate zu archivieren, damit ich die Datenbank wieder mit den neuen Resultaten bestücken kann.


# Wie führe ich IVKResultArchiver aus?

Das Projekt buildet mit Gradle. Um die PDFs zu generieren muss erst Gradle installiert werden. Das geht ganz einfach und ist hier beschrieben: [Gradle Installation](http://www.gradle.org/docs/current/userguide/installation.html). Danach kann man im Projekt-Verzeichnis `gradle shadowJar` ausführen. Damit erstellt man in `build/libs` ein self containing jar (fatJar). Das lässt sich dann mit einer Java Runtime >= 8 ausführen. Die PDFs liegen danach im selben Verzeichnis.