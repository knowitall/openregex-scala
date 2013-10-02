package edu.knowitall.openregex

import edu.washington.cs.knowitall.logic.LogicExpression
import edu.washington.cs.knowitall.logic.Expression.Arg

import com.google.common.base.{ Function => GuavaFunction }

class Logic[E](val logex: LogicExpression[E]) {
  def apply(entity: E) = logex.apply(entity)
}

object Logic {
  def compile[E](string: String, factory: String=>Arg[E]) = {
    val logex = new LogicExpression[E](string) {
      override def factory(string: String) = factory(string)
    }

    new Logic(logex)
  }

  def compile[E](string: String, factory: String=>Arg[E], readToken: String=>String) = {
    val logex = new LogicExpression[E](string) {
      override def factory(string: String) = factory(string)
      override def readToken(string: String) = readToken(string)
    }

    new Logic(logex)
  }
}
