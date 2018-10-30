trait UserOp[F[_]] {
  def sendUserData(data: UserData): F[Boolean]
}
