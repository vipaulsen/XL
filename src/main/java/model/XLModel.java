package model;

import expr.Environment;
import expr.ExprResult;
import gui.menu.XLBufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import util.XLException;

import expr.*;

public class XLModel implements Environment {
  public static final int COLUMNS = 10, ROWS = 10;
  private Map<String, Cell> values = new HashMap<>();



  public XLModel(){
    for(int r = 0; r < ROWS; r++){
      for(int c = 0; c < COLUMNS; c++){
        values.put(new CellAddress(r,c).toString(), new EmptyCell());
      }
    }
  }
  /**
   * Called when the code for a cell changes.
   *
   * @param address address of the cell that is being edited
   * @param text    the new code for the cell - can be raw text (starting with #) or an expression
   *
   *
   */


  public void update(CellAddress address, String text) {
    if(text.charAt(0) == '#'){
      values.put(address.toString(), new CellComment(text));
    }

    /*ExprParser parser = new ExprParser();
    try{
      Expr parsedText = parser.build(text);
    }catch(IOException e){
      System.out.println(e);


    }*/

    /*switch(){
      case "comment" : values.put(address.toString(), new CellComment(text));
    }*/
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
}