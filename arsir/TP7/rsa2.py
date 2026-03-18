import random
from utils import *
import socket
import pickle

PORT = 11112

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
    
    e = 3
    while True:
        try:
            d = inverse_mod(e, phi)
            break
        except ValueError:
            e += 2
                
    return (e, n), (d, n)

def encrypt(nb, pub_key):
    e, n = pub_key
    return pow(nb, e, n)

def decrypt(cipher, priv_key):
    d, n = priv_key
    return pow(cipher, d, n)

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

    # Echange des clés publiques (récupéré par Laurent)
    alice_pub_key = pickle.loads(conn1.recv(4096))
    printc(f"Laurent a reçu la clé publique d'Alice : {alice_pub_key}")
    conn2.send(pickle.dumps(alice_pub_key))
    printc(f"Laurent a envoyé la clé publique d'Alice à Bertrand")

    bert_pub_key = pickle.loads(conn2.recv(4096))
    bert_e, bert_n = bert_pub_key
    printc(f"Laurent a reçu la clé publique de Bertrand : {bert_pub_key}")
    conn1.send(pickle.dumps(bert_pub_key))
    printc(f"Laurent a envoyé la clé publique de Bertrand à Alice")

    while True:
        try:
            data1 = conn1.recv(4096)
            if not data1:
                break
            encrypted_msg1 = pickle.loads(data1)
            factor = pow(2, bert_e, bert_n)
            modified_cipher1 = (encrypted_msg1 * factor) % bert_n
            conn2.send(pickle.dumps(modified_cipher1))
            prints(f"Message modifié d'Alice vers Bertrand ({len(data1)} bytes)")

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
        123456789,
        987654321,
        555555555
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
        111111111,
        222222222,
        333333333
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
    
    try:
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
    except Exception as e:
        print(f"Erreur dans le réseau: {e}")
        serveur_thread.join(timeout=1)
        alice_thread.join(timeout=1)
        bertrand_thread.join(timeout=1)
    print("Communication terminée.")
