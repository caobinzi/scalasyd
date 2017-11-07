name := "EffLiftApp"

scalaVersion := "2.12.0"

libraryDependencies += "org.atnos" % "eff-scalaz_2.12" % "4.6.1"
libraryDependencies += "org.atnos" % "eff_2.12" % "4.6.1"

scalacOptions += "-Ypartial-unification"
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
