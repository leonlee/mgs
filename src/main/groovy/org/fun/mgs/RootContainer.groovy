package org.fun.mgs

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory
import org.fun.mgs.sup.RootSupervisor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.fun.mgs.Constant.ROOT_SUPERVISOR

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-6
 */
class RootContainer {
    private Logger log = LoggerFactory.getLogger(RootContainer.class)

    ActorSystem system;
    ActorRef root;
    ActorRef service;
    ActorRef state;

    private static class Holder {
        static final RootContainer container = new RootContainer()
    }

    private RootContainer() {
        log.debug("initializing root container...")

        system = ActorSystem.create("mgs",
                ConfigFactory.load().getConfig("MgsSys"))
        root = system.actorOf(new Props(RootSupervisor.class), ROOT_SUPERVISOR)
        service = system.actorFor('akka://mgs/user/root/service/')
        state = system.actorFor('akka://mgs/user/root/service/')

        log.debug("root container was initialized")
    }

    static RootContainer getInstance() {
        Holder.container
    }

    static init() {
        getInstance()
    }

    static ActorSystem getSystem() {
        getInstance().system
    }
}
