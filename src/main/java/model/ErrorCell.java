package model;

import expr.*;

public class ErrorCell implements Cell{
    private String ogExpr;
    private ExprResult errorMsg;


    public ErrorCell(String text, ExprResult errorMsg){
        ogExpr = text;
        this.errorMsg = errorMsg;
    }

    @Override
    public ExprResult value(Environment env) {
        return errorMsg;
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
