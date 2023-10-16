package org.jetbrains.plugins.scala.util

import com.intellij.psi.PsiMethod
import com.intellij.psi.impl.PsiClassImplUtil
import com.intellij.psi.util.PsiMethodUtil
import org.jetbrains.plugins.scala.caches.{BlockModificationTracker, cachedInUserData}
import org.jetbrains.plugins.scala.extensions._
import org.jetbrains.plugins.scala.lang.psi.api.base.ScAnnotation
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScFunctionDefinition
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.{ScMember, ScObject}
import org.jetbrains.plugins.scala.lang.psi.light.PsiClassWrapper
import org.jetbrains.plugins.scala.lang.refactoring.util.ScalaNamesUtil

object ScalaMainMethodUtil {

  def isMainMethod(funDef: ScFunctionDefinition): Boolean =
    isScala2MainMethod(funDef) ||
      isScala3MainMethod(funDef)

  def isScala2MainMethod(funDef: ScFunctionDefinition): Boolean = {
    ScalaNamesUtil.toJavaName(funDef.name) == "main" &&
      isInTopLevelObject(funDef) &&
      PsiMethodUtil.isMainMethod(funDef)
  }

  private def isInTopLevelObject(m: ScMember): Boolean =
    m.containingClass match {
      case o: ScObject => o.isTopLevel
      case _ => false
    }

  def isScala3MainMethod(funDef: ScFunctionDefinition): Boolean =
    if (funDef.isInScala3File) {
      val mainAnnotation = funDef.annotations.find(isMainAnnotation)
      mainAnnotation.isDefined
    }
    else false

  // NOTE: we could truly to "scala.main" (but currently resolve for the annotation is broken for Scala3 for some reason)
  //  maybe it's OK to do this for optimisation reasons
  private def isMainAnnotation(annotation: ScAnnotation): Boolean = {
    val text = annotation.annotationExpr.getText
    text == "main" || text == "scala.main"
  }

  def hasScala2MainMethod(obj: ScObject): Boolean = findScala2MainMethod(obj).isDefined

  def findScala2MainMethod(obj: ScObject): Option[PsiMethod] = {
    if (!obj.isTopLevel) None
    else {
      cachedInUserData("findScala2MainMethod", obj, BlockModificationTracker(obj)) {
        //NOTE: PsiClassWrapper is used in order PsiMethodUtil.isMainMethod can detect main method from base class
        // otherwise some of the conditions doesn't hold (AFAIR it can't check the presence of static modifier)
        val objWrapper = new PsiClassWrapper(obj, obj.qualifiedName, obj.name)
        val mainMethods = PsiClassImplUtil.findMethodsByName(objWrapper, "main", true)
        mainMethods.find(PsiMethodUtil.isMainMethod)
      }
    }
  }
}
