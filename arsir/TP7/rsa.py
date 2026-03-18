import random
from utils import *
import socket
import pickle

PORT = 17878

cbl = crible(1_000_000)

def generate_rsa_keypair():
    # Prendre deux grands nombres premiers de la liste
    primes = [p for p in cbl if p > 1000]
    p = random.choice(primes)
    q = random.choice(primes)
    while p == q:
        q = random.choice(primes)
        
    n = p * q
    phi = (p - 1) * (q - 1)
    
    e = 65537
    # s'assurer que e est premier avec phi
    try:
        d = inverse_mod(e, phi)
    except ValueError:
        e = 3
        while True:
            try:
                d = inverse_mod(e, phi)
                break
            except ValueError:
                e += 2
                
    return (e, n), (d, n)

def encrypt(message, pub_key):
    e, n = pub_key
    # Convertir le string en liste d'entiers, puis chiffrer chaque entier
    return [pow(ord(char), e, n) for char in message]

def decrypt(cipher, priv_key):
    d, n = priv_key
    # Déchiffrer chaque entier, puis convertir en string
    return ''.join([chr(pow(char, d, n)) for char in cipher])

def prints(s):
    print(f"[SERVEUR] {s}")

def printc(s):
    print(f"[CLIENT] {s}")

def laurent():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(('localhost', PORT))
    s.listen(True)

    prints(f"En attente de connexions sur le port {PORT}...")

    conn1, addr1 = s.accept()
    prints(f"Alice connectée depuis {addr1}")
    
    conn2, addr2 = s.accept()
    prints(f"Bertrand connecté depuis {addr2}")

    while True:
        try:
            data1 = conn1.recv(4096)
            if not data1:
                break
            prints(f"Message relayé d'Alice vers Bertrand ({len(data1)} bytes)")
            conn2.send(data1)

            data2 = conn2.recv(4096)
            if not data2:
                break
            prints(f"Message relayé de Bertrand vers Alice ({len(data2)} bytes)")
            conn1.send(data2)
        except Exception as e:
            prints(f"Erreur de relais: {e}")
            break
            
    conn1.close()
    conn2.close()
    s.close()

def alice():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('localhost', PORT))
    printc(f"Alice connectée à Laurent sur le port {PORT}")

    # Génération des clés d'Alice
    pub_key, priv_key = generate_rsa_keypair()
    printc(f"Alice a généré ses clés (pub: {pub_key})")
    
    # Envoi de la clé publique
    s.send(pickle.dumps(pub_key))
    
    # Réception de la clé publique de Bertrand
    bert_pub_key = pickle.loads(s.recv(4096))
    printc(f"Alice a reçu la clé publique de Bertrand : {bert_pub_key}")
    
    messages_to_send = [
        "Salut Bertrand, tu m'entends bien ?",
        "Super ! C'est vraiment sécurisé grâce à RSA.",
        "A plus tard ! Fin de transmission."
    ]

    for msg in messages_to_send:
        # Envoi d'un message chiffré
        encrypted_msg = encrypt(msg, bert_pub_key)
        s.send(pickle.dumps(encrypted_msg))
        printc(f"Alice a envoyé : '{msg}' (chiffré)")
        
        # Réception de la réponse
        encrypted_resp = pickle.loads(s.recv(4096))
        resp = decrypt(encrypted_resp, priv_key)
        printc(f"Alice a reçu et déchiffré : '{resp}'")

    s.close()

def bertrand():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('localhost', PORT))
    printc(f"Bertrand connecté à Laurent sur le port {PORT}")

    # Génération des clés de Bertrand
    pub_key, priv_key = generate_rsa_keypair()
    printc(f"Bertrand a généré ses clés (pub: {pub_key})")
    
    # Envoi de la clé publique
    s.send(pickle.dumps(pub_key))
    
    # Réception de la clé publique d'Alice
    alice_pub_key = pickle.loads(s.recv(4096))
    printc(f"Bertrand a reçu la clé publique d'Alice : {alice_pub_key}")
    
    responses = [
        "Salut Alice ! Oui 5/5, la connexion est claire.",
        "Absolument, Laurent n'y voit que du feu !",
        "Ça marche, bonne journée !"
    ]

    for resp in responses:
        # Attente d'un message chiffrée d'Alice
        encrypted_msg = pickle.loads(s.recv(4096))
        msg = decrypt(encrypted_msg, priv_key)
        printc(f"Bertrand a reçu et déchiffré : '{msg}'")

        # Envoi de la réponse chiffrée
        encrypted_resp = encrypt(resp, alice_pub_key)
        s.send(pickle.dumps(encrypted_resp))
        printc(f"Bertrand a envoyé : '{resp}' (chiffré)")

    s.close()

if __name__ == "__main__":
    import threading
    import time

    print("Démarrage du réseau...")
    
    # Démarrage de Laurent (serveur) dans un thread
    serveur_thread = threading.Thread(target=laurent, daemon=True)
    serveur_thread.start()
    
    time.sleep(1) # Laisse le temps au serveur de s'initialiser
    
    # Démarrage d'Alice (client 1)
    alice_thread = threading.Thread(target=alice)
    alice_thread.start()
    
    time.sleep(1) # Laisse le temps à Alice de se connecter en premier
    
    # Démarrage de Bertrand (client 2)
    bertrand_thread = threading.Thread(target=bertrand)
    bertrand_thread.start()
    
    # Attente de la fin des échanges clients
    alice_thread.join()
    bertrand_thread.join()
    print("Communication terminée.")
