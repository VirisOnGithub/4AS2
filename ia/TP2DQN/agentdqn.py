import numpy as np
import random
from collections import namedtuple, deque

from QNN import QNN
from replaybuffer import ReplayBuffer

import torch
from torch import nn
import torch.nn.functional as F
import torch.optim as optim


class AgentDQN():
    """Agent qui utilise l'algorithme de deep QLearning avec replaybuffer."""

    def __init__(self, dim_etat:int, dim_action:int, gamma=0.99, batch_size=64):
        """Constructeur.
        

        """
        self.state_size = dim_etat
        self.action_size = dim_action
        self.network = QNN(dim_etat, dim_action)
        self.optimizer = optim.Adam(self.network.parameters(), lr=0.001)
        self.memory = ReplayBuffer(taille_buffer=10000, taille_batch=batch_size)
        self.nb_pas = 0
        self.pas_par_apprentissage = 4
        self.gamma = gamma
        # self.batch_size = batch_size
        

    def phase_interaction(self,etat : np.ndarray ,action : np.ndarray ,recompense: float,etat_suivant: np.ndarray ,terminaison: bool):
        self.memory.add(etat, action, recompense, etat_suivant, terminaison)
        self.nb_pas += 1
        
        if self.nb_pas % self.pas_par_apprentissage == 0 and len(self.memory) > self.memory.batch_size:
            self.phase_apprentissage()
        
    def phase_apprentissage(self):
        states, actions, rewards, next_states, dones = self.memory.sample()
        q_pred = self.network(states).gather(1, actions)
        
        with torch.no_grad():
            q_next = self.network(next_states).max(1)[0]
            q_target = rewards + (self.gamma * q_next * (1 - dones))
        
        loss = F.mse_loss(q_pred, q_target)
        self.optimizer.zero_grad()
        loss.backward()
        self.optimizer.step()

    
    def action_egreedy(self, etat : np.ndarray ,eps: float = 0.0) -> int:
        if random.random() > eps:
            return random.randint(0, self.action_size - 1)
        else:
            state_tensor = torch.FloatTensor(etat).unsqueeze(0)
            with torch.no_grad():
                q_values = self.network(state_tensor)
            return q_values.argmax().item()
