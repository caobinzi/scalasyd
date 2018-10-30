trait LogOp[F[_]] {
  def info(s: String): F[Unit]

}
