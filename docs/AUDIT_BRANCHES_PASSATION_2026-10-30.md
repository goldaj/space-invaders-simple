# Audit GitHub et passation — 30/10/2026

## Périmètre
Space Invaders possède un prototype Android Java et une édition web Canvas distincte. Cette passe inspecte GitHub et la documentation, pas les builds ou le gameplay exécuté.

## Branches (base `main`)
- `main` (`27d4265e0fe01f210b39cd546b18a1f566386878`) : conserver.
- `chatgpt/review-20261024` (`da63aea668e9b39537ba1699369909dca41901b2`) : behind, ahead 0, behind 6, ancêtre intégré, candidate à suppression seulement après vérification des consommateurs.
- `feat/playable-web-edition-20261029` (`45c1507e406212ca478413eff02df086e629f8a4`) : behind, ahead 0, behind 3, ancêtre intégré, candidate à suppression seulement après vérification des consommateurs.

## PR
- [#2](https://github.com/goldaj/space-invaders-simple/pull/2) : fusionnée, `feat/playable-web-edition-20261029` → `main`.
- [#1](https://github.com/goldaj/space-invaders-simple/pull/1) : fusionnée, `chatgpt/review-20261024` → `main`.

## Ce qu'il faut challenger
Comparer collisions continues projectile/vaisseau, cadence et progression de difficulté, pauses/pertes de focus, contrôles tactiles et gestes multi-doigts, sauvegarde locale éventuelle et rendu à haut DPI. L'édition web ne valide pas le build Android. Ne pas effacer l'implémentation Java lors d'une modification Canvas.

## Étapes prochain agent
1. Lire `docs/WEB_EDITION.md`, `AGENTS.md`, `docs/CHATGPT_PROJECT_HISTORY.md` et README (« # Space Invaders Simple »).
2. Lancer tests `node --test tests/*.test.cjs`, tests navigateurs/tactiles, puis un build Android Studio/AGP sur SDK 35 séparé.
3. Mesurer difficulté et collisions sur plusieurs résolutions et scénarios ; ne pas annoncer une compatibilité complète sans preuve.

Tests non exécutés lors de ce passage. Aucun code, branche, PR ou déploiement modifié ; ajout documentaire uniquement.
