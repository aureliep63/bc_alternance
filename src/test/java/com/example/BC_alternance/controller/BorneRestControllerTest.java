//package com.example.BC_alternance.controller;
//
//import com.example.BC_alternance.dto.BorneDto;
//import com.example.BC_alternance.dto.LieuxDto;
//import com.example.BC_alternance.model.Borne;
//import com.example.BC_alternance.service.BorneService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.List;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Test traversant : il teste le contrôleur REST qui fait appel au vrai service, etc
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//public class BorneRestControllerTest {
//    @Autowired
//    MockMvc mockMvc; // cet objet imite ce que fait Postman, Hoppscotch, Insomnia, Swagger ou le front Angular, React ou Vue
//
//    @Autowired
//    BorneService borneService;
//
//    @Autowired
//    ObjectMapper objectMapper; // cet objet va sérialiser des objets DTO
//
//    @BeforeEach
//    void beforeEach() {
//        List<Borne> bornes = borneService.getAllBornes();
//        for (Borne borne : bornes) {
//            borneService.deleteBorne(borne.getId());
//        }
//    }
//
//   /* @Test
//    void testPostBorne() throws Exception {
//
//        String nom = "test";
//        LieuxDto lieuxDto = new LieuxDto(1L, null, null, null);
//        BorneDto borneDto = new BorneDto(1L, null, 0, null, null, null, 0, 0, 0,null);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/api/bornes")
//                // On place dans le corps de la requête la version Json de l'objet borneDto
//                .content(objectMapper.writeValueAsString(borneDto))
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.nom").value(nom))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lieu.nom").value("Dijon"))
//                .andExpect(status().isCreated())
//                .andDo(MockMvcResultHandlers.print());
//    }
//*/
//
//}
