# ProjetSockets

## Description du projet



## Importer sous eclipse


## Génération d'un exécutable 

Pour 
Clic droit sur le projet > Run As > Run Configurations > Set "Goals" = "install" > Run



## Tester le projet 

Dans le dossier /target, double cliquer sur le fichier "launcher.bat" pour exécuter le projet dans la console Windows.
Une fois la console ouverte, entrez le nombre d'instances de managers puis le nombre d'instances de checkers que vous voulez générer.
Pour chaque manager, saisir un nom et la requête.
Pour chacque checker, saisir un nom, le type de protocole à utiliser (UDP ou TCP) et la requête.
Une fois cette étape de préparation terminée, les serveurs se lancent et les échanges se font.

Attention : pour que le test se déroule correctement, les requêtes saisies doivent être de la forme : TYPEDEREQUETE + " " + LOGIN + " " + PASSWORD
