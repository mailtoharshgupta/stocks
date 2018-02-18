package com.exchange.stockmarket.web.controller;

import com.exchange.stockmarket.StockMarketApplication;
import com.exchange.stockmarket.core.exception.ResourceNotFoundException;
import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.core.util.StockMarketUtil;
import com.exchange.stockmarket.services.web.IStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Harsh Gupta on {2/11/18}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StockMarketWebServiceController.class)
@ContextConfiguration(classes = StockMarketApplication.class)
public class StockMarketWebServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "stockService")
    private IStockService stockService;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetAllStocks() throws Exception {
        List<StockSRO> sampleStocks = createStock(Arrays.asList("Apple", "Intuit"));
        Page<StockSRO> page = new PageImpl<>(sampleStocks);
        when(stockService.getAllStocks(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get(StockMarketWebServiceController.URL + "/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is(sampleStocks.get(0).getName())))
                .andExpect(jsonPath("$.content[1].name", is(sampleStocks.get(1).getName())));
    }

    @Test
    public void testPaginatedGetAll() throws Exception {
        List<StockSRO> sampleStocks = createStock(Arrays.asList("Apple"));
        Page<StockSRO> page = new PageImpl<>(sampleStocks);
        when(stockService.getAllStocks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(StockMarketWebServiceController.URL + "/")
                .param("pageNumber", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(sampleStocks.get(0).getName())));

    }

    @Test
    public void testGetByStockIdExists() throws Exception {

        when(stockService.getStockById(1L)).thenReturn(StockSRO.builder().id(1L).name("Apple").price(100.00).build());
        mockMvc.perform(get(StockMarketWebServiceController.URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Apple")))
                .andExpect(jsonPath("$.price", is(100.00)))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void testGetByStockIdWhenDoesNotExist() throws Exception {
        when(stockService.getStockById(1L)).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get(StockMarketWebServiceController.URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStock() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(100.00).build();
        StockSRO responseStock = StockSRO.builder().name("Apple").id(1L).price(100.00).updated(StockMarketUtil.getCurrentTime()).build();
        when(stockService.saveStock(requestStock)).thenReturn(responseStock);

        mockMvc.perform(
                post(StockMarketWebServiceController.URL + "/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.price", is(100.00)));
    }

    @Test
    public void testCreateStockWithoutName() throws Exception {
        StockSRO requestStock = StockSRO.builder().price(100.00).build();

        mockMvc.perform(
                post(StockMarketWebServiceController.URL + "/").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStockWithoutPrice() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").build();

        mockMvc.perform(
                post(StockMarketWebServiceController.URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStockWithId() throws Exception {
        StockSRO requestStock = StockSRO.builder().id(1L).build();

        mockMvc.perform(
                post(StockMarketWebServiceController.URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateStockWithUpdated() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(100.00).updated(StockMarketUtil.getCurrentTime()).build();

        mockMvc.perform(
                post(StockMarketWebServiceController.URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateStockPrice() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(150.00).build();
        StockSRO responseStock = StockSRO.builder().name("Apple").price(150.00).id(1L).updated(StockMarketUtil.getCurrentTime()).build();

        when(stockService.getStockById(1L)).thenReturn(requestStock);
        when(stockService.updateStock(requestStock)).thenReturn(responseStock);

        mockMvc.perform(
                put(StockMarketWebServiceController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(150.00)));
    }

    @Test
    public void testUpdateStockPriceForDoesNotExist() throws Exception {
        StockSRO requestStock = StockSRO.builder().name("Apple").price(140.00).build();
        when(stockService.getStockById(1L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(
                put(StockMarketWebServiceController.URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestStock)))
                .andExpect(status().isNotFound());
    }


    private List<StockSRO> createStock(List<String> stockNames) {
        return stockNames.stream()
                .map(name -> StockSRO.builder().name(name).price(100.00).build())
                .collect(Collectors.toList());
    }


}