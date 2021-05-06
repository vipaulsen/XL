package model;

import expr.Environment;
import expr.ErrorResult;
import expr.ExprResult;

public class CircularCell implements Cell{
    private String comment;
    public CircularCell(String s){
        comment = s;
    }

    @Override
    public ExprResult value(Environment env) {
        return new ErrorResult("Circular reference");
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
