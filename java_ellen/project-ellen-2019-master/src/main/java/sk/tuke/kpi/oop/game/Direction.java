package sk.tuke.kpi.oop.game;

public enum Direction {
    NORTH (0, 1),
    EAST (1, 0),
    SOUTH (0, -1),
    WEST (-1, 0),
    NORTHEAST(1, 1),
    SOUTHEAST(1, -1),
    SOUTHWEST(-1, -1),
    NORTHWEST(-1, 1),
    NONE(0, 0);


    private final int dx;
    private final int dy;
    private float uhol;

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public float getAngle() {
         return uhol;

       /* if(getDx() == 0 && getDy() == 1) {
            return 0;
        }
        if(getDx() == 1 && getDy() == 0){
            return 270;
        }
        if(getDx() == 0 && getDy() == -1){
            return 180;
        }
        if(getDx() == -1 && getDy() == 0){
            return 90;
        }

        if(getDx() == 1 && getDy() == 1) {
            return 315;
        }

        if(getDx() == 1 && getDy() == -1) {
            return 225;
        }

        if(getDx() == -1 && getDy() == -1) {
            return 135;
        }
        return 45;

        */
    }

    static {
        NORTH.uhol = 0;
        EAST.uhol = 270;
        SOUTH.uhol = 180;
        WEST.uhol = 90;
        NORTHEAST.uhol=315;
        SOUTHEAST.uhol=225;
        SOUTHWEST.uhol=135;
        NORTHWEST.uhol=45;
        //NONE.uhol = 0;
    }

    public static Direction fromAngle(float angle) {
        if (angle == 0) return NORTH;
        if (angle == 45) return NORTHWEST;
        if (angle == 180) return SOUTH;
        if (angle == 90) return WEST;
        if (angle == 135) return SOUTHWEST;
        if (angle == 225) return SOUTHEAST;
        if (angle == 270) return EAST;
        return NORTHEAST;

         /*Direction smer = null;

        for (Direction value : Direction.values()) {
            if (value.getAngle() == angle) {
                smer = value;
            }
        }
        return smer;

          */
    }

    public Direction combine(Direction other) {
        int newX;
        int newY;

        if (this==other)
            return this;

        if (getDx()==other.getDx())
            newX=getDx();
        else
            newX=getDx()+other.getDx();

        if (getDy()==other.getDy())
            newY=getDy();
        else
            newY=getDy()+other.getDy();
        Direction direction = NONE;



        for (Direction value : Direction.values()) {
            if (value.getDx() == newX && value.getDy() == newY)
            {
                direction=value;
            }
        }
        return direction;

    }
}
