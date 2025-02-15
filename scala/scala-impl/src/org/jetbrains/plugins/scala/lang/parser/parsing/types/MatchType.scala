package org.jetbrains.plugins.scala.lang.parser.parsing.types

import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes
import org.jetbrains.plugins.scala.lang.parser.ScalaElementType
import org.jetbrains.plugins.scala.lang.parser.parsing.ParsingRule
import org.jetbrains.plugins.scala.lang.parser.parsing.builder.ScalaPsiBuilder

/**
 * [[MatchType]] ::= [[InfixType]] [[MatchTypeSuffix]]
 */
object MatchType extends ParsingRule {
  override def parse(implicit builder: ScalaPsiBuilder): Boolean = {
    val marker = builder.mark()

    if (!InfixType()) {
      marker.rollbackTo()
      false
    } else // todo: handle indention
      builder.getTokenType match {
        case ScalaTokenTypes.kMATCH =>
          builder.advanceLexer()
          MatchTypeSuffix()
          marker.done(ScalaElementType.MATCH_TYPE)
          true
        case _ =>
          marker.rollbackTo()
          false
      }
  }
}
