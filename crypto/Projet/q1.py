import math

from lib import Paillier

sk = 0


class Alice:
    def __init__(self, xa: int, ya: int):
        self.xa = xa
        self.ya = ya
        self._pk, self._sk = Paillier.KeyGen()
        # global sk
        # sk = self._sk

    def get_pk(self) -> int:
        return self._pk

    def send_and_encrypt_coords(self) -> tuple[int, int]:
        # print(
        #     "test :",
        #     Paillier.Decrypt(Paillier.Encrypt(self.xa, self._pk), self._sk, self._pk),
        # )
        return Paillier.Encrypt(self.xa, self._pk), Paillier.Encrypt(self.ya, self._pk)

    def distance(self, bob_encrypted: int) -> int:
        # Le message Paillier vit dans Z_n : une valeur "négative" est représentée modulo n.
        # On repasse en entier signé avant de calculer la distance.
        bob_decrypted = Paillier.DecryptSigned(bob_encrypted, self._sk, self._pk)
        d2 = bob_decrypted + self.xa**2 + self.ya**2
        if d2 < 0:
            raise ValueError(
                "Distance^2 négative : problème d'encodage modulo n ou dépassement."
            )
        return math.isqrt(d2)


class Bob:
    def __init__(self, xb: int, yb: int, alice_pk: int):
        self.xb = xb
        self.yb = yb
        self.alice_pk = alice_pk

    def needed_crypted_info(self, alice_encrypted_coords: tuple[int, int]) -> int:
        Xa, Ya = alice_encrypted_coords
        XaXb = Paillier.MulConst(Xa, self.xb, self.alice_pk)
        # print("xaxb = ", Paillier.DecryptSigned(XaXb, sk, self.alice_pk))
        YaYb = Paillier.MulConst(Ya, self.yb, self.alice_pk)
        # print("yayb = ", Paillier.DecryptSigned(YaYb, sk, self.alice_pk))
        XaXbPlusYaYb = Paillier.Add(XaXb, YaYb, self.alice_pk)
        # print("xaxb + yayb = ", Paillier.DecryptSigned(XaXbPlusYaYb, sk, self.alice_pk))
        Minus2 = Paillier.MulConst(XaXbPlusYaYb, -2, self.alice_pk)
        # print("minus 2 = ", Paillier.DecryptSigned(Minus2, sk, self.alice_pk))
        return Paillier.Add(
            Minus2,
            Paillier.Encrypt(self.xb**2 + self.yb**2, self.alice_pk),
            self.alice_pk,
        )


def send_distance(alice: Alice, bob: Bob) -> int:
    alice_encrypted_coords = alice.send_and_encrypt_coords()
    bob_returns = bob.needed_crypted_info(alice_encrypted_coords)
    return alice.distance(bob_returns)


if __name__ == "__main__":
    alice = Alice(2000, 1000)
    bob = Bob(1000, 2000, alice.get_pk())
    print(send_distance(alice, bob))
