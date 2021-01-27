package com.justeat.tasks.scenarios

import io.gatling.core.Predef.{jsonPath, _}
import io.gatling.http.Predef.{http, status, _}
import io.gatling.http.request.builder.HttpRequestBuilder

object AuthScenarios {

  def getAuthToken: HttpRequestBuilder = {
    http("POST /auth/login")
      .post("/auth/login")
      .body(StringBody(
        "{" +
          "\"email\": \"test@test.com\"," +
          "\"password\": \"4nak1n\"" +
          "}".stripMargin)).asJson
      .check(jsonPath("$.access_token").saveAs("accessToken"),
        status.is(_ => 201))
  }

}
