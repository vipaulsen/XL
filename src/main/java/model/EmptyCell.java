package model;

import expr.Environment;
import expr.ExprResult;

public class EmptyCell implements Cell{

    @Override
    public ExprResult value(Environment env){
        return null;
    }
    @Override
    public String toString(){
        return "";
    }
}
