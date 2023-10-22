package com.nealio.event_sourcing.aggregate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Aggregate {

    private int aggregateVersion = 0;

    private List<AggregateEvent> events = new ArrayList<>();

    public int aggregateVersion() {
        return this.aggregateVersion;
    }

    abstract public AggregateId aggregateId();

    public static Aggregate buildFromEvents(List<AggregateEvent> events, Aggregate aggregate) {

        if (aggregate.aggregateVersion > 0) {
            throw new CannotOverwriteAggregateWithExistingEventsException();
        }

        for (AggregateEvent event : events) {
            aggregate.apply(event);
        }

        return aggregate;
    }

    public List<AggregateEvent> flush() {

        List<AggregateEvent> events = new ArrayList<>(this.events);

        this.events.clear();

        return events;
    }

    protected void raise(AggregateEvent event) throws NoAggregateEventApplicationMethodException {
        this.events.add(event);

        this.apply(event);
    }

    private void apply(AggregateEvent event) {
        String eventClassName = event.getClass().getSimpleName();
        String methodName = "apply" + eventClassName;
        try {
            Method method = this.getClass().getDeclaredMethod(methodName, event.getClass());

            method.invoke(this, event);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            throw new NoAggregateEventApplicationMethodException(String.format("No method named %s was found in the %s class", methodName, this.getClass().getSimpleName()));
        }

        this.aggregateVersion++;
    }
}
