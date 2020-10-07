package org.xanho.nlp.ops

import org.xanho.nlp.Constants
import org.xanho.proto.nlp

import scala.language.implicitConversions

trait NlpCharOps {

  implicit def nlpCharOps(char: Char): NlpChar =
    new NlpChar(char)

}

object NlpCharOps extends NlpCharOps

class NlpChar(val char: Char) extends AnyVal {

  def maybePunctuationToken: Option[nlp.Token] =
    char match {
      case Constants.Period => Some(Constants.PeriodToken)
      case Constants.ExclamationMark => Some(Constants.ExclamationMarkToken)
      case Constants.QuestionMark => Some(Constants.QuestionMarkToken)
      case _ => None
    }

  def maybeLineBreakToken: Option[nlp.Token] =
    char match {
      case Constants.LineBreak => Some(Constants.LineBreakToken)
      case _ => None
    }

  def maybeSpaceToken: Option[nlp.Token] =
    char match {
      case Constants.Space => Some(Constants.SpaceToken)
      case _ => None
    }

}