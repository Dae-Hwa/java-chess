package kr.codesquad.freddie.chess.board;

import kr.codesquad.freddie.chess.piece.Color;
import kr.codesquad.freddie.chess.piece.Kind;
import kr.codesquad.freddie.chess.piece.Piece;
import kr.codesquad.freddie.chess.piece.PieceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private static final int MAX_SIZE = Board.RANK_SIZE * File.SIZE;

    @BeforeEach
    void setBoard() {
        board = new Board();
    }

    @Test
    @DisplayName("1개 부터 64개까지 넣으면서 사이즈 일치하는지 확인")
    void add() {
        for (int i = 0; i <= MAX_SIZE; i++) {
            Board board = new Board();

            for (int j = 0; j < i; j++) {
                board.add(new Piece(Color.WHITE, Kind.PAWN));
            }
            assertThat(board.pieceCount())
                    .isEqualTo(i);

            System.out.println(board);
        }
    }

    @Test
    @DisplayName("8개가 넘어가면 다음 줄로 넘어가기 때문에 에러가 발생하면 안된다.")
    void add_fillRank() {
        int size = File.SIZE + 1;
        for (int i = 1; i <= size; i++) {
            board.add(new Piece(Color.WHITE, Kind.PAWN));
            assertThat(board.pieceCount())
                    .isEqualTo(i);
        }
    }

    @Test
    void add_outOfRange() {
        for (int i = 0; i < MAX_SIZE; i++) {
            board.add(new Piece(Color.WHITE, Kind.PAWN));
        }

        assertThatThrownBy(() -> board.add(new Piece(Color.WHITE, Kind.PAWN)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("더 이상 추가할 수 없습니다. 현재 크기 : 64");
    }

    @Test
    @DisplayName("하나만 추가하여 탐색")
    void findPiece_one() {
        Piece white = new Piece(Color.WHITE, Kind.PAWN);
        board.add(white);
        assertAll(
                () -> assertThat(board.pieceCount()).isEqualTo(1),
                () -> assertThat(board.findPiece('a', 8)).isEqualTo(white)
        );
    }

    @Test
    @DisplayName("하나 이상 추가하여 탐색")
    void findPiece_moreThanOne() {
        Piece white = new Piece(Color.WHITE, Kind.PAWN);
        board.add(white);
        Piece black = new Piece(Color.BLACK, Kind.PAWN);
        board.add(black);

        assertAll(
                () -> assertThat(board.pieceCount()).isEqualTo(2),
                () -> assertThat(board.findPiece('a', 8)).isEqualTo(white),
                () -> assertThat(board.findPiece('b', 8)).isEqualTo(black)
        );
    }

    @Test
    void initialize() {
        board.initialize();
        checkInitializeRoyal(Color.BLACK);
        checkInitializeRoyal(Color.WHITE);
        checkInitializePawn(Color.BLACK);
        checkInitializePawn(Color.WHITE);
    }

    private void checkInitializeRoyal(Color color) {
        int rank = color == Color.BLACK ? 8 : 1;
        PieceFactory pieceFactory = new PieceFactory(color);

        assertThat(board.findPiece('a', rank)).isEqualTo(pieceFactory.createRook());
        assertThat(board.findPiece('b', rank)).isEqualTo(pieceFactory.createKnight());
        assertThat(board.findPiece('c', rank)).isEqualTo(pieceFactory.createBishop());
        assertThat(board.findPiece('d', rank)).isEqualTo(pieceFactory.createQueen());
        assertThat(board.findPiece('e', rank)).isEqualTo(pieceFactory.createKing());
        assertThat(board.findPiece('f', rank)).isEqualTo(pieceFactory.createBishop());
        assertThat(board.findPiece('g', rank)).isEqualTo(pieceFactory.createKnight());
        assertThat(board.findPiece('h', rank)).isEqualTo(pieceFactory.createRook());
    }

    private void checkInitializePawn(Color color) {
        int rank = color == Color.BLACK ? 7 : 2;
        Piece pawn = new Piece(color, Kind.PAWN);

        assertThat(board.findPiece('a', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('b', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('c', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('d', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('e', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('f', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('g', rank)).isEqualTo(pawn);
        assertThat(board.findPiece('h', rank)).isEqualTo(pawn);
    }

    @Test
    void getRepresentation_init_board() {
        String representation = board.initialize().getRepresentation();

        String expected = new StringBuilder()
                .append("RNBQKBNR").append(System.lineSeparator())
                .append("PPPPPPPP").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("pppppppp").append(System.lineSeparator())
                .append("rnbqkbnr")
                .toString();
        assertThat(representation)
                .isEqualTo(expected);
    }

    @Test
    void getRepresentation_not_init_board() {
        String representation = board.getRepresentation();

        String expected = new StringBuilder()
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append("........")
                .toString();
        assertThat(representation)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("set으로 넣은 뒤 getRepresentation과 getNumberOf로 확인")
    void verify_set_with_getRepresentation_and_getNumberOf() {
        for (int i = 0; i < MAX_SIZE; i++) {
            board.add(Piece.createBlank());
        }

        PieceFactory blackPieceFactory = new PieceFactory(Color.BLACK);
        PieceFactory whitePieceFactory = new PieceFactory(Color.WHITE);

        board.set('b', 8, blackPieceFactory.createKing());
        board.set('c', 8, blackPieceFactory.createRook());
        board.set('a', 7, blackPieceFactory.createPawn());
        board.set('c', 7, blackPieceFactory.createPawn());
        board.set('d', 7, blackPieceFactory.createBishop());
        board.set('b', 6, blackPieceFactory.createPawn());
        board.set('e', 6, blackPieceFactory.createQueen());
        board.set('f', 4, whitePieceFactory.createKnight());
        board.set('g', 4, whitePieceFactory.createQueen());
        board.set('f', 3, whitePieceFactory.createPawn());
        board.set('g', 2, whitePieceFactory.createPawn());
        board.set('e', 1, whitePieceFactory.createRook());
        board.set('f', 1, whitePieceFactory.createKing());

        String expected = new StringBuilder()
                .append(".KR.....").append(System.lineSeparator())
                .append("P.PB....").append(System.lineSeparator())
                .append(".P..Q...").append(System.lineSeparator())
                .append("........").append(System.lineSeparator())
                .append(".....nq.").append(System.lineSeparator())
                .append(".....p..").append(System.lineSeparator())
                .append("......p.").append(System.lineSeparator())
                .append("....rk..")
                .toString();
        assertAll(
                () -> assertThat(board.getRepresentation()).isEqualTo(expected),
                () -> assertThat(board.getNumberOf(Color.BLACK, Kind.KING)).isEqualTo(1),
                () -> assertThat(board.getNumberOf(Color.BLACK, Kind.PAWN)).isEqualTo(3),
                () -> assertThat(board.getNumberOf(Color.BLACK, Kind.ROOK)).isEqualTo(1),
                () -> assertThat(board.getNumberOf(Color.BLACK, Kind.QUEEN)).isEqualTo(1),
                () -> assertThat(board.getNumberOf(Color.WHITE, Kind.KNIGHT)).isEqualTo(1),
                () -> assertThat(board.getNumberOf(Color.WHITE, Kind.QUEEN)).isEqualTo(1),
                () -> assertThat(board.getNumberOf(Color.WHITE, Kind.PAWN)).isEqualTo(2),
                () -> assertThat(board.getNumberOf(Color.WHITE, Kind.ROOK)).isEqualTo(1),
                () -> assertThat(board.getNumberOf(Color.WHITE, Kind.KING)).isEqualTo(1)
        );
    }


    @Test
    void getScoreOf() {
        for (int i = 0; i < MAX_SIZE; i++) {
            board.add(new Piece(Color.WHITE, Kind.PAWN));
        }

        PieceFactory blackPieceFactory = new PieceFactory(Color.BLACK);

        board.set('a', 1, blackPieceFactory.createPawn());
        board.set('a', 2, blackPieceFactory.createPawn());
        board.set('a', 3, blackPieceFactory.createPawn());

        double result = board.getScoreOf(Color.BLACK);

        assertThat(result).isEqualTo(1.5);
    }
}
