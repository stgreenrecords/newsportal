/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.open.core.models;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.open.core.util.StreamProvider;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationModel {

    @SlingObject
    private Resource navigationResource;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<Page> childPages = new ArrayList<>();

    @PostConstruct
    protected void init() {
      childPages=  Optional.ofNullable(resourceResolver.adaptTo(PageManager.class))
                .map(pageManager -> pageManager.getContainingPage(navigationResource))
                .map(page -> page.listChildren(filterChildPage()))
                .map(converIteratorToStream())
                .orElse(Stream.empty())
                .collect(Collectors.toList());
    }

    public List<Page> getChildPages() {
        return childPages;
    }

    public void setChildPages(List<Page> childPages) {
        this.childPages = childPages;
    }

    private static Filter<Page> filterChildPage(){
        return page -> !page.isHideInNav();
    }

    private Function<Iterator<Page>, Stream<Page>> converIteratorToStream(){
        return iterator -> new StreamProvider().getStreamFromIterator(iterator);
    }

}
