package fp.data

trait ConsoleOp[F[_]] {
  def printStrLn(s: String): F[Unit]
}
