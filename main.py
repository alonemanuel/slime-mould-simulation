import numpy as np
import matplotlib.pyplot as plt

randim_img1 = np.asarray([[(1., 0.,1.), (0.,1.,0.)],[(1.,0.,0.),(0.,0.,0.)]])
randim_img2 = np.asarray([[(1, 0,1), (50,1,0)],[(1,0,0),(0,240,0)]])

plt.imshow(randim_img2)
plt.show()