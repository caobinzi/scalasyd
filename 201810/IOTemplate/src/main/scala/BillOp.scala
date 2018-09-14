import org.atnos.eff._
import cats._
import cats.data._
import cats.syntax.all._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.concurrent._

sealed trait BillOp[A]

case class CheckBill(bill: String) extends BillOp[IO[Boolean]]

case class UpdateBill(bill: String, status: String) extends BillOp[IO[String]]

object BillOp {

  type _billOp[R] = BillOp |= R
  import scala.concurrent.ExecutionContext.Implicits.global

  def updateBill = Future {
    println("Update Start")
    (1 to 5).foreach { x =>
      Thread.sleep(1000)
      val threadId = Thread.currentThread().getId();
      println(
        s"Updating bills -> waiting 1 seconds and with thread id ${threadId}")
    }
    println("Update Finished")
    "done"
  }

  def checkBill = Future {
    println("Checking Start")
    (1 to 5).foreach { x =>
      Thread.sleep(1000)
      val threadId = Thread.currentThread().getId();
      println(
        s"Checking bills -> waiting 1 seconds and with thread id ${threadId}")
    }
    println("Checking Finished")
    true
  }

  val ntTask = new (BillOp ~> Id) {

    def apply[A](fa: BillOp[A]): A =
      fa match {
        case UpdateBill(bill, card) =>
          println(">>>Translating update bill")
          (IO.fromFuture(IO(checkBill)), IO.fromFuture(IO(updateBill)))
            .parMapN((_, _) => "OK now")

        case CheckBill(bill) =>
          println(">>>Translating check bill")
          IO.fromFuture(IO(checkBill))
      }
  }
}
