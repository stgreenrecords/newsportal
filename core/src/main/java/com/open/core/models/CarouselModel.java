package com.open.core.models;

import com.open.core.util.StreamProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import javax.jcr.query.Query;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselModel {

    private List<NewsPageModel> topNews;

    private static final String QUERY = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(s,'%s') AND s.[jcr:created] > CAST('%sT02:30:00.000+02:00' AS DATE) ORDER BY s.[likes] desc ";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Self
    private Resource carouselResource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ChildResource
    private String[] pathToNewsPage;

    @ValueMapValue
    private long numberOfPages;

    @ValueMapValue
    private String pathToNewsRoot;

    @ValueMapValue
    private Calendar dateOfArticle;

    private List<NewsPageModel> createListFromPathToNewsRootProperty() {
        return Optional.ofNullable(pathToNewsRoot)
                .map(path -> resourceResolver.findResources(String.format(QUERY, path, dateParser()), Query.JCR_SQL2))
                .map(iterator -> new StreamProvider().getStreamFromIterator(iterator))
                .orElse(Stream.empty())
                .map(resource -> resource.adaptTo(NewsPageModel.class))
                .limit(numberOfPages)
                .collect(toList());
    }

    private List<NewsPageModel> createListFromPathToNewsPageProperty() {
        return Optional.ofNullable(pathToNewsPage)
                .map(path -> Arrays.stream(path))
                .orElse(Stream.empty())
                .map(path -> resourceResolver.getResource(path))
                .map(resource -> resource.adaptTo(NewsPageModel.class))
                .collect(toList());
    }

    public List<NewsPageModel> getTopNews() {
        return Stream.of(createListFromPathToNewsPageProperty(), createListFromPathToNewsRootProperty())
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private String dateParser() {
        return Optional.ofNullable(dateOfArticle)
                .map(dateOfArticle -> new SimpleDateFormat(DATE_PATTERN).format(dateOfArticle.getTime()))
                .orElse(StringUtils.EMPTY);
    }

}