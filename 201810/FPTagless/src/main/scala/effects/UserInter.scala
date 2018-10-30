import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._

object UserIter extends UserOp[Eval] {

  override def sendUserData(data: UserData) = Eval.later {
    Thread.sleep(10000)
    println(s"Updating Data now:${data}")
    true
  }

}
