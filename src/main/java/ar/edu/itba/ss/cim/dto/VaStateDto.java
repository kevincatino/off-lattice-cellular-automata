package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Board;

import java.util.Collection;
import java.util.stream.Collectors;

public class VaStateDto {
    private final int time;

    public double getVa() {
        return va;
    }

    private final double va;

    private VaStateDto(int time, double va) {
        this.time = time;
        this.va = va;
    }

    public static VaStateDto from(Board board) {
        return new VaStateDto(board.getTime(),  board.getCurrentVa());
    }

    public int getTime() {
        return time;
    }

}
