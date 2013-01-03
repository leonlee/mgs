package org.fun.mgs

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Cancellable
import akka.actor.Props
import akka.actor.UntypedActor
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.zeromq.Bind
import akka.zeromq.Frame
import akka.zeromq.Listener
import akka.zeromq.SocketOption
import akka.zeromq.ZMQMessage
import akka.zeromq.ZeroMQExtension
import org.junit.Test
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-3
 */
class ZmqTest {
    static class ServerActor extends UntypedActor {
        ActorRef repSocket = ZeroMQExtension.get(context.system())
                .newRepSocket([new Bind('tcp://127.0.0.1:1237'), new Listener(self)] as SocketOption[])

        @Override
        void onReceive(Object message) throws Exception {
            switch (message) {
                case ZMQMessage:
                    String msg = new String((message as ZMQMessage).payload(0))
                   repSocket.tell(new ZMQMessage(
                           new Frame("${msg}, good to see you")
                   ), self)
            }
        }
    }

    static class ClientAtctor extends UntypedActor {
        static final Object TICK = 'TICK'
        int count = 0
        Cancellable cancellable
        ActorRef reqSocket = ZeroMQExtension.get(context.system())
            .newReqSocket([new Bind('tcp://127.0.0.1:1237'), new Listener(self)] as SocketOption[])
        LoggingAdapter log = Logging.getLogger(context.system(), this)

        @Override
        void preStart() {
            Duration duration = Duration.create(1l, TimeUnit.SECONDS)
            cancellable = context.system().scheduler().schedule(
                    duration, duration, self, TICK, context.system().dispatcher()
            )
        }

        @Override
        void onReceive(Object message) throws Exception {
            switch (message) {
                case TICK:
                    reqSocket.tell(new ZMQMessage(new Frame("Hi there! ${context.self().hashCode()}")))
                    if (++count == 10) cancellable.cancel()
                    break
                case ZMQMessage:
                    String msg = (message as ZMQMessage).payload(0)
                    log.info("Receive msg! $msg")
                    println("Receive msg! $msg")
                    break;
                default:
                    unhandled(message)
            }
        }
    }

    @Test
    def void repReqTest() {
        ActorSystem serverSystem = ActorSystem.create('zmqRepReqServerTest')
        serverSystem.actorOf(new Props(ServerActor.class), 'server')

        ActorSystem clientSystem = ActorSystem.create('zmqRepReqClientTest')
        clientSystem.actorOf(new Props(ClientAtctor.class), 'client')

        println "testing zmq"
        sleep(5000l)
    }
}
