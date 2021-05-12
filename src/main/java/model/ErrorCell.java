package model;

import expr.Environment;
import expr.ErrorResult;
import expr.ExprResult;

public class ErrorCell implements Cell{
    private String ogExpr;

    public ErrorCell(String text){
        ogExpr = text;
    }

    @Override
    public ExprResult value(Environment env) {
        return new ErrorResult("error cell");
    }

    @Override
    public String toString(){
     return ogExpr;
    }

    @Override
    public String toRawString() {
        return ogExpr;
    }
}
