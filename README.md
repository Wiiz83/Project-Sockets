# ProjetSockets

## Description du projet

Ce projet JAVA est un projet MAVEN. 
Il permet de simuler le fonctionnement d'un service d'authentification basé sur des associations login / password.

## Génération d'un exécutable 

Pour générer un exécutable :
1. Clic droit sur le nom du projet 
2. "Run As"
3. "Run Configurations"
4. "ProjetSockets" sous "Maven Build"
5. Goals = "install"
6. "Run"

## Tester le projet 

Une la génération de l'exécutable effectuée (étape précédente), ouvrez le dossier /target puis double cliquer sur le fichier "launcher.bat" pour exécuter le projet dans la console Windows.
Une fois la console ouverte, entrez le nombre d'instances de managers puis le nombre d'instances de checkers que vous voulez générer.
Pour chaque manager, saisir un nom et la requête.
Pour chacque checker, saisir un nom, le type de protocole à utiliser (UDP ou TCP) et la requête.
Une fois cette étape de préparation terminée, les serveurs se lancent et les échanges se font.

Attention : pour que le test se déroule correctement, les requêtes saisies doivent être de la forme : TYPEDEREQUETE + " " + LOGIN + " " + PASSWORD
