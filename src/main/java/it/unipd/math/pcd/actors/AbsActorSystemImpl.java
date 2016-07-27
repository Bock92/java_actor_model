package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

/**
 * Created by bock on 03/04/16.
 */
public class AbsActorSystemImpl extends AbsActorSystem {


    /**
     * tempo massimo di attesa prima di eliminare un attore
     */
    private int timeout;

    /**
     * singleton AbsActorSystemImpl devo tener traccia della sua
     * unica istanza
     */
    private static AbsActorSystemImpl actorSystemImpl = null;

    /**
     * costruttore a un parametro
     * @param time indica il tempo massimo di attesa prima di eliminare un attore
     */
    public AbsActorSystemImpl(int time){
        this.timeout = time;
        actorSystemImpl = this;

    }

    /**
     * costruttore di default.
     * Imposta il tempo massimo di attesa prima di eliminare un attore a 5 secondi
     */
    public AbsActorSystemImpl(){
        this(5);
    }

    public static AbsActorSystemImpl getActorSystemImpl(){
        if(actorSystemImpl != null){
            return actorSystemImpl;
        }
        return actorSystemImpl = new AbsActorSystemImpl();
    }


    @Override
    protected ActorRef createActorReference(ActorMode mode) throws IllegalArgumentException{
        if(mode == ActorMode.LOCAL){
            return new ActorRefImpl(this);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void stop(ActorRef<?> actor){
        //fermo l'attore
        getActor(actor).shutDownActor(this.timeout);
        //rimuovo l'attore dalla mappa
        removeActor(actor);

    }

    @Override
    public void stop(){
        for(ActorRef actorRef : actors.keySet()){
            stop(actorRef);
        }
    }
}
