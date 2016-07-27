package it.unipd.math.pcd.actors;

/**
 * Created by bock on 03/04/16.
 */
public class ActorRefImpl <T extends Message> implements ActorRef<T>{

    /**
     * salvo un riferimento dell'ActorSystem per poter ottenere l'attore
     */
    protected final AbsActorSystemImpl actorSystem;

    /**
     * costruttore della classe
     */

    public ActorRefImpl(AbsActorSystemImpl actSystem){
        this.actorSystem = actSystem;

    }

    @Override
    public void send(T message, ActorRef to){

        MessageBy messaggio = new MessageBy<>(message,this);
        actorSystem.getActor(to).addMessageToMailBox(messaggio);

    }

    /**
     * confronta due oggetti
     * @param actorRef riferimento ad un attore
     * @return 0 se sono uguali -1 se sono diversi
     */
    @Override
    public int compareTo(ActorRef actorRef){
        if (actorRef != null){
            if(this.equals(actorRef)){
                return 0;
            }
            return -1;
        }
        else throw new NullPointerException();
    }
}
