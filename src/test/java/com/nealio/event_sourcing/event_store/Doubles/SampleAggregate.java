package com.nealio.event_sourcing.event_store.Doubles;

import com.nealio.event_sourcing.aggregate.Aggregate;
import com.nealio.event_sourcing.aggregate.AggregateEvent;
import com.nealio.event_sourcing.aggregate.AggregateId;

public class SampleAggregate extends Aggregate {
    public void addEvent(AggregateEvent event) {
        this.raise(event);
    }

    // @todo should probably find a way to stop needing these methods from being public
    // it's necessary here because this aggregate class lives outside of the same namespace
    // as the base aggregate. however, this will be a problem for *all* userland classes
    // which extend the abstract Aggregate class, so moving the EventStore into the same
    // namespace only fixes the issue for _this_ implementation. Maybe there's a way to
    // use the visitor pattern on the subclass from within the base class, which would allow
    // the apply... methods to remain 'protected'?
    public void applyFirstEvent(FirstEvent event) {
        return;
    }

    public void applySecondEvent(SecondEvent event) {
        return;
    }

    public void applyThirdEvent(ThirdEvent event) {
        return;
    }

    public AggregateId aggregateId() {
        return new SampleAggregateId();
    }
}
