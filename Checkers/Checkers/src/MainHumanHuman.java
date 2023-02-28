import Game.*;

import java.util.Objects;
import java.util.Scanner;

public class MainHumanHuman {
    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println(game.toAsciiString());
            if(game.colorNowWinning() != null) {
                System.out.println("congratulations to " + game.colorNowWinning());
                break;
            }
            if(game.getChoosePos() == null) {
                System.out.println("choose figure for side " + game.getSideThatMoveNow());
                Pos pos = inputPos(scanner.nextLine());
                if(pos == null) System.out.println("illegal pos input");
                else game.chooseFigure(pos);
            }
            else {
                System.out.println("choose pos for move chosen figure or input or press R to reset choose pos");
                String str = scanner.nextLine();
                if (Objects.equals(str, "R")) game.resetChoosePos();
                else {
                    Pos pos = inputPos(str);
                    if(pos == null) System.out.println("illegal pos input");
                    else game.move(pos);
                }
            }

        }
    }

    static Pos inputPos(String input){
        try {
            String[] inputArr = input.split(" ");
            return new Pos(Integer.parseInt(inputArr[0]), Integer.parseInt(inputArr[1]));
        } catch (Exception e) {
            return null;
        }
    }
}