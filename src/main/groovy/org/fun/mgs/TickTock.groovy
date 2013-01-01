package org.fun.mgs

import akka.actor.UntypedActor

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-31
 */
class TickTock extends UntypedActor {
    boolean state = false
    @Override
    void onReceive(Object message) throws Exception {
        switch (message) {
            case Tick:
                tick(message)
                break
            case Tock:
                tock(message)
                break
            default:
                throw new IllegalArgumentException('boom!')
        }
    }

    def tick(message) {
        sender.tell("processed tick message", self)
    }

    def tock(message) {
        state = !state
    }

    final static class Tick {
        final String message

        Tick(String message) {
            this.message = message
        }
    }

    final static class Tock {
        final String message

        Tock(String message) {
            this.message = message
        }
    }
}
