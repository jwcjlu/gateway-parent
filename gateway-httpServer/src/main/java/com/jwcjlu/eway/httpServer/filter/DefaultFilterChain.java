package com.jwcjlu.gateway.httpServer.filter;


import com.jwcjlu.gateway.httpServer.handler.HttpHandlerContent;
import com.jwcjlu.gateway.httpServer.exception.FilterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultFilterChain implements FilterChain {
    private Logger logger = LoggerFactory.getLogger(DefaultFilterChain.class);
    private int pos;
    List<HttpFilter> filterList = new ArrayList<>();

    public DefaultFilterChain(List<HttpFilter> filterList, List<FilterType> types) {
        if (!Objects.isNull(filterList)) {
            this.filterList = filterList.stream().filter(f -> types.contains(f.getFilterType()))
                .sorted((f1, f2) -> {
                    if (f1 == null) {
                        return 1;
                    }
                    if (f2 == null) {
                        return -1;
                    }
                    int order = f1.getFilterType().ordinal() - f2.getFilterType().ordinal();
                    return order != 0 ? order : f1.getOrder() - f2.getOrder();
                }).collect(Collectors.toList());
        }

    }

    public DefaultFilterChain(List<HttpFilter> filterList) {
        if (!Objects.isNull(filterList)) {
            this.filterList = filterList.stream().
                sorted((f1, f2) -> {
                    if (f1 == null) {
                        return 1;
                    }
                    if (f2 == null) {
                        return -1;
                    }
                    int order = f1.getFilterType().ordinal() - f2.getFilterType().ordinal();
                    return order != 0 ? order : f1.getOrder() - f2.getOrder();
                }).collect(Collectors.toList());
        }

    }

    @Override
    public void doFilter(HttpHandlerContent content) throws FilterException {
        while (pos < filterList.size()) {
            HttpFilter filter = filterList.get(pos);
            pos++;
            try {
                filter.doFilter(content, this);
            } catch (Throwable e) {
                logger.error("DefaultFilterChain  error", e);
                filter.decorationesponse(content);
                throw new FilterException(e.getMessage());
            }
        }

    }


}
