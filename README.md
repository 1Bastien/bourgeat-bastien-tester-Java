# Application de Paiement de Parking - Park'it - Projet Etudiant

Ce projet a été réalisé dans le cadre d'un projet étudiant.
Cette application est une simulation d'une application de paiement de parking automatisée pour une société fictive nommée Move'it.

## Contexte

Move'it est une société de solutions de mobilité en zones urbaines qui collabore avec des établissements publics afin d’améliorer la circulation et la gestion des parkings dans les villes. La société a récemment démarré un nouveau système de paiement de parking automatisé appelé Park'it.

## Objectif

L'objectif de ce projet est de corriger les bugs existants, d'introduire de nouvelles fonctionnalités, et de tester l'application pour assurer son bon fonctionnement. Voici les tâches à accomplir :

1. Ajouter une fonctionnalité de stationnement gratuit pour les 30 premières minutes.
2. Ajouter une réduction de 5 % pour les utilisateurs récurrents.
3. Corriger le code pour qu'il valide tous les tests unitaires.
4. Effectuer les tests d'intégration marqués par les commentaires "TODO".

## Technologies utilisées

Le projet utilise les technologies suivantes :

- **Java**
- **Maven**
- **MySQL**
- **Jacoco et JUnit**

## Prérequis

- Java
- Maven

## Installation

```bash
git clone https://github.com/1Bastien/bourgeat-bastien-tester-Java
```
```
cd bourgeat-bastien-tester-Java
```
```
mvn clean package
```
```
cd target
```
```
java -jar parking-system-1.0-SNAPSHOT-jar-with-dependencies.jar
```
