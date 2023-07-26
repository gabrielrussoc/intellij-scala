package org.jetbrains.plugins.scala.findUsages.factory

import com.intellij.openapi.project.Project

final class ScalaMemberFindUsagesOptions(project: Project) extends ScalaFindUsagesOptionsBase(project) {
  isSearchForTextOccurrences = false
}


