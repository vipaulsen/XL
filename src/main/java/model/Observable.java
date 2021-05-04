package model;

//Kanske är användbar framöver
public interface Observable {
    void addObserver(XLModelObserver observer);

    void removeObserver(XLModelObserver observer);

    //Kanske ska vara private, vi vill inte kunna kalla på den utifrån modellen
    void notifyObservers(CellAddress address, String value);
}
