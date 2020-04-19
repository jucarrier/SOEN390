package Models;


import java.io.Serializable;
import java.util.List;

public class MyPlaces implements Serializable {
    //stores all nearby places result in list format
    private String nextPageToken;

    private List<Results> results;

    private String[] htmlAttribute;

    private String status;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String[] getHtmlAttribute() {
        return htmlAttribute;
    }

    public void setHtmlAttribute(String[] htmlAttribute) {
        this.htmlAttribute = htmlAttribute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Results [next_page_token = " + nextPageToken + ", results = " + results + ", html_attributions = " + htmlAttribute + ", status = " + status + "]";
    }
}
