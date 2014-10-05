package com.lahodiuk.tinynlp.treebank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class SyntaxTreeNode {

	private static final String SPECIAL_PREFIX = "Special";

	private static final String EMPTY_NODE_NAME = "Empty";

	private static final String ROOT_NODE_NAME = "Root";

	private String leftHandSide;

	private List<SyntaxTreeNode> children = new ArrayList<>();

	public SyntaxTreeNode(String leftHandSide) {
		this.leftHandSide = leftHandSide;
	}

	public String getLeftHandSide() {
		return this.leftHandSide;
	}

	public boolean isTerminal() {
		return this.children.isEmpty();
	}

	public void addChild(SyntaxTreeNode child) {
		this.children.add(child);
	}

	public Collection<SyntaxTreeNode> getRightHandSide() {
		return Collections.unmodifiableList(this.children);
	}

	public void visit(SyntaxTreeNodeVisitor visitor) {
		if (this.isTerminal()) {
			visitor.visitTerminal(this);
		} else {
			visitor.visitNonTerminal(this);
			for (SyntaxTreeNode child : this.getRightHandSide()) {
				child.visit(visitor);
			}
		}
	}

	public static Collection<SyntaxTreeNode> parse(String str) {
		List<String> tokens = tokenize(str);

		Collection<SyntaxTreeNode> syntaxTrees = new ArrayList<>();
		Stack<SyntaxTreeNode> stack = new Stack<>();

		int extractedTreesNumber = 0;

		int i = 0;
		while (i < tokens.size()) {
			String token = tokens.get(i);

			if ("(".equals(token)) {

				String lhs = tokens.get(i + 1);
				if ("(".equals(lhs)) {
					// Workaround special case of Penn Treebank:
					// Each sentence - is additionally wrapped into brackets,
					// e.g.:
					// ((S (NP-SBJ (NNP Mr.) (NNP Vinken) ) ... ))
					// So, I assume that double brackets is the "Root" node of
					// syntax tree, e.g.:
					// (Root (S (NP-SBJ (NNP Mr.) (NNP Vinken) ) ... ))
					lhs = ROOT_NODE_NAME;
				} else if (")".equals(lhs)) {
					// Workaround special case of 4000 questions treebank:
					// Sometimes, there is questions with empty nodes, e.g.:
					// (SBARQ ... (SQ ( )(VP (VBD wrote) ... )
					// So, I assume that empty brackets is the "Empty" node of
					// syntax tree, e.g.:
					// (SBARQ ... (SQ (Empty)(VP (VBD wrote) ... )
					lhs = EMPTY_NODE_NAME;
				} else {
					i++;
					if (lhs.equals(tokens.get(i + 1))) {
						// Sometimes, treebanks has following syntax for special
						// characters:
						// (? ?) (name of token == value of token)
						// So, I transform name of token by adding special
						// prefix, e.g.:
						// (Special? ?)
						lhs = SPECIAL_PREFIX + lhs;
					}
				}

				SyntaxTreeNode node = new SyntaxTreeNode(lhs);
				stack.push(node);

				i++;

			} else if (")".equals(token)) {

				SyntaxTreeNode node = stack.pop();
				if (stack.isEmpty()) {
					if (!node.getLeftHandSide().equals(ROOT_NODE_NAME)) {
						SyntaxTreeNode root = new SyntaxTreeNode(ROOT_NODE_NAME);
						root.addChild(node);
						node = root;
					}
					syntaxTrees.add(node);

					extractedTreesNumber++;
					System.out.println("Extracted trees number: " + extractedTreesNumber);
				} else {
					stack.peek().addChild(node);
				}

				i++;

			} else {

				SyntaxTreeNode terminal = new SyntaxTreeNode(token);
				stack.peek().addChild(terminal);

				i++;

			}
		}

		return syntaxTrees;
	}

	private static List<String> tokenize(String str) {
		StringTokenizer tokenizer = new StringTokenizer(str, "() \n\t", true);
		List<String> tokens = new ArrayList<>();
		while (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken().trim();
			if (token.isEmpty()) {
				continue;
			}
			tokens.add(token);
		}
		return tokens;
	}
}
