import torch
import torch.nn as nn
import torch.nn.functional as F
import numpy as np

class QNN(nn.Module):
    """Reseau de neurones pour approximer la Q fonction."""

    def __init__(self,dim_entree:int, dim_sortie:int, hidden_size:int=64):
        """Initialisation des parametres ...
        """
        super(QNN, self).__init__()
        
        "*** TODO ***"
        self.nw = nn.Sequential(
            nn.Linear(dim_entree, hidden_size),
            nn.ReLU(),
            nn.Linear(hidden_size, hidden_size),
            nn.ReLU(),
            nn.Linear(hidden_size, dim_sortie)
        )
        
    def forward(self, etat: np.ndarray) -> torch.Tensor :
        """Forward pass"""

        if isinstance(etat, np.ndarray):
            etat = torch.tensor(etat, dtype=torch.float)
            
        "*** TODO ***"
        etat = self.nw(etat)
        
        return etat


