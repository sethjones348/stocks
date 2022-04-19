package com.sethj.stocks.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sethj.stocks.controller.StocksResponse;
import com.sethj.stocks.model.ClosingPrice;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StocksService {

    private static final String POLYGON_IO_DAILY_OPEN_CLOSE_URL = "https://api.polygon.io/v1/open-close";

    private final String apiKey;

    public StocksService(){
        apiKey = System.getenv("POLYGON_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Make sure you set your polygon API key in the POLYGON_API_KEY environment variable!");
        }
    }

    public ResponseEntity<StocksResponse> getStocksBySymbol(String symbol){
        List<ClosingPrice> closingPrices = getClosingPricesForTheLast11Days(symbol);

        double averageClosingPrice = 
            calculateAverage(closingPrices.stream()
                .filter(c -> c.getClosingPrice().doubleValue() > 0.0)
                .map(c -> c.getClosingPrice())
                .collect(Collectors.toList()));

        return new ResponseEntity<>(new StocksResponse(String.valueOf(averageClosingPrice), closingPrices), HttpStatus.OK);
    }

    private double calculateAverage(List<BigDecimal> numbers){
        double sum = 0.0;

        for(BigDecimal number : numbers){
            sum += number.doubleValue();
        }

        return sum/numbers.size();
    }

    private List<ClosingPrice> getClosingPricesForTheLast11Days(String symbol){
        List<String> dateListOfLast11Days = getDateListLast11Days();

        List<ClosingPrice> results = new ArrayList<>();

        dateListOfLast11Days.forEach(date -> {
            ClosingPrice closingPrice = fetchOpenCloseForDate(date, symbol);

            if(closingPrice.getClosingPrice() != null){
                results.add(closingPrice);
                System.out.println("Date: " + closingPrice.getDate() + " Closing Price: " + closingPrice.getClosingPrice());
            }
        });

        return results;
    }

    private ClosingPrice fetchOpenCloseForDate(String date, String symbol) {
        StringBuilder apiCallUrl = new StringBuilder();
        ClosingPrice closingPrice = new ClosingPrice(date, BigDecimal.ZERO);

        apiCallUrl
            .append(POLYGON_IO_DAILY_OPEN_CLOSE_URL)
            .append("/").append(symbol)
            .append("/").append(date)
            .append("?adjusted=true")
            .append("&apiKey=").append(this.apiKey);

        try{    
            URL url = new URL(apiCallUrl.toString());
    
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            Reader streamReader = null;

            if (responseCode != 200) {
                streamReader = new InputStreamReader(conn.getErrorStream());
            } else {
                streamReader = new InputStreamReader(conn.getInputStream());
            }
            
            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();

            conn.disconnect();

            System.out.println(content.toString());
        
            JSONObject obj = new JSONObject(content.toString());

            String status = obj.getString("status");
            BigDecimal close = null;

            if(status.equals("OK")){
                close = obj.getBigDecimal("close");
            }

            System.out.println("Closing price: " + close);
            closingPrice = new ClosingPrice(date, close);

        } catch (MalformedURLException m){
            System.out.println("URL was formatted incorrectly");
        } catch (Exception e){
            System.out.println(e);
        }

        return closingPrice;
    }

    private List<String> getDateListLast11Days(){
        LocalDate today = LocalDate.now();
        LocalDate elevenDaysAgo = today.minusDays(11); 
    
        return elevenDaysAgo
            .datesUntil(today)
            .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .collect(Collectors.toList());
    }
}