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

package org.fun.mgs

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.kernel.Bootable
import com.typesafe.config.ConfigFactory
import org.fun.mgs.server.HttpServer
import org.slf4j.bridge.SLF4JBridgeHandler

import static org.fun.mgs.Constant.*

public class Bootstrap implements Bootable {
    final static ActorSystem system = ActorSystem.create("mgs",
            ConfigFactory.load().getConfig("MgsSys"))
    final static LoggingAdapter log = Logging.getLogger(system)

    public void startup() {
        log.info("MGS is starting...")

        initLogging()
        HttpServer.startup()
        ActorRef gameSup = system.actorOf(new Props(GameSupervisor.class), GAME_SUPERVISOR)

        log.info("MGS was started successfully")
    }

    public void shutdown() {
        log.info("MGS is stopping")

        system.shutdown()

        log.info("MGS was stopped")
    }

    def initLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger()
        SLF4JBridgeHandler.install()
    }
}
