package Models;


import java.io.Serializable;
import java.util.List;

public class MyPlaces implements Serializable {
    //stores all nearby places result in list format
    private String next_page_token;

    private List<Results> results;

    private String[] html_attribute;

    private String status;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String[] getHtml_attribute() {
        return html_attribute;
    }

    public void setHtml_attribute(String[] html_attribute) {
        this.html_attribute = html_attribute;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Results [next_page_token = " + next_page_token + ", results = " + results + ", html_attributions = " + html_attribute + ", status = " + status + "]";
    }
}
