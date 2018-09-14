import cats._
import data._
import org.atnos.eff._
import cats.implicits._

object IvrIter {
  import org.atnos.eff._
  type WriterString[A] = Writer[String, A]
  def myDate = new java.util.Date
  def readLine(): String = scala.io.StdIn.readLine()

  def ask(s: String): String = {
    println(s)
    Thread.sleep(2000)
    readLine()
  }
  def tell(msg: String) = println(msg)

  def check(msg: String): Result = msg match {
    case "0" => Stop
    case "1" => RequestAgain
    case _   => Continue
  }

  implicit val nt = new (IvrOp ~> Id) {

    def apply[A](fa: IvrOp[A]): Id[A] =
      fa match {
        case Request(prompt) => ask(prompt)
        case Response(msg)   => tell(msg)
        case CheckInput(msg) => check(msg)

      }
  }

}
