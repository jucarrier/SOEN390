package Models;


import androidx.annotation.NonNull;

import java.io.Serializable;
//to store photo references, then pass onto results to later display

public class Photos implements Serializable {
    private String photoReference;

    private String height;

    private String[] htmlAttributions;

    private String width;

    public String getPhotoReference() {
        return this.photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String[] getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(String[] htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @NonNull
    @Override
    public String toString() {
        return "Results [photo_reference = " + photoReference + ", height = " + height + ", html_attributions = " + htmlAttributions + ", width = " + width + "]";
    }
}