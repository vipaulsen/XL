package model;

import expr.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class XLModel implements Environment {
  public static final int COLUMNS = 10, ROWS = 10;
  private Map<String, Cell> values = new HashMap<>();
  private Map<String, Cell> temp = new HashMap<>();
  private ArrayList<XLModelObserver> observerList;
  private ExprParser parser;

  public XLModel() {
    setup(false);
    observerList = new ArrayList<>();
    parser = new ExprParser();
  }

  public void setup(Boolean clear) {
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLUMNS; c++) {
        EmptyCell empt = new EmptyCell();
        CellAddress addr = new CellAddress(r, c);
        values.put(addr.toString(), empt);
        if (clear)
          notifyObservers(addr.toString(), empt.toString());
      }
    }
  }

  /**
   * Called when the code for a cell changes.
   *
   * @param address address of the cell that is being edited
   * @param text    the new code for the cell - can be raw text (starting with #) or an expression
   */
  public void update(CellAddress address, String text) {

    if (text.equals(values.get(address.toString()).toRawString())) {
      return;
    }

    if (checkReferenceSelf(address.toString(), text)) {
      Cell currentCell = new CellFactory().makeCell(text);
      values.put(address.toString(), currentCell);
      updateAll();
    }
  }

  private boolean checkReferenceSelf(String address, String text) {

    if (text.toUpperCase(Locale.ROOT).contains(address.toString())) {
      Cell circularCell = new CircularCell(text);
      values.put(address.toString(), circularCell);
      notifyObservers(address.toString(), circularCell.value(this).toString());
      return false;
    }

    return true;
  }

  private boolean checkCircularity(String address, String text) {
    Cell odlCell = values.get(address);
    CircularCell bomb = new CircularCell(text);
    values.put(address, bomb);

      if (odlCell.value(this) instanceof ErrorResult){
        return false;
      }
      else{
        values.put(address, odlCell);
        return true;
      }
  }

  private void updateAll() {
    values.forEach((currentAddress, value) -> {

      Cell cell = values.get(currentAddress);

      if (cell instanceof CellComment || cell instanceof EmptyCell) {
        notifyObservers(currentAddress, cell.toString());
      }

      if (cell instanceof CellExpr) {

        if (checkCircularity(currentAddress, value.toRawString())) {

            notifyObservers(currentAddress, "" + cell.value(this).value());
        }
        else{
          //System.out.println("cell is error");
          notifyObservers(currentAddress, cell.value(this).toString());
        }
      }
    });
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
    setup(true);
    try {
      reader.load(values);
      updateAll();
    } catch (IOException e) {
      e.getMessage();
    }
  }

  public void saveFile(File file) {
    try {
      XLPrintStream reader = new XLPrintStream(file.getName());
      reader.save(values.entrySet());
    } catch (IOException e) {
      e.getMessage();
    }
  }

  @Override
  public ExprResult value(String address) {
    return values.get(address).value(this);
  }

  public String getRawString(String address) {
    return values.get(address).toRawString();
  }

  public void addObserver(XLModelObserver observer) {
    observerList.add(observer);
  }

  /*public void removeObserver(XLModelObserver observer) {
    observerList.remove(observer);
  }*/

  private void notifyObservers(String address, String text) {
    for (XLModelObserver o : observerList) {
      o.notifyChange(address, text);
    }
  }
}