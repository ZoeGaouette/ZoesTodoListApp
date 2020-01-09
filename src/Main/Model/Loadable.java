package Model;

import Model.Exceptions.InvalidInputException;

import java.io.IOException;
import java.io.File;

public interface Loadable {
    public void load(File file) throws IOException, InvalidInputException;
}
