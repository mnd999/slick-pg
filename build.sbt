lazy val commonSettings = Seq(
  organizationName := "slick-pg",
  organization := "com.github.tminglei",
  name := "slick-pg",
  version := "0.15.0-M4",

  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.11.8", "2.12.1"),
  scalacOptions ++= Seq("-deprecation", "-feature",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:existentials"),

  resolvers += Resolver.mavenLocal,
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += "spray" at "http://repo.spray.io/",
  //    publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository"))),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  makePomConfiguration ~= { _.copy(configurations = Some(Seq(Compile, Runtime, Optional))) },

  pomExtra :=
    <url>https://github.com/tminglei/slick-pg</url>
      <licenses>
        <license>
          <name>BSD-style</name>
          <url>http://www.opensource.org/licenses/bsd-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:tminglei/slick-pg.git</url>
        <connection>scm:git:git@github.com:tminglei/slick-pg.git</connection>
      </scm>
      <developers>
        <developer>
          <id>tminglei</id>
          <name>Minglei Tu</name>
          <timezone>+8</timezone>
        </developer>
      </developers>

)

def mainDependencies(scalaVersion: String) = {
  val extractedLibs = CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4" % "provided")
    case _ =>
      Seq()
  }
  Seq (
    "org.scala-lang" % "scala-reflect" % scalaVersion,
    "com.typesafe.slick" %% "slick" % "3.2.0-M2",
    "org.postgresql" % "postgresql" % "9.4.1212",
    "org.slf4j" % "slf4j-simple" % "1.7.21" % "provided",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  ) ++ extractedLibs
}

lazy val slickPgCore = Project(id = "slick-pg_core", base = file("./core"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_core",
    description := "Slick extensions for PostgreSQL - Core",
    libraryDependencies := mainDependencies(scalaVersion.value)
  )
)

lazy val slickPgProject = Project(id = "slick-pg", base = file("."),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg",
    description := "Slick extensions for PostgreSQL",
    libraryDependencies := mainDependencies(scalaVersion.value)
  )
).dependsOn (slickPgCore)
  .aggregate (slickPgCore, slickPgJoda, slickPgJson4s, slickPgJts, slickPgPlayJson, slickPgSprayJson, slickPgCirceJson, slickPgArgonaut)

lazy val slickPgJoda = Project(id = "slick-pg_joda-time", base = file("./addons/joda-time"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_joda-time",
    description := "Slick extensions for PostgreSQL - joda time module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "joda-time" % "joda-time" % "2.9.6",
      "org.joda" % "joda-convert" % "1.8.1"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgJson4s = Project(id = "slick-pg_json4s", base = file("./addons/json4s"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_json4s",
    description := "Slick extensions for PostgreSQL - json4s module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "org.json4s" %% "json4s-ast" % "3.5.0",
      "org.json4s" %% "json4s-core" % "3.5.0",
      "org.json4s" %% "json4s-native" % "3.5.0" % "test"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgJts = Project(id = "slick-pg_jts", base = file("./addons/jts"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_jts",
    description := "Slick extensions for PostgreSQL - jts module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "com.vividsolutions" % "jts" % "1.13"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgPlayJson = Project(id = "slick-pg_play-json", base = file("./addons/play-json"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_play-json",
    description := "Slick extensions for PostgreSQL - play-json module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "com.typesafe.play" %% "play-json" % "2.6.0-M1"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgSprayJson = Project(id = "slick-pg_spray-json", base = file("./addons/spray-json"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_spray-json",
    description := "Slick extensions for PostgreSQL - spray-json module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "io.spray" %%  "spray-json" % "1.3.2"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgCirceJson = Project(id = "slick-pg_circe-json", base = file("./addons/circe-json"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_circe-json",
    description := "Slick extensions for PostgreSQL - circe module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "io.circe" %% "circe-core" % "0.6.1",
      "io.circe" %% "circe-generic" % "0.6.1",
      "io.circe" %% "circe-parser" % "0.6.1"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgArgonaut = Project(id = "slick-pg_argonaut", base = file("./addons/argonaut"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_argonaut",
    description := "Slick extensions for PostgreSQL - argonaut module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "io.argonaut" %% "argonaut" % "6.2-RC2"
    )
  )
) dependsOn (slickPgCore)

lazy val slickPgJawn = Project(id = "slick-pg_jawn", base = file("./addons/jawn"),
  settings = Defaults.coreDefaultSettings ++ commonSettings ++ Seq(
    name := "slick-pg_jawn",
    description := "Slick extensions for PostgreSQL - jawn module",
    libraryDependencies := mainDependencies(scalaVersion.value) ++ Seq(
      "org.spire-math" %% "jawn-ast" % "0.10.4"
    )
  )
) dependsOn (slickPgCore)



