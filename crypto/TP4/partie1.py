from lib import Paillier, gen_random_znz

class Bob:
    def __init__(self):
        self._X = None
        self._Y = None
        self.pk = None
    
    def alice_gives_pk(self, pk: int):
        self.pk = pk
    
    def encryptions_setup(self, X: int, Y: int):
        self._X = X
        self._Y = Y

class Alice:
    def __init__(self):
        self.pk, self._sk = Paillier.KeyGen(1024)



def multiplication(bob: Bob, alice: Alice) -> int:
    """
    Protocole : 
    
    - Bob envoie des encryptions de (r + x) et (s + y) à Alice, pour r et s sont aléatoirement choisis par Bob
    - Alice envoie une encryption de (r + x)(s + y) à Bob
    - Bob peut alors calculer une encryption de (r + x)(s + y) - sx - ry - rs = xy
    """
    # Bob
    def mult1():
        r = gen_random_znz(alice.pk)
        s = gen_random_znz(alice.pk)
        c_rx = Paillier.Add(bob._X, Paillier.Encrypt(r, alice.pk), alice.pk)
        c_sy = Paillier.Add(bob._Y, Paillier.Encrypt(s, alice.pk), alice.pk)
        return r, s, c_rx, c_sy
    
    # Alice
    def mult2(c_rx: int, c_sy: int) -> int:
        ex = Paillier.Decrypt(c_rx, alice._sk, alice.pk)
        c_rx_sy = Paillier.MulConst(c_sy, ex, alice.pk)
        return c_rx_sy
    
    # Bob
    def mult3(r: int, s: int, c_rx_sy: int) -> int:
        minus_sx = Paillier.MulConst(bob._X, -s, alice.pk)
        minus_ry = Paillier.MulConst(bob._Y, -r, alice.pk)
        minus_rs = Paillier.Encrypt(-r * s, alice.pk)
        c_xy = Paillier.Add(c_rx_sy, minus_sx, alice.pk)
        c_xy = Paillier.Add(c_xy, minus_ry, alice.pk)
        c_xy = Paillier.Add(c_xy, minus_rs, alice.pk)
        return c_xy
    
    r, s, c_rx, c_sy = mult1()
    c_rx_sy = mult2(c_rx, c_sy)
    c_xy = mult3(r, s, c_rx_sy)
    return c_xy
    
    


if __name__ == "__main__":
    alice = Alice()
    bob = Bob()
    bob.alice_gives_pk(alice.pk)
    x, y = 12345, 67890
    X = Paillier.Encrypt(x, alice.pk)
    Y = Paillier.Encrypt(y, alice.pk)
    bob.encryptions_setup(X, Y)
    print(f"Bob a chiffré x={x} en X={X} et y={y} en Y={Y}")
    c_xy = multiplication(bob, alice)
    xy = Paillier.Decrypt(c_xy, alice._sk, alice.pk)
    print(f"Bob a calculé une encryption de xy={x*y} : c_xy={c_xy} et Alice a déchiffré pour obtenir {xy}")
    