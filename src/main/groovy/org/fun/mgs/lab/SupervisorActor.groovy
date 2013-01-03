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
import akka.actor.OneForOneStrategy
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.actor.UntypedActor
import akka.japi.Function
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

import static akka.actor.SupervisorStrategy.escalate
import static akka.actor.SupervisorStrategy.resume
import static akka.actor.SupervisorStrategy.stop

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class SupervisorActor extends UntypedActor{
    static SupervisorStrategy strategy = new OneForOneStrategy(10,
            Duration.create(10l, TimeUnit.SECONDS),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                    switch (t) {
                        case IllegalArgumentException:
                            return stop()
                        case NullPointerException:
                            return resume()
                        default:
                            return escalate()
                    }
                }
            }
    )

    @Override
    SupervisorStrategy supervisorStrategy() {
        return strategy
    }

    ActorRef childActor
    @Override
    void onReceive(Object message) throws Exception {
        switch (message) {
            case Props:
                childActor = context.actorOf(message as Props, 'childActor')
                sender.tell(childActor, self)
                break
            default:
                childActor.tell(message, sender)
        }
    }
}
