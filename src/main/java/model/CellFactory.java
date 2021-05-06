package model;

public class CellFactory {
    public Cell makeCell(String text) {
        Cell producedCell;
        if (text.isEmpty()) {
            producedCell = new EmptyCell();
        }else if(text.charAt(0) == '#'){
            producedCell = new CellComment(text);
        }else{
            producedCell = new CellExpr(text);
        }
        return producedCell;
    }
}