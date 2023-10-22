package com.nealio.event_sourcing.aggregate;

public class NoAggregateEventApplicationMethodException extends RuntimeException {
    public NoAggregateEventApplicationMethodException(String s) {
        super(s);
    }
}
