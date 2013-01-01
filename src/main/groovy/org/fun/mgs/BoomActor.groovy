package org.fun.mgs

import akka.actor.UntypedActor

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class BoomActor extends UntypedActor{
    @Override
    void onReceive(Object message) throws Exception {
        switch (message) {
            case String:
            case GString:
                throw new IllegalArgumentException('boom!')
            case Integer:
                throw new NullPointerException('caput')
            default:
                unhandled(message)
        }
    }
}
