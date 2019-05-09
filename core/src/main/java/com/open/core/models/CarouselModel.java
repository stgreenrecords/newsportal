package com.open.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselModel {

    private List<NewsPageModel> topNews;

    @Self
    private Resource carouselResource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ChildResource
    private String[] pathToNewsPage;

    @PostConstruct
    protected void init(){
        topNews = Arrays.stream(pathToNewsPage).map(path -> resourceResolver.getResource(path))
                .map(resource -> resource.adaptTo(NewsPageModel.class))
                .sorted(Comparator.comparingInt(NewsPageModel::getLikes).reversed())
                .limit(2)
                .collect(toList());
    }

    public List<NewsPageModel> getTopNews() {
        return topNews;
    }
}
