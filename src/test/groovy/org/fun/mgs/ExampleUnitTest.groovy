/*
 * Copyright (c) 2013. FunZen Inc. <http://www.funzen.com>
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

import akka.actor.*
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.testkit.TestKit
import akka.testkit.TestProbe
import com.typesafe.config.ConfigFactory
import org.fun.mgs.lab.BoomActor
import org.fun.mgs.lab.EchoActor
import org.fun.mgs.lab.ForwardingActor
import org.fun.mgs.lab.SequencingActor
import org.fun.mgs.lab.SupervisorActor
import org.junit.Test
import scala.concurrent.Await
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

import static akka.pattern.Patterns.ask
import static junit.framework.Assert.assertFalse

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-1
 */
class ExampleUnitTest extends TestKit {
    static ActorSystem system = ActorSystem.create('TestSys',
            ConfigFactory.load().getConfig('TestSys'))
    LoggingAdapter log = Logging.getLogger(system, this)

    ExampleUnitTest() {
        super(system)
    }

    @Test
    def void echoActorTest() {
        ActorRef echoActor = system.actorOf(new Props(EchoActor.class))
        echoActor.tell('Hi there', super.testActor())
        expectMsg('Hi there')
    }

    @Test
    def void forwardingActorTest() {
        ActorRef forwardingActor = system.actorOf(
                new Props(new UntypedActorFactory() {
                    @Override
                    Actor create() throws Exception {
                        return new ForwardingActor(testActor())
                    }
                }))

        forwardingActor.tell("test message", testActor())
        expectMsg("test message")
    }

    @Test
    def void sequencingActorTest() {
        def head = []
        def tail = []

        new Random().nextInt(6).each {
            head.add(it)
        }
        [1..new Random().nextInt(10)].each {
            tail.add(it)
        }

        ActorRef sequencingActor = system.actorOf(new Props(new UntypedActorFactory() {
            @Override
            Actor create() throws Exception {
                return new SequencingActor(testActor(), head, tail)
            }
        }))

        sequencingActor.tell('test message', super.testActor())

        head.each {
            expectMsg(it)
        }
        expectMsg('test message')
        tail.each {
            expectMsg(it)
        }
        expectNoMsg()
    }

    @Test
    def void supervisorActorTest() {
        ActorRef supervisorActor = system.actorOf(new Props(SupervisorActor.class), 'supervisor')
        Duration timeout = Duration.create(5l, TimeUnit.SECONDS)
        final ActorRef child = Await.result(
                ask(supervisorActor, new Props(BoomActor.class), 5000), timeout) as ActorRef

        child.tell(123, super.testActor())
        assertFalse(child.isTerminated())
    }

    @Test
    def void supervisorActorTestProbe() {
        ActorRef supervisorActor = system.actorOf(new Props(SupervisorActor.class), 'supervisorTP')
        final ActorRef child = Await.result(
                ask(supervisorActor, new Props(BoomActor.class), 5000), Duration.create(5l, TimeUnit.SECONDS)
        ) as ActorRef
        final TestProbe probe = new TestProbe(system)
        probe.watch(child)
        child.tell('do something', super.testActor())
        probe.expectMsg(new Terminated(child, true, true))
    }
}
