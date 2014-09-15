package com.lahodiuk.tinynlp.treebank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/treebank/combined/wsj_0005.mrg"))) {
			StringBuilder sb = new StringBuilder();
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			Collection<SyntaxTreeNode> trees = SyntaxTreeNode.parse(sb.toString());

			for (SyntaxTreeNode tree : trees) {
				RuleGatheringVisitor visitor = new RuleGatheringVisitor();
				tree.visit(visitor);
				System.out.println(visitor.getSentence());
				for (String rule : visitor.getRules()) {
					System.out.println("\"" + rule + "\", ");
				}
				System.out.println();
			}
		}
	}

	private static class RuleGatheringVisitor implements SyntaxTreeNodeVisitor {

		private Set<String> rules = new HashSet<>();

		private StringBuilder sentenceBuilder = new StringBuilder();

		@Override
		public void visitNonTerminal(SyntaxTreeNode node) {
			StringBuilder ruleBuilder = new StringBuilder();
			ruleBuilder.append(node.getLeftHandSide()).append(" -> ");
			for (SyntaxTreeNode child : node.getRightHandSide()) {
				ruleBuilder.append(child.getLeftHandSide()).append(" ");
			}
			this.rules.add(ruleBuilder.toString());
		}
		@Override
		public void visitTerminal(SyntaxTreeNode node) {
			this.sentenceBuilder.append(node.getLeftHandSide()).append(" ");
		}

		public Collection<String> getRules() {
			return this.rules;
		}

		public String getSentence() {
			return this.sentenceBuilder.toString();
		}
	}

}
