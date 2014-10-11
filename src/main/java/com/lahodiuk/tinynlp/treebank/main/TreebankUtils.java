package com.lahodiuk.tinynlp.treebank.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;

import com.lahodiuk.tinynlp.treebank.SyntaxTreeNode;
import com.lahodiuk.tinynlp.treebank.SyntaxTreeNodeVisitor;

public class TreebankUtils {

	public static void processBNCTreebank(SyntaxTreeNodeVisitor grammarExtractor) throws Exception {
		processFileWithBracketedSentence(new File("src/main/resources/bnc/bnc_1000_gold_trees_09"), grammarExtractor);
	}

	public static void process4000QuestionsTreebank(SyntaxTreeNodeVisitor grammarExtractor) throws Exception {
		processFileWithBracketedSentence(new File("src/main/resources/questionbank/4000qs.txt"), grammarExtractor);
	}

	public static void processPennTreebank(SyntaxTreeNodeVisitor grammarExtractor) throws Exception {
		File treebankFolder = new File("src/main/resources/treebank/combined");
		for (File treebankFile : treebankFolder.listFiles()) {
			processFileWithBracketedSentence(treebankFile, grammarExtractor);
		}
	}

	public static void processFileWithBracketedSentence(File treebankFile, SyntaxTreeNodeVisitor grammarExtractor) throws Exception {
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
}
