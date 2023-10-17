package com.nealio.event_sourcing.aggregate;

final class NoAggregateEventApplicationMethodException extends RuntimeException {
    public NoAggregateEventApplicationMethodException(String s) {
        super(s);
    }
}
