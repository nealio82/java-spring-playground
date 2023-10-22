package com.nealio.event_sourcing.event_store;

import com.nealio.event_sourcing.aggregate.Aggregate;
import com.nealio.event_sourcing.aggregate.AggregateEvent;
import com.nealio.event_sourcing.aggregate.AggregateId;

import java.util.List;

public class EventStoreRepository {

    private final EventStore eventStore;
    private final EventsBus eventsBus;

    public EventStoreRepository(
            EventStore eventStore,
            EventsBus eventsBus
    ) {
        this.eventStore = eventStore;
        this.eventsBus = eventsBus;
    }

    public void persist(Aggregate aggregate) {

        List<AggregateEvent> events = aggregate.flush();

        this.eventStore.persist(events);
        this.eventsBus.dispatch(events);
    }

    // @todo handle these exceptions in a better manner; probably best to throw a custom exception instead
    public Aggregate fetch(AggregateId aggregateId, String aggregateType) {
        try {
            return Aggregate.buildFromEvents(this.eventStore.events(aggregateId), (Aggregate) Class.forName(aggregateType).newInstance());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException();
        }

    }
}
