import json
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
