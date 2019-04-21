package sample;

public class Point {


    private int _x;
    private int _y;

    public Point() {
        _x = 0;
        _y = 0;
    }

    public Point(int x_pos, int y_pos) {
        _x = x_pos;
        _y = y_pos;
    }

    public int get_x() {
        return _x;
    }

    public int get_y() {
        return _y;
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    public void set_y(int _y) {
        this._y = _y;
    }

}
