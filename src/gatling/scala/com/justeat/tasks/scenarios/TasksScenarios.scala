package com.justeat.tasks.scenarios

import io.gatling.core.Predef.{exec, _}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, _}

object TasksScenarios {

  def getUserTasks: ChainBuilder =
    exec(http("GET /tasks")
      .get("/tasks")
      .header("Authorization", "Bearer ${accessToken}")
      .check(status.is(_ => 200)))

  def createUserTasks: ChainBuilder =
    exec(http("POST /tasks")
      .post("/tasks")
      .body(
        StringBody(
          "{\n \"title\": \"Final Interview\"\n}".stripMargin)
      ).asJson
      .header("Authorization", "Bearer ${accessToken}")
      .check(status.is(_ => 201)))

}
