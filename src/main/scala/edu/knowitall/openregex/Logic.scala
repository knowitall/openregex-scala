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
  private def arg[E](string: String, lambda: E=>Boolean) = {
    new Arg.Pred[E](string) {
      override def apply(e: E) = lambda(e)
    }
  }

  def compile[E](string: String, factoryF: String=>(E=>Boolean)): Logic[E] = {
    this.parser(factoryF).apply(string)
  }

  def parser[E](factoryF: String=>(E=>Boolean)): LogicParser[E] = {
    val parser = new LogicExpressionParser[E] {
      override def factory(string: String) = {
        arg(string, factoryF(string))
      }
    }
    new LogicParser(parser)
  }

  def parser[E](factoryF: String=>(E=>Boolean), readToken: String=>String): LogicParser[E] = {
    val parser = new LogicExpressionParser[E] {
      override def factory(string: String) = arg(string, factoryF(string))
      override def readToken(string: String) = readToken(string)
    }
    new LogicParser(parser)
  }
}
