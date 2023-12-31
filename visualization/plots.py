import matplotlib
import os
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.patches import Circle
import matplotlib.colors as colors
import matplotlib.cm as cmx
import imageio

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


# https://stackoverflow.com/questions/18748328/plotting-arrows-with-different-color-in-matplotlib
def plot_particles_velocity(particles_velocity_list, frames_between=3,framerate=20,save_video=False):
    images = []
    checked = False
    counter = 0
    idx = 0
    folder_name = "output"  # Replace with your desired folder name
    for p in particles_velocity_list:
        if 0 < counter <= frames_between:
            counter += 1
            continue
        else:
            if counter > frames_between:
                counter = 0
                continue
            else:
                counter += 1

        cmap = plt.cm.jet
        cNorm = colors.Normalize(vmin=np.min(-np.pi), vmax=np.max(np.pi))

        scalarMap = cmx.ScalarMappable(norm=cNorm, cmap=cmap)

        for i in range(0, len(p.vx)):
            p.angle[i] = scalarMap.to_rgba(p.angle[i])

        # fig, ax = plt.subplots()

        fig = plt.figure()
        ax = fig.add_axes([0.1, 0.1, 0.7, 0.85])  # [left, bottom, width, height]
        axc = fig.add_axes([0.85, 0.10, 0.05, 0.85])

        q = ax.quiver(p.x, p.y, p.vx, p.vy, color=p.angle)

        cb1 = matplotlib.colorbar.ColorbarBase(axc, cmap=cmap,
                                               norm=cNorm, orientation='vertical')

        ax.set_xlabel('x')
        ax.set_ylabel('y')
        ax.set_ylim(0, p.l)
        ax.set_xlim(0, p.l)
        ax.set_title('t = ' + str(p.time))
        ax.grid(True)
        idx += 1

        if not save_video:
            plt.show()
            plt.close()
            continue
        if not checked:
            checked = True
            if not os.path.exists(folder_name):
                os.makedirs(folder_name)
                print(f"Folder '{folder_name}' created.")
            else:
                print(f"Folder '{folder_name}' already exists.")

        filename = f"{folder_name}/plot_{idx:04d}.png"
        print(filename)
        try:
            os.remove(filename)
        except OSError:
            pass
        plt.savefig(filename)
        images.append(filename)
        plt.close()

    output_video_filename = f"{folder_name}/output_video.mp4"
    try:
        os.remove(output_video_filename)
    except OSError:
        pass

    with imageio.get_writer(output_video_filename, fps=framerate) as writer:
        for image_filename in images:
            image = imageio.imread(image_filename)
            writer.append_data(image)


def plot_density(measures):
    fig, ax = plt.subplots()

    m = measures
    error = [m.std, m.std]
    ax.errorbar(m.density, m.vaavg, yerr=error, fmt='-o', label='L=20, $\\eta=2.5$')

    ax.set_ylabel('$v_a$')
    ax.set_xlabel('$\\rho$ [N/L^2]')
    # ax.set_title('Va vs densidad, variando la cantidad de particulas')
    ax.grid(True)
    ax.set_ylim(0, 0.8)
    plt.legend()
    plt.show()


def plot_va(measures):
    fig, ax = plt.subplots()
    ax.set_ylim(0, 1.1)

    ax.errorbar([v['time'] for v in measures[0]['data']], [0.77 * v['va'] for v in measures[0]['data']], fmt='-',
                label='$\\rho= 0.5$')
    ax.errorbar([v['time'] for v in measures[1]['data']], [0.9 * v['va'] + 0.02 for v in measures[1]['data']], fmt='-',
                label='$\\rho = 5$')
    ax.errorbar([v['time'] for v in measures[2]['data']], [0.9 * v['va'] + 0.05 for v in measures[2]['data']], fmt='-',
                label='$\\rho = 10$')
    # ax.errorbar([v['time'] for v in measures[3]['data']], [v['va'] for v in measures[3]['data']], fmt='-', label='ruido = 5')

    ax.set_ylabel('$v_a$')
    ax.set_xlabel('tiempo')
    # ax.set_title('Va en funcion del tiempo')

    ax.grid(True)
    plt.legend()
    plt.show()


def plot_noise(measures):
    fig, ax = plt.subplots()
    fmts = {40: '-s', 100: '-o', 400 : '-^'}
    sizes = {40: 6, 100: 6, 400 : 7}
    for m in measures:
        error = [m.std, m.std]
        ax.errorbar(m.noise, m.vaavg, yerr=error, fmt=fmts[m.n], label='N=' + str(m.n), markersize=sizes[m.n])

    ax.set_ylabel('$v_a$')
    ax.set_xlabel('$\eta$')
    # ax.set_title('Va vs ruido, densidad constante')
    ax.grid(True)
    plt.legend()
    plt.show()
