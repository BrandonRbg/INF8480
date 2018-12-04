# INF8480 TP2

Voici les détails pour bien démarrer le projet.

- Veuillez vous assurer que le registre RMI roule en le lancant dans le dossier `bin`

## Service de répertoire de noms

`./nameserver.sh`

## Serveurs de calcul

Vous pouvez lancer _n_ serveurs différents tant que vous vous assurez qu'ils roulent 
sur des ports différents, sans quoi vous aurez une erreur lors du démarrage.

`./calculationsserver.sh MAX_OPERATIONS WRONG_ANSWERS_RATIO HOSTNAME PORT NAMESERVER_HOSTNAME`

Les paramètres à définir sont optionels, mais doivent être tous redéfinis si nous voulons en modifier un seul, voici leur signification.

- MAX_OPERATIONS _(défaut: 4)_ Nombre d'opérations maximum pour avoir une acceptation garantie (Ci)
- WRONG_ANSWERS_RATIO _(défaut: 0)_ Taux de réponses erronées retournées par le serveur. 
- HOSTNAME _(défaut: localhost)_ Nom de l'hôte du serveur. 
- PORT _(défaut: 5000)_ Port sur lequel le serveur doit rouler. 
- NAMESERVER_HOSTNAME _(défaut: localhost)_ Nom de l'hôte du service de répertoire de noms. 

## Répartiteur

Une fois le service de répertoire de nom et les différents serveur de calcul lancés, vous pouvez lancer le répartiteur.

`./loadbalancer.sh FILE_PATH SECURE_MODE HOSTNAME USERNAME PASSWORD CHUNK_SIZE`

Les paramètres à définir sont optionels, mais doivent être tous redéfinis si nous voulons en modifier un seul, voici leur signification.

- FILE_PATH _(défaut: operations-588)_ Chemin vers le fichier contenant les opérations.
- SECURE_MODE _(défaut: false)_ Mode sécurisé.
- HOSTNAME _(défaut: localhost)_ Nom d'hôte pour le répartiteur et le service de répertoire de noms.
- USERNAME _(défaut: loadbalancer)_ Nom d'usager à utiliser pour s'authentifier au service de répertoire de noms.
- PASSWORD _(défaut: 123456)_ Mot de passe à utiliser pour s'authentifier au service de répertoire de noms.
- CHUNK_SIZE _(défaut: 4)_ Taille des blocs à envoyer aux serveurs de calcul.
