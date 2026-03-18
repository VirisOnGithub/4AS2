#!/bin/bash

# Créer les répertoires de boîtes mails
mkdir -p mail/u
mkdir -p mail/u2

# Ajouter des mails d'exemple pour u
cat > mail/u/mail1.txt << 'EOF'
From: alice@example.com
To: u@example.com
Subject: Premier mail
Date: Mon, 3 Mar 2025 10:00:00 +0000

Bonjour u,

Ceci est votre premier mail de test.

Cordialement,
Alice
EOF

cat > mail/u/mail2.txt << 'EOF'
From: bob@example.com
To: u@example.com
Subject: Deuxième mail
Date: Mon, 3 Mar 2025 11:00:00 +0000

Bonjour u,

Ceci est votre deuxième mail de test.

Cordialement,
Bob
EOF

# Ajouter des mails d'exemple pour u2
cat > mail/u2/mail1.txt << 'EOF'
From: charlie@example.com
To: u2@example.com
Subject: Mail pour u2
Date: Mon, 3 Mar 2025 12:00:00 +0000

Bonjour u2,

Ceci est un mail de test pour u2.

Cordialement,
Charlie
EOF

echo "Boîtes mails initialisées avec succès!"
ls -la mail/
ls -la mail/u/
ls -la mail/u2/

