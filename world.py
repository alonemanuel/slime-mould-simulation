import numpy as np
import matplotlib.pyplot as plt

from PIL


class world:
    DEF_DIM: int = 4
    N_RGB: int = 3

    def __init__(self, width: int = DEF_DIM, height: int = DEF_DIM):
        self._w: int = width
        self._h: int = height
        self._pixelate_world()

    def _pixelate_world(self) -> None:
        pixels: np.ndarray = np.empty((self._w, self._h), np.uint32)
        pixels.shape = self._h, self._w
        self._pixels = pixels

    def show_world(self) -> None:
        plt.imshow()
        plt.show()
