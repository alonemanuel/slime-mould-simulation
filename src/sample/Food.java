package sample;

public class Food {
    public static final double DEAFULT_CALORIES = 1.0;
    private Tile _position;
    private double _calories_left;

    public Food() {
        this(new Tile(), DEAFULT_CALORIES);
    }

    public Food(Tile position, double calories) {
        _position = position;
        _calories_left = calories;
    }


    public double get_calories_left() {
        return _calories_left;
    }

    public void set_calories_left(double _calories_left) {
        this._calories_left = _calories_left;
    }
}
