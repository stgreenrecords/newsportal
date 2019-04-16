package com.open.core.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class StreamProvider {

    public<T> Stream<T> getStreamFromIterator(Iterator<T> iterator){
        return StreamSupport.
                stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

}
