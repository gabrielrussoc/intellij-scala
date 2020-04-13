package org.jetbrains.plugins.scala
package testingSupport.test.specs2

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.scala.extensions.{PsiElementExt, TraversableExt}
import org.jetbrains.plugins.scala.lang.psi.ScalaPsiUtil
import org.jetbrains.plugins.scala.lang.psi.api.expr.{ScExpression, ScInfixExpr}
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScTypeDefinition
import org.jetbrains.plugins.scala.testingSupport.test.AbstractTestConfigurationProducer.CreateFromContextInfo
import org.jetbrains.plugins.scala.testingSupport.test.AbstractTestConfigurationProducer.CreateFromContextInfo.{AllInPackage, ClassWithTestName}
import org.jetbrains.plugins.scala.testingSupport.test.structureView.TestNodeProvider
import org.jetbrains.plugins.scala.testingSupport.test.{AbstractTestConfigurationProducer, TestConfigurationUtil}

final class Specs2ConfigurationProducer extends AbstractTestConfigurationProducer[Specs2RunConfiguration] {

  override def getConfigurationFactory: ConfigurationFactory = Specs2ConfigurationType.instance.confFactory

  override def suitePaths = List(
    "org.specs2.specification.SpecificationStructure",
    "org.specs2.specification.core.SpecificationStructure"
  )

  override protected def configurationName(contextInfo: CreateFromContextInfo): String = contextInfo match {
    case AllInPackage(_, packageName)           =>
      ScalaBundle.message("test.in.scope.specs2.presentable.text", packageName)
    case ClassWithTestName(testClass, Some(testName)) =>
      s"${testClass.qualifiedName}::$testName"
    case ClassWithTestName(testClass, _) =>
      testClass.name // TODO: maybe this should also be qualified name? verify
  }

  override protected def isClassOfTestConfigurationFromLocation(configuration: Specs2RunConfiguration, location: PsiElementLocation): Boolean = {
    val parent: ScTypeDefinition = PsiTreeUtil.getParentOfType(location.getPsiElement, classOf[ScTypeDefinition], false)
    if (parent == null) return false
    val suiteClasses = suitePaths.flatMap(parent.elementScope.getCachedClass)
    if (suiteClasses.isEmpty) return false
    val suiteClazz = suiteClasses.head // TODO: why head?

    if (ScalaPsiUtil.isInheritorDeep(parent, suiteClazz)) {
      super.isClassOfTestConfigurationFromLocation(configuration, location)
    } else {
      false
    }
  }

  private def extractStaticTestName(testDefExpr: ScInfixExpr): Option[String] =
    testDefExpr.getChildren.toSeq
      .filterBy[ScExpression]
      .headOption
      .flatMap(TestConfigurationUtil.getStaticTestName(_))

  override def getTestClassWithTestName(location: PsiElementLocation): Option[ClassWithTestName] = {
    val element = location.getPsiElement
    val testClassDef: ScTypeDefinition = PsiTreeUtil.getParentOfType(element, classOf[ScTypeDefinition], false)
    if (testClassDef == null) None

    val suiteClasses = suitePaths.flatMap {
      element.elementScope.getCachedClass(_)
    }
    if (suiteClasses.isEmpty) return None
    val suiteClazz = suiteClasses.head // TODO: why head???
    if (!ScalaPsiUtil.isInheritorDeep(testClassDef, suiteClazz)) return None

    val testName = ScalaPsiUtil.getParentWithProperty(element, strict = false, e => TestNodeProvider.isSpecs2TestExpr(e)) match {
      case Some(infixExpr: ScInfixExpr) => extractStaticTestName(infixExpr)
      case _                            => None
    }
    Some(ClassWithTestName(testClassDef, testName))
  }
}
