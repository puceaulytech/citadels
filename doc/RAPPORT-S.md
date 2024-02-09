# Rapport

## 1. Point d'avancement

### Fonctionnalités réalisées

Le jeu complet est jouable de 4 à 7 joueurs, à l'exception des merveilles (il n'y en a que deux : Donjon et Ecole de magie). Tous les pouvoirs des personnages sont utilisables.

Il y a 5 bots utilisables:

- __FastBuilder__, qui va construire beaucoup de petit quartier, 
- __ExpensiveBuilder__, qui va viser des quartiers avec un gros score,
- __Random__, qui va faire des actions complétement aléatoires,
- __Tryharder__, qui va maximiser ses points en fin de partie grâce à diverses méthodes, et va habilement utiliser les pouvoirs de ses personnages pour augmenter son score,
- __Richard__, qui suit la stratégie de Richard,

### Logging

Nous utilisons notre propre interface de logging, ce qui nous permet de supprimer les logs facilement.

Les logs de parties sont en couleurs :

![](logs.png)

### Statistiques en CSV

Nous utilisons la librairie OpenCSV pour lire et écrire les fichiers CSV.

### Richard

## 2. Architecture et qualité

### Architecture

Dès le début du projet, nous avons fait attention à isoler le comportement des bots de la logique de jeu pour éviter toute triche. Les bots ne peuvent pas accéder et/ou modifier des informations et éléments propres au jeu. Cela se fait via les classes View, qui permettent un accès en lecture personalisé du bot par rapport au jeu.

Tout bot doit implémenter l'interface [`Behavior`](https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-s/blob/master/src/main/java/com/github/the10xdevs/citadels/interaction/behaviors/Behavior.java) pour pouvoir jouer au jeu. Il serait en théorie possible de publier notre projet en tant que librairie, et n'importe qui pourrait importer la librairie, implémenter son propre bot et lancer une partie avec son bot.

### Documentation

Il n'y a pas de fichier de documentation dédié, mais nous avons écrit de la Javadoc dans la majeure partie du projet.

### Qualité

Nous sommes plutôt confiant de la qualité du code, à peu près tout est bien fait. Les parties de AbilityAction sont peut-être trop complexes et pourraient être refactor.

## 3. Processus

### Qui a fait quoi ?

Romain s'est occupé de l'architecture initiale. Pour la suite sur projet, tout le monde a un peu touché à tout.

### Process

Nous avons utilisé GitHub Flow pour nous organiser (master est toujours stable, une pull-request par feature). Nous pensons que c'est la git branching strategy la plus simple.