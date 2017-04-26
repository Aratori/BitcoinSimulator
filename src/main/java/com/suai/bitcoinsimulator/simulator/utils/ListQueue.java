package com.suai.bitcoinsimulator.simulator.utils;

import java.lang.*;
import java.util.Vector;

public class ListQueue extends OrderedSet {
    Vector elements = new Vector();

    /**
     * Добавляем если не достигнут предел контейнера или пока элементы не меньше нового значения
     * @param x
     */
    public void insert(Comparable x) {
        int i = 0;
        while (i < elements.size() && ((Comparable) elements.elementAt(i)).lessThan(x)) {
            i++;
        }
        elements.insertElementAt(x,i);
    }
    public Comparable removeFirst() {
        if (elements.size() ==0) return null;
        Comparable x = (Comparable) elements.firstElement();
        elements.removeElementAt(0);
        return x;
    }

    /**
     * Удаляем одинаковые элементы в очереди
     * @param x
     * @return
     */
    public Comparable remove(Comparable x) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.elementAt(i).equals(x)) {
                Object y = elements.elementAt(i);
                elements.removeElementAt(i);
                return (Comparable) y;
            }
        }
        return null;
    }

    public Comparable getFirst()
    {
        if (elements.size() ==0) return null;
        Comparable x = (Comparable) elements.firstElement();
        return x;
    }

    public int size() {
        return elements.size();
    }

}
