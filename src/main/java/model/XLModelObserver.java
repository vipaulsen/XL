package model;

public interface XLModelObserver {
    void notifyChange(String cell, String value);
}
