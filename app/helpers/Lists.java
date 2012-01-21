package helpers;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Tools for {@link List}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public final class Lists {
    
    private Lists() {};
    
    /**
     * Reorder list so that items are put on top of list
     * @param items Items to put on top of list
     * @param list List to reorder with item on top.
     * @return new ordered list, with item on top.
     */
    public static <T> List<T> putOnTop(List<T> list, T... items) {
        return putOnTop(list, (items == null ? Collections.<T>emptyList() : Arrays.asList(items)));
    }
    
    /**
     * Reorder list so that items are put on top of list
     * @param items Items to put on top of list
     * @param list List to reorder with item on top.
     * @return new ordered list, with item on top.
     */
    public static <T> List<T> putOnTop(List<T> list, List<T> items) {
        List<T> result = new ArrayList<T>(list.size() + items.size());
        result.addAll(items);
        Set<T> itemSet = Sets.newHashSet(items);
        for (T i : list) {
            if (!itemSet.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }
}
