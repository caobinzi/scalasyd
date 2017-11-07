import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import cats.implicits._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.interpret._
import org.atnos.eff.syntax.all._

object EffStupidLogApp extends App {

  import org.atnos.eff._
  import IvrOp._
  import BillOp._
  import BankOp._
  import EffHelper._
  import LogHelper._
  type WriterString[A]  = Writer[String, A]
  type _writerString[R] = WriterString |= R

  def getTimeStamp = s"${new java.util.Date}"
  def program[R: _ivrOp: _billOp: _bankOp: _writerString]: Eff[R, Unit] =
    for {
      _         <- tell("<<<${getTimeStamp}>>>:Request for bill reference Start")
      bill      <- Request("Please type in your bill reference ")
      _         <- tell("<<<${getTimeStamp}>>>:Request for bill reference End")
      _         <- tell("<<<${getTimeStamp}>>>:Response for bill reference Start")
      _         <- Response(s"Your bill reference: ${bill}")
      _         <- tell("<<<${getTimeStamp}>>>:Response for bill reference End")
      _         <- tell("<<<${getTimeStamp}>>>:Request for card info Start")
      card      <- Request("Please type in your credit card info ")
      _         <- tell("<<<${getTimeStamp}>>>:Request for card info End")
      _         <- tell("<<<${getTimeStamp}>>>:Response for card info Start")
      _         <- Response(s"Your credit card is : ${card}, we are processing now")
      _         <- tell("<<<${getTimeStamp}>>>:Response for card info End")
      _         <- tell("<<<${getTimeStamp}>>>:Purchase from Bank Start")
      reference <- Purchase(bill, card)
      _         <- tell("<<<${getTimeStamp}>>>:Purchase from Bank End")
      _         <- tell("<<<${getTimeStamp}>>>:Update Bill Start")
      receipt   <- UpdateBill(bill, reference.get)
      _         <- tell("<<<${getTimeStamp}>>>:Update Bill End")
      _         <- tell("<<<${getTimeStamp}>>>:Response for receipt Start")
      _         <- Response(s"Your payment refrence is ${receipt}")
      _         <- tell("<<<${getTimeStamp}>>>:Response for receipt End")
    } yield ()

  type Stack = Fx.fx4[IvrOp, BillOp, BankOp, WriterString]
  program[Stack].runBill.runIvr.runBank.runWriter.run
}
