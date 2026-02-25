# RENDU TP4 

Auteurs : 
- Clément RENIERS
- Sofian TAQUI
- Alin BONCIU

## Rendu du TP

le rendu du TP4 se situe dans le fichier `rendu.md`. Il a également été compilé en PDF et se trouve dans le fichier `rendu.pdf`.

## Code source

Les codes sources sont tous écrits en Rust. Pour compiler du rust, il faut installer `rustc` et `cargo`.
Le meilleur moyen d'installer ces outils est d'installer la toolchain Rustup : <https://rust-lang.org/fr/learn/get-started/>.

### Script d'envoi de mails

Le script d'envoi SMTP se trouve dans le dossier `script-smtp`. Pour compiler et exécuter le script, il faut se placer dans le dossier `script-smtp` :

```bash
cd script-smtp
cargo run -r
```

### Client POP

Le client POP se trouve dans le dossier `pop-client`. Pour compiler et exécuter le client, il faut se placer dans le dossier `pop-client` :

```bash
cd pop-client
cargo run -r
```