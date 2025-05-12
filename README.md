# Java-Typs

## Évitez les méthodes privées complexes

Avoir de petite méthode privée. Utiliser les package private si l'on veut tester un élément d'un méthode privé.
Externaliser les méthode privée dans une nouvelle class en les rendant public.

## Évitez les méthodes finale

La principal utilisation de l'instruction final empêche que des subclass surcharge une implémentation. L'utilisation de l'instruction final dans les méthodes permet ainsi qu'elle ne soient pas surchargeable, cela peut nuire a de future mise en place de tests par un coéquipier (et peut être vu comme un défiance envers les autres membres de sont équipe). Et de toute façon il est possible de supprimer ces modificateurs (final) avec de la réflexion.

## Évitez les méthodes statiques

Il ne faut pas positionner des méthodes en static pour en facilité l'accès => c'est un mauvais usage du mot clef static. L'utilisation du mot clef static est a garder pour les méthodes utiles ou pour vraiment vouloir rendre une zone de code unique dans la JVM (singleton, throttling, synchronisation). Tout autre utilisation du mot clef static est a proscrire.

## Utilisation du new avec parcimonie et utilisation de l'injection de dépendance dans les constructeurs

Bien a savoir selon LASSE Koskela le mot clef new est une des formes de hardcoding la plus répandu en Java.
La création d'objet dans une méthode fige l'utilisation qu'on a de cet objet et bloque la possibilité de bouchonner cette dépendance pour les tests, de ce fait il vaux mieux ne pas mixer la création d'objet et la logique métier.
Création d'objet = factory. Sauf pour les beans

## Évitez la logique dans les constructeurs

Pour garder une bonne testabilité de la code base il faut éviter toute logique qui peux nuire a un bouchonnage avec du code spécifique dans un constructeur.
Si nous avons vraiment besoin de positionner de la logique dans un constructeur il faut passer par une méthode protected (cela permet de bouchonner ce comportement en Test)
Tout code dans les constructeurs ne doit pas être bloquant pour un Test unitaire sinon on passe par des méthodes protected.

## Évitez le Singleton ou injectez-le

Due principalement au constructeur privée le design pattern singleton peut créer des blocages pour réaliser des tests, il vaut mieux ne pas le sur exploiter ou utiliser de l'injection afin de simplifier les capacités de test du code l'utilisant.

## Privilégier la composition à l'héritage

L'héritage est a privilégier pour ses forces comme le polymorphisme, mais pour mutualiser du code il faut privilégier la composition car l'héritage augmente le couplage entre père fils (le fils est a la merci des changements du père) et limite les options du fils.

## Encapsuler les bibliothèques externes

Il faut privilégier l'encapsulation des librairies externe, et faire de l'héritage avec ce type de librairie en bonne intelligence. Tous changements ou problèmes présent dans les api externe sera importer dans notre code base par un couplage fort. De plus a des fins de tests il est beaucoup plus simple de mocker un appel a une api externe avec une interface que de fournir tout les éléments nécessaire a la bonne utilisation d'un code qui est extérieur a l'équipe. Le code des autres n'est pas forcément aussi testable que le notre.

## Évitez l'appel statique à des services

L'appel de service via un accès statique impose un minimum de code dédié au test dans la class du service appelé pour permettre le minimum de bouchonnage. Cela n'est pas impossible, mais si on utilise de l'injection au lieux d'avoir un service avec méthodes d'appel statique on limite le nombre de ligne de code dédié au test dans du code de production, et on simplifie les tests car l'injection le permet. (Pas d'appel a setMonService(avecMonMock))

# Using

Copy, inspire, grab, drag and drop, any pices of code you want.
