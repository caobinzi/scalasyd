import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.atnos.eff.syntax.addon.cats.effect._

import cats._
import cats.data._

import cats.instances._

object MyApp extends App {
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def program[F[_]: Monad, R: F |= ?](
      gdpr: GdprOp[F],
      user: UserOp[F],
      log:  LogOp[F]
  ): Eff[R, Unit] = {
    import EffHelper._
    for {
      data   <- gdpr.deleteUserEmail("testuser@abcd.com")
      _      <- log.info(s"Found ${data.info}")
      result <- user.sendUserData(data)
      _      <- log.info(s"Finished Delete")
    } yield ()
  }
  type Stack = Fx.fx1[Future]

  {
    import cats.implicits._

    import scala.concurrent._, duration._
    implicit val scheduler = ExecutorServices.schedulerFromGlobalExecutionContext
    import org.atnos.eff.syntax.future._
    program[Future, Stack](
      GdprIter,
      UserIter,
      LogIter
    )
//    Await.result(result.runSequential, 1 second)
  }

  println("Getting here")
//  Thread.sleep(1000000)

  //   .runFuture

}
