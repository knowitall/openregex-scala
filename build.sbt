organization := "edu.washington.cs.knowitall"

name := "openregex-scala"

version := "1.1.1-SNAPSHOT"

scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.10.2", "2.9.3")

resolvers ++= Seq()

libraryDependencies ++= Seq("edu.washington.cs.knowitall" % "openregex" % "1.1.0",
    "com.google.code.findbugs" % "jsr305" % "2.0.1",
    "edu.washington.cs.knowitall.common-scala" %% "common-scala" % "1.1.2",
    "junit" % "junit" % "4.11",
    "org.scalacheck" %% "scalacheck" % "1.10.1" % "test",
    "org.specs2" % "specs2" % "1.12.3" % "test" cross CrossVersion.binaryMapped {
      case "2.9.3" => "2.9.2"
      case "2.10.2" => "2.10"
      case x => x
    })

licenses := Seq("LGPL" -> url("http://www.gnu.org/licenses/lgpl.html"))

homepage := Some(url("http://github.com/knowitall/openregex-scala"))

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <scm>
    <url>https://github.com/knowitall/openregex-scala</url>
    <connection>scm:git://github.com/knowitall/openregex-scala.git</connection>
    <developerConnection>scm:git:git@github.com:knowitall/openregex-scala.git</developerConnection>
    <tag>HEAD</tag>
  </scm>
  <developers>
   <developer>
      <name>Michael Schmitz</name>
    </developer>
  </developers>)
