package com.justeat.tasks.simulation

import com.justeat.tasks.TestConfig._
import com.justeat.tasks.scenarios.{AuthScenarios, TasksScenarios}
import io.gatling.core.Predef.{exec, _}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder

class GetUserTaskSimulation extends Simulation {

  var jwt = ""

  val getUserTasksSimulation: ScenarioBuilder = scenario("GET /tasks")
    .group("Just Eat Challenge") {
      exec(AuthScenarios.getAuthToken)
        .exec(session => {
          jwt = getValueFromSession(session, "accessToken")
          session
        }).exec(_.setAll(("accessToken", jwt)))
        .exec(TasksScenarios.getUserTasks)
    }

  setUp(
    getUserTasksSimulation.inject(
      rampUsers(300).during(60)
    )
  )

    .protocols(getHttpProtocol())
    .assertions(global.responseTime.mean.lte(500))
    .assertions(global.failedRequests.percent.lte(2))

}
