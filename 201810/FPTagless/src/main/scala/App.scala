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
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def program[
      F1[_]: Monad,
      F2[_]: Monad,
      F3[_]: Monad,
      R:     _Option: MemberIn[F1, ?]: MemberIn[F2, ?]: MemberIn[F3, ?]
  ](
      gdpr: GdprOp[F1],
      user: UserOp[F2],
      log:  ConsoleOp[F3]
  ): Eff[R, Unit] = {
    import EffHelper._
    for {

      a      <- fromOption(Some("test"))
      data   <- gdpr.deleteUserEmail("testuser@abcd.com")
      _      <- log.println(s"Found ${data.info}")
      result <- user.sendUserData(data)
      _      <- log.println(s"Finished Delete")
    } yield ()
  }

  type Stack = Fx.fx3[IO, Eval, Option]

  program[IO, Eval, Eval, Stack](
    GdprIter,
    UserIter,
    ConsoleIter
  ).runEval.runOption.unsafeRunSync

  println("Getting here")

}
