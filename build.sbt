name := "finagle-hands-on"

organization := "com.github.akskscala"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "twttr.com" at "http://maven.twttr.com",
  "jboss.org" at "http://repository.jboss.org/nexus/content/groups/public/",
  "seratch-github-repo"   at "http://seratch.github.com/mvn-repo/releases",
  "sbt-idea-repo"         at "http://mpeltonen.github.com/maven/"
)

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  Seq(
    "com.twitter"             %% "finagle-core"         % "1.9.12",
    "com.twitter"             %% "finagle-http"         % "1.9.12",
    "com.twitter"             %% "finagle-stream"       % "1.9.12",
    "com.twitter"             %% "util-core"            % "1.12.8",
    "org.jboss.netty"         %  "netty"                % "3.2.7.Final",
    "net.databinder"          %% "unfiltered-filter"    % "0.5.1",
    "net.databinder"          %% "unfiltered-jetty"     % "0.5.1",
    "net.databinder"          %% "unfiltered-spec"      % "0.5.1",
    "net.databinder"          %% "unfiltered-scalate"   % "0.5.1",
    "junit"                   %  "junit"                % "4.10"   % "test",
    "org.scalatest"           %% "scalatest"            % "1.6.1"  % "test",
    "org.scala-tools.testing" %% "scalacheck"           % "1.9"    % "test"
  )
}

seq(testgenSettings :_*)

