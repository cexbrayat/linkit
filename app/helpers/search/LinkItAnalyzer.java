package helpers.search;

import java.io.Reader;
import java.util.Set;
import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.fr.FrenchStemFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * Custom Lucene Analyzer for Link-IT needs : ignore diacritics (accents, ..).
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkItAnalyzer extends StandardAnalyzer {

    private static final Set<?> STOP_WORDS = FrenchAnalyzer.getDefaultStopSet();
    
    public LinkItAnalyzer(Version matchVersion) {
        super(matchVersion, STOP_WORDS);
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = super.tokenStream(fieldName, reader);
        // Ignore accents
        result = new ASCIIFoldingFilter(result);
        // French stemmer
        result = new FrenchStemFilter(result);
        return result;
    }
}
