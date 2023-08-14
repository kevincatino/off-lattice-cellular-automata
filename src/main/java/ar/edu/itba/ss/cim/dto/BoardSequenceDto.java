package ar.edu.itba.ss.cim.dto;

import ar.edu.itba.ss.cim.models.Board;
import ar.edu.itba.ss.cim.models.BoardSequence;

import java.util.ArrayList;
import java.util.List;

public class BoardSequenceDto {
    private final double L;
    private final int M;
    private final double rc;

    private final List<BoardStateDto> data;

    private BoardSequenceDto(double L, int M, double rc, List<BoardStateDto> data) {
        this.L = L;
        this.M = M;
        this.rc = rc;
        this.data = data;
    }

    public static BoardSequenceDto from(BoardSequence bs) {
        List<BoardStateDto> data = new ArrayList<>();

        for (Board b : bs) {

            data.add(BoardStateDto.from(b));
        }

        return new BoardSequenceDto(bs.getBoardLength(), bs.getM(), bs.getInteractionRadius(), data);

    }

    public double getL() {
        return L;
    }



    public int getM() {
        return M;
    }



    public double getRc() {
        return rc;
    }


    public List<BoardStateDto> getData() {
        return data;
    }

}
