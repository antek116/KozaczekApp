package example.kozaczekapp;

import java.io.InputStream;
import java.util.ArrayList;

public interface IParser<E> {

    ArrayList<E> parse(String url);
}
