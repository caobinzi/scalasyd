import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.atnos.eff.syntax.addon.cats.effect._
import cats.implicits._
import cats.effect.IO

object MyApp extends App {

  import GdprOp._
  import UserOp._
  import EffHelper._
  import IOHelper._
  import LogOp._
  import LogHelper._

  def program[R: _gdprOp: _userOp: _logOp: _io]: Eff[R, Unit] = {
    for {
      data   <- DeleteUserEmail("testuser@abcd.com")
      _      <- Info(s"Found ${data.info}")
      result <- SendUserData(data)
      _      <- fromIO(result)
    } yield ()
  }

  type Stack = Fx.fx4[GdprOp, UserOp, LogOp, IO]

  val r = program[Stack]
    .logTimes[GdprOp]
    .logTimes[UserOp]
    .runEffect(GdprIter.nt)
    .runEffect(LogIter.nt)
    .runEffect(UserIter.nt)
    .unsafeRunSync

  println("Getting here")

  //   .runFuture

}
