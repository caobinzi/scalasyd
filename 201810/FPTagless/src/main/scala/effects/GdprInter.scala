import cats._
import data._
import org.atnos.eff._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._
import cats.effect.IO

object GdprIter extends GdprOp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global
  override def deleteUser(id: Int) = IO.fromFuture(
    IO(Future(UserData(Nil)))
  )
  override def deleteUserEmail(id:   String) = IO.fromFuture(IO(Future(UserData(Nil))))
  override def retrieveUser(id:      Int)    = IO.fromFuture(IO(Future(UserData(Nil))))
  override def retrieveUserEmail(id: String) = IO.fromFuture(IO(Future(UserData(Nil))))
}
