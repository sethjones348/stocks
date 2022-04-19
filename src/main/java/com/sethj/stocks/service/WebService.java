package com.sethj.stocks.service;

import com.sethj.stocks.model.ClosingPrice;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class WebService {
    private static final String POLYGON_IO_DAILY_OPEN_CLOSE_URL = "https://api.polygon.io/v1/open-close";

    private final String apiKey;

    public WebService() {
        apiKey = System.getenv("POLYGON_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Make sure you set your polygon API key in the POLYGON_API_KEY environment variable!");
        }
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public ClosingPrice fetchOpenCloseForDate(String date, String symbol) {
        StringBuilder apiCallUrl = new StringBuilder();
        ClosingPrice closingPrice = new ClosingPrice(date, BigDecimal.ZERO);

        apiCallUrl
                .append(POLYGON_IO_DAILY_OPEN_CLOSE_URL)
                .append("/").append(symbol)
                .append("/").append(date)
                .append("?adjusted=true")
                .append("&apiKey=").append(this.apiKey);

        try {
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

            if (status.equals("OK")) {
                close = obj.getBigDecimal("close");
            }

            System.out.println("Closing price: " + close);
            closingPrice = new ClosingPrice(date, close);

        } catch (MalformedURLException m) {
            System.out.println("URL was formatted incorrectly");
        } catch (Exception e) {
            System.out.println(e);
        }

        return closingPrice;
    }

}
