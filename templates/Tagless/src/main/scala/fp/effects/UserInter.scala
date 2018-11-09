package fp.effects
import cats._
import data._
import org.atnos.eff._
import cats.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._
import fp.data._

object UserIter extends UserOp[Eval] {

  override def sendUserData(email: String, data: UserData) = Eval.later {
    println(s"Sleep 3 seconds")
    Thread.sleep(3000)
    println(s"Sending Data now to ${email}:${data}")
    true
  }

}
