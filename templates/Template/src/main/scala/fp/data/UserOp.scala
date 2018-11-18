package fp.data

trait UserOp[F]
case class SendUserData(email: String, data: UserData) extends UserOp[Boolean]
