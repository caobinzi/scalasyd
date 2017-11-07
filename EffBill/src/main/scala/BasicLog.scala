import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object EffBasicLogApp extends App {

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

  type WriterString[A]  = Writer[String, A]
  type _writerString[R] = WriterString |= R

  def program[R: _ivrOp: _billOp: _bankOp: _option: _writerString]: Eff[R, Unit] =
    for {
      _          <- tell("A customer called in, let's start ")
      billOption <- askBill
      bill       <- fromOption(billOption)
      _          <- Response(s"Your bill reference: ${bill}")
      card       <- Request("Please type in your credit card info ")
      _          <- Response(s"Your credit card is : ${card}, we are processing now")
      reference  <- Purchase(bill, card)
      receipt    <- finishCall(bill, reference)
      _          <- Response(s"Your payment refrence is ${receipt}")
      _          <- tell("We have finished everyting")
    } yield ()

  type Stack = Fx.fx5[IvrOp, BankOp, BillOp, Option, WriterString]
  program[Stack].runBill.runIvr.runBank.runOption.runWriter.run

}
