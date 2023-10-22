package com.nealio.event_sourcing.event_store;

import com.nealio.event_sourcing.aggregate.AggregateEvent;
import com.nealio.event_sourcing.aggregate.AggregateId;

import java.util.List;

public interface EventStore {
    void persist(List<AggregateEvent> events);

    List<AggregateEvent> events(AggregateId $aggregateId);
}
