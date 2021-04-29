package model;

import expr.*;

import java.io.IOException;

public class CellExpr implements Cell{
    private Expr expr;

    public CellExpr(String stringExpr){
        ExprParser parser = new ExprParser();
        try {
            expr = parser.build(stringExpr);
        } catch (IOException e){
            expr = new ErrorExpr("Test");
        }

    }

    @Override
    public ExprResult value(Environment env) {
        return expr.value(env);
    }



}
