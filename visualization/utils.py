import json
import math

import models


def parse_json():
    # Opening JSON file
    f = open('../sequence4.json')

    # returns JSON object as
    # a dictionary
    json_input = json.load(f)

    L = json_input['l']
    M = json_input['m']
    rc = json_input['rc']
    data = json_input['data']

    # Closing file
    f.close()

    board_list = []

    for time in data:
        particles = []
        for part in time['particles']:
            obj_particle = models.Particle(part['id'], part['coordinates']['x'], part['coordinates']['y'], part['radius'])
            for n in part['neigbours']:
                neighbour = n['id']
                obj_particle.neighbours.append(neighbour)
            particles.append(obj_particle)
        board_list.append(models.Board(L, M, rc, time['time'], particles))

    return board_list


def parse_time_json():
    # Opening JSON file
    f = open('../stats.json')

    # returns JSON object as
    # a dictionary
    json_input = json.load(f)

    time_measures = models.TimeMeasures()

    for t in json_input:
        time_measures.particles_count.append(t['particles'])
        time_measures.bf_max.append(t['bruteForce']['max'])
        time_measures.bf_min.append(t['bruteForce']['min'])
        time_measures.bf_avg.append(t['bruteForce']['avg'])
        time_measures.cim_max.append(t['cim']['max']-t['cim']['avg'])
        time_measures.cim_min.append(t['cim']['avg']-t['cim']['min'])
        time_measures.cim_avg.append(t['cim']['avg'])

    return time_measures


def parse_m_time_json():
    # Opening JSON file
    f = open('../mstats.json')

    # returns JSON object as
    # a dictionary
    json_input = json.load(f)

    time_measures = models.MTimeMeasures()

    for t in json_input:
        time_measures.m.append(t['m'])
        time_measures.max.append(t['time']['max']-t['time']['avg'])
        time_measures.min.append(t['time']['avg']-t['time']['min'])
        time_measures.avg.append(t['time']['avg'])

    return time_measures


def parse_particle_velocity():
    f = open('../sequence40-01.json')

    json_input = json.load(f)
    l = json_input['l']
    json_input = json_input['data']

    time_velocity_list = []
    for t in json_input:
        particle = models.ParticleVelocity(t['time'], l)
        for p in t['particles']:
            particle.id.append(p['id'])
            particle.x.append(p['coordinates']['x'])
            particle.y.append(p['coordinates']['y'])
            vx = p['velocity']['vx']
            vy = p['velocity']['vy']
            particle.vx.append(vx)
            particle.vy.append(vy)
            particle.angle.append(math.atan2(vy, vx))
            particle.length.append(math.sqrt(vx**2 + vy**2))
        time_velocity_list.append(particle)

    return time_velocity_list


def parse_density():
    f = open('../density.json')

    measures = json.load(f)

    m = models.DensityMeasures()
    for measure in measures:
        p = measure['values'][0]
        m.vaavg.append(p['va']['avg'])
        m.vamin.append(p['va']['avg']-p['va']['min'])
        m.vamax.append(p['va']['max']-p['va']['avg'])
        m.std.append(p['va']['std']/2)
        m.density.append(p['density'])

    return m


def parse_noise():
    f = open('../noise.json')

    measures = json.load(f)

    noise_list = []
    for measure in measures:
        m = models.NoiseMeasures(measure['n'])
        for p in measure['values']:
            m.vaavg.append(p['va']['avg'])
            m.vamin.append(p['va']['avg']-p['va']['min'])
            m.vamax.append(p['va']['max']-p['va']['avg'])
            m.std.append(p['va']['std'])
            m.noise.append(p['noise'])
        noise_list.append(m)

    return noise_list
