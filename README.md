# PayMyBuddy

## Description de l'application

### Technologies
L'Application web est développée en Java Spring Boot, avec Thymeleaf et MySQL.

L'ensemble des technologies utilisées se compose de: 
- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Maven
- HTML
- CSS

Comme convenu, cette application permet:
- De créer un compte utilisateur;
- Se connecter;
- Gérer des relations entre utilisateurs pour échanger des transferts d'argent;
- Effectuer ces transactions.

Toutes les données se mettent à jour et sont consultables depuis la base de données.

### Architecture de l'application
L'application est organisée en plusieurs couches:
- Controller: gestion des requêtes HTTP et des pages web;
- Service: logique métier;
- Repository: l'accès aux données;
- Entity: Représentation des tables de la base de données;
- DTO: Transfert de données entre les différentes couches;
- templates: pages HTML Thymeleaf;
- static: fichiers de style CSS dans notre cas.

Le parcours type d'une requête suit ce modèle:

Interface web -> Controller -> Service -> Repository / DAL -> Base de données SQL

### Modèle physique des données
Voici le diagramme qui représente la base de données: 
<img width="724" height="405" alt="diagramme bdd" src="https://github.com/user-attachments/assets/d0e40cc0-e8ce-40dc-a216-3797c92b1920" />

- user: données de l'utilisateur;
- transactions: historique des transactions;
- user_connections: relations et bénéficiaires associés aux utilisateurs.

### Scripts SQL
Les scripts SQL de chaque table se trouve dans le dossier pay-my-buddy/scripts SQL

### Repository / DAL
L'accès à la base de données se fait avec Spring Data JPA.

Les interfaces Repository permettent dans l'application de:
- Enregistrer les utilisateurs;
- Rechercher un utilisateur;
- Enregistrer les transactions;
- Consulter l'historique des transactions;
- Ajouter / Supprimer des bénéficiaires.

### Les transactions
Les opérations qui nécessitent plusieurs modifications en base de données utilisent l'annotation:
@Transactional;
Cette annotation permet d'assurer la cohérence des données.
Lorsqu'une opération réussie, elle est validée avec un commit. 
Mais lorsqu'une erreur intervient, les modifications sont allulées avec un rollback.

### Connexion à la base de données
L'application utilise une base de donnée MySQL.
La configuration se trouve dans: src/main/resources/application.properties.
Le compte MySQL utilisé est entièrement dédié au projet et à la base de donnée de l'application. 

### Interface web
L'interface web est réalisée avec Thymeleaf, HTML et CSS.
Les pages se trouvent dans: src/main/resources/templates/

Elles ont été réalisées à partir des maquettes à l'origine du visuel Front-End du projet.
Les formulaires sont reliés aux contrôleurs Spring Boot et utilisent la couche DAL pour lire et enregistrer les données.

### Installation
Pré-requis:
- Java 21
- Maven
- MySQL
- MySQL Workbench

Etapes:
- Cloner le dépôt;
- Créer la base de données MySQL;
- Exécuter le script SQL présent dans le dossier /scripts SQL;
- Configurer les informations de connexion dans application.properties;
- Lancer l'application : bouton Run ou commande mvn spring-boot:run;
- Ouvrir l'application dans le navigateur: http://localhost:8080.

PITTIN Amaryllis.




