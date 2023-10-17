package com.nealio.event_sourcing;

final class NoAggregateEventApplicationMethodException extends RuntimeException {
    public NoAggregateEventApplicationMethodException(String s) {
        super(s);
    }
}
