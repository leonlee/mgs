/*
 * Copyright (c) 2013. FunLaiLe Inc. <http://www.funlaile.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fun.mgs.lab

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
