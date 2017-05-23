package consulo.javaee.jsp.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import consulo.javaee.jsp.psi.JspTokens;
%%

%public
%class _JspLexer
%extends LexerBase
%public
%unicode
%public

%function advanceImpl
%type IElementType

%eof{ return;
%eof}

WHITESPACE=[ \n\r\t]+

%state JSP_FRAGMENT

%%

<YYINITIAL>
{
   "<%--"  { yybegin(JSP_FRAGMENT);  return JspTokens.JSP_FRAGMENT; }

   "<%"    { yybegin(JSP_FRAGMENT);  return JspTokens.JSP_FRAGMENT; }

   "<%@"   { yybegin(JSP_FRAGMENT); return JspTokens.JSP_FRAGMENT; }

   "<%="   { yybegin(JSP_FRAGMENT); return JspTokens.JSP_FRAGMENT; }

   "<${"   { yybegin(JSP_FRAGMENT); return JspTokens.JSP_FRAGMENT; }

   {WHITESPACE} { return JspTokens.HTML_TEXT; }

   . { return JspTokens.HTML_TEXT; }
}

<JSP_FRAGMENT>
{
	"%>"               { yybegin(YYINITIAL); return JspTokens.JSP_FRAGMENT; }

	"--%>"             { yybegin(YYINITIAL); return JspTokens.JSP_FRAGMENT; }

	"@%>"              { yybegin(YYINITIAL); return JspTokens.JSP_FRAGMENT; }

	"=%>"              { yybegin(YYINITIAL); return JspTokens.JSP_FRAGMENT; }

	"}$>"              { yybegin(YYINITIAL); return JspTokens.JSP_FRAGMENT; }

	[^]                { return JspTokens.JSP_FRAGMENT; }
}