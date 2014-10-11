package com.lahodiuk.tinynlp.treebank.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lahodiuk.tinynlp.treebank.SyntaxTreeNode;
import com.lahodiuk.tinynlp.treebank.SyntaxTreeNodeVisitor;

public class POSTaggedSequencesExtractor implements SyntaxTreeNodeVisitor {

	private List<POSTaggedToken> currentSequence = new ArrayList<>();

	private List<List<POSTaggedToken>> posTaggedSequences = new ArrayList<>();

	@Override
	public void visitNonTerminal(SyntaxTreeNode node) {
		if (node.isRoot()) {
			if (!this.currentSequence.isEmpty()) {
				this.posTaggedSequences.add(this.currentSequence);
			}
			this.currentSequence = new ArrayList<>();
		}

		Collection<SyntaxTreeNode> rhs = node.getRightHandSide();
		if (rhs.size() == 1) {
			SyntaxTreeNode child = rhs.iterator().next();
			if (child.getRightHandSide().size() == 0) {
				POSTaggedToken token = new POSTaggedToken(child.getLeftHandSide(), node.getLeftHandSide());
				this.currentSequence.add(token);
			}
		}
	}

	@Override
	public void visitTerminal(SyntaxTreeNode node) {
		// ignore
	}

	public List<List<POSTaggedToken>> getPosTaggedSequences() {
		return this.posTaggedSequences;
	}

	public static class POSTaggedToken {

		private String token;

		private String partOfSpeech;

		public POSTaggedToken(String token, String partOfSpeech) {
			this.token = token;
			this.partOfSpeech = partOfSpeech;
		}

		public String getPartOfSpeech() {
			return this.partOfSpeech;
		}

		public String getToken() {
			return this.token;
		}
	}
}
