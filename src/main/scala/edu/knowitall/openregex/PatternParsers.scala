package edu.knowitall.openregex

import scala.collection.JavaConverters._
import com.google.common.base.{ Function => GuavaFunction }
import edu.knowitall.collection.immutable.Interval
import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.regex.RegularExpression
import edu.washington.cs.knowitall.regex.RegularExpressionParser
import edu.washington.cs.knowitall.regex.{ Match => JavaMatch }
import edu.washington.cs.knowitall.regex.Expression

object PatternParsers {
  val singleQuoteStringLiteralRegex = ("'" + """([^']*+)""" + "'").r
  val regexLiteralRegex = ("/" + """((?:[^/\\]*+(?:\\)*+(?:\\/)*+)*+)""" + "/").r

  // build a pattern against the public members of a class
  def reflection[T] = {
    Pattern.parser[T] { string: String =>
      new BaseExpression[T](string) {
        val Array(base, quotedValue) = string.split("=").map(_.trim)

        val compare = quotedValue match {
            case singleQuoteStringLiteralRegex(string) =>
              (that: String) => that == string
            case regexLiteralRegex(string) =>
              val unescapedString = string.replace("""\\""", """\""").replace("""\/""", "/")
              val regex = unescapedString.r
              (that: String) => {
                regex.pattern.matcher(that).matches()
              }
            case _ => throw new IllegalArgumentException("Value not enclosed in quotes (') or (/): " + quotedValue)
          }

        override def apply(t: T): Boolean = {
          // see if we have a matching public field
          val field = scala.util.control.Exception.catching(classOf[NoSuchFieldException]) opt t.getClass.getField(base)
          val fieldValue = field match {
            case Some(field) => field.get(t).toString
            case None =>
              // see if we have a matching public method
              val method = scala.util.control.Exception.catching(classOf[NoSuchMethodException]) opt t.getClass.getMethod(base)
              method match {
                case Some(method) if method.getParameterTypes.isEmpty => method.invoke(t).toString
                case None => throw new IllegalArgumentException("No such field or method: " + base)
              }
          }

          compare(fieldValue)
        }
      }
    }
  }
}