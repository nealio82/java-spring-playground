package com.nealio.event_sourcing.event_store;

import com.nealio.event_sourcing.aggregate.AggregateEvent;
import com.nealio.event_sourcing.aggregate.AggregateId;
import com.nealio.event_sourcing.event_store.Doubles.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EventStoreTest {
    @Test
    public void aggregateEventsArePersistedToStorage() {

        InMemoryEventStore eventStore = new InMemoryEventStore();
        EventStoreRepository repository = new EventStoreRepository(eventStore, new StubEventsBus());

        SampleAggregate sampleAggregate = new SampleAggregate();
        sampleAggregate.addEvent(new FirstEvent());
        sampleAggregate.addEvent(new SecondEvent());

        repository.persist(sampleAggregate);

        Assertions.assertIterableEquals(List.of(new FirstEvent(), new SecondEvent()), eventStore.events(new SampleAggregateId()));
    }

    @Test
    public void aggregateIsRecreatedFromStorage() {
        InMemoryEventStore eventStore = new InMemoryEventStore();
        EventStoreRepository repository = new EventStoreRepository(eventStore, new StubEventsBus());

        eventStore.persist(List.of(new FirstEvent(), new SecondEvent()));

        SampleAggregate sampleAggregate = new SampleAggregate();
        sampleAggregate.addEvent(new FirstEvent());
        sampleAggregate.addEvent(new SecondEvent());

        AggregateSpy storedAggregate = (AggregateSpy) repository.fetch(new SampleAggregateId(), AggregateSpy.class.getCanonicalName());

        Assertions.assertTrue(storedAggregate.wasApplied(new FirstEvent()));
        Assertions.assertTrue(storedAggregate.wasApplied(new SecondEvent()));
    }

    @Test
    public void aggregateEventsAreDispatchedOverMessageBus() {
        EventsBusSpy eventsBus = new EventsBusSpy();
        EventStoreRepository repository = new EventStoreRepository(new InMemoryEventStore(), eventsBus);

        SampleAggregate sampleAggregate = new SampleAggregate();
        sampleAggregate.addEvent(new FirstEvent());
        sampleAggregate.addEvent(new SecondEvent());

        repository.persist(sampleAggregate);

        Assertions.assertTrue(eventsBus.wasEmitted(new FirstEvent()));
        Assertions.assertTrue(eventsBus.wasEmitted(new SecondEvent()));
    }


    class InMemoryEventStore implements EventStore {

        private final List<AggregateEvent> events = new ArrayList<>();

        public void persist(List<AggregateEvent> events) {
            this.events.addAll(events);
        }

        public List<AggregateEvent> events(AggregateId aggregateId) {
            return this.events;
        }
    }
}

