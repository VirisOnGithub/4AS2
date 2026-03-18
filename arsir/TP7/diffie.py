from utils import *
import socket
import random

PORT = 12345

cbl = crible(1_000_000)

p = random.choice(cbl[1000:]) #on prend un nombre premier au hasard parmi les premiers plus grands que 1000
g = find_gen(p)

def prints(s):
    print(f"[SERVEUR] {s}")

def printc(s):
    print(f"[CLIENT] {s}")

def server():
    b = random.randint(1, 100000)

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(('localhost', PORT))
    s.listen(True)

    prints(f"Serveur en écoute sur le port {PORT} avec p={p} et g={g}")

    conn, addr = s.accept()
    prints(f"Connexion de {addr}")

    conn.send(str(p).encode())
    prints(f"Envoyé p={p} au client")

    conn.recv(1024) #ack

    conn.send(str(g).encode())
    prints(f"Envoyé g={g} au client")

    ga = int(conn.recv(1024).decode())
    prints(f"Reçu ga={ga} du client")

    gb = exp_mod(g, b, p)
    conn.send(str(gb).encode())
    prints(f"Envoyé gb={gb} au client")

    gab = exp_mod(ga, b, p)
    prints(f"Clé partagée gab={gab}")
    conn.close()

    s.listen(False)
    s.close()

def client():
    a = random.randint(1, 100000)

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('localhost', PORT))
    printc(f"Client connecté au serveur sur le port {PORT}")

    p = int(s.recv(1024).decode())
    printc(f"Reçu p={p} du serveur")

    s.send("ack".encode()) #ack
    
    g = int(s.recv(1024).decode())
    printc(f"Reçu g={g} du serveur")

    ga = exp_mod(g, a, p)
    s.send(str(ga).encode())
    printc(f"Envoyé ga={ga} au serveur")

    gb = int(s.recv(1024).decode())
    printc(f"Reçu gb={gb} du serveur")
    
    gab = exp_mod(gb, a, p)
    
    printc(f"Clé partagée gab={gab}")
    s.close()

if __name__ == "__main__":
    choice = input("Démarrer en mode serveur (s) ou client (c) ? ")
    if choice.lower() == 's':
        server()
    elif choice.lower() == 'c':
        client()
    else:
        print("Choix invalide. Veuillez entrer 's' pour serveur ou 'c' pour client.")