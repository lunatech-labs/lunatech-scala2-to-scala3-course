/**
 * Copyright © 2016-2020 Lightbend, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * NO COMMERCIAL SUPPORT OR ANY OTHER FORM OF SUPPORT IS OFFERED ON
 * THIS SOFTWARE BY LIGHTBEND, Inc.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package akkapi.cluster

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}

import scala.concurrent.duration.{Duration, FiniteDuration, MILLISECONDS => Millis}
import scala.jdk.CollectionConverters._

object Settings {

  def apply(): Settings = {
    val baseConfig = ConfigFactory.load()

    val nodeHostname = baseConfig.getString("cluster-node-configuration.node-hostname")

    implicit val config = baseConfig.withValue("akka.remote.artery.canonical.hostname", ConfigValueFactory.fromAnyRef(nodeHostname))

    new Settings()
  }
}

class Settings(implicit val config: Config) {

  private val clusterNodeConfig = config.getConfig("cluster-node-configuration")

  val actorSystemName = s"pi-${config.getString("cluster-node-configuration.cluster-id")}-system"

  private val clusterId = clusterNodeConfig.getString("cluster-id")
}
