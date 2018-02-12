package com.exchange.stockmarket.web.controller;

import com.exchange.stockmarket.base.model.StockSRO;
import com.exchange.stockmarket.services.web.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Harsh Gupta on {2/11/18}
 */
@Controller
@RequestMapping(StockMarketWebAdminController.PATH)
public class StockMarketWebAdminController {

    public static final String PATH = "/";

    private final IStockService stockService;

    @Autowired
    public StockMarketWebAdminController(IStockService stockService) {
        this.stockService = stockService;
    }


    @GetMapping(value = {"", "/"})
    public String getDashboard(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                               @RequestParam(name = "size", defaultValue = "10") int size, Map<String, Object> model) {
        PageRequest pageRequest = new PageRequest(pageNumber, size);
        Page<StockSRO> page = stockService.getAllStocks(pageRequest);
        model.put("message", "Hello Harsh");
        model.put("stocks", page.getContent());
        model.put("pages", IntStream.range(0, page.getTotalPages()).boxed().collect(Collectors.toList()));
        model.put("currentPage", pageNumber);
        model.put("size", size);
        return "dashboard";
    }
}
