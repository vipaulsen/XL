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
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLUMNS; c++) {
        values.put(new CellAddress(r, c).toString(), new EmptyCell());
      }
    }
    observerList = new ArrayList<>();
    parser = new ExprParser();

  }
  /**
   * Called when the code for a cell changes.
   *
   * @param address address of the cell that is being edited
   * @param text    the new code for the cell - can be raw text (starting with #) or an expression
   */


  public void update(CellAddress address, String text){
    Expr temp;
    try {
      temp = parser.build(text);
    } catch (IOException e){
      temp = new ErrorExpr("Test");
    }

    if (temp.toString().equals(values.get(address.toString()).toString()) || text.equals()) {
      return;
    }

    System.out.println("NOTIFYING ALL OBSERVERS");
    if(text.isEmpty()){
      EmptyCell emptyCell = new EmptyCell();
      values.put(address.toString(),emptyCell);
      notifyObservers(address, emptyCell.toString());
    }
    else if (text.charAt(0) == '#') {
      CellComment comment = new CellComment(text);
      values.put(address.toString(), comment);
      notifyObservers(address, comment.toString().substring(1));
    }
    else{
      CellExpr expr = new CellExpr(text);
      values.put(address.toString(), expr);

      //Kolla om det är ett expr eller value eller nått som måste räknas
      //Environment env = name -> {
      //  return new ValueResult(value(name).value());
      //};
      notifyObservers(address, "" + expr.value(this).value());

    }
  }

  public void loadFile(File file) throws FileNotFoundException {
    XLBufferedReader reader = new XLBufferedReader(file);
  }

  public void saveFile(File file) {
  }

  @Override
  public ExprResult value(String name) {
    return values.get(name).value(this);
  }

  public String toString(String name){
    return values.get(name).toString();
  }

  public void addObserver(XLModelObserver observer) {
    observerList.add(observer);
    System.out.println("added a listener");
  }

  public void removeObserver(XLModelObserver observer) {
    observerList.remove(observer);
    System.out.println("removed a listener");
  }

  private void notifyObservers(CellAddress address, String text){
    for (XLModelObserver o: observerList) {
      o.notifyChange(address, text);
    }
  }

}