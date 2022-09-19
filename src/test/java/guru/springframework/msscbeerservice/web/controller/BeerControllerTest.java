package guru.springframework.msscbeerservice.web.controller;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
class BeerControllerTest {

   @Autowired
   MockMvc mockMvc;

   @Autowired
   ObjectMapper objectMapper;

   @MockBean
   BeerRepository beerRepository;

   @Test
   void getBeerById() throws Exception {

      mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andDo(document("v1/beer", pathParameters(
                  parameterWithName("beerId").description("UUID of desired beer to get")
            )));

   }

   @Test
   void saveNewBeer() throws Exception {

      BeerDto beerDto = getValidBeerDto();
      String beerDtoJson = objectMapper.writeValueAsString(beerDto);

      mockMvc.perform(post("/api/v1/beer/").contentType(MediaType.APPLICATION_JSON).content(beerDtoJson)).andExpect(status().isCreated());
   }

   @Test
   void updateBeerById() throws Exception {
      BeerDto beerDto = getValidBeerDto();
      String beerDtoJson = objectMapper.writeValueAsString(beerDto);

      mockMvc
            .perform(put("/api/v1/beer/" + UUID.randomUUID().toString()).contentType(MediaType.APPLICATION_JSON).content(beerDtoJson))
            .andExpect(status().isNoContent());
   }

   BeerDto getValidBeerDto() {
      return BeerDto.builder().beerName("Xavis").beerStyle(BeerStyleEnum.LAGER).price(new BigDecimal("2.99")).upc(4548454L).build();
   }
}