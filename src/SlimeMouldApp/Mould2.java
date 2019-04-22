package SlimeMouldApp;

public class Mould2 {

    public static final int DEFAULT_ENERGY = 50;

    private double _energy;
    private Tile _head_pos;

    public Mould2() {
        this(DEFAULT_ENERGY, new Tile());
    }

    public Mould2(double energy, Tile head_pos) {
        _energy = energy;
        _head_pos = head_pos;
    }

    public void eat_some(Food2 food_to_eat, double ratio_to_eat) {
        ratio_to_eat = (ratio_to_eat < 0) ? 0 : ratio_to_eat;
        ratio_to_eat = (ratio_to_eat > 1) ? 1 : ratio_to_eat;

        double calories_left = food_to_eat.get_calories_left() * (1-ratio_to_eat);
        food_to_eat.set_calories_left(calories_left);


    }




}
