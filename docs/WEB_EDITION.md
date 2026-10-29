# Space Invaders — édition web jouable

Un jeu HTML5 Canvas complet **indépendant** du code Java Android historique. Ouvrir `web/index.html` dans un navigateur, sans serveur ni compte. Compatible clavier (gauche/droite, A/D, P pause, R recommencer), souris et glissement du doigt.

Le vaisseau tire automatiquement. Formation initiale de 24 ennemis, tirs ennemis, vagues successives, trois boucliers destructibles, bonus de soin (jusqu'à cinq vies) et tir rapide de huit secondes. Le score est conservé localement dans `localStorage` ; aucune synchronisation avec l'application Android.

## Architecture
- `web/engine.js` : moteur JavaScript indépendant du DOM ; simulation déterministe possible en passant une graine aléatoire.
- `web/app.js` : dessin Canvas, clavier, pointeur/tactile, interface et record local.
- `tests/game.test.cjs` : tests Node sans navigateur ni dépendances.
- `.github/workflows/web-tests.yml` : CI tests et vérification de syntaxe.

## Validation
`node --test tests/*.test.cjs` (Node 18+) et `node --check web/engine.js` / `web/app.js`. Une validation visuelle de l'écran mobile et des performances reste nécessaire. La logique et l'équilibrage du moteur web peuvent différer de la version Android, qui reste inchangée.

## Suite pour Codex
1. Mesurer l'équilibrage sur plusieurs centaines de vagues et plusieurs scénarios d'adresse, puis ajuster les fréquences de tir, vitesse et bonus.
2. Ajouter un niveau de boss, des formations spéciales et une sélection de difficulté sans punir les écrans étroits.
3. Écrire des tests de navigateur sur les interactions pointer et keyboard ainsi que la sauvegarde du record.
4. Définir une éventuelle mutualisation Android/Web de règles de gameplay, à condition d'être testée et de conserver l'expérience native existante.
5. Ajouter une option d'accessibilité pour arrêter les effets visuels/flash, et une gestion de son optionnelle.
