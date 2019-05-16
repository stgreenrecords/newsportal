package com.open.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsPageModel {

    @ValueMapValue
    private String mainTitle;
    @ValueMapValue
    private String pathToMainImage;
    @ValueMapValue
    private int likes;

    public String getMainTitle() {
        return mainTitle;
    }

    public String getPathToMainImage() {
        return pathToMainImage;
    }

    public int getLikes() {
        return likes;
    }
}
