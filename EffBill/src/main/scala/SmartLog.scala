import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object EffBasicLogTimesApp extends App {

  import org.atnos.eff._
  import IvrOp._
  import BillOp._
  import BankOp._
  import EffHelper._
  import LogHelper._

  def checkInput[R: _ivrOp](input: String): Eff[R, Option[String]] =
    (CheckInput(input): Eff[R, Result]) >>= { r =>
      r match {
        case Continue     => Eff.pure(input.some)
        case Stop         => Eff.pure(None)
        case RequestAgain => askBill[R]
      }
    }

  def askBill[R: _ivrOp]: Eff[R, Option[String]] =
    for {
      input <- Request("Please type in your bill reference or type 0 to stop")
      bill  <- checkInput(input)
    } yield bill

  def program[R: _ivrOp: _billOp: _bankOp: _option]: Eff[R, Unit] =
    for {
      billOption <- askBill
      bill       <- fromOption(billOption)
      cats       <- Response(s"Your bill reference: ${bill}")
      card       <- Request("Please type in your credit card info ")
      cats       <- Response(s"Your credit card is : ${card}, we are processing now")
      reference  <- Purchase(bill, card)
      receipt    <- UpdateBill(bill, "Paid")
      _          <- Response(s"Your payment refrence is ${receipt}")
    } yield ()

  type Stack = Fx.fx5[IvrOp, BillOp, BankOp, Writer[String, ?], Option]

  val (result, logs) =
    program[Stack].logTimes[BillOp].runBill.runBank.runIvr.runOption.runWriter.run
  logs.foreach(println)
}
