package jk6e11;

import java.util.List;
import java.util.Map;

/**
 *
 * @author bill
 */
public abstract class SentimentExtractor {

    /**
     * @param strings
     * @return extract sentiments
     */
    public abstract Map<String, Object> extract(List<String> strings);
}
