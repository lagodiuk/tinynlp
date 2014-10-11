package com.lahodiuk.tinynlp.treebank.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lahodiuk.tinynlp.treebank.SyntaxTreeNode;
import com.lahodiuk.tinynlp.treebank.SyntaxTreeNodeVisitor;

public class GrammarExctractor implements SyntaxTreeNodeVisitor {

	private Map<String, Integer> ruleCount = new HashMap<>();

	@Override
	public void visitNonTerminal(SyntaxTreeNode node) {
		String rule = this.getRule(node);
		Integer count = this.ruleCount.get(rule);
		count = (count == null) ? 1 : count + 1;
		this.ruleCount.put(rule, count);
	}

	private String getRule(SyntaxTreeNode node) {
		StringBuilder sb = new StringBuilder();

		sb.append(node.getLeftHandSide());
		sb.append(" -> ");
		for (SyntaxTreeNode child : node.getRightHandSide()) {
			sb.append(child.getLeftHandSide());
			sb.append(" ");
		}
		sb.setLength(sb.length() - 1);

		return sb.toString();
	}

	@Override
	public void visitTerminal(SyntaxTreeNode node) {
		// TODO Auto-generated method stub
	}

	public List<Entry<String, Integer>> getRulesSortedByFrequency() {
		List<Entry<String, Integer>> result = new ArrayList<>();
		result.addAll(this.ruleCount.entrySet());
		Collections.sort(result, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		return result;
	}
}