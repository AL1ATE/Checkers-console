package Game;

import java.util.ArrayList;
import java.util.List;

public class Move {
    boolean isKill = false;

    List<Pos> poses = new ArrayList<>();

    public Move(Pos startPos, Pos endPos){
        poses.add(startPos);
        poses.add(endPos);
    }

    public Move(List<Pos> poses) {
        if(poses.size() < 2) throw new IllegalArgumentException();
        this.poses = poses;
    }

    public Move() {}

    void addPos(Pos pos){
        poses.add(pos);
    }

    void addAll(List<Pos> poses){
        this.poses.addAll(poses);
    }

    Pos lastPos(){
        return poses.get(poses.size()-1);
    }

    void setKill(){
        isKill = true;
    }
}
