package Game;

import java.util.ArrayList;
import java.util.List;

public class Figure {
    private boolean isKing = false;
    public final Color side;

    public Pos getPos() {
        return pos;
    }

    private Pos pos;

    private Game game;

    public Figure(Color side, Pos pos, Game game) {
        this.side = side;
        this.pos = pos;
        this.game = game;
    }

    public String toString(){
        if(isKing) {
            return switch (side) {
                case WHITE -> "★";
                case BLACK -> "✪";
            };
        }
        else {
            return switch (side) {
                case WHITE -> "●";
                case BLACK -> "◯";
            };
        }
    }

    public int forwardMoveDir(){
        return switch (side) {
            case WHITE -> 1;
            case BLACK -> -1;
        };
    }

    public int kingY(){
        return switch (side) {
            case WHITE -> game.size - 1;
            case BLACK -> 0;
        };
    }

    List<Pos> getMoveDirections(){
        List<Pos> directions = new ArrayList<>();
        directions.add(new Pos(-1, forwardMoveDir()));
        directions.add(new Pos(+1, forwardMoveDir()));
        if(isKing) {
            directions.add(new Pos(-1, -forwardMoveDir()));
            directions.add(new Pos(+1, -forwardMoveDir()));
        }
        return directions;
    }



    List<Move> getAllMoves(){
        List<Move> moves = new ArrayList<>();

        for (Pos direction: getMoveDirections()) {
            Pos newPos = pos.add(direction);
            if (!game.ifPosIsEmpty(newPos)) continue;
            if(!isKing) {
                Move move = new Move(pos, newPos);
                moves.add(move);
            }
            else {
                List<Pos> poses = new ArrayList<>();
                poses.add(pos);
                poses.add(newPos);
                while (game.ifPosIsEmpty(newPos)) {
                    Move move = new Move(new ArrayList<>(poses));
                    moves.add(move);

                    newPos = newPos.add(direction);
                    poses.add(newPos);
                }
            }
        }

        List<Pos> moveBefore = new ArrayList<>();
        moveBefore.add(pos);
        moves.addAll(getAllKillMovesRecursive(pos, moveBefore));

        return moves;
    }

    private List<Move> getAllKillMovesRecursive(Pos pos, List<Pos> moveBefore){
        List<Move> moves = new ArrayList<>();

        for (Pos direction : getMoveDirections()) {
            List<Pos> poses = new ArrayList<>(moveBefore);

            Pos toKill = pos.add(direction);
            if(moveBefore.contains(toKill)) continue;
            poses.add(toKill);
            if(isKing) {
                while (game.ifPosIsEmpty(toKill)){
                    toKill = toKill.add(direction);
                    poses.add(toKill);
                }
            }

            if(game.getFigure(toKill) == null) continue;
            if(game.getFigure(toKill).side != game.getOppositeColor(side)) continue;


            Pos newPos = toKill.add(direction);
            poses.add(newPos);
            if(!game.ifPosIsEmpty(newPos) || moveBefore.contains(newPos)) continue;

            // TODO add if king possibility to stop after any num of empty cells

            Move move = new Move(poses);
            move.setKill();
            moves.add(move);

            if(isKing) {
                List<Pos> afterPoses = new ArrayList<>(poses);
                Pos newPos1 = newPos.add(direction);
                afterPoses.add(newPos1);
                while (game.ifPosIsEmpty(newPos1)){
                    Move move1 = new Move(new ArrayList<>(afterPoses));
                    move1.setKill();
                    moves.add(move1);

                    newPos1 = newPos1.add(direction);
                    afterPoses.add(newPos1);
                }
            }

            moves.addAll(getAllKillMovesRecursive(newPos, poses));
        }
        return moves;
    }

    public Move getMove(Pos endPos){
        List<Move> moves = getAllMoves();
        Move move = null;
        for (Move testMove : moves) {
            if(testMove.lastPos().equals(endPos)) {
                move = testMove;
                if(move.isKill) break;
            }
        }

        return move;
    }

    public boolean move(Move move){
        for (Move testMove : getAllMoves()) {
            if(testMove.equals(move)) {
                move = testMove;
                break;
            }
        }
        if(move == null) return false;

        pos = move.lastPos();

        if(pos.y == kingY()) isKing = true;

        return true;
    }

}
