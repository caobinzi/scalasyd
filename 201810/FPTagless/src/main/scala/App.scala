import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.atnos.eff.syntax.addon.cats.effect._

import cats._
import cats.data._
import cats.effect.IO

import cats.instances._

object MyApp extends App {

  def program[
      F1[_]: FlatMap,
      F2[_]: FlatMap,
      F3[_]: FlatMap,
      R:     MemberIn[LogOp, ?]: MemberIn[Option, ?]: MemberIn[F1, ?]: MemberIn[F2, ?]: MemberIn[F3, ?]
  ](
      gdpr:    GdprOp[F1],
      user:    UserOp[F2],
      console: ConsoleOp[F3]
  ): Eff[R, Unit] = {
    import EffHelper._
    for {

      a      <- fromOption(Some("test"))
      data   <- gdpr.deleteUserEmail("testuser@abcd.com")
      _      <- console.printStrLn(s"Found ${data.info}")
      result <- user.sendUserData(data)
      _      <- console.printStrLn(s"Finished Delete")
      _      <- Warn(s"ok")
    } yield ()
  }

  type Stack = Fx.fx4[IO, Eval, Option, LogOp]
  import IOHelper._

  program[IO, Eval, Eval, Stack](
    GdprIter,
    UserIter,
    ConsoleIter
  ).runEval.runOption.runIO(LogIter.nt).unsafeRunSync

}
