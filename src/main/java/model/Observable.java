package model;

public interface Observable {
    void addObserver(XLModelObserver observer);

    void removeObserver(XLModelObserver observer);

    void notifyObservers(CellAddress address, String value);
}
