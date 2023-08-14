import matplotlib
import matplotlib.pyplot as plt
from matplotlib.patches import Circle

from visualization.models import Board, TimeMeasures, MTimeMeasures


def configure_plot(board: Board):
    fig, ax = plt.subplots(figsize=(6, 6))

    circles_data = {}
    for particle in board.particles:
        circle = Circle((particle.x, particle.y), radius=particle.radius, alpha=0.7, color='b')
        circles_data[particle.id] = {'particle': particle, 'circle': circle}
        ax.add_patch(circle)

    return fig, ax, circles_data


def configure_axes(board, ax):
    subgrid_size = board.cell_length
    x_ticks = []
    y_ticks = []

    for i in range(board.M + 1):
        x_tick_value = subgrid_size * i
        y_tick_value = subgrid_size * i
        x_ticks.append(x_tick_value)
        y_ticks.append(y_tick_value)

    ax.set_xticks(x_ticks)
    ax.set_yticks(y_ticks)
    ax.grid()

    ax.set_xlim(0, board.L)
    ax.set_ylim(0, board.L)

    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_title('Particle Distribution for time ' + str(board.time))


def motion_hover(event, plotting_data, ax, fig):
    if event.inaxes == ax:
        hovered_circle = None
        hovered_particle = None
        for particle_id, data in plotting_data.items():
            c = data['circle']
            contains, _ = c.contains(event)
            if contains:
                hovered_circle = c
                hovered_particle = data['particle']
                break
        if hovered_circle and hovered_particle:
            hovered_circle.set_color('red')
            for neighbour_id in hovered_particle.neighbours:
                if neighbour_id in plotting_data:
                    neighbour_circle = plotting_data[neighbour_id]['circle']
                    neighbour_circle.set_color('green')
        else:
            for particle_id, data in plotting_data.items():
                c = data['circle']
                original_color = 'b'
                c.set_color(original_color)
        fig.canvas.draw()


def plot_particles_in_board(board):
    matplotlib.use('TkAgg')

    fig, ax, plotting_data = configure_plot(board)

    fig.canvas.mpl_connect('motion_notify_event', lambda event: motion_hover(event, plotting_data, ax, fig))

    configure_axes(board, ax)

    return fig


def plot_times(time_measures: TimeMeasures):
    fig, ax = plt.subplots()

    bf_error = [time_measures.bf_min, time_measures.bf_max]
    cim_error = [time_measures.cim_min, time_measures.cim_max]

    ax.errorbar(time_measures.particles_count, time_measures.bf_avg, yerr=bf_error, label='Brute force', fmt='-o')
    ax.errorbar(time_measures.particles_count, time_measures.cim_avg, yerr=cim_error, label='CIM', fmt='-o')

    ax.set_xlabel('Particles')
    ax.set_ylabel('Time [ms]')
    ax.set_title('Brute force vs CIM method')
    ax.grid(True)
    plt.legend()
    plt.show()

def plot_m_times(time_measures: MTimeMeasures):
    fig, ax = plt.subplots()

    error = [time_measures.min, time_measures.max]
    ax.errorbar(time_measures.m, time_measures.avg, yerr=error, fmt='-o')

    ax.set_xlabel('M')
    ax.set_ylabel('Time [ms]')
    ax.set_title('Computation time for different M')
    ax.grid(True)
    plt.legend()
    plt.show()