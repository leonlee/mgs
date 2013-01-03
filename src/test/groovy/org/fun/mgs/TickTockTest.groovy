package org.fun.mgs

import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestActorRef
import org.fun.mgs.lab.TickTock
import org.junit.Test
import scala.concurrent.Await
import scala.concurrent.duration.Duration

import static akka.pattern.Patterns.ask
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 12-12-31
 */
class TickTockTest {
    @Test
    def void tickTest() {
        ActorSystem system = ActorSystem.create("TickTockTest")
        TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(TickTock.class), system)
        def result = Await.result(ask(actorRef, new TickTock.Tick("msg"), 5000), Duration.apply("5 second"))
        assertEquals("processed tick message", result)
    }

    @Test
    def void tockTest() {
        ActorSystem system = ActorSystem.create("TickTockTest")
        TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(TickTock.class), system)
        TickTock ttActor = actorRef.underlyingActor()
        ttActor.tock(new TickTock.Tock("tock something"))
        assertTrue(ttActor.state)
        ttActor.tick(new TickTock.Tick("tick something"))
    }

    @Test
    def void exTest() {
        ActorSystem system = ActorSystem.create("TickTockTest")
        TestActorRef<TickTock> actorRef = TestActorRef.apply(new Props(TickTock.class), system)
        try {
            actorRef.receive('do something')
            fail()
        } catch (IllegalArgumentException e) {
            assertEquals('boom!', e.getMessage())
        }
    }
}
