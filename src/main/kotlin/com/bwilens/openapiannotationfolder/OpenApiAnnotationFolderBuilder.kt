package com.bwilens.openapiannotationfolder

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor

class OpenApiAnnotationFolderBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<out FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        root.acceptChildren(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.text.startsWith("@Operation") || element.text.startsWith("@ApiResponse")) {
                    val startOffset = element.textRange.startOffset + element.text.indexOf("(") + 1
                    val endOffset = element.textRange.endOffset - 1
                    val foldRange = TextRange(startOffset, endOffset)
                    descriptors.add(FoldingDescriptor(element.node, foldRange))
                }
                super.visitElement(element)
            }
        })
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }

}
