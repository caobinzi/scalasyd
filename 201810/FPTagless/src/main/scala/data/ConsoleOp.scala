trait ConsoleOp[F[_]] {
  def println(s: String): F[Unit]

}
