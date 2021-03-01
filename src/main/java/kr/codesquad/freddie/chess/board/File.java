package kr.codesquad.freddie.chess.board;


import kr.codesquad.freddie.chess.piece.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class File {
    public static final int SIZE = 8;
    private List<Piece> pieces = new ArrayList<>();

    public File add(Piece piece) {
        if (!isAddable()) {
            throw new IllegalArgumentException("파일 당 " + SIZE + "개만 추가 가능합니다.");
        }

        pieces.add(piece);

        return this;
    }

    public Piece get(int index) {
        return pieces.get(index);
    }

    public Piece set(int index, Piece piece) {
        return pieces.set(index, piece);
    }

    public int size() {
        return pieces.size();
    }

    public int getNumberOf(Color color, Kind kind) {
        long numberOfPieces = pieces.stream()
                .filter(piece -> piece.getColor() == color && piece.getKind() == kind)
                .count();
        return Long.valueOf(numberOfPieces).intValue();
    }

    public boolean isAddable() {
        return size() < SIZE;
    }

    public void fillWithPawn(Color color) {
        fillWith(new Piece(color, Kind.PAWN));
    }

    public void fillWithBlank() {
        fillWith(PieceFactory.createBlank());
    }

    private void fillWith(Piece piece) {
        while (isAddable()) {
            add(piece);
        }
    }

    public void fillWithRoyal(Color color) {
        PieceFactory pieceFactory = new PieceFactory(color);

        this.add(pieceFactory.createRook())
                .add(pieceFactory.createKnight())
                .add(pieceFactory.createBishop())
                .add(pieceFactory.createQueen())
                .add(pieceFactory.createKing())
                .add(pieceFactory.createBishop())
                .add(pieceFactory.createKnight())
                .add(pieceFactory.createRook());

    }

    public List<Piece> getPieces() {
        return Collections.unmodifiableList(pieces);
    }

    public List<CalculablePiece> getCalculablePieces() {
        return IntStream.range(0, SIZE)
                .mapToObj(i -> CalculablePiece.create(this.pieces.get(i), (char) (i + 'a')))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "File{" +
                "pieces=" + pieces +
                '}';
    }
}
