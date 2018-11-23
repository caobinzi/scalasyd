package fp.data
import org.atnos.eff._

sealed trait LogOp[A]

case class Info(s:  String) extends LogOp[Unit]
case class Warn(s:  String) extends LogOp[Unit]
case class Error(s: String) extends LogOp[Unit]
case class Debug(s: String) extends LogOp[Unit]

object LogOp {
  type _logOp[R] = LogOp |= R
}
