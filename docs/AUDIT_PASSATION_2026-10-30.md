# Contre-audit et passation — Space Invaders Simple (2026-10-30)

Contrôles GitHub : 3 branches, 2 PR et arborescence. Partie web et tests non exécutés durant la revue.

## Branches
- `main` : `f6000f77302df85063b811706c75d4a5a548a50d`.
- `chatgpt/review-20261024` : behind, avance 0, retard 4, contenu déjà intégré, candidate nettoyage après vérification des usages.
- `feat/playable-web-edition-20261029` : behind, avance 0, retard 1, contenu déjà intégré, candidate nettoyage après vérification des usages.

## PR
- [#2](https://github.com/goldaj/space-invaders-simple/pull/2) : closed, fusionnée — feat: fully playable Space Invaders web edition with power-ups and mobile controls
- [#1](https://github.com/goldaj/space-invaders-simple/pull/1) : closed, fusionnée — [DRAFT] Space Invaders: collisions continues des projectiles

## Racine
`.github`, `.gitignore`, `AGENTS.md`, `README.md`, `README.txt`, `app`, `build.gradle`, `docs`, `gradle.properties`, `settings.gradle`, `tests`, `web`.

## Points de validation
1. Tester les collisions continues des projectiles et le comportement aux FPS variables, y compris collisions à forte vitesse.
2. Vérifier l'édition web jouable : contrôles mobiles, overlays, mise en pause, sons et redémarrage sans état résiduel.
3. Mesurer équilibre des ennemis, power-ups et difficulté progressive sur plusieurs sessions complètes.
4. Contrôler performances et sécurité d'exécution navigateur/offline, et adapter la taille de l'écran.
5. Branches secondaires déjà incluses dans main : ne pas réouvrir de PR juste pour les supprimer ; inspecter workflows/références d'abord.

Aucun résultat de test ou équilibre n'est revendiqué comme démontré.
