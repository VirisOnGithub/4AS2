import time
from tp279 import KeyGen, Encrypt, Decrypt

def benchmark():
    # KeyGen
    t0 = time.perf_counter()
    pub, priv = KeyGen(1024)
    t_keygen = time.perf_counter() - t0

    m = 123456789
    # Encrypt
    t0 = time.perf_counter()
    c = Encrypt(m, pub)
    t_enc = time.perf_counter() - t0

    # Decrypt
    t0 = time.perf_counter()
    m2 = Decrypt(c, priv)
    t_dec = time.perf_counter() - t0

    print(f"KeyGen  : {t_keygen:.3f} s")
    print(f"Encrypt : {t_enc*1000:.3f} ms")
    print(f"Decrypt : {t_dec*1000:.3f} ms")

    bloc = 128  # octets par bloc
    n_blocs = (1 * 1024**3) // bloc
    t_1go = n_blocs * t_enc
    t_1go_dec = n_blocs * t_dec
    print(f"\nTemps estimé pour 1 Go encrypté : {t_1go:.1f} s ({t_1go/3600:.2f} heures)")
    print(f"\nTemps estimé pour 1 Go décrypté : {t_1go_dec:.1f} s ({t_1go_dec/3600:.2f} heures)")
    
def moyenne_temps():
    repetitions = 10
    t_keygen_total = 0
    t_enc_total = 0
    t_dec_total = 0

    for _ in range(repetitions):
        pub, priv = KeyGen(1024)

        m = 123456789
        t0 = time.perf_counter()
        c = Encrypt(m, pub)
        t_enc_total += time.perf_counter() - t0

        t0 = time.perf_counter()
        m2 = Decrypt(c, priv)
        t_dec_total += time.perf_counter() - t0

    print(f"Temps moyen KeyGen : {t_keygen_total / repetitions:.3f} s")
    print(f"Temps moyen Encrypt : {t_enc_total / repetitions * 1000:.3f} ms")
    print(f"Temps moyen Decrypt : {t_dec_total / repetitions * 1000:.3f} ms")
    
    bloc = 128  # octets par bloc
    n_blocs = (1 * 1024**3) // bloc
    t_1go = n_blocs * (t_enc_total / repetitions)
    t_1go_dec = n_blocs * (t_dec_total / repetitions)
    print(f"\nTemps estimé pour 1 Go encrypté : {t_1go:.1f} s ({t_1go/3600:.2f} heures)")
    print(f"\nTemps estimé pour 1 Go décrypté : {t_1go_dec:.1f} s ({t_1go_dec/3600:.2f} heures)")

    

moyenne_temps()