package model;

import expr.ExprResult;

public class CellFactory {
    public Cell makeCell(String text) {
        Cell producedCell;
        if (text.isEmpty()) {
            producedCell = new EmptyCell();
        }else if(text.charAt(0) == '#'){
            producedCell = new CommentCell(text);
        }else{
            producedCell = new CellExpr(text);
        }
        return producedCell;
    }
    public CircularCell makeCircularCell(String text){
        return new CircularCell(text);
    }

    public ErrorCell makeErrorCell(String text, ExprResult expr){
        return new ErrorCell(text, expr);
    }
}