package model;

import util.XLException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class XLBufferedReader extends BufferedReader {
  public XLBufferedReader(File file) throws FileNotFoundException {
    super(new FileReader(file));
  }

  // TODO Change Object to something appropriate DONE 2021-05-06
  public void load(Map<String, Cell> map) throws IOException {
    try {
      while (ready()) {
        String string = readLine();
        int i = string.indexOf('=');
        map.put(string.substring(0,i),new CellFactory().makeCell(string.substring(i+1)));
      }
    } catch (Exception e) {
      throw new XLException(e.getMessage());
    }
  }
}
