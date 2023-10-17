package com.nealio.event_sourcing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AggregateTest {
    @Test
    public void aggregateVersionIsZeroWhenNoEvents() {
        AggregateStub aggregate = new AggregateStub();

        Assertions.assertSame(0, aggregate.aggregateVersion());
    }

    @Test
    public void aggregateVersionIncrementsOnNewEvents() {
        AggregateStub aggregate = new AggregateStub();
        Assertions.assertSame(0, aggregate.aggregateVersion());

        aggregate.raise(new EmptyEvent());
        Assertions.assertSame(1, aggregate.aggregateVersion());

        aggregate.raise(new EmptyEvent());
        Assertions.assertSame(2, aggregate.aggregateVersion());
    }

    @Test
    public void raisedAggregateEventIsApplied() {

        AggregateSpy aggregate = new AggregateSpy();
        Assertions.assertFalse(aggregate.wasFirstEventApplied());

        aggregate.raise(new FirstEvent());
        Assertions.assertTrue(aggregate.wasFirstEventApplied());
        Assertions.assertFalse(aggregate.wasSecondEventApplied());

        aggregate.raise(new SecondEvent());
        Assertions.assertTrue(aggregate.wasSecondEventApplied());
    }

    @Test
    public void exceptionIsThrownWhenEventHasNoApplicationMethod() {

        AggregateStub aggregate = new AggregateStub();

        class FirstEvent implements AggregateEvent {
        }

        Assertions.assertThrows(NoAggregateEventApplicationMethodException.class, () -> {
            aggregate.raise(new FirstEvent());
        });
    }

    @Test
    public void aggregateIsBuiltFromExistingEvents() {
        AggregateSpy aggregate = (AggregateSpy) Aggregate.buildFromEvents(new AggregateEvent[]{new FirstEvent(), new SecondEvent()}, new AggregateSpy());

        Assertions.assertTrue(aggregate.wasFirstEventApplied());
        Assertions.assertTrue(aggregate.wasSecondEventApplied());
    }

    @Test
    public void aggregateCannotBeBuiltFromExistingEventsIfOtherEventsHaveAlreadyBeenApplied() {
        AggregateStub aggregate = new AggregateStub();
        aggregate.raise(new FirstEvent());

        Assertions.assertThrows(CannotOverwriteAggregateWithExistingEventsException.class, () -> {
            Aggregate.buildFromEvents(new AggregateEvent[]{new FirstEvent()}, aggregate);
        });
    }

    @Test
    public void eventsAreFlushedForStorageAndDispatch() {
        AggregateStub aggregate = new AggregateStub();
        aggregate.raise(new FirstEvent());
        aggregate.raise(new SecondEvent());
        aggregate.raise(new ThirdEvent());

        List<AggregateEvent> expectedEvents = List.of(new FirstEvent(), new SecondEvent(), new ThirdEvent());

        Assertions.assertIterableEquals(expectedEvents, aggregate.flush());
    }


    @Test
    public void previouslyFlushedEventsAreNotFlushedASecondTime() {
        AggregateStub aggregate = new AggregateStub();
        aggregate.raise(new FirstEvent());
        aggregate.raise(new SecondEvent());

        aggregate.flush();

        aggregate.raise(new ThirdEvent());
        Assertions.assertIterableEquals(List.of(new ThirdEvent()), aggregate.flush());

        aggregate.raise(new SecondEvent());
        aggregate.raise(new SecondEvent());

        Assertions.assertIterableEquals(List.of(new SecondEvent(), new SecondEvent()), aggregate.flush());

        Assertions.assertIterableEquals(List.of(), aggregate.flush());
    }

    @Test
    public void newEventsAreAppendedAfterAggregateIsBuiltFromPrevious() {
        AggregateStub aggregate = (AggregateStub) Aggregate.buildFromEvents(
                new AggregateEvent[]{new FirstEvent(), new SecondEvent()}, new AggregateStub()
        );
        aggregate.raise(new ThirdEvent());

        Assertions.assertIterableEquals(List.of(new ThirdEvent()), aggregate.flush());
    }

    static class EmptyEvent implements AggregateEvent {

    }

    class FirstEvent implements AggregateEvent {
        @Override
        public boolean equals(Object other) {
            return this.getClass().equals(other.getClass());
        }
    }

    class SecondEvent implements AggregateEvent {
        @Override
        public boolean equals(Object other) {
            return this.getClass().equals(other.getClass());
        }
    }

    class ThirdEvent implements AggregateEvent {
        @Override
        public boolean equals(Object other) {
            return this.getClass().equals(other.getClass());
        }
    }

    class AggregateStub extends Aggregate {
        protected void applyEmptyEvent(EmptyEvent event) {
            return;
        }

        protected void applyFirstEvent(FirstEvent event) {
            return;
        }

        protected void applySecondEvent(SecondEvent event) {
            return;
        }

        protected void applyThirdEvent(ThirdEvent event) {
            return;
        }
    }

    class AggregateSpy extends Aggregate {

        private boolean wasFirstEventApplied = false;
        private boolean wasSecondEventApplied = false;

        protected void applyFirstEvent(FirstEvent event) {
            this.wasFirstEventApplied = true;
        }

        public boolean wasFirstEventApplied() {
            return this.wasFirstEventApplied;
        }

        protected void applySecondEvent(SecondEvent event) {
            this.wasSecondEventApplied = true;
        }

        public boolean wasSecondEventApplied() {
            return this.wasSecondEventApplied;
        }
    }
}