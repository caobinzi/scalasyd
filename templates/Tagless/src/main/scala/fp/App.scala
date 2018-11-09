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
      F1[_]: FlatMap, //Tagless effect 1
      F2[_]: FlatMap, //Tagless effect 2
      F3[_]: FlatMap, //Tagless effect 3
      R:     MemberIn[LogOp, ?]: MemberIn[Option, ?]: MemberIn[F1, ?]: MemberIn[F2, ?]: MemberIn[F3, ?]
  ](
      gdpr:    GdprOp[F1],
      user:    UserOp[F2],
      console: ConsoleOp[F3]
  ): Eff[R, Unit] = {
    for {
      a      <- fromOption(Some("test"))
      email  <- gdpr.retrieveUserEmail(1)
      info   <- gdpr.deleteUser(1)
      _      <- console.printStrLn(s"Found ${info.data}")
      result <- user.sendUserData(email, info)
      _      <- console.printStrLn(s"Finished Delete")
      _      <- Warn(s"ok") //This one is for customized effect
    } yield ()
  }

  type Stack = Fx.fx4[IO, Eval, Option, LogOp]

  val app = program[IO, Eval, Eval, Stack](
    GdprIter,
    UserIter,
    ConsoleIter
  )

  app.runEval.runOption
    .runEffect(LogIter.ioNt)
    .unsafeRunSync

}
