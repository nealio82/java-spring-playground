package com.nealio.event_sourcing.event_store.Doubles;

import com.nealio.event_sourcing.aggregate.AggregateEvent;
import com.nealio.event_sourcing.event_store.EventsBus;

import java.util.List;

public class StubEventsBus  implements EventsBus {
    @Override
    public void dispatch(List<AggregateEvent> events) {
        return;
    }
}
