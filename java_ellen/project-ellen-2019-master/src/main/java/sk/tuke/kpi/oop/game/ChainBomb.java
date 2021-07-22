package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class ChainBomb extends TimeBomb {


    public ChainBomb(float time) {
        super(time);
    }

    @Override
    public void explode() {
        super.explode();


        Ellipse2D.Float Ellipse = new Ellipse2D.Float(this.getPosX()-50, this.getPosY()-50, 102, 102);
        System.out.println(Ellipse.getCenterX());
        System.out.println(Ellipse.getCenterY());
        System.out.println(Ellipse.getMinX());
        System.out.println(Ellipse.getMinY());
        System.out.println(Ellipse.getMaxX());
        System.out.println(Ellipse.getMaxY());
        List<Actor> actorslist = getScene().getActors();
        for (Actor actor: actorslist) {
            if (actor instanceof ChainBomb && !((ChainBomb) actor).isActivated()) {
                Rectangle2D.Float nextChainBomb = new Rectangle2D.Float(actor.getPosX()-actor.getWidth()/2, actor.getPosY()-actor.getHeight()/2, actor.getWidth(),actor.getHeight() );
                System.out.println(nextChainBomb.getCenterX());
                System.out.println(nextChainBomb.getCenterY());
                System.out.println(nextChainBomb.getMinX());
                System.out.println(nextChainBomb.getMinY());
                System.out.println(nextChainBomb.getMaxX());
                System.out.println(nextChainBomb.getMaxY());


                if (Ellipse.intersects(nextChainBomb)) {
                    ((ChainBomb) actor).activate();}

            }
        }

    }

}


