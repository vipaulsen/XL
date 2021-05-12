package model;

import expr.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class XLModel implements Environment {
  public static final int COLUMNS = 10, ROWS = 10;
  private Map<String, Cell> values = new HashMap<>();
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

    if (text.equals(values.get(address.toString()).toRawString()) ){
      return;
    }

    Cell newCell = new CellFactory().makeCell(text);
    if (newCell instanceof CellComment || newCell instanceof EmptyCell){
      values.put(address.toString(), newCell);
    } else{
      validity(address.toString(), newCell);
    }
      values.forEach((currentAddress, value) -> {
        if (!(value instanceof EmptyCell || value instanceof CellComment)) {
          validity(currentAddress, value);
        }
      });

      updateAll();
    //CHECKA VALIDITY, SEDAN VALIDITY PÅ ALLA ANDRA CELLER SOM INTE ÄR TOMMA, SKAPA ERRORCELL OM DET PAJJAR
  }

  private void validity(String address, Cell cell){

    if ((cell instanceof CircularCell || cell instanceof ErrorCell)){
      cell = new CellExpr(cell.toRawString());
    }

    CircularCell bomb = new CircularCell(cell.toRawString());
    values.put(address, bomb);

    if (cell.value(this) instanceof ErrorResult){
      //System.out.println("kom hit");
      values.put(address, new ErrorCell(cell.toRawString(), cell.value(this)));     //TODO: Make ErrorCell
    }
    else{
      values.put(address, cell);
    }
  }

  private void updateAll() {
    values.forEach((currentAddress, value) -> {

      Cell cell = values.get(currentAddress);

      if (cell instanceof CellComment || cell instanceof EmptyCell) {
        notifyObservers(currentAddress, cell.toString());
      } /*else if (cell instanceof CircularCell){
        notifyObservers(currentAddress, cell.value(this).toString());
      }*/else if (cell instanceof CellExpr){
        notifyObservers(currentAddress, "" + values.get(currentAddress).value(this).value());
      } else if(cell instanceof ErrorCell){
        notifyObservers(currentAddress, cell.value(this).toString());
      }
    });
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
    setup(true);
    try {
      reader.load(values);
      values.forEach((currentAddress, value) -> {
        if(!(value instanceof EmptyCell || value instanceof CellComment)){
          validity(currentAddress, value);
        }
      });
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