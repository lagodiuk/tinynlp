package com.lahodiuk.tinynlp.treebank;

public interface SyntaxTreeNodeVisitor {

	void visitNonTerminal(SyntaxTreeNode node);

	void visitTerminal(SyntaxTreeNode node);

}
