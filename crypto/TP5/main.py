from lib import Paillier


class DNF:
    def __init__(self, base_value: list[set]):
        nb_var = len(base_value[0])
        for index_clause in range(nb_var):
            clause = base_value[index_clause]
            if sorted([x % nb_var for x in clause]) != list(range(nb_var)):
                raise ValueError(
                    f"Impossible de créer cette DNF pour la clause {index_clause}"
                )
        self.bv = base_value

    def query(self, clause_index: int, variable_index) -> bool:
        return variable_index in self.bv[clause_index]


class Bob:
    def __init__(self, variables_values: list[bool]):
        self.pk, self.sk = Paillier.KeyGen()
        self.vv = variables_values

    def get_pk(self):
        return self.pk

    def evalDNF1(self) -> list[int]:
        return [Paillier.Encrypt(x, self.pk) for x in self.vv]


class Alice:
    def __init__(self, bob_pk: int, dnf: DNF):
        self.pk = bob_pk
        self.dnf = dnf

    def evalDNF2(self, bob_enc_var: list[int]) -> list[int]:
        # génération des xbarre
        extended_enc = bob_enc_var
        for i in range(len(bob_enc_var)):
            xibarre = Paillier.Add(
                Paillier.Encrypt(1, self.pk),
                Paillier.MulConst(bob_enc_var[i], -1, self.pk),
                self.pk,
            )
            extended_enc.append(xibarre)

        # génération des Cj
        res = []
        


def evalDNF(bob: Bob, alice: Alice) -> bool:
    evaldnf1 = bob.evalDNF1()
    evaldnf2 = alice.evalDNF2(evaldnf1)


dnf = DNF([{1, 2, 3}, {4, 5, 6}, {1, 2, 6}])
variables_values = [True, True, True]
bob = Bob(variables_values)
alice = Alice(bob.get_pk(), dnf)
print(evalDNF(bob, alice))
