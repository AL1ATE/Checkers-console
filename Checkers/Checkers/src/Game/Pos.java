package Game;

public class Pos {
    public final int x;
    public final int y;
    public Pos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Pos add(Pos pos){
        return new Pos(x + pos.x, y + pos.y);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Pos pos){
            return (x == pos.x) && (y == pos.y);
        }
        return false;
    }

    @Override
    public String toString(){
        return "(" + x  + ", " + y + ")";
    }
}
