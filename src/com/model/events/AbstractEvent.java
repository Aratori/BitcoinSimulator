package com.model.events;

import com.model.AbstractSimulator;
import com.model.utils.Comparable;

public abstract class  AbstractEvent implements Comparable {
    public abstract void execute(AbstractSimulator simulator);
}
