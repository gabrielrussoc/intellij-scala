package org.jetbrains.plugins.scala.projectHighlighting.local

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import org.jetbrains.plugins.scala.projectHighlighting.base.SbtProjectHighlightingLocalProjectsTestBase
import org.jetbrains.plugins.scala.projectHighlighting.reporter.HighlightingProgressReporter
import org.jetbrains.sbt.project.ProjectStructureMatcher.ProjectComparisonOptions
import org.jetbrains.sbt.project.{ExactMatch, ProjectStructureMatcher}

class SbtProjectWithProjectMatrixAndSourceGenerators
  extends SbtProjectHighlightingLocalProjectsTestBase
    with ProjectStructureMatcher
    with ExactMatch {

  //Note in this test sbt project sources are generated automatically on project reload (in "update" task)
  //but in common build sources are not automatically generated during project reload
  override def projectName = "sbt-projectmatrix-with-source-generators"

  override protected val projectFileName = projectName

  override def testHighlighting(): Unit = {
    super.testHighlighting()
  }

  override protected def highlightSingleFile(
    virtualFile: VirtualFile,
    psiFile: PsiFile,
    reporter: HighlightingProgressReporter,
  ): Unit =
    doHighlightingForFile(virtualFile, psiFile, reporter)

  //noinspection ScalaUnusedSymbol,TypeAnnotation
  def testProjectStructure(): Unit = {
    import org.jetbrains.sbt.project.ProjectStructureDsl._

    class myModule(name: String, contentRootRelativePaths: Option[Seq[String]] = None) extends module(name) {
      def this(name: String, contentRootRelativePaths: String) = {
        this(name, Some(Seq(contentRootRelativePaths)))
      }

      //explicitly define attributes to some default values to ensure that they will be tested
      //(those attributes, which are not defined are not tested)

      //`projectmatrix` sbt plugin uses special generated paths as module content roots
      private val contentRootRelativeName = name.split('.').last
      contentRoots := contentRootRelativePaths.getOrElse(Seq(s".sbt/matrix/$contentRootRelativeName")).map(relativeProjectPath)
      sources := Seq()
      testSources := Seq()
      resources := Seq()
      testResources := Seq()
      moduleDependencies := Seq()
      excluded := Seq()
    }
    class myNonRootModule(name: String, contentRootRelativePaths: Option[Seq[String]] = None) extends myModule(s"$projectName.$name", contentRootRelativePaths) {
      def this(name: String, contentRootRelativePaths: String) = {
        this(name, Some(Seq(contentRootRelativePaths)))
      }
    }

    val expectedProject: project = new project(projectName) {
      val `sbt-projectmatrix-with-source-generator` = new myModule(projectName, "") {
        excluded := Seq("target")
      }
      val `sbt-projectmatrix-with-source-generators-build` = new myNonRootModule("sbt-projectmatrix-with-source-generators-build", "project") {
        sources := Seq("")
        excluded := Seq("target", "project/target")
      }

      val `downstream` = new myNonRootModule("downstream.downstream")
      val `downstream2_11` = new myNonRootModule("downstream.downstream2_11")
      val `downstream2_12` = new myNonRootModule("downstream.downstream2_12")
      val `downstream-sources` = new myNonRootModule("downstream.downstream-sources", "downstream") {
        sources := Seq(
          "src/main/scala",
          "target/jvm-2.13/src_managed/main"
        )
        testSources := Seq("target/jvm-2.13/src_managed/test")
        excluded := Seq("target")
      }

      val `upstream` = new myNonRootModule("upstream.upstream")
      val `upstream2_11` = new myNonRootModule("upstream.upstream2_11")
      val `upstream2_12` = new myNonRootModule("upstream.upstream2_12")
      val `upstream-sources` = new myNonRootModule("upstream.upstream-sources", "upstream") {
        sources := Seq(
          "src/main/scala",
          "target/jvm-2.13/src_managed/main"
        )
        excluded := Seq("target")
      }

      val `downstreamBothPlatforms` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatforms")
      val `downstreamBothPlatforms2_11` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatforms2_11")
      val `downstreamBothPlatforms2_12` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatforms2_12")
      val `downstreamBothPlatformsJS` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatformsJS")
      val `downstreamBothPlatformsJS2_11` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatformsJS2_11")
      val `downstreamBothPlatformsJS2_12` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatformsJS2_12")
      val `downstreamBothPlatforms-sources` = new myNonRootModule("downstreamBothPlatforms.downstreamBothPlatforms-sources", "downstreamBothPlatforms") {
        sources := Seq(
          "src/main/scala",
          "target/jvm-2.13/src_managed/main"
        )
        testSources := Seq("src/test/scala", "target/jvm-2.13/src_managed/test")
        excluded := Seq("target")
      }

      val `upstreamBothPlatforms` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatforms")
      val `upstreamBothPlatforms2_11` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatforms2_11")
      val `upstreamBothPlatforms2_12` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatforms2_12")
      val `upstreamBothPlatformsJS` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatformsJS")
      val `upstreamBothPlatformsJS2_11` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatformsJS2_11")
      val `upstreamBothPlatformsJS2_12` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatformsJS2_12")
      val `upstreamBothPlatforms-sources` = new myNonRootModule("upstreamBothPlatforms.upstreamBothPlatforms-sources", "upstreamBothPlatforms") {
        sources := Seq(
          "src/main/scala",
          "target/jvm-2.13/src_managed/main"
        )
        excluded := Seq("target")
      }

      //
      // Define module groups separately for better test data readability
      //
      val downstreamGroup: Seq[myModule] = Seq(
        `downstream-sources`,
        `downstream`,
        `downstream2_11`,
        `downstream2_12`,
      )
      val upstreamGroup: Seq[myModule] = Seq(
        `upstream-sources`,
        `upstream`,
        `upstream2_11`,
        `upstream2_12`,
      )
      val downstreamBothPlatformsGroup: Seq[myModule] = Seq(
        `downstreamBothPlatforms-sources`,
        `downstreamBothPlatforms`,
        `downstreamBothPlatforms2_11`,
        `downstreamBothPlatforms2_12`,
        `downstreamBothPlatformsJS`,
        `downstreamBothPlatformsJS2_11`,
        `downstreamBothPlatformsJS2_12`,
      )
      val upstreamBothPlatformsGroup: Seq[myModule] = Seq(
        `upstreamBothPlatforms`,
        `upstreamBothPlatforms-sources`,
        `upstreamBothPlatforms2_11`,
        `upstreamBothPlatforms2_12`,
        `upstreamBothPlatformsJS`,
        `upstreamBothPlatformsJS2_11`,
        `upstreamBothPlatformsJS2_12`,
      )

      //
      // Define dependencies between modules separately for better test data readability
      //
      `downstream-sources`.dependsOn(`upstream`)
      `downstream`.dependsOn(`upstream`, `downstream-sources`, `upstream-sources`)
      `downstream2_11`.dependsOn(`upstream2_11`, `downstream-sources`, `upstream-sources`)
      `downstream2_12`.dependsOn(`upstream2_12`, `downstream-sources`, `upstream-sources`)

      `upstream-sources`.dependsOn()
      `upstream`.dependsOn(`upstream-sources`)
      `upstream2_11`.dependsOn(`upstream-sources`)
      `upstream2_12`.dependsOn(`upstream-sources`)

      `downstreamBothPlatforms-sources`.dependsOn(`upstreamBothPlatforms`)
      `downstreamBothPlatforms`.dependsOn(`upstreamBothPlatforms`, `downstreamBothPlatforms-sources`, `upstreamBothPlatforms-sources`)
      `downstreamBothPlatforms2_11`.dependsOn(`upstreamBothPlatforms2_11`, `downstreamBothPlatforms-sources`, `upstreamBothPlatforms-sources`)
      `downstreamBothPlatforms2_12`.dependsOn(`upstreamBothPlatforms2_12`, `downstreamBothPlatforms-sources`, `upstreamBothPlatforms-sources`)
      `downstreamBothPlatformsJS`.dependsOn(`upstreamBothPlatformsJS`, `downstreamBothPlatforms-sources`, `upstreamBothPlatforms-sources`)
      `downstreamBothPlatformsJS2_11`.dependsOn(`upstreamBothPlatformsJS2_11`, `downstreamBothPlatforms-sources`, `upstreamBothPlatforms-sources`)
      `downstreamBothPlatformsJS2_12`.dependsOn(`upstreamBothPlatformsJS2_12`, `downstreamBothPlatforms-sources`, `upstreamBothPlatforms-sources`)

      `upstreamBothPlatforms-sources`.dependsOn()
      `upstreamBothPlatforms`.dependsOn(`upstreamBothPlatforms-sources`)
      `upstreamBothPlatforms2_11`.dependsOn(`upstreamBothPlatforms-sources`)
      `upstreamBothPlatforms2_12`.dependsOn(`upstreamBothPlatforms-sources`)
      `upstreamBothPlatformsJS`.dependsOn(`upstreamBothPlatforms-sources`)
      `upstreamBothPlatformsJS2_11`.dependsOn(`upstreamBothPlatforms-sources`)
      `upstreamBothPlatformsJS2_12`.dependsOn(`upstreamBothPlatforms-sources`)

      modules := Seq(
        `sbt-projectmatrix-with-source-generator`,
        `sbt-projectmatrix-with-source-generators-build`,
      ) ++
        downstreamGroup ++
        upstreamGroup ++
        downstreamBothPlatformsGroup ++
        upstreamBothPlatformsGroup
    }

    val matcher = new ProjectStructureMatcher {
      override protected def defaultAssertMatch: ProjectStructureMatcher.AttributeMatchType =
        ProjectStructureMatcher.AttributeMatchType.Exact
    }
    implicit val comparisonOptions: ProjectComparisonOptions = ProjectComparisonOptions(strictCheckForBuildModules = true)
    matcher.assertProjectsEqual(expectedProject, getProject)(comparisonOptions)
  }
}
