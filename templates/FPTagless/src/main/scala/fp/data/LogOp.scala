package fp.data
import scala.language.higherKinds
import scala.language.implicitConversions
import cats._
import data._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.all._
import cats.implicits._
import org.atnos.eff._, interpret._
import IntoPoly._

sealed trait LogOp[A]

case class Info(s:  String) extends LogOp[Unit]
case class Warn(s:  String) extends LogOp[Unit]
case class Debug(s: String) extends LogOp[Unit]
