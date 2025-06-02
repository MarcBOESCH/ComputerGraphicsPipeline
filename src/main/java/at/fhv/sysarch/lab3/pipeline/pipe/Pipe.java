package at.fhv.sysarch.lab3.pipeline.pipe;

import at.fhv.sysarch.lab3.pipeline.filter.PushFilter;

/**
 * Eine konkrete Pipe-Implementierung, die ein Objekt T direkt an
 * ihren verknüpften PushFilter<T, ?> weiterreicht.
 */
public class Pipe<T> implements PushPipe<T>{

    private PushFilter<T, ?> successor;

    /**
     * Leitet ein Objekt vom Typ T an den verknüpften PushFilter weiter.
     *
     * @param input Das Objekt, dass an successor.push(...) übergeben wird.
     */
    @Override
    public void push(T input) {
        if (successor != null) {
            successor.push(input);
        }
    }

/**
 * Verknüpft diese Pipe mit einem PushFilter, der Objekte vom Typ T empfängt.
 *
 * @param successor Der nächste PushFilter in der Kette
 */
    @Override
    public void setSuccessor(PushFilter<T, ?> successor) {
        this.successor = successor;
    }
}
