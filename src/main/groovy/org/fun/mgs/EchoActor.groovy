package org.fun.mgs

import akka.actor.UntypedActor

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class EchoActor extends UntypedActor{
    @Override
    void onReceive(Object message) throws Exception {
        sender.tell(message, self)
    }
}
