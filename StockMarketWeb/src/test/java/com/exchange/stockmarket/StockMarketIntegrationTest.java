package com.exchange.stockmarket;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.util.StockMarketUtil;
import com.exchange.stockmarket.services.repository.IStockRepository;
import com.exchange.stockmarket.services.web.IStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Harsh Gupta on {2/11/18}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = StockMarketApplication.class)
@AutoConfigureMockMvc
public class StockMarketIntegrationTest {

    private static final String URL = "/api/stocks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IStockRepository stockRepository;

    private ObjectMapper mapper;

    private List<StockSRO> persistedSROs;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        persistedSROs = createStock(Arrays.asList("Apple", "Intuit", "Alphabet", "Amazon"));
    }

    @Test
    public void testPaginatedGetAll() throws Exception {
        mockMvc.perform(get(URL + "/")
                .param("pageNumber", "0")
                .param("size", "1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(persistedSROs.get(0).getName())))
                .andExpect(content().string(containsString(persistedSROs.get(1).getName())));
    }

    @Test
    public void testPaginatedGetAllByPageNumber() throws Exception {

        mockMvc.perform(get(URL + "/")
                .param("pageNumber", "1")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(persistedSROs.get(2).getName())))
                .andExpect(jsonPath("$.content[1].name", is(persistedSROs.get(3).getName())));

    }

    @Test
    public void testGetByStockIdExists() throws Exception {
        StockSRO persistedStock = stockService.saveStock(StockSRO.builder().name("ING").price(150.00).build());
        mockMvc.perform(get(URL + "/" + persistedStock.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ING")))
                .andExpect(jsonPath("$.price", is(150.00)))
                .andExpect(jsonPath("$.id", is(persistedStock.getId().intValue())));
    }

    @Test
    public void testGetByStockIdWhenDoesNotExist() throws Exception {
        mockMvc.perform(get(URL + "/10000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveStock() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("NCR").price(150.00).build();

        mockMvc.perform(
                post(URL + "/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(requestStock.getName())))
                .andExpect(jsonPath("$.price", is(requestStock.getPrice())))
                .andExpect(jsonPath("$.id", anything()));
    }

    @Test
    public void testCreateStockWithoutName() throws Exception {
        StockSRO requestStock = StockSRO.builder().price(100.00).build();

        mockMvc.perform(
                post(URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStockWithoutPrice() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").build();

        mockMvc.perform(
                post(URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStockWithId() throws Exception {
        StockSRO requestStock = StockSRO.builder().id(1L).build();

        mockMvc.perform(
                post(URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStockWithUpdated() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(100.00).updated(StockMarketUtil.getCurrentTime()).build();

        mockMvc.perform(
                post(URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateStockPrice() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(150.00).build();
        StockSRO persisted = stockService.saveStock(requestStock);
        requestStock.setPrice(110.00);
        mockMvc.perform(
                put(URL + "/" + persisted.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(requestStock.getPrice())));
    }

    @Test
    public void testUpdateStockPriceForDoesNotExist() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(140.00).build();

        mockMvc.perform(
                put(URL + "/10000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isNotFound());
    }


    private List<StockSRO> createStock(List<String> stockNames) {
        cleanUp();
        return stockNames.stream()
                .map(name -> StockSRO.builder().name(name).price(100.00).build())
                .map(sro -> stockService.saveStock(sro))
                .collect(Collectors.toList());
    }

    private void cleanUp() {
        stockRepository.deleteAll();
    }
}

