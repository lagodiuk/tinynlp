package com.lahodiuk.tinynlp.treebank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExtractGrammar {

	public static void main(String[] args) throws Exception {
		GrammarExctractor grammarExtractor = new GrammarExctractor();

		processPennTreebank(grammarExtractor);
		process4000QuestionsTreebank(grammarExtractor);
		processBNCTreebank(grammarExtractor);
		System.out.println();

		for (Entry<String, Integer> e : grammarExtractor.getRulesSortedByFrequency()) {
			if (e.getKey().contains("NONE") || e.getKey().contains("*")) {
				continue;
			}
			System.out.println(e.getKey() + "\t" + e.getValue());
		}
	}

	private static void processBNCTreebank(GrammarExctractor grammarExtractor) throws Exception {
		processFileWithBracketedSentence(new File("src/main/resources/bnc/bnc_1000_gold_trees_09"), grammarExtractor);
	}

	private static void process4000QuestionsTreebank(GrammarExctractor grammarExtractor) throws Exception {
		processFileWithBracketedSentence(new File("src/main/resources/questionbank/4000qs.txt"), grammarExtractor);
	}

	private static void processPennTreebank(GrammarExctractor grammarExtractor) throws Exception {
		File treebankFolder = new File("src/main/resources/treebank/combined");
		for (File treebankFile : treebankFolder.listFiles()) {
			processFileWithBracketedSentence(treebankFile, grammarExtractor);
		}
	}

	private static void processFileWithBracketedSentence(File treebankFile, SyntaxTreeNodeVisitor grammarExtractor) throws Exception {
		if (".DS_Store".equals(treebankFile.getName())) {
			return;
		} else {
			System.out.println(treebankFile.getName());
		}

		String treebankFileContent = readToString(treebankFile);
		Collection<SyntaxTreeNode> trees = SyntaxTreeNode.parse(treebankFileContent);
		for (SyntaxTreeNode root : trees) {
			root.visit(grammarExtractor);
		}
	}

	private static String readToString(File file) throws Exception {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	private static class GrammarExctractor implements SyntaxTreeNodeVisitor {

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
}
