package helpers.search;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;
import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * Custom Lucene Analyzer for Link-IT needs : ignore diacritics (accents, ..).
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkItAnalyzer extends StandardAnalyzer {

    public LinkItAnalyzer(Version matchVersion, Reader stopwords) throws IOException {
        super(matchVersion, stopwords);
    }

    public LinkItAnalyzer(Version matchVersion, File stopwords) throws IOException {
        super(matchVersion, stopwords);
    }

    public LinkItAnalyzer(Version matchVersion, Set<?> stopWords) {
        super(matchVersion, stopWords);
    }

    public LinkItAnalyzer(Version matchVersion) {
        super(matchVersion);
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream result = super.tokenStream(fieldName, reader);
        // Ignore accents
        result = new ASCIIFoldingFilter(result);
        return result;
    }
}
