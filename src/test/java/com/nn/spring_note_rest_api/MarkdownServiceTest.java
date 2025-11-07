package com.nn.spring_note_rest_api;

import com.nn.spring_note_rest_api.note.service.MarkdownService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

public class MarkdownServiceTest {
    private MarkdownService markdownService;

    @BeforeEach
    public void setUp() {
        markdownService = new MarkdownService();
    }

    @Test
    public void resourceToHtmlShouldWork() {
        // given
        String fileContent = "# Heading 1\n**bold**";
        Resource resource = new ByteArrayResource(fileContent.getBytes());

        // when
        String html = markdownService.resourceToHtml(resource);

        // then
        String expectedHtml = "<h1>Heading 1</h1>\n<p><strong>bold</strong></p>\n";
        assertThat(html).isEqualTo(expectedHtml);
    }
}
