package org.jetbrains.plugins.scala.lang.parser.parsing.types

import org.jetbrains.plugins.scala.ScalaBundle
import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes
import org.jetbrains.plugins.scala.lang.parser.ScalaElementType
import org.jetbrains.plugins.scala.lang.parser.parsing.builder.ScalaPsiBuilder

/*
 *  typeArgs ::= '[' Types ']'
 */
object TypeArgs extends TypeArgs {
  override protected def parseComponent(isPattern: Boolean)(implicit builder: ScalaPsiBuilder): Boolean =
    Type(isPattern = isPattern, typeVariables = true)
}

trait TypeArgs {
  def apply(isPattern: Boolean)(implicit builder: ScalaPsiBuilder): Boolean =
    builder.build(ScalaElementType.TYPE_ARGS) {
      builder.getTokenType match {
        case ScalaTokenTypes.tLSQBRACKET =>
          builder.advanceLexer() //Ate [
          builder.disableNewlines()

          def checkTypeVariable: Boolean = {
            if (isPattern) {
              builder.getTokenType match {
                case ScalaTokenTypes.tIDENTIFIER | ScalaTokenTypes.tUNDER =>
                  val idText = builder.getTokenText
                  val firstChar = idText.charAt(0)
                  if (firstChar == '_' || (firstChar != '`' && firstChar.isLower)) {
                    val typeParameterMarker = builder.mark()
                    val idMarker            = builder.mark()
                    builder.advanceLexer()
                    builder.getTokenType match {
                      case ScalaTokenTypes.tCOMMA | ScalaTokenTypes.tRSQBRACKET =>
                        idMarker.drop()
                        typeParameterMarker.done(ScalaElementType.TYPE_VARIABLE)
                        true
                      case _ =>
                        idMarker.rollbackTo()
                        typeParameterMarker.drop()
                        false
                    }
                  } else false
                case _ => false
              }
            } else false
          }

          if (checkTypeVariable || parseComponent(isPattern)) {
            var parsedType = true
            while (builder.getTokenType == ScalaTokenTypes.tCOMMA && parsedType &&
              !builder.consumeTrailingComma(ScalaTokenTypes.tRSQBRACKET)) {
              builder.advanceLexer()
              parsedType = checkTypeVariable || parseComponent(isPattern)
              if (!parsedType) builder error ScalaBundle.message("wrong.type")
            }
          } else builder error ScalaBundle.message("wrong.type")

          builder.getTokenType match {
            case ScalaTokenTypes.tRSQBRACKET =>
              builder.advanceLexer() //Ate ]
            case _ => builder error ScalaBundle.message("rsqbracket.expected")
          }
          builder.restoreNewlinesState()
          true
        case _ => false
      }
    }

  protected def parseComponent(isPattern: Boolean)(implicit builder: ScalaPsiBuilder): Boolean
}
