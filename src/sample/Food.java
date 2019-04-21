package sample;

public class Food {
    public static final double DEAFULT_CALORIES = 1.0;
    private Point _position;
    private double _calories;

    public Food() {
        this(new Point(), DEAFULT_CALORIES);
    }

    public Food(Point position, double calories) {
        _position = position;
        _calories = calories;
    }
}
