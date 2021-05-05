package model;

import expr.Environment;
import expr.ErrorResult;
import expr.ExprResult;

public class EmptyCell implements Cell{

    @Override
    public ExprResult value(Environment env){
        return new ErrorResult("Referencing empty cell");
    }
    @Override
    public String toString(){
        return "";
    }

    @Override
    public String toRawString() { return "";}
}
