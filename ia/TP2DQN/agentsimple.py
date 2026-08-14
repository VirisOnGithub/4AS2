import numpy as np
import random
import torch

from QNN import QNN

class AgentSimple():
    """Agent qui utilise la prédiction de son réseau de neurones pour choisir ses actions selon une stratégie d’exploration (pas d'apprentissage)."""

    def __init__(self, state_size, action_size, hidden_size=64):
        self.action_size = action_size
        self.network = QNN(state_size, action_size)

    def action_egreedy(self, etat : np.ndarray , eps: float = 0.0) -> int:
        if np.random.rand() < eps:
            return np.random.randint(self.action_size)
        else:
            state_tensor = torch.FloatTensor(etat).unsqueeze(0)
            with torch.no_grad():
                q_values = self.network(state_tensor)
            return q_values.argmax().item()
    
        



