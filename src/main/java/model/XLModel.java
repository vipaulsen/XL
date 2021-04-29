package model;

import expr.Environment;
import expr.ExprResult;
import util.XLBufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class XLModel implements Environment {
  public static final int COLUMNS = 10, ROWS = 10;
  private map<String, CellText> values = new HashMap<>();
  /**
   * Called when the code for a cell changes.
   *
   * @param address address of the cell that is being edited
   * @param text    the new code for the cell - can be raw text (starting with #) or an expression
   */
  public void update(CellAddress address, String text) {
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
  }

  public void saveFile(File file) {
  }

  @Override
  public ExprResult value(String name) {
    return null;
  }
}
