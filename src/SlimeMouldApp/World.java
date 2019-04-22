package SlimeMouldApp;

import java.util.HashSet;
import java.util.Set;

public class World {

    private Mould2 _mould;
    private Set<Food2> _food;

    public World(){
        _food = new HashSet<Food2>();
    }
}
