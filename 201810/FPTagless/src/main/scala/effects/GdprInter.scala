import cats._
import data._
import org.atnos.eff._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._

object GdprIter extends GdprOp[Future] {

  import scala.concurrent.ExecutionContext.Implicits.global
  override def deleteUser(id:        Int)    = (Future(UserData(Nil)))
  override def deleteUserEmail(id:   String) = (Future(UserData(Nil)))
  override def retrieveUser(id:      Int)    = (Future(UserData(Nil)))
  override def retrieveUserEmail(id: String) = (Future(UserData(Nil)))
}
