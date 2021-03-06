/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package consulo.javaee.jsp.format.copy;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.DelegatingFormattingModelBuilder;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.formatting.WrapType;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageFormatting;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;

/**
 * @author Alexey Chmutov
 *         Date: Jun 26, 2009
 *         Time: 4:07:09 PM
 */
public abstract class TemplateLanguageFormattingModelBuilder2 implements DelegatingFormattingModelBuilder, TemplateLanguageBlockFactory2
{
	@Override
	@Nonnull
	public FormattingModel createModel(PsiElement element, CodeStyleSettings settings)
	{
		final PsiFile file = element.getContainingFile();
		Block rootBlock = getRootBlock(file, file.getViewProvider(), settings);
		return new DocumentBasedFormattingModel(rootBlock, element.getProject(), settings, file.getFileType(), file);
	}

	protected Block getRootBlock(PsiElement element, FileViewProvider viewProvider, CodeStyleSettings settings)
	{
		ASTNode node = element.getNode();
		if(node == null)
		{
			return createDummyBlock(node);
		}
		if(viewProvider instanceof TemplateLanguageFileViewProvider)
		{
			final Language dataLanguage = ((TemplateLanguageFileViewProvider) viewProvider).getTemplateDataLanguage();
			final FormattingModelBuilder builder = LanguageFormatting.INSTANCE.forLanguage(dataLanguage);
			if(builder instanceof DelegatingFormattingModelBuilder && ((DelegatingFormattingModelBuilder) builder).dontFormatMyModel())
			{
				return createDummyBlock(node);
			}
			if(builder != null)
			{
				final FormattingModel model = builder.createModel(viewProvider.getPsi(dataLanguage), settings);
				List<DataLanguageBlockWrapper2> childWrappers = BlockUtil2.buildChildWrappers(model.getRootBlock());
				if(childWrappers.size() == 1)
				{
					childWrappers = BlockUtil2.buildChildWrappers(childWrappers.get(0).getOriginal());
				}
				return createTemplateLanguageBlock(node, Wrap.createWrap(WrapType.NONE, false), null,
						BlockUtil2.filterBlocksByRange(childWrappers, node.getTextRange()), settings);
			}
		}
		return createTemplateLanguageBlock(node, Wrap.createWrap(WrapType.NONE, false), null, Collections.<DataLanguageBlockWrapper2>emptyList(), settings);
	}

	protected AbstractBlock createDummyBlock(final ASTNode node)
	{
		return new AbstractBlock(node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment())
		{
			@Override
			protected List<Block> buildChildren()
			{
				return Collections.emptyList();
			}

			@Override
			public Spacing getSpacing(final Block child1, @Nonnull final Block child2)
			{
				return Spacing.getReadOnlySpacing();
			}

			@Override
			public boolean isLeaf()
			{
				return true;
			}
		};
	}

	@Override
	public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset)
	{
		return null;
	}

	@Override
	public boolean dontFormatMyModel()
	{
		return true;
	}
}
