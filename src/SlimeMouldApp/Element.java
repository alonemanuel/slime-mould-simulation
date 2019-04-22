package SlimeMouldApp;

import javafx.scene.text.Text;

 abstract class Element {
    public Text _text;
    protected int _xPos;
    protected int _yPos;

    public Element(int xPos, int yPos){
        _xPos=xPos;
        _yPos=yPos;
    }

     public int get_xPos() {
         return _xPos;
     }

     public int get_yPos() {
         return _yPos;
     }

     public void set_xPos(int _xPos) {
         this._xPos = _xPos;
     }

     public void set_yPos(int _yPos) {
         this._yPos = _yPos;
     }
 }
