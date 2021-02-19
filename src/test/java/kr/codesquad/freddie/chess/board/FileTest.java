package kr.codesquad.freddie.chess.board;

import kr.codesquad.freddie.chess.piece.Color;
import kr.codesquad.freddie.chess.piece.Kind;
import kr.codesquad.freddie.chess.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class FileTest {
    private File file;

    @BeforeEach
    void setFile() {
        file = new File();
    }

    @Test
    void add() {
        for (int i = 0; i < File.SIZE; i++) {
            File file = new File();

            for (int j = 0; j < i; j++) {
                file.add(new Piece(Color.WHITE, Kind.PAWN));
            }
            assertThat(file.size())
                    .isEqualTo(i);
        }
    }

    @Test
    @DisplayName("8개 초과하여 추가하는 경우")
    void add_moreThan_8() {
        for (int i = 0; i < File.SIZE; i++) {
            file.add(new Piece(Color.WHITE, Kind.PAWN));
        }

        assertThatThrownBy(() -> file.add(new Piece(Color.WHITE, Kind.PAWN)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("파일 당 8개만 추가 가능합니다.");
    }

    @Test
    void get() {
        Piece pieceA = new Piece(Color.WHITE, Kind.PAWN);
        Piece pieceB = new Piece(Color.BLACK, Kind.PAWN);
        file.add(pieceA);
        file.add(pieceB);

        assertAll(
                () -> assertThat(file.get('a')).isEqualTo(pieceA).isEqualTo(file.get(0)),
                () -> assertThat(file.get('b')).isEqualTo(pieceB).isEqualTo(file.get(1))
        );
    }

    @Test
    @DisplayName("a~h를 벗어나도록 get을 하는 경우")
    void get_outOfRange() {
        for (int i = 0; i < File.SIZE; i++) {
            file.add(new Piece(Color.WHITE, Kind.PAWN));
        }

        assertAll(
                () -> assertThatThrownBy(() -> file.get('i'))
                        .isInstanceOf(IndexOutOfBoundsException.class),
                () -> assertThatThrownBy(() -> file.get((char) 96))
                        .isInstanceOf(ArrayIndexOutOfBoundsException.class),
                () -> assertThatThrownBy(() -> file.get('0'))
                        .isInstanceOf(ArrayIndexOutOfBoundsException.class),
                () -> assertThatThrownBy(() -> file.get((char) 0))
                        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        );
    }

    @Test
    void isAddable() {
        for (int i = 0; i < File.SIZE - 1; i++) {
            file.add(new Piece(Color.WHITE, Kind.PAWN));
            assertThat(file.isAddable()).isTrue();
        }

        file.add(new Piece(Color.WHITE, Kind.PAWN));
        assertThat(file.isAddable()).isFalse();
    }

    @Test
    void fillWith_black() {
        file.fillWithPawn(Color.BLACK);
        checkFillWith(Color.BLACK);
    }

    @Test
    void fillWith_white() {
        file.fillWithPawn(Color.WHITE);
        checkFillWith(Color.WHITE);
    }

    private void checkFillWith(Color expected) {
        for (int i = 0; i < File.SIZE; i++) {
            assertThat(file.get(i).getColor()).isEqualTo(expected);
        }
    }

    @Test
    void getRepresentation_fillWithBlackPawn() {
        file.fillWithPawn(Color.BLACK);
        assertThat(file.getRepresentation()).isEqualTo("PPPPPPPP");
    }

    @Test
    void getRepresentation_fillWithWhitePawn() {
        file.fillWithPawn(Color.WHITE);
        assertThat(file.getRepresentation()).isEqualTo("pppppppp");
    }

    @Test
    void getRepresentation_fillWithBlackRoyal() {
        file.fillWithRoyal(Color.BLACK);
        assertThat(file.getRepresentation()).isEqualTo("RNBQKBNR");
    }

    @Test
    void getRepresentation_fillWithWhiteRoyal() {
        file.fillWithRoyal(Color.WHITE);
        assertThat(file.getRepresentation()).isEqualTo("rnbqkbnr");
    }
}
