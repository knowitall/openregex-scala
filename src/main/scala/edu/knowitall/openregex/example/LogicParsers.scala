package edu.knowitall.openregex.example

import scala.Array.canBuildFrom

import edu.knowitall.openregex.Logic
import edu.washington.cs.knowitall.logic.Expression.Arg

object LogicParsers {

  // build a logic expression against the public members of a class
  def reflection[T] = {
    Logic.parser[T] { (string: String) =>
      val Array(base, quotedValue) = string.split("=").map(_.trim)
      val compare = Common.unquote(quotedValue)
      (t: T) => {
        val fieldValue = Common.publicValue(t, base).toString
        compare(fieldValue)
      }
    }
  }
}
