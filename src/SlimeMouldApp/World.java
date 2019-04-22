package SlimeMouldApp;

import java.util.HashSet;
import java.util.Set;

public class World {

    private Mould _mould;
    private Set<Food> _food;

    public World(){
        _food = new HashSet<Food>();
    }
}
