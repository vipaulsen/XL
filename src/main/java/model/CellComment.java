package model;

import expr.Environment;
import expr.ErrorResult;
import expr.ExprResult;

public class CellComment implements Cell{
    private String comment;
    public CellComment(String s){
        comment = s;
    }

    @Override
    public ExprResult value(Environment env) {
        return new ErrorResult("Referencing comment");
    }

    @Override
    public String toString(){
        return comment;
    }

    @Override
    public String toRawString(){
        return comment;
    }
}
