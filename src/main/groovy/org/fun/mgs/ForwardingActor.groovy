package org.fun.mgs

import akka.actor.ActorRef
import akka.actor.UntypedActor

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class ForwardingActor extends UntypedActor {
    ActorRef nextRef

    ForwardingActor(ActorRef nextRef) {
        this.nextRef = nextRef
    }

    @Override
    void onReceive(Object message) throws Exception {
        nextRef.tell(message, self)
    }
}
