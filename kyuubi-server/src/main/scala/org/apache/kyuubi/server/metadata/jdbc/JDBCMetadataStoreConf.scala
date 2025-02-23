/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.server.metadata.jdbc

import java.util.{Locale, Properties}

import org.apache.kyuubi.config.{ConfigEntry, KyuubiConf, OptionalConfigEntry}
import org.apache.kyuubi.config.KyuubiConf.buildConf

object JDBCMetadataStoreConf {
  final val METADATA_STORE_JDBC_DATASOURCE_PREFIX = "kyuubi.metadata.store.jdbc.datasource"

  /** Get metadata store jdbc datasource properties. */
  def getMetadataStoreJDBCDataSourceProperties(conf: KyuubiConf): Properties = {
    val datasourceProperties = new Properties()
    conf.getAllWithPrefix(METADATA_STORE_JDBC_DATASOURCE_PREFIX, "").foreach { case (key, value) =>
      datasourceProperties.put(key, value)
    }
    datasourceProperties
  }

  val METADATA_STORE_JDBC_DATABASE_TYPE: ConfigEntry[String] =
    buildConf("kyuubi.metadata.store.jdbc.database.type")
      .doc("The database type for server jdbc metadata store.<ul>" +
        " <li>DERBY: Apache Derby, JDBC driver `org.apache.derby.jdbc.AutoloadedDriver`.</li>" +
        " <li>MYSQL: MySQL, JDBC driver `com.mysql.cj.jdbc.Driver`.</li>" +
        " <li>CUSTOM: User-defined database type, need to specify corresponding JDBC driver.</li>" +
        " Note that: The JDBC datasource is powered by HiKariCP, for datasource properties," +
        " please specify them with the prefix: kyuubi.metadata.store.jdbc.datasource." +
        " For example, kyuubi.metadata.store.jdbc.datasource.connectionTimeout=10000.")
      .version("1.6.0")
      .serverOnly
      .stringConf
      .transform(_.toUpperCase(Locale.ROOT))
      .createWithDefault("DERBY")

  val METADATA_STORE_JDBC_DATABASE_SCHEMA_INIT: ConfigEntry[Boolean] =
    buildConf("kyuubi.metadata.store.jdbc.database.schema.init")
      .doc("Whether to init the JDBC metadata store database schema.")
      .version("1.6.0")
      .serverOnly
      .booleanConf
      .createWithDefault(true)

  val METADATA_STORE_JDBC_DRIVER: OptionalConfigEntry[String] =
    buildConf("kyuubi.metadata.store.jdbc.driver")
      .doc("JDBC driver class name for server jdbc metadata store.")
      .version("1.6.0")
      .serverOnly
      .stringConf
      .createOptional

  val METADATA_STORE_JDBC_URL: ConfigEntry[String] =
    buildConf("kyuubi.metadata.store.jdbc.url")
      .doc("The JDBC url for server JDBC metadata store. By default, it is a DERBY in-memory" +
        " database url, and the state information is not shared across kyuubi instances. To" +
        " enable high availability for multiple kyuubi instances," +
        " please specify a production JDBC url.")
      .version("1.6.0")
      .serverOnly
      .stringConf
      .createWithDefault("jdbc:derby:memory:kyuubi_state_store_db;create=true")

  val METADATA_STORE_JDBC_USER: ConfigEntry[String] =
    buildConf("kyuubi.metadata.store.jdbc.user")
      .doc("The username for server JDBC metadata store.")
      .version("1.6.0")
      .serverOnly
      .stringConf
      .createWithDefault("")

  val METADATA_STORE_JDBC_PASSWORD: ConfigEntry[String] =
    buildConf("kyuubi.metadata.store.jdbc.password")
      .doc("The password for server JDBC metadata store.")
      .version("1.6.0")
      .serverOnly
      .stringConf
      .createWithDefault("")
}
