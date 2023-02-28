import Game.Game;
import Game.Pos;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class MainBotBot {
    public static void main(String[] args) {
        Game game = new Game();
        while (true){
            System.out.println(game.toAsciiString());
            if(game.colorNowWinning() != null) {
                System.out.println("congratulations to " + game.colorNowWinning());
                break;
            }
            System.out.println("choose figure for side " + game.getSideThatMoveNow());
            List<Pos> posesFigure = game.getAllFiguresThatCanMovePoses(game.getSideThatMoveNow());
            if(posesFigure.size() == 0)  throw new IllegalStateException("can't choose");
            game.chooseFigure(posesFigure.get(new Random().nextInt(posesFigure.size())));

            if(game.getChoosePos() == null) throw new IllegalStateException("can't choose");
            System.out.println("chosen " + game.getChoosePos());
            System.out.println(game.toAsciiString());

            boolean res = false;
            for (int y = 0; y < game.size; y++) {
                for (int x = 0; x < game.size; x++) {
                    Pos pos = new Pos(x, y);
                    res = game.move(pos);
                    if(res) break;
                }
                if(res) break;
            }

            if(!res) throw new IllegalStateException("can't move");
            System.out.println("moved!");

            System.out.println(game.toAsciiString());

        }
    }
}