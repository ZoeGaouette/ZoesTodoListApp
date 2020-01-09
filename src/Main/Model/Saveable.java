package Model;

import java.io.IOException;
import java.io.File;

public interface Saveable {
    public void save(File file) throws IOException;
}
