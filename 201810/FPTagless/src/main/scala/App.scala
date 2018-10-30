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
      R:     _Option
  ](
      gdpr:     GdprOp[F1],
      user:     UserOp[F2],
      log:      LogOp[F3]
  )(implicit c: F1 |= R, q: F2 |= R, k: F3 |= R): Eff[R, Unit] = {
    import EffHelper._

    for {

      a      <- fromOption(Some("test"))
      data   <- gdpr.deleteUserEmail("testuser@abcd.com")
      _      <- log.info(s"Found ${data.info}")
      result <- user.sendUserData(data)
      _      <- log.info(s"Finished Delete")
    } yield ()
  }

  type Stack = Fx.fx3[IO, Eval, Option]

  {
    import cats.implicits._

    program[IO, Eval, Eval, Stack](
      GdprIter,
      UserIter,
      LogIter
    ).runEval.runOption.unsafeRunSync
//    Await.result(result.runSequential, 1 second)
  }

  println("Getting here")
//  Thread.sleep(1000000)

  //   .runFuture

}
