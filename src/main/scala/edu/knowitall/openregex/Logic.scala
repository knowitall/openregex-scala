package edu.knowitall.openregex

import edu.washington.cs.knowitall.logic.LogicExpression
import edu.washington.cs.knowitall.logic.LogicExpressionParser
import edu.washington.cs.knowitall.logic.Expression.Arg

import com.google.common.base.{ Function => GuavaFunction }

case class Logic[E](val logex: LogicExpression[E]) {
  def apply(entity: E) = logex.apply(entity)
}

case class LogicParser[E](val parser: LogicExpressionParser[E]) {
  def apply(string: String) = new Logic(parser(string))
}

object Logic {
  def compile[E](string: String, factoryF: String=>Arg[E]): Logic[E] = {
    this.parser(factoryF).apply(string)
  }

  def parser[E](factoryF: String=>Arg[E]): LogicParser[E] = {
    val parser = new LogicExpressionParser[E] {
      override def factory(string: String) = factoryF(string)
    }
    new LogicParser(parser)
  }

  def parser[E](factoryF: String=>Arg[E], readToken: String=>String): LogicParser[E] = {
    val parser = new LogicExpressionParser[E] {
      override def factory(string: String) = factoryF(string)
      override def readToken(string: String) = readToken(string)
    }
    new LogicParser(parser)
  }
}
