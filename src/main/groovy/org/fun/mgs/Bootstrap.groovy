package org.fun.mgs

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.kernel.Bootable

public class Bootstrap implements Bootable {
    final ActorSystem system = ActorSystem.create("hellokernel");

    static class HelloActor extends UntypedActor {
        LoggingAdapter log = Logging.getLogger(context.system(), this)
        final ActorRef worldActor = context.actorOf(new Props(WorldActor.class));

        public void onReceive(Object message) {
            switch (message) {
                case "start":
                    log.info("received start")
                    worldActor.tell("Hello", self)
                    break
                case String:
                case GString:
                    log.info("received $message")
                    println "Received message $message"
                    break
                    log.info("received gstring")
                    break
                default:
                    log.info("unknown $message")
                    unhandled(message)
            }
        }
    }

    static class WorldActor extends UntypedActor {
        LoggingAdapter log = Logging.getLogger(context.system(), this)
        public void onReceive(Object message) {
            switch (message) {
                case String:
                    log.info("received string $message")
                    sender.tell("${message.toUpperCase()} world !", self)
                    break
                default:
                    log.info("unknown message ${message}")
                    unhandled(message)
            }
        }
    }

    public void startup() {
        system.actorOf(new Props(HelloActor.class)).tell("start", null);
    }

    public void shutdown() {
        system.shutdown();
    }
}
