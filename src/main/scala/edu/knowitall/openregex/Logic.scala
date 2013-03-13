package edu.knowitall.openregex

import edu.washington.cs.knowitall.logic.LogicExpression
import edu.washington.cs.knowitall.logic.Expression.Arg

import com.google.common.base.{ Function => GuavaFunction }

class Logic[E](val logex: LogicExpression[E]) {
  def apply(entity: E) = logex.apply(entity)
}

object Logic {
  def compile[E](string: String, factory: String=>Arg[E]) = {
    val logex = LogicExpression.compile(string, new GuavaFunction[String, Arg[E]]() {
      override def apply(string: String) = factory(string)
    })

    new Logic(logex)
  }
}
