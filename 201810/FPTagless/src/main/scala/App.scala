import cats._
import cats.data._
import cats.implicits._
import cats.instances._

object MyApp extends App {
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def program[F[_]: Monad](
      gdpr: GdprOp[F],
      user: UserOp[F],
      log:  LogOp[F]
  ): F[Unit] = {
    for {
      data   <- gdpr.deleteUserEmail("testuser@abcd.com")
      _      <- log.info(s"Found ${data.info}")
      result <- user.sendUserData(data)
      _      <- log.info(s"Finished Delete")
    } yield ()
  }
  program(
    GdprIter,
    UserIter,
    LogIter
  )

  println("Getting here")
  Thread.sleep(1000000)

  //   .runFuture

}
