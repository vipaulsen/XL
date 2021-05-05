package model;

import expr.*;

import java.io.IOException;

public class CellExpr implements Cell{
    private Expr expr;
    private String ogexpr;

    public CellExpr(String stringExpr){
        ogexpr = stringExpr;
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

    public String toString(){
        return expr.toString();
    }

    public String toRawString(){return ogexpr;}

}
