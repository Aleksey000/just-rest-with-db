//package ru.nobody.rest.test;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Slf4j
//@Order(value = Ordered.HIGHEST_PRECEDENCE)
//@Component
//@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
//public class RequestCachingFilter extends OncePerRequestFilter {
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
//        log.info("REQUEST DATA:\n'{}'\n{} \n{}", request.getServletPath(), String.join("\n", getHeaders(request)), IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8));
//        filterChain.doFilter(cachedHttpServletRequest, response);
//        log.info("RESPONSE DATA:{}\n{}", response.getStatus(), String.join("\n", getHeaders(response)));
//    }
//
//    private List<String> getHeaders(HttpServletRequest request) {
//        Enumeration<String> headerNames = request.getHeaderNames();
//        LinkedList<String> strings = new LinkedList<>();
//        Iterator<String> stringIterator = headerNames.asIterator();
//
//        while (stringIterator.hasNext()) {
//            String headerName = stringIterator.next();
//            Enumeration<String> headerValues = request.getHeaders(headerName);
//
//            Iterator<String> stringIterator1 = headerValues.asIterator();
//            while (stringIterator1.hasNext()) {
//                String headerValue = stringIterator1.next();
//                strings.add(String.format("%s: %s", headerName, headerValue));
//            }
//        }
//
//        return strings;
//    }
//
//    private List<String> getHeaders(HttpServletResponse response) {
//        Collection<String> headerNames = response.getHeaderNames();
//        LinkedList<String> strings = new LinkedList<>();
//
//        for (String headerName : headerNames) {
//            Collection<String> headerValues = response.getHeaders(headerName);
//
//            for (String headerValue : headerValues) {
//                strings.add(String.format("%s: %s", headerName, headerValue));
//            }
//        }
//
//
//        return strings;
//    }
//}