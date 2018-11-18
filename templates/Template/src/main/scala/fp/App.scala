package fp

import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.syntax.addon.cats.effect._

import fp.common._
import fp.effects._
import fp.data._

import cats._
import cats.effect.IO

import EffHelper._

object MyApp extends App {

  def program[
      R: MemberIn[LogOp, ?]: MemberIn[Gdpr, ?]: MemberIn[UserOp, ?]: MemberIn[ConsoleOp, ?]
  ]: Eff[R, Unit] = {
    for {
      email  <- RetrieveUserEmail(1)
      info   <- DeleteUser(1)
      _      <- PrintStrLn(s"Found ${info.data}")
      result <- SendUserData(email, info)
      _      <- PrintStrLn(s"Finished Delete")
      _      <- Warn(s"ok") //This one is for customized effect
    } yield ()
  }

  type Stack = Fx.fx5[IO, LogOp, Gdpr, UserOp, ConsoleOp]

  val app = program[Stack]

  app
    .runEffect(LogIter.ioNt)
    .runEffect(GdprIter.ioNt)
    .runEffect(ConsoleIter.ioNt)
    .runEffect(UserIter.ioNt)
    .unsafeRunSync

}
