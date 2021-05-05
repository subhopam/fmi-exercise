package search;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TextSearcher implements a search interface to an underlying text file
 * consisting of the single method {@link #search(String, int)}.
 */
public class TextSearcher {
    private LinkedList<String> words;

    /**
     * Initializes the text searcher with the contents of a text file.
     * The current implementation just reads the contents into a string
     * and passes them to #init().  You may modify this implementation if you need to.
     *
     * @param f Input file.
     * @throws IOException
     */
    public TextSearcher(File f) throws IOException {

        FileReader r = null;
        try {
            r = new FileReader(f);
            StringWriter w = new StringWriter();
            char[] buf = new char[4096];
            int readCount;

            while ((readCount = r.read(buf)) > 0) {
                w.write(buf, 0, readCount);
            }

            init(w.toString());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    /**
     * Initializes any internal data structures that are needed for
     * this class to implement search efficiently.
     */
    protected void init(String fileContents) {

        /*
         * TODO
         */
        words = Arrays.stream(fileContents.split("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})")).sequential()
                //.map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedList::new));

    }

    /**
     * @param queryWord    The word to search for in the file contents.
     * @param contextWords The number of words of context to provide on
     *                     each side of the query word.
     * @return One context string for each time the query word appears in the file.
     */
    public String[] search(String queryWord, int contextWords) {

        /*
         * TODO
         */
        List<String> res = new ArrayList<>();
        int startIdx = 0, endIndex=words.size(),idx=0;
		while (idx!=-1) {
			idx = words.indexOf(queryWord);
			if(idx==-1) break;
			res.add(searchHelper(contextWords, idx));
			words.remove(idx);
		}
		String[] ret = res.toArray(new String[res.size()]);
		return ret;
    }

	private String searchHelper(int contextWords, int idx) {

		LinkedList res= new LinkedList();
		ListIterator<String> nextListIterator = null;
		ListIterator<String> prevListIterator = null;
		if (idx +1 < words.size() && words.listIterator(idx).hasNext()) {
			nextListIterator = words.listIterator(idx +1);

		}

		if (idx-1>=0 && words.listIterator(idx).hasPrevious()) {
			prevListIterator = words.listIterator(idx);
		}
		int i=0;
		while (null != nextListIterator && i < contextWords && nextListIterator.hasNext()) {
			String temp=nextListIterator.next();
			res.addLast(temp);
			if(!temp.matches("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})"))
				i++;
		}
		res.addFirst(words.get(idx));
		System.out.println(i);
		i=contextWords-1;
		while (null != prevListIterator && i >= 0 && prevListIterator.hasPrevious()) {
			String temp=prevListIterator.previous();
			res.addFirst(temp);
			if(!temp.matches("\\s+|(?=\\p{Punct})|(?<=\\p{Punct})"))
				i--;
		}

		return String.valueOf(res.stream().collect(Collectors.joining(" ")));
	}

	// Any needed utility classes can just go in this file.
}


