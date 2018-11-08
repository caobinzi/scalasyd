package fp.data

sealed trait LogOp[A]

case class Info(s:  String) extends LogOp[Unit]
case class Warn(s:  String) extends LogOp[Unit]
case class Debug(s: String) extends LogOp[Unit]
