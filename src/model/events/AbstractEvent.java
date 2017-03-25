package model.events;

import model.AbstractSimulator;
import model.utils.Comparable;

public abstract class  AbstractEvent implements Comparable {
    public abstract void execute(AbstractSimulator simulator);
}
