package edu.knowitall.openregex.example

import scala.Array.canBuildFrom

import edu.knowitall.openregex.Pattern
import edu.washington.cs.knowitall.regex.Expression.BaseExpression

object PatternParsers {
  val singleQuoteStringLiteralRegex = ("'" + """([^']*+)""" + "'").r
  val regexLiteralRegex = ("/" + """((?:[^/\\]*+(?:\\)*+(?:\\/)*+)*+)""" + "/").r

  // build a pattern against the public members of a class
  def reflection[T] = {
    Pattern.parser[T] { string: String =>
      new BaseExpression[T](string) {
        val Array(base, quotedValue) = string.split("=").map(_.trim)

        val compare = Common.unquote(quotedValue)

        override def apply(t: T): Boolean = {
          // see if we have a matching public field
          val fieldValue = Common.publicValue(t, base).toString
          compare(fieldValue)
        }
      }
    }
  }

  // build a pattern against the public members of a class
  // allow logic expressions within the token
  def reflectionWithLogic[T] = {
    val logicParser = LogicParsers.reflection[T]
    Pattern.parser[T] { tokenString: String =>
      new BaseExpression[T](tokenString) {
        val logic = logicParser(tokenString)
        override def apply(t: T): Boolean = {
          logic(t)
        }
      }
    }
  }
}