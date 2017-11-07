package de.hm.ccwi.api.benchmark.api.response;


public class ResponseEntry {

    private String entry = "";
    private Double confidence = 0D;
    private boolean isHashtag;

    /**
     * @return Boolean if it is also a Hashtag
     */
    public boolean getIsHashtag () {
        return isHashtag;
    }

    /**
     * @param Set Boolean if it is also Hashtag
     */
    public void setIsHashtag (boolean entity) {
        this.isHashtag = entity;
    }

    /**
     * @param entity Set Name of the Entity
     */
    public void setEntry (String entry) {
        this.entry = entry;
    }

    /**
     * @param confidence Set Confidence of the Entity
     */
    public void setConfidence (Double confidence) {
        this.confidence = confidence;
    }

    /**
     * @return Name of the Entity
     */
    public String getEntry () {
        return entry;
    }


    /**
     * @return Confidence of the Entity
     */
    public Double getConfidence () {
        return confidence;
    }

}
