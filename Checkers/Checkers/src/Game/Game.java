package Game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public final int size = 8;

    private Pos choosePos = null;

    private Color sideThatMoveNow = Color.WHITE;

    private final Figure[][] board = new Figure[size][size];

    public Game(){
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < size; x++) {
                if(getCellColor(new Pos(x, y)) == Color.BLACK) {
                    Pos pos = new Pos(x, y);
                    setFigure(pos, new Figure(Color.WHITE, pos, this));
                }
            }
        }


        for (int y = 5; y < 8; y++) {
            for (int x = 0; x < size; x++) {
                if(getCellColor(new Pos(x, y)) == Color.BLACK) {
                    Pos pos = new Pos(x, y);
                    setFigure(pos, new Figure(Color.BLACK, pos, this));
                }
            }
        }

        //Pos pos = new Pos(3, 3);
       // setFigure(pos, new Figure(Color.BLACK, pos, this));
    }

    public Color getCellColor(Pos pos){
        if((pos.x + pos.y) % 2 == 0) return Color.BLACK;
        return Color.WHITE;
    }



    public String toAsciiString(){
        String redAscii = "\u001b[31m";
        String greenAscii = "\u001b[32m";
        String resetAscii = "\u001b[0m";

        List<Pos> chooseFigureMoveEnds = null;
        if(choosePos != null && getFigure(choosePos) != null){
            chooseFigureMoveEnds = getFigure(choosePos).getAllMoves().stream().
                    map(move -> move.lastPos()).toList();
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" ");
        for (int x = 0; x < size; x++) {
            stringBuilder.append(x);
        }
        stringBuilder.append("\n");


        for (int y = size - 1; y >= 0; y--) {
            stringBuilder.append(y);
            for (int x = 0; x < size; x++) {
                Pos pos = new Pos(x, y);

                if(chooseFigureMoveEnds != null && chooseFigureMoveEnds.contains(pos)) stringBuilder.append(greenAscii);
                if(choosePos != null && choosePos.equals(pos)) stringBuilder.append(redAscii);

                if(getFigure(pos) != null) {
                    stringBuilder.append(getFigure(pos));
                }
                else {
                    stringBuilder.append("⬚");
                }

                stringBuilder.append(resetAscii);
            }
            stringBuilder.append(y);
            stringBuilder.append("\n");
        }

        stringBuilder.append(" ");
        for (int x = 0; x < size; x++) {
            stringBuilder.append(x);
        }


        return stringBuilder.toString();
    }

    public boolean isPosOnBoard(Pos pos){
        return pos.x >= 0 && pos.x < size && pos.y >= 0 && pos.y < size;
    }

    Figure getFigure(Pos pos){
        if(!isPosOnBoard(pos)) return null;
        return board[pos.y][pos.x];
    }

    boolean setFigure(Pos pos, Figure figure){
        if(!isPosOnBoard(pos)) return false;
        board[pos.y][pos.x] = figure;
        return true;
    }

    public Color getOppositeColor(Color color){
        return switch (color){
            case WHITE -> Color.BLACK;
            case BLACK -> Color.WHITE;
        };
    }

    public boolean ifPosIsEmpty(Pos pos){
        return isPosOnBoard(pos) && getFigure(pos) == null;
    }

    public boolean chooseFigure(Pos pos){
        if(getFigure(pos) == null || getFigure(pos).side != sideThatMoveNow) return false;
        choosePos = pos;
        return true;
    }

    public boolean move(Pos endPos){
        if(colorNowWinning() != null) return false;
        if(choosePos == null) return false;
        Figure figure = getFigure(choosePos);
        if(figure == null || figure.side != sideThatMoveNow) return false;
        Move move = figure.getMove(endPos);

        if(move == null) return false;
        if(!move.isKill && countKillMoves(sideThatMoveNow) != 0) return false;


        if(!figure.move(move)) throw new IllegalStateException();
        for (Pos poses : move.poses) {
            setFigure(poses, null);
        }

        setFigure(move.lastPos(), figure);

        sideThatMoveNow = getOppositeColor(sideThatMoveNow);
        choosePos = null;
        return true;
    }

    private List<Figure> getAllFigures(Color color){
        List<Figure> figures = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Pos pos = new Pos(x, y);
                Figure figure = getFigure(pos);
                if(figure != null && figure.side == color) figures.add(figure);
            }
        }
        return figures;
    }

    private List<Figure> getAllFiguresThatCanMove(Color color){
        boolean onlyKill = countKillMoves(color) > 0;
        List<Figure> figures = new ArrayList<>();
        for (Figure figure : getAllFigures(color)) {
            List<Move> moves = figure.getAllMoves();
            if(moves.size() == 0) continue;
            if(!onlyKill) figures.add(figure);
            else {
                for (Move move : moves) {
                    if(move.isKill) {
                        figures.add(figure);
                        break;
                    }
                }
            }
        }
        return figures;
    }

    public List<Pos> getAllFiguresThatCanMovePoses(Color color){
        return getAllFiguresThatCanMove(color).stream().map(figure -> figure.getPos()).toList();
    }

    private int countKillMoves(Color color){
        List<Figure> figures = getAllFigures(color);
        int counter = 0;
        for (Figure figure: figures){
            List<Move> moves = figure.getAllMoves();
            for (Move move : moves) {
                if(move.isKill) counter++;
            }
        }
        return counter;
    }

    private int countMoves(Color color){
        List<Figure> figures = getAllFigures(color);
        int counter = 0;
        for (Figure figure: figures){
            List<Move> moves = figure.getAllMoves();
            counter += moves.size();
        }
        return counter;
    }

    public Color colorNowWinning(){
        int movesBlack = countMoves(Color.BLACK);
        int movesWhite = countMoves(Color.WHITE);
        if(movesBlack == 0 && movesWhite == 0) throw new IllegalStateException("that can't be");
        if(movesBlack == 0) return Color.WHITE;
        if(movesWhite == 0) return Color.BLACK;
        return null;
    }

    public Color getSideThatMoveNow(){
        return sideThatMoveNow;
    }

    public Pos getChoosePos(){
        return choosePos;
    }

    public void resetChoosePos() {
        this.choosePos = null;
    }


}
