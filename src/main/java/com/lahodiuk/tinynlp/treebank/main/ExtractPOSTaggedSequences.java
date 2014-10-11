package com.lahodiuk.tinynlp.treebank.main;

import java.util.List;

import com.lahodiuk.tinynlp.treebank.visitors.POSTaggedSequencesExtractor;
import com.lahodiuk.tinynlp.treebank.visitors.POSTaggedSequencesExtractor.POSTaggedToken;

public class ExtractPOSTaggedSequences {

	public static void main(String[] args) throws Exception {
		POSTaggedSequencesExtractor posSequencesExtractor = new POSTaggedSequencesExtractor();

		TreebankUtils.processPennTreebank(posSequencesExtractor);
		TreebankUtils.process4000QuestionsTreebank(posSequencesExtractor);
		TreebankUtils.processBNCTreebank(posSequencesExtractor);
		System.out.println();

		for (List<POSTaggedToken> sequence : posSequencesExtractor.getPosTaggedSequences()) {
			for (POSTaggedToken posToken : sequence) {
				System.out.println(posToken.getPartOfSpeech() + "\t" + posToken.getToken());
			}
			System.out.println();
		}
	}
}
