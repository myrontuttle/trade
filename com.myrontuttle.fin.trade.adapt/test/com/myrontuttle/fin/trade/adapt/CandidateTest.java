package com.myrontuttle.fin.trade.adapt;

import static org.junit.Assert.*;

import org.junit.Test;

public class CandidateTest {

	int[] genome = new int[]{72,36,99,70,93,79,68,9,65,24,61,79,84,62,68,30,92,11,76,59,13,39,52,68,34,
			78,78,80,70,85,11,31,46,35,89,23,94,23,5,80,98,2,85,5,88,57,80,16,42,98,15,73,20,33,85,76,55,
			70,16,31,56,79,68,39,12,54,42,22,16,74,53,34,76,78,88,33,59,83,63,57,85,94,11,56,4,83,19,92,
			95,10,68,78,62,3,30,88,85,88,90,0,19,47,11,66,4,58,91,59,76,93,70,88,61,99,70,39,45,47,60,19,
			25,50,5,70,3,7,87,74,73,16,52,46,57,17,81,29,60,90,40,47,70,37,99,52,83,6,29,7,75,20,52,68,
			75,53,55,77,41,60,90,37,46,17,16,64,51,27,85,50,89,69,14,2,5,64,62,48,38,98,11,46,47,17,54,25,
			26,84,43,36,76,80};
	
	@Test
	public void testGenomeStringGenerationAndParsing() {
		
		String genomeString = Candidate.generateGenomeString(genome);
		int[] genomeB = Candidate.parseGenomeString(genomeString);
		for (int i=0; i<genomeB.length; i++) {
			assertEquals(genome[i], genomeB[i]);
		}
	}

}
