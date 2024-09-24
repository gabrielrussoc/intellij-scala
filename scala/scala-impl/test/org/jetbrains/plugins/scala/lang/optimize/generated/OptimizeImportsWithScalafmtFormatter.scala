package org.jetbrains.plugins.scala.lang.optimize.generated

import com.intellij.application.options.CodeStyle
import org.jetbrains.plugins.scala.ScalaVersion
import org.jetbrains.plugins.scala.lang.formatter.scalafmt.ScalaFmtForTestsSetupOps
import org.jetbrains.plugins.scala.lang.formatting.settings.ScalaCodeStyleSettings
import org.jetbrains.plugins.scala.lang.optimize.OptimizeImportsTestBase
import org.scalafmt.dynamic.ScalafmtVersion

class OptimizeImportsWithScalafmtFormatter
  extends OptimizeImportsTestBase
    with ScalaFmtForTestsSetupOps {

  override protected def supportedIn(version: ScalaVersion): Boolean =
    version == ScalaVersion.Latest.Scala_2_13

  //This class was generated by build script, please don't change this
  override def folderPath: String = super.folderPath + "scalafmt/"

  protected override def sourceRootPath: String = folderPath

  override protected def scalafmtConfigsBasePath: String = folderPath

  private lazy val scalaCodeStyleSettings = CodeStyle.getSettings(getProject).getCustomSettings(classOf[ScalaCodeStyleSettings])

  override def setUp(): Unit = {
    super.setUp()

    ScalaFmtForTestsSetupOps.ensureDownloaded(
      ScalafmtVersion.parse("3.7.15").get,
    )

    val scalafmtConfigFileName = getTestName(false) + ".scalafmt.conf"
    setScalafmtConfig(scalafmtConfigFileName)

    scalaCodeStyleSettings.FORMATTER = ScalaCodeStyleSettings.SCALAFMT_FORMATTER
  }

  def testWithMaxColumn(): Unit = {
    scalaCodeStyleSettings.SCALAFMT_USE_INTELLIJ_FORMATTER_FOR_RANGE_FORMAT = false
    doTest()

    scalaCodeStyleSettings.SCALAFMT_USE_INTELLIJ_FORMATTER_FOR_RANGE_FORMAT = true
    doTest()
  }
}
