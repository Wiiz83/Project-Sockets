# ProjetSockets

## Description du projet

Ce projet JAVA est un projet MAVEN. 
Il permet de simuler le fonctionnement d'un service d'authentification basé sur des associations login / password.

## Génération d'un exécutable 

Pour générer un exécutable :
1. Clic droit sur le nom du projet 
2. "Run As"
3. "Run Configurations"
4. Double cliquer sur "Maven Build" pour créer une nouvelle configuration
5. Dans cette nouvelle configuraton : 
5.1 Base directory : ${project_loc:sockets}
5.2 Goals : install
6. "Run"

PS : Il est possible qu'un erreur du type "Perhaps you are running on a JRE rather than a JDK" survienne lors de la génération. 
Il suffira de changer de compilateur, pour cela :
1. Clic droit sur le nom du projet
2. "Build Path"
3. "Configure Bluid Path"
4. Sous l'onglet "Libraries", clic sur le compilateur Java
5. "Edit"
6. Sélectionnez un compilateur jdk dans la liste déroulante
7. Re-essayer la génération

## Tester le projet 

Une fois la génération de l'exécutable effectuée (étape précédente), ouvrez le dossier /target puis double cliquer sur le fichier "launcher.bat" pour exécuter le projet dans la console Windows.

Une fois la console ouverte, entrez le nombre d'instances de managers puis le nombre d'instances de checkers que vous voulez générer.
Pour chaque manager, saisir un nom et la requête.

Pour chacque checker, saisir un nom, le type de protocole à utiliser (UDP ou TCP) et la requête.

Une fois cette étape de préparation terminée, les serveurs se lancent et les échanges se font.

Attention : pour que le test se déroule correctement, les requêtes saisies doivent être de la forme : TYPEDEREQUETE + " " + LOGIN + " " + PASSWORD
