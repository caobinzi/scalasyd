package fp.data
import org.atnos.eff._

trait ConsoleOp[F]

case class PrintStrLn(s: String) extends ConsoleOp[Unit]

object ConsoleOp {
  type _consoleOp[R] = ConsoleOp |= R
}
