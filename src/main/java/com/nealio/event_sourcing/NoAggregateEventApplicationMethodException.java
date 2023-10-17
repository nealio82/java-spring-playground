package com.nealio.event_sourcing;

public class NoAggregateEventApplicationMethodException extends RuntimeException {
    public NoAggregateEventApplicationMethodException(String s) {
        super(s);
    }
}
