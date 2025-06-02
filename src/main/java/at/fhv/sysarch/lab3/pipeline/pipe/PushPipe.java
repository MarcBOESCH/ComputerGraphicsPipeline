package at.fhv.sysarch.lab3.pipeline.pipe;

import at.fhv.sysarch.lab3.pipeline.filter.PushFilter;

/**
 * PushPipe<T> ist das Interface, über das Filter ihre Ausgabe
 * an den nächsten Schritt (successor) schicken. Jeder PushFilter
 * erwartet einen PushPipe<O> als successor, um Werte weiterzugeben.
 *
 * @param <T> Typ der Objekte, die diese Pipe empfängt (z.B. Face)
 */
public interface PushPipe<T> {

/**
 * Gibt ein Element des Typs T an den nächsten Filter weiter.
 *
 * @param object Das Objekt, das weitergereicht wird.
 */
    void push(T object);

    public void setSuccessor(PushFilter<T, ?> successor);
}
