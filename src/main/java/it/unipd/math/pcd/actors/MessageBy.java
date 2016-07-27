package it.unipd.math.pcd.actors;

/**
 * Created by bock on 03/04/16.
 */
public final class MessageBy<T extends Message, X extends ActorRef<T>> {


    private final X emittente;
    private final T messaggio;

    public MessageBy(T mess, X emitt){
        this.emittente = emitt;
        this.messaggio = mess;
    }

    public T getMessaggio(){
        return this.messaggio;

    }

    public ActorRef<T> getEmittente(){

        return this.emittente;
    }


}
