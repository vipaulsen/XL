package model;

public interface XLModelObserver {
    void notifyChange(CellAddress cell, String value);
}
