package edu.knowitall.openregex.example

object Common {
  val singleQuoteStringLiteralRegex = ("'" + """([^']*+)""" + "'").r
  val regexLiteralRegex = ("/" + """((?:[^/\\]*+(?:\\)*+(?:\\/)*+)*+)""" + "/").r

  /**
   * Parse quotes and return a matcher for the string.
   */
  def unquote(string: String): String=>Boolean =
    string match {
      case singleQuoteStringLiteralRegex(string) =>
        (that: String) => that == string
      case regexLiteralRegex(string) =>
        val unescapedString = string.replace("""\\""", """\""").replace("""\/""", "/")
        val regex = unescapedString.r
        (that: String) => {
          regex.pattern.matcher(that).matches()
        }
      case _ => throw new IllegalArgumentException("Value not enclosed in quotes (') or (/): " + string)
    }

  /**
   * Look for a field or 0-argument method on t.
   */
  def publicValue[T](t: T, fieldName: String): Any = {
    val field = scala.util.control.Exception.catching(classOf[NoSuchFieldException]) opt t.getClass.getField(fieldName)
    field match {
      case Some(field) => field.get(t)
      case None =>
        // see if we have a matching public method
        val method = scala.util.control.Exception.catching(classOf[NoSuchMethodException]) opt t.getClass.getMethod(fieldName)
        method match {
          case Some(method) if method.getParameterTypes.isEmpty => method.invoke(t).toString
          case None => throw new IllegalArgumentException("No such field or method: " + fieldName)
        }
    }
  }
}