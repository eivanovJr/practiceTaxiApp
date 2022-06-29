package com.example.taxiapp.utils;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.mockito.internal.util.StringUtil;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class JsonUtilTest {

    private static final String yandexTariffJson = "{\"tariffs\":[{\"name\":\"econom\",\"supported_requirements\":[{\"name\":\"childchair\"}]}]}\n";

    private static final String correctPath = "https://taxi-routeinfo.taxi.yandex.net/zone_info?ll=36,54&clid=ak220627&apikey=BqECcAddYtUbJzHcnrrsLPiRlrCzepDNMF";

    @Test
    public void getJSONObjectFromURL() throws JSONException, IOException, ExecutionException, InterruptedException {
        JsonUtil jsonUtil = new JsonUtil();
        try {
            assert jsonUtil.getJSONObjectFromURL(correctPath).equals(yandexTariffJson);
        }
        catch (Exception e) {
            throw new Error("Should not throw exception");
        }
    }
}