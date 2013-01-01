package org.fun.mgs

import akka.actor.ActorRef
import akka.actor.UntypedActor

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class SequencingActor extends UntypedActor {
    ActorRef nextRef
    List<Integer> head
    List<Integer> tail

    SequencingActor(ActorRef nextRef, List<Integer> head, List<Integer> tail) {
        this.nextRef = nextRef
        this.head = head
        this.tail = tail
    }

    @Override
    void onReceive(Object message) throws Exception {
        head.each {
            sender.tell(it, self)
        }
        sender.tell(message, self)
        tail.each {
            sender.tell(it, self)
        }
    }
}
