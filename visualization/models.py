class Particle:
    def __init__(self, particle_id: int, x: float, y: float, radius: float):
        self.id = particle_id
        self.x = x
        self.y = y
        self.radius = radius
        self.neighbours = []

    def __str__(self):
        return f'({self.x}, {self.y}) id = {self.id} n = {self.neighbours}'


class Board:
    def __init__(self, L: int, M: int, rc: int, time: int, particles: list[Particle]):
        self.L = L
        self.M = M
        self.rc = rc
        self.time = time
        self.particles = particles

    @property
    def cell_length(self):
        return self.L / self.M


class TimeMeasures:
    def __init__(self):
        self.particles_count = []
        self.bf_max = []
        self.bf_min = []
        self.bf_avg = []
        self.cim_max = []
        self.cim_min = []
        self.cim_avg = []


class MTimeMeasures:
    def __init__(self):
        self.m = []
        self.max = []
        self.min = []
        self.avg = []


class ParticleVelocity:
    def __init__(self, time, l):
        self.l = l
        self.time = time
        self.id = []
        self.x = []
        self.y = []
        self.vx = []
        self.vy = []
        self.angle = []
        self.length = []


class DensityMeasures:
    def __init__(self):
        self.vamin = []
        self.vamax = []
        self.vaavg = []
        self.std = []
        self.density = []


class NoiseMeasures:
    def __init__(self, n):
        self.n = n
        self.vamin = []
        self.vamax = []
        self.vaavg = []
        self.std = []
        self.noise = []