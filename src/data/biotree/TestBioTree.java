package data.biotree;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import data.FileProcessor;

public class TestBioTree {

	@Before
	public static void main(String[] args) throws IOException {
//		BioTree.init();
//		BioTree.processRecord(125125);
//		BioTree.processRecord(125125);
//		BioTree.processRecord(125125);
//		BioTree.processRecord(125125);
//		BioTree.processRecord(125125);
//		BioTree.processRecord(125125);
//		BioTree.processRecord(125123);
//		BioTree.processRecord(125122);
//		BioTree.processRecord(125392);
//		BioTree.processRecord(125391);
//		BioTree.processRecord(600000000);
//		//System.out.println(nodes.size());
//		//System.out.println(nodes.get(123207));
//		//Iterable<Integer> keys = nodes.keys();
//		//for (Integer i: keys) {
//		//	System.out.println(nodes.get(i));
//			//System.out.println(String.format("%-26s %s", nodes.get(i).getName(), nodes.get(i).getTaxonType()));
//		//}
//		BioTree.printTree();
	}
	
	@Test
	public void testSmallDataset() throws NumberFormatException, ParseException, IOException {
		BioTree.init();
		//FileProcessor.setPath("smalldata.csv");
		FileProcessor.initProcessing();
		assert(BioTree.getTaxonRecord("Anura") != null);
		assert(false);
	}
	

}
