# realestate

API REST immobilière construite task après task. Le projet est écrit avec Spring Boot 4.1, Maven, JDK 21 et une base de données H2 embarquée. À ce stade (task 0), c'est une application Spring Boot vide : elle démarre et écoute les requêtes HTTP, mais n'expose encore aucune route.

## Prérequis

- JDK 21 (vérifier avec `java -version`).
- Maven n'est pas à installer : le Maven wrapper (`mvnw`, ou `mvnw.cmd` sous Windows) télécharge la bonne version de Maven tout seul.

## Démarrer l'application

Depuis le dossier `realestate/` :

```bash
./mvnw spring-boot:run
```

L'application répond sur `http://localhost:8080`. Pour l'arrêter, appuyer sur `Ctrl+C` dans le terminal où elle tourne.

Le premier lancement est plus long : le wrapper télécharge Maven, puis toutes les dépendances, dans `~/.m2/`. Les lancements suivants sont beaucoup plus rapides.

Si `./mvnw` répond `Permission denied` (Linux ou macOS), rendre le script exécutable puis relancer :

```bash
chmod +x mvnw
```

## Lancer les tests

```bash
./mvnw test                      # tous les tests
./mvnw test -Dtest=Task00Test    # une seule task (ici la task 0)
```

Le test généré, `RealestateApplicationTests`, vérifie seulement que l'application Spring démarre correctement.

Les tests de chaque task (`Task00Test`, etc.) sont fournis dans une archive à décompresser : ils arrivent dans le dossier `checks/`, à côté de `RealestateApplicationTests.java`. Tant que l'archive n'est pas extraite, `-Dtest=Task00Test` ne trouve aucun test.

```bash
tar xf ~/Downloads/spring-tests.tar.gz -C src/test/java/com/holberton/realestate/
```

## Tester avec curl

Application démarrée, dans un second terminal :

```bash
curl -i http://localhost:8080/
```

On obtient pour l'instant une réponse `HTTP/1.1 404`. C'est normal et plutôt bon signe : le serveur tourne, mais il n'y a aucun contrôleur, donc rien à cette adresse.

## Structure du projet

Les dossiers ci-dessous seront créés au fil des tasks :

```
realestate/
├── pom.xml                      # dépendances et version de Spring Boot
├── mvnw, mvnw.cmd, .mvn/        # Maven wrapper
├── src/main/java/com/holberton/realestate/
│   ├── RealestateApplication.java   # classe principale (@SpringBootApplication)
│   ├── controller/              # reçoit les requêtes HTTP, renvoie les réponses
│   ├── service/                 # logique métier
│   ├── repository/              # accès à la base de données (Spring Data JPA)
│   ├── model/                   # entités JPA (les tables)
│   ├── dto/                     # objets échangés avec l'API (entrée et sortie)
│   ├── exception/               # exceptions métier et gestion des erreurs
│   └── config/                  # configuration et chargement de données
├── src/main/resources/
│   └── application.properties   # configuration de l'application
└── src/test/java/com/holberton/realestate/
    ├── RealestateApplicationTests.java
    └── checks/                  # tests fournis pour chaque task
```
