object MyApp extends App {

  def deleteUser(id: String): Boolean = {
    Log.debug(s"Delete user ${id}")
    val db = DB("jdbc:postgres://locahost:5432/user_db")
    val result = db.runQuery(
      s"""
      DELETE FROM 
          user_table 
      WHERE 
          id = ${id}
      """
    )
    Log.debug(s"Delete user ${id}, return ${result}")
    result
  }

}
