package com.nealio.event_sourcing.event_store.Doubles;

import com.nealio.event_sourcing.aggregate.AggregateEvent;

import java.util.ArrayList;
import java.util.List;

public class AggregateSpy extends SampleAggregate {

    private final List<String> events = new ArrayList<String>();

    public void applyFirstEvent(FirstEvent event) {
        this.recordEventSideEffect(event);
        super.applyFirstEvent(event);
    }

    public void applySecondEvent(SecondEvent event) {
        this.recordEventSideEffect(event);
        super.applySecondEvent(event);
    }

    public void applyThirdEvent(ThirdEvent event) {
        this.recordEventSideEffect(event);
        super.applyThirdEvent(event);
    }

    private void recordEventSideEffect(AggregateEvent event) {
        this.events.add(event.getClass().toString());
    }

    public boolean wasApplied(AggregateEvent event) {
        return this.events.contains(event.getClass().toString());
    }
}
