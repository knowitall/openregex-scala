package edu.knowitall.openregex.example

import scala.Array.canBuildFrom

import edu.knowitall.openregex.Logic
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