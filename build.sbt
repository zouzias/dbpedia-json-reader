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

name := "dbpedia-json-reader"
organization := "org.zouzias"
version := "0.0.1"
scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.10.6", "2.11.8")
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
homepage := Some(url("https://github.com/zouzias/dbpedia-json-reader"))

scalacOptions ++= Seq("-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-language:implicitConversions")

javacOptions ++= Seq("-Xlint")


// scalastyle:off
val specs2_core               = "org.specs2"                     %% "specs2-core"             % "2.3.11" % "test"
val scala_check               = "org.scalacheck"                 %% "scalacheck"              % "1.12.2" % "test"
val scalatest                 = "org.scalatest"                  %% "scalatest"                % "2.2.6" % "test"

val typesafe_config           = "com.typesafe"                   % "config"                    % "1.2.1"

val commons_logging           = "commons-logging"                % "commons-logging"           % "1.2"
val slf4j                     = "org.slf4j"                      % "slf4j-simple"              % "1.7.10"

val gson                      = "com.google.code.gson"           % "gson"                      % "2.3.1"
val spray_json                = "io.spray"                       %%  "spray-json"              % "1.3.2"

// scalastyle:on


libraryDependencies ++= Seq(
  commons_logging,
  slf4j,
  spray_json,
  gson,
  specs2_core,
  scalatest
)


lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle


parallelExecution in Test := false

// Skip tests during assembly
test in assembly := {}
