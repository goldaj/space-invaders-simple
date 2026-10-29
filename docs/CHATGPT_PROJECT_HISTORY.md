# Contexte de projet ChatGPT : Space Invaders Simple

> Journal pour Codex créé le 2026-10-24. **Partiel**, car les exports complets de toutes les conversations ChatGPT ne sont pas accessibles dans cette session. Les résumés ci-dessous ne sont pas des citations.

## Périmètre et état vérifiables
- Dépôt : `goldaj/space-invaders-simple`.
- Objet d'après le README : Prototype Android Java de jeu arcade Space Invaders avec rendu et contrôles tactiles ; sources importées, build pas encore validé.
- Invariants : Ne pas présenter ce prototype comme une application distribuée. Préserver le code Java, gradle, rendu et ressources ; pas de secrets Android.
- Entrée de vérification : Importer dans Android Studio (AGP 8.6.1, Java 17, compileSdk 35) ; ne pas déclarer build réussi sans l'exécuter.

## Échanges et références retrouvés

2026-10-18 — Assistant, résumé non verbatim : import d'archives de jeux simples vers des dépôts distincts.

2026-10-18 13:24:13 UTC — Utilisateur (extrait exact) : « N'écrase aucun repo existant. »

2026-10-24 — Utilisateur (verbatim, heure indisponible) : « je te demande de lire mes repo git où tu es le dernier utilisateur, de rajouter un AGENTS.md pertinent s’il n’y en a pas. Mais en plus, tu rajoutera un document repérable par codex [...] où tu mets l’intégralité de nos échanges liés à ce projet. Mes questions, tes réponses [...] et les horodatages si tu les as. » (extrait avec ellipses signalées, donc non intégral).

## À compléter avec les transcriptions sources
**Manquent :** questions/réponses intégrales de chaque session de conception, horaires individuels de certains messages et échanges Codex non publiés. Ne jamais reconstituer un dialogue en prétendant citer des propos exacts.

Lorsque des exports ChatGPT authentiques sont fournis, les archiver par session, avec date ISO/fuseau, rôles et texte intégral en préservant leur ordre. Revoir toute donnée sensible avant de pousser sur GitHub. Après chaque modification, ajouter les échanges exacts nouvellement disponibles, puis actualiser les décisions et l'état vérifié.

## Reprise du 24 octobre 2026 : vrais correctifs de collisions
- **Utilisateur (verbatim, heure non disponible)** : « Est-ce qu'il y aurait d'autres choses à faire sur d'autres repo ? Notamment sur Jade ou n'importe lequel autre - juste ne touche pas au bot hyperliquid pour le reste de la journée (on pourra revenir dessus une autre fois). »
- **Assistant (résumé non verbatim)** : inspection du gameplay Java,
  correction des collisions manquées lorsque les tirs sautent une cible
  entre deux images, avec tests JVM isolés. Aucune intervention sur un
  dépôt de bot Hyperliquid ; branche et PR brouillon uniquement.
- Validation : 8 assertions de géométrie exécutées localement sur Java 21,
  sans SDK Android. Tests d'intégration Android restant à faire.

## 2026-10-29 : jeu web autonome

Utilisateur (verbatim, date du fil, heure non certifiée) : « Je ne te demande pas de créer une tâche planifiée qui continue chaque jour. Je te demande de continuer maintenant en boucle et de ne pas t'arrêter sur juste 3 améliorations alors qu'il y a plus de 40 repo et plusieurs améliorations possibles par repo ».

Assistant (résumé non verbatim) : évolution de `space-invaders-simple` avec une édition web Canvas jouable, mécanique de vagues, boucliers, bonus, score et contrôles tactiles/clavier. Le code Android est resté intact. Tests automatiques et documentation dans `docs/WEB_EDITION.md`. Essais visuels réels et build Android non réalisés.
