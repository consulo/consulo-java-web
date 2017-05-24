/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.javaee.jsp.highlight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import consulo.javaee.jsp.psi.JspTokens;

/**
 * @author VISTALL
 * @since 08.11.13.
 */
public class JspEditorHighlighter extends LayeredLexerEditorHighlighter
{
	public JspEditorHighlighter(@Nullable final Project project, @Nullable final VirtualFile virtualFile, @NotNull final EditorColorsScheme colors)
	{
		super(new JspSyntaxHighlighter(), colors);

		SyntaxHighlighter htmlHighlight = SyntaxHighlighterFactory.getSyntaxHighlighter(HtmlFileType.INSTANCE, project, virtualFile);
		assert htmlHighlight != null;
		registerLayer(JspTokens.HTML_FRAGMENT, new LayerDescriptor(htmlHighlight, ""));

		SyntaxHighlighter javaHighlight = SyntaxHighlighterFactory.getSyntaxHighlighter(JavaFileType.INSTANCE, project, virtualFile);
		assert javaHighlight != null;
		registerLayer(JspTokens.JAVA_FRAGMENT, new LayerDescriptor(new SyntaxHighlighterBase()
		{
			@NotNull
			@Override
			public Lexer getHighlightingLexer()
			{
				return javaHighlight.getHighlightingLexer();
			}

			@NotNull
			@Override
			public TextAttributesKey[] getTokenHighlights(IElementType iElementType)
			{
				return pack(javaHighlight.getTokenHighlights(iElementType), DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);
			}
		}, ""));
	}

}
