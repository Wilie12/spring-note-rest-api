package com.nn.spring_note_rest_api.note.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MarkdownService {

    public String resourceToHtml(Resource resource) {
        String resourceAsString;
        try {
            resourceAsString = resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ResourceAccessException("Resource not found");
        }

        Parser parser = Parser.builder().build();
        Node document = parser.parse(resourceAsString);
        return HtmlRenderer.builder().build().render(document);
    }
}
