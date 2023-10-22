package com.nealio.event_sourcing.event_store.Doubles;

import com.nealio.event_sourcing.aggregate.AggregateEvent;

public class ThirdEvent implements AggregateEvent {
    @Override
    public boolean equals(Object other) {
        return this.getClass().equals(other.getClass());
    }
}
