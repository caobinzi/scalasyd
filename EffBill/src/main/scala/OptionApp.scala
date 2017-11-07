import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object EffBasicOptionApp extends App {

  import org.atnos.eff._
  import IvrOp._
  import BankOp._
  import BillOp._
  import EffHelper._
  import LogHelper._

  def checkInput[R: _ivrOp](input: String): Eff[R, Option[String]] =
    (CheckInput(input): Eff[R, Result]) >>= { r =>
      r match {
        case Continue     => Eff.pure(input.some)
        case Stop         => Eff.pure(None)
        case RequestAgain => askForBill[R]
      }
    }

  def askForBill[R: _ivrOp]: Eff[R, Option[String]] =
    for {
      input <- Request("Please type in your bill reference or type 0 to stop")
      bill  <- checkInput(input)
    } yield bill

  def finishCall[R: _ivrOp: _billOp](
      bill:      String,
      reference: Option[String]
  ): Eff[R, Unit] = {
    reference match {
      case Some(s) =>
        for {
          receipt <- UpdateBill(bill, s)
          _       <- Response(s"Your payment refrence is ${receipt}")
        } yield ()
      case _ =>
        Response(s"We can't process your payment, please contact the customer service")
    }

  }
  def program[R: _ivrOp: _billOp: _bankOp: _option]: Eff[R, Unit] =
    for {
      billOption <- askForBill
      bill       <- fromOption(billOption)
      _          <- Response(s"Your bill reference: ${bill}")
      card       <- Request("Please type in your credit card info ")
      _          <- Response(s"Your credit card is : ${card}, we are processing now")
      reference  <- Purchase(bill, card)
      receipt    <- finishCall(bill, reference)
    } yield ()

  type Stack = Fx.fx4[IvrOp, BillOp, BankOp, Option]
  program[Stack].runBill.runIvr.runBank.runOption.run

}
