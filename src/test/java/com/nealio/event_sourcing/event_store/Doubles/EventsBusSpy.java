package com.nealio.event_sourcing.event_store.Doubles;

import com.nealio.event_sourcing.aggregate.AggregateEvent;
import com.nealio.event_sourcing.event_store.EventsBus;

import java.util.ArrayList;
import java.util.List;

public class EventsBusSpy implements EventsBus {
    private final List<String> events = new ArrayList<String>();

    public void dispatch(List<AggregateEvent> events) {

        for (AggregateEvent event : events) {
            this.events.add(event.getClass().toString());
        }
    }

    public boolean wasEmitted(AggregateEvent event) {
        return this.events.contains(event.getClass().toString());
    }
}
