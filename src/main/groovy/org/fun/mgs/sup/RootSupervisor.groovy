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

package org.fun.mgs.sup

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.SupervisorStrategy
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import org.fun.mgs.RootContainer

import static org.fun.mgs.Constant.*

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class RootSupervisor extends UntypedActor {
    private static final SupervisorStrategy strategy = new RootStrategy()

    private LoggingAdapter log = Logging.getLogger(RootContainer.system)

    ActorRef service;
    ActorRef state;

    RootSupervisor() {
        log.debug("initializing game supervisor...")

        service = context.actorOf(new Props(ServiceSupervisor.class), SERVICE_SUPERVISOR)
        state = context.actorOf(new Props(StateSupervisor.class), STATE_SUPERVISOR)

        log.debug("game supervisor was initialized")
    }

    @Override
    SupervisorStrategy supervisorStrategy() {
        return strategy
    }

    @Override
    void onReceive(Object o) throws Exception {

    }
}
