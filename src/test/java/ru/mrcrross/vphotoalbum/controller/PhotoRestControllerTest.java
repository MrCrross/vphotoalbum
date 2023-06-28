package ru.mrcrross.vphotoalbum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.mrcrross.vphotoalbum.modules.photos.controllers.PhotoRestController;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = PhotoRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PhotoRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PhotoService photoService;
    @Autowired
    private ObjectMapper objectMapper;
    private Integer page = 1;
    private List<Integer> categories;
    private String search = "";
    private Integer owner = 1;
    @BeforeEach
    public void init() {

    }
    @Test
    public void PhotoRestController_GetPages_ReturnedListPhoto() throws Exception {
        given(photoService.getPage(this.page,this.categories, this.search, this.owner));

        ResultActions response = mockMvc.perform(get("api/photo/get")
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
