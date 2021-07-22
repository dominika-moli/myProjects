package sk.tuke.kpi.oop.game.iterator;

public class ZoznamPredmetov implements MojContainer{

    private String[] predmety = {"Energy", "Ammo", "Hammer", "Hamburger", "Barrel", "LargeBox", "AccessCard"};

    @Override
    public MojIterator getIterator() {
        return new PredmetyIterator();
    }

    public class PredmetyIterator implements MojIterator {
        int index;

        @Override
        public boolean hasNext() {
            return index < predmety.length;
        }

        @Override
        public Object next() {
            if (this.hasNext()) {
                return predmety[index++];
            }
            return null;
        }
    }
}
