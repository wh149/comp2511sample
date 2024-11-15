package q15.confluence;

import java.util.ArrayList;
import java.util.List;

public class ConfluencePage extends ConfluenceNode {
    private String content;
    private String title;
    private String status;
    private List<ConfluenceAuthor> contributors = new ArrayList<>();

    public ConfluencePage() {
        this.status = "Editing";
    }

    public String interact(String using, ConfluenceAuthor interactor) {
        switch (status) {
            case "Editing":
                String content = using;
                if (content.isEmpty()) {
                    throw new IllegalArgumentException("Document content cannot be empty");
                }

                if (!contributors.contains(interactor)) {
                    contributors.add(interactor);
                }

                this.content = content;

                return "";
            case "Viewing":
                String result = "==== Draft in Progress === \n" +
                        this.content.length() + " characters long \n" +
                        "=============================\n" + this.content;
                return result;
            case "Publishing":
                String title = using;
                if (title.isEmpty()) {
                    throw new IllegalArgumentException("Title cannot be empty");
                } else if (title.length() > 50) {
                    throw new IllegalArgumentException("Title cannot be > 50 characters long");
                }

                this.title = title;
                return "";
            case "Published":
                String result2 = "==== " + this.title + " === \n" +
                        "By " + getContributors() + "\n" +
                        "=============================\n" + this.content;
                return result2;
            default:
                return null;
        }
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    private String getContributors() {
        String contributors = "";
        for (ConfluenceAuthor contributor : this.contributors) {
            contributors += contributor.getName() + ", ";
        }
        contributors = contributors.substring(0, contributors.length() - 2);
        return contributors;
    }
}