package sk.tuke.kpi.oop.game.iterator;

public class TestIterator {

    public static void main(String[] args) {
        ZoznamPredmetov zoznamPredmetov = new ZoznamPredmetov();

            for (MojIterator mojIterator = zoznamPredmetov.getIterator(); mojIterator.hasNext(); ) {
                String predmet = (String)mojIterator.next();
                System.out.println("Predmet: " + predmet);
            }
        }
}

