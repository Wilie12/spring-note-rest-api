package com.nn.spring_note_rest_api;

import com.nn.spring_note_rest_api.language.service.LanguageService;
import com.nn.spring_note_rest_api.note.controller.NoteController;
import com.nn.spring_note_rest_api.note.service.MarkdownService;
import com.nn.spring_note_rest_api.note.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private NoteService noteService;
    @MockitoBean
    private LanguageService languageService;
    @MockitoBean
    private MarkdownService markdownService;

    @Test
    public void uploadNoteShouldWork() throws Exception {
        String name = "file";
        String content = "Test file content.";
        String originalName = "test.txt";
        String contentType = "text/plain";

        MockMultipartFile multipartFile = new MockMultipartFile(
                name,
                originalName,
                contentType,
                content.getBytes()
        );

        mvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/v1/notes")
                        .file(multipartFile))
                .andExpect(status().isOk());

        verify(noteService).uploadNote(multipartFile);
    }
}
