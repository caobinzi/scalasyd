import cats._
import data._
import org.atnos.eff._
import cats.implicits._

object GdprIter {

  implicit val nt = new (GdprOp ~> Id) {

    def apply[A](fa: GdprOp[A]): Id[A] =
      fa match {
        case DeleteUser(id)        => UserData(Nil)
        case DeleteUserEmail(id)   => UserData(Nil)
        case RetrieveUser(id)      => UserData(Nil)
        case RetrieveUserEmail(id) => UserData(Nil)
      }
  }

}
