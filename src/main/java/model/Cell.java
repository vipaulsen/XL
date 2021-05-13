package model;

import expr.Environment;
import expr.ExprResult;

public interface Cell {

    ExprResult value(Environment env);

    String toString();

}
