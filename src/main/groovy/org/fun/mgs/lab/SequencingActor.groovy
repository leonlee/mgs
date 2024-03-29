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
