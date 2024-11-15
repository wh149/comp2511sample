package q15.confluence;

import java.util.Iterator;

public class ConfluenceSpace extends ConfluenceNode implements Iterable<ConfluencePage> {

    public void addSubnode(ConfluenceNode node) {

    }

    public int getNumPages() {
        return 0;
    }

    @Override
    public Iterator<ConfluencePage> iterator() {
        return null;
    }
}