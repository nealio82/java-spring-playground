package com.nealio.event_sourcing.event_store;

import com.nealio.event_sourcing.aggregate.AggregateEvent;

import java.util.List;

public interface EventsBus {
    void dispatch(List<AggregateEvent> events);
}
