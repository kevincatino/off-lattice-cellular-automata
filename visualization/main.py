from matplotlib import pyplot as plt
from matplotlib.backends._backend_tk import NavigationToolbar2Tk
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

from visualization.models import Board, Particle
from visualization.plots import plot_particles_in_board, plot_times, plot_m_times
from visualization.utils import parse_time_json, parse_json, parse_m_time_json

boards = parse_json()

# board = Board(20, 6, 1, 0,
#               [
#                   Particle(1, 2, 3, 1, [2, 3]),
#                   Particle(2, 15, 4, 3, []),
#                   Particle(3, 7, 5, 2, [2])
#               ]
#               )
#
# board1 = Board(20, 6, 1, 1,
#               [
#                   Particle(1, 2, 3, 1, [2, 3]),
#                   Particle(2, 15, 8, 3, []),
#                   Particle(3, 7, 5, 2, [2])
#               ]
#               )

# boards = [board, board1]
for board in boards:
    fig = plot_particles_in_board(board)
    plt.plot()
    plt.figure(fig.number)
plt.show()

time_measures = parse_time_json()
plot_times(time_measures)
time_measures = parse_m_time_json()
plot_m_times(time_measures)