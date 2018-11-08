trait UserOp[F[_]] {
  def sendUserData(email: String, data: UserData): F[Boolean]
}
