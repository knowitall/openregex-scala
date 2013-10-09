package edu.knowitall.openregex

import scala.collection.JavaConverters._
import com.google.common.base.{ Function => GuavaFunction }
import edu.knowitall.collection.immutable.Interval
import edu.washington.cs.knowitall.regex.Expression.BaseExpression
import edu.washington.cs.knowitall.regex.RegularExpression
import edu.washington.cs.knowitall.regex.RegularExpressionParser
import edu.washington.cs.knowitall.regex.{ Match => JavaMatch }
import edu.washington.cs.knowitall.regex.Expression
import edu.washington.cs.knowitall.logic.Expression.Arg

object LogicParsers {

  // build a logic expression against the public members of a class
  def reflection[T] = {
    Logic.parser[T] { string: String =>
      new Arg.Pred[T](string) {
        val Array(base, quotedValue) = string.split("=").map(_.trim)

        val compare = Common.unquote(quotedValue)

        override def apply(t: T): Boolean = {
          val fieldValue = Common.publicValue(t, base).toString
          compare(fieldValue)
        }
      }
    }
  }
}