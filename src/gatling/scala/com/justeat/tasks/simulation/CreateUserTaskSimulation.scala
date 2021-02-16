package com.justeat.tasks.simulation

import com.justeat.tasks.TestConfig._
import com.justeat.tasks.scenarios.{AuthScenarios, TasksScenarios}
import io.gatling.core.Predef.{exec, _}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder

class CreateUserTaskSimulation extends Simulation {

  var jwt = ""

  val getToken: ScenarioBuilder = scenario("GET /auth/login")
    .group("Auth") {
      exec(AuthScenarios.getAuthToken)
        .exec(session => {
          jwt = getValueFromSession(session, "accessToken")
          session
        })
    }

  val getUserTasksSimulation: ScenarioBuilder = scenario("GET /tasks")
    .group("GET /tasks") {
      exec(_.setAll(("accessToken", jwt)))
        .exec(TasksScenarios.getUserTasks)
    }

  val createUserTasksSimulation: ScenarioBuilder = scenario("POST /tasks")
    .group("POST /tasks") {
      exec(_.setAll(("accessToken", jwt)))
        .exec(TasksScenarios.createUserTasks)
    }

  /** *
   * Stage One:
   * The number of requests slowly increases from 0 to 5 rps during the first minute.
   *
   * Stage Two:
   * Keep on the level of 5rps for 15 seconds
   * Increase the tasks by 15 and keeps level of 5rps for 15 seconds
   * Increase level from 5rps to 10rps for 15 seconds
   *
   * Stage Three:
   * Drop to 0rps in 15sec
   */

  setUp(
    getToken.inject(atOnceUsers(1)),
    getUserTasksSimulation.inject(
      constantUsersPerSec(300).during(60)
    ).throttle(
      reachRps(5).in(60),
      holdFor(15),
      jumpToRps(10),
      holdFor(15),
      jumpToRps(0)
    )
//    , createUserTasksSimulation.inject(atOnceUsers(1))
  )

    .protocols(getHttpProtocol())
    .assertions(global.responseTime.mean.lte(500))
    .assertions(global.failedRequests.percent.lte(2))

}
