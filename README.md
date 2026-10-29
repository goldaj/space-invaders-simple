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

## Édition web jouable (2026-10-29)

Une nouvelle édition HTML5 Canvas est disponible : **ouvrir `web/index.html` directement dans un navigateur**, même sans connexion. Vagues, bonus, boucliers, score, contrôles tactiles/souris/clavier et record local. Version autonome, sans modification du Java Android. Tests `node --test tests/*.test.cjs`. Voir [docs/WEB_EDITION.md](docs/WEB_EDITION.md).
