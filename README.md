# Space Invaders Simple

**Prototype Android | Java · Android SDK · rendu et interactions tactiles**

Recréation minimaliste d'une boucle de gameplay inspirée des jeux de tir arcade. L'objectif est de travailler une application Android autonome, sans backend ni compte utilisateur.

## Contenu

- Activité Android Java et rendu du jeu.
- Manifeste Android et ressources associées.
- Échantillon de code source historique récupéré depuis les archives du projet.

## Développement

Importer les sources avec Android Studio et configurer le SDK local. Le dépôt contient `settings.gradle`, `build.gradle` et `app/build.gradle` ; cette configuration déclare le plugin Android Gradle 8.6.1, Java 17 et compileSdk 35. Le wrapper Gradle n'est pas fourni dans l'état inspecté.

**Stade :** prototype source de reprise et de refactorisation. La compilation, le gameplay sur appareil et la distribution ne sont pas validés par cette revue documentaire.

## Améliorations

- Ajouter une structure de projet Android actualisée.
- Isoler moteur, contrôles tactiles et rendu.
- Ajouter tests, CI et captures d'écran.

## Compétences

Java, API Android, UI interactive, boucle d'animation et développement de jeux simples.

## Correction de gameplay (branche `chatgpt/review-20261024`)

Les projectiles testent désormais **tout le trajet vertical parcouru pendant
une image**, et plus uniquement leur position finale. Une balle rapide ne peut
donc pas traverser une rangée d'ennemis ou le vaisseau du joueur par saut
entre deux frames. En cas de deux ennemis traversés dans la même image,
le plus proche du point de départ est touché. Les balles sortant de l'écran
ne sont retirées qu'après les collisions.

Test de géométrie indépendant de l'Android SDK, exécuté en JVM :

```bash
mkdir -p build/collision-tests
javac -d build/collision-tests app/src/main/java/com/jason/spaceinvaders/CollisionMath.java tests/java/com/jason/spaceinvaders/CollisionMathTest.java
java -cp build/collision-tests com.jason.spaceinvaders.CollisionMathTest
```

**Vérification observée :** 8 assertions JVM réussies sous Java 21.
**Limite :** compilation APK et gameplay Android réel non testés (wrapper
Gradle absent). Le correctif reste dans une PR brouillon non fusionnée.
