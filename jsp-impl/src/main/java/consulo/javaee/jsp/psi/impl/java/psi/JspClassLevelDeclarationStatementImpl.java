package consulo.javaee.jsp.psi.impl.java.psi;

import javax.annotation.Nonnull;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.impl.source.jsp.jspJava.JspClassLevelDeclarationStatement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import consulo.java.psi.impl.java.stub.PsiClassLevelDeclarationStatementStub;

/**
 * @author VISTALL
 * @since 25-May-17
 */
public class JspClassLevelDeclarationStatementImpl extends StubBasedPsiElementBase<PsiClassLevelDeclarationStatementStub> implements JspClassLevelDeclarationStatement,
		StubBasedPsiElement<PsiClassLevelDeclarationStatementStub>
{
	public JspClassLevelDeclarationStatementImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	public JspClassLevelDeclarationStatementImpl(@Nonnull PsiClassLevelDeclarationStatementStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public PsiElement getContext()
	{
		return PsiTreeUtil.getParentOfType(this, JspClassImpl.class);
	}
}
