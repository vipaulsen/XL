package model;

import expr.Environment;
import expr.ErrorResult;
import expr.ExprResult;

public class CellComment implements Cell{
    private String comment;
    public CellComment(String s){
        comment = s.substring(1);
    }

    @Override
    public ExprResult value(Environment env) {
        return new ErrorResult(comment);
    }

    @Override
    public String toString(){
        return comment;
    }

}