package model;

import expr.Environment;
import expr.ErrorResult;
import expr.ExprResult;

public class CommentCell implements Cell{
    private String comment;
    public CommentCell(String s){
        comment = s;
    }

    @Override
    public ExprResult value(Environment env) {
        return new ErrorResult("Referencing comment");
    }

    @Override
    public String toString(){
        return comment.substring(1);
    }

    @Override
    public String toRawString(){
        return comment;
    }
}
