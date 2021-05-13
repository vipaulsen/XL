package model;

import expr.*;

import java.io.IOException;

public class CellExpr implements Cell{
    private Expr expr;
    private String origExpr;

    public CellExpr(String stringExpr){
        origExpr = stringExpr;
        ExprParser parser = new ExprParser();
        try {
            expr = parser.build(stringExpr);
        } catch (IOException e){
            expr = new ErrorExpr(e.getMessage());
        }

    }

    @Override
    public ExprResult value(Environment env) {
        return expr.value(env);
    }

    @Override
    public String toString(){
        return origExpr;
    }

}
