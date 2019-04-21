package sample;

public class Mould {

    public static final int DEFAULT_ENERGY = 50;

    private double _energy;
    private Point _head_pos;

    public Mould() {
        this(DEFAULT_ENERGY, new Point());
    }

    public Mould(double energy, Point head_pos) {
        _energy = energy;
        _head_pos = head_pos;
    }

}
