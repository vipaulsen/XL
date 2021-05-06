package model;

import expr.*;
import gui.CellSelectionObserver;
import gui.GridCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class XLModel implements Environment  {
  public static final int COLUMNS = 10, ROWS = 10;
  private Map<String, Cell> values = new HashMap<>();
  private ArrayList<XLModelObserver> observerList;
  private ExprParser parser;

  public XLModel() {
    setup(false);
    observerList = new ArrayList<>();
    parser = new ExprParser();
  }

  public void setup(Boolean clear){
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLUMNS; c++) {
        EmptyCell empt = new EmptyCell();
        CellAddress addr = new CellAddress(r,c);
        values.put(addr.toString(), empt);
        if(clear)
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

  public void update(CellAddress address, String text){

    if(text.equals(values.get(address.toString()).toRawString())) {
      return;
    }

    if (text.toUpperCase(Locale.ROOT).contains(address.toString())){
      Cell circularCell = new CircularCell(text);
      values.put(address.toString(), circularCell);
      notifyObservers(address.toString(), circularCell.value(this).toString());
      return;
    }

    System.out.println("NOTIFYING ALL OBSERVERS");
    if(text.isEmpty()){
      EmptyCell emptyCell = new EmptyCell();
      values.put(address.toString(),emptyCell);
      notifyObservers(address.toString(), emptyCell.toString());
    }
    else if (text.charAt(0) == '#') {
      CellComment comment = new CellComment(text);
      values.put(address.toString(), comment);
      notifyObservers(address.toString(), comment.toString());
    }
    else{
      values.put(address.toString(), new CellExpr(text));
    }

    values.forEach((currentAddress, value) -> {
      Cell currentCell = values.get(currentAddress);

      if (currentCell instanceof CellExpr) {

        if (currentCell.value(this).isError()) {
          notifyObservers(currentAddress, currentCell.value(this).toString());
        } else {
          notifyObservers(currentAddress, "" + currentCell.value(this).value());
        }
      }
    });
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
    System.out.println("XLMODEL LOADFILE");
    setup(true);
    try{
      reader.load(values);

      values.forEach((currentAddress, value) -> {
        Cell currentCell = values.get(currentAddress);
        if (currentCell instanceof CellComment){
          notifyObservers(currentAddress, currentCell.toString());
        }
        if (currentCell instanceof CellExpr ) {

          if (currentCell.value(this).isError()) {
            notifyObservers(currentAddress, currentCell.value(this).toString());
          } else {
            notifyObservers(currentAddress, "" + currentCell.value(this).value());
          }
        }
      });
    } catch(IOException e){
      e.getMessage();
    }
  }

  public void saveFile(File file) {
  }

  @Override
  public ExprResult value(String address) {
    return values.get(address).value(this);
  }

  public String getRawString(String address){
    return values.get(address).toRawString();
  }

  public void addObserver(XLModelObserver observer) {
    observerList.add(observer);
    System.out.println("added a listener");
  }

  public void removeObserver(XLModelObserver observer) {
    observerList.remove(observer);
    System.out.println("removed a listener");
  }

  private void notifyObservers(String address, String text){
    for (XLModelObserver o: observerList) {
      o.notifyChange(address, text);
    }
  }

}