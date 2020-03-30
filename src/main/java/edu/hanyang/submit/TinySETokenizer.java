package edu.hanyang.submit;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.ext.PorterStemmer;

import edu.hanyang.indexer.Tokenizer;

public class TinySETokenizer implements Tokenizer {
	
	public SimpleAnalyzer analyzer = null;
	public TokenStream ts = null;
	public CharTermAttribute cattr = null;
	public PorterStemmer stemmer = null;

	public void setup() {
		analyzer = new SimpleAnalyzer();
		stemmer = new PorterStemmer();
	}

	public List<String> split(String text) {
		
		List<String> result = new ArrayList<String>();
		ts = analyzer.tokenStream(null, new StringReader(text));
		
		cattr = ts.addAttribute(CharTermAttribute.class);
		
		try {
			ts.reset();
			while (ts.incrementToken()){
				stemmer.setCurrent(cattr.toString());
				stemmer.stem();
				result.add(stemmer.getCurrent());
			}
			
			ts.end();
			ts.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public void clean() {
		analyzer = null;
		ts = null;
		cattr = null;
		stemmer = null;
	}
	
//	public static void main(String[] args) throws IOException {		
//		String text = "I hope my death makes more cents than my life";
//		
//		TinySETokenizer tt = new TinySETokenizer();
//		
//		tt.setup();
//		List<String> r = tt.split(text);
//		tt.clean();
//		
//		System.out.println(r);
//	}

}