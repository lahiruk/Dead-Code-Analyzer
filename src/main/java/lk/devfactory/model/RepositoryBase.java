package lk.devfactory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryBase {
    private String url;

    @JsonProperty
    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = name;
    }
}
