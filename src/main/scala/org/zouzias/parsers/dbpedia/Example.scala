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
package org.zouzias.parsers.dbpedia

import java.io.{File, PrintWriter}

import org.apache.commons.logging.LogFactory

import spray.json._
import DefaultJsonProtocol._

object Example {

  private val logger = LogFactory.getLog("Example")

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {

    val entity = "Food"
    val filePath = s"data/${entity}.json"

    val iter = new DbpediaJsonIterator(filePath)

    // scalastyle:off println
    val pw = new PrintWriter(new File(s"${entity}.json" ))
    iter.foreach { case json =>
      pw.println(json.toJson)
    }
    pw.close()
    // scalastyle:on println
  }

}
