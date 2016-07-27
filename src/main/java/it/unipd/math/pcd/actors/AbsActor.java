/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;


    /**
     *  Uso un ExecutorService che funge da MailBox
     */
    private static ExecutorService mailbox = Executors.newSingleThreadExecutor();




    /**
     * Inserisce un messaggio nella mailbox dell'attore
     * @param messBy oggetto che contiene il messaggio inviato e il mittente
     */

    public void addMessageToMailBox(final MessageBy<T,ActorRef<T>> messBy){
        try{
            mailbox.execute(new Runnable(){
                @Override
                public void run(){
                    setSender(messBy.getEmittente());
                    receive(messBy.getMessaggio());

                }
            });
        }
        //Se il messaggio non viene accettato per essere processato
        // l'Executor lancia l'eccezione RejectedExecutionException
        catch (RejectedExecutionException exc){
            mailbox = Executors.newSingleThreadExecutor();
            addMessageToMailBox(messBy);

        }

    }



    protected final void shutDownActor(int timeout){
        mailbox.shutdown();
        try{
            if(!mailbox.awaitTermination(timeout, TimeUnit.SECONDS)){
                //se abbiamo sforato il tempo massimo indicato da timeout
                //devo fermare subito la mailbox
                mailbox.shutdown();
            }
        }
        //awaitTermination potrebbe lanciare una eccezzione di tipo InterruptedExcetion
        catch (InterruptedException exc){
            mailbox.shutdown();
            if(!mailbox.isShutdown()){
                //devo fermare il Thread corrente
                Thread.currentThread().interrupt();
            }
        }
    }





    /**
     * Sets the self-referece.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    /**
     * Sets the sender-reference
     * @param sender The reference to the sender
     */

    protected final void setSender(ActorRef<T> sender){

        this.sender = sender;
    }
}
