package com.justeat.tasks

import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.session.Session
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocol

object TestConfig {

  def getHttpProtocol()(implicit gatlingConfiguration: GatlingConfiguration): HttpProtocol = {
    http(gatlingConfiguration).baseUrl("http://localhost:8000/api/v1")
  }

  def getValueFromSession(session: Session, attributeName: String): String = {
    session(attributeName).as[String].trim
  }

}
