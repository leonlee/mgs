package org.fun.mgs.sup

import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.japi.Function
import scala.concurrent.duration.Duration

import java.util.concurrent.TimeUnit

/**
 * User: Leon Lee <mail.lgq@gmail.com>
 * Date: 13-1-6
 */
class StateStrategy extends OneForOneStrategy {
    StateStrategy() {
        super(10, Duration.create(10l, TimeUnit.SECONDS), new Function<Throwable, SupervisorStrategy.Directive>() {

            @Override
            SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                return restart()
            }
        })
    }
}
