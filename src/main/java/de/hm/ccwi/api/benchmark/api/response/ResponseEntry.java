package de.hm.ccwi.api.benchmark.api.response;


public class ResponseEntry {

    private String entry = "";
    private Double confidence = 0D;
    private boolean isHashtag;

    public boolean getIsHashtag() {
        return isHashtag;
    }

    public void setIsHashtag(boolean entity) {
        this.isHashtag = entity;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

}
