import cats._
import data._
import org.atnos.eff._
import cats.implicits._

object LogIter {

  val nt = new (LogOp ~> Id) {

    def apply[A](fa: LogOp[A]): Id[A] =
      fa match {
        case Info(s) => println(s)
      }
  }

}
