package fp.data

trait ConsoleOp[F]

case class PrintStrLn(s: String) extends ConsoleOp[Unit]
