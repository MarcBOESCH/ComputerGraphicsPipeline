package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;

/**
 * Generisches Interface für alle Push-Filter in der Pipeline.
 * Ein Push-Filter empfängt Objekte des Typs I (Input) und
 * leitet nach der Verarbeitung Objekte des Typs O (Output) an
 * seinen Nachfolger ("successor") weiter.
 */
public interface PushFilter<I, O> {

/**
 * Verknüpft diesen Filter mit seinem Nachfolger.
 * Der Nachfolger verarbeitet den Output-Typ O dieses Filters weiter.
 * */
    public void setSuccessor(PushFilter<O, ?> successor);

    /**
     * Verarbeitet ein einzelnes Objekt vom Typ I (Input). Das Ergebnis
     * wird (sofern nicht null) an successor.push(...) weitergereicht
     */
    public void push(I input);
}
