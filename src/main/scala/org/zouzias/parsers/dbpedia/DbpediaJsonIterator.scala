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

import java.io.{FileReader, IOException}

import com.google.gson.stream.{JsonReader, JsonToken}
import org.apache.commons.logging.LogFactory


/**
 * Iterator of any Dbpedia entity to a map of keys to Array of String.
 *
 * The input entities can be found at: http://oldwiki.dbpedia.org/DBpediaAsTables
 *
 * TODO: Use a better type instead of Map[String, Array[String]]
 *
 * @param dbpediaJsonFileName file path to a Json dbpedia, say Person.json
 */
class DbpediaJsonIterator(dbpediaJsonFileName: String) extends Iterator[Map[String, Array[String]]]
  with AutoCloseable {

  private val logger = LogFactory.getLog("DbpediaJsonIterator")

  /* Json stream reader */
  val reader = new JsonReader(new FileReader(dbpediaJsonFileName))

  var isInit: Boolean = false

  override def hasNext: Boolean = {
    reader.hasNext
  }

  /**
   * Initialite the streaming reader
   */
  private def initReader(): Unit = {
    reader.beginObject()

    if (reader.hasNext()) {
      val props = reader.nextName()
      if (props == "properties") {
        reader.skipValue()
      }
    }

    if (reader.hasNext()) {
      val name = reader.nextName()
      if (name.compareToIgnoreCase("instances") == 0) {
        reader.beginArray()
      }
    }

    isInit = true
  }

  override def next(): Map[String, Array[String]] = {

    if ( !isInit) initReader()

    reader.beginObject()
    val id = reader.nextName() // Read the identifier of the dbpedia record, URI
    var record = readRecord(reader)

    record = record.updated("id", Array(id)) // Add an extra "id" field
    reader.endObject() // end of object
    record
  }

  /**
   * Read a Dbpedia record as map
   *
   * @return Dbpedia record as a map of String to Array[String]
   */
  private def readRecord(reader: JsonReader): Map[String, Array[String]] = {

    var record = Map.empty[String, Array[String]]
    try {

      reader.beginObject()
      while (reader.hasNext()) {
        val field = reader.nextName()

        val token: JsonToken = reader.peek()

        token match {
          case JsonToken.BEGIN_ARRAY =>
            val values = readStringArray(reader)
            record = record.updated(field, values.toArray)
          case JsonToken.STRING =>
            val value = reader.nextString()
            if (!isNull(value)) {
              record = record.updated(field, Array(value))
            }
          case JsonToken.NUMBER =>
            val valueNumber = reader.nextString()
            if (!isNull(valueNumber)) {
              record = record.updated(field, Array(valueNumber))
            }
          case JsonToken.BOOLEAN =>
            val valueBool = reader.nextString()
            if (!isNull(valueBool)) {
              record = record.updated(field, Array(valueBool))
            }
        }
      }
      reader.endObject() // end of id : {}

    } catch {
      case e: IOException => logger.error("Read array of strings exception", e)
    }

    record
  }

  def isNull(text: String): Boolean = {
    return text.compareToIgnoreCase("null") == 0 || text.isEmpty()
  }

  /**
   * Read an array of strings
   *
   * @param reader
   * @return Values of string array
   */
  private def readStringArray(reader: JsonReader): Set[String] = {
    var values = Set.empty[String]
    try {
      reader.beginArray()

      while (reader.hasNext()) {
        val value = reader.nextString()
        if (!isNull(value)) values += value
      }

      reader.endArray()
    } catch {
      case e: IOException => logger.error("Read array of strings exception", e)
    }

    values
  }

  override def close(): Unit = {
    try {
      reader.close()
    }
    catch {
      case e: IOException => logger.error("Could not close Dbpedia iterator", e)
    }
  }
}

