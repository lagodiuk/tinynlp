package com.lahodiuk.tinynlp.treebank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class SyntaxTreeNode {

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

		int i = 0;
		while (i < tokens.size()) {
			String token = tokens.get(i);

			if ("(".equals(token)) {

				String lhs = tokens.get(i + 1);
				if ("(".equals(lhs)) {
					lhs = "Root";
				} else {
					i++;
					if (lhs.equals(tokens.get(i + 1))) {
						lhs = "Special" + lhs;
					}
				}

				SyntaxTreeNode node = new SyntaxTreeNode(lhs);
				stack.push(node);

				i++;

			} else if (")".equals(token)) {

				SyntaxTreeNode node = stack.pop();
				if (stack.isEmpty()) {
					syntaxTrees.add(node);
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
