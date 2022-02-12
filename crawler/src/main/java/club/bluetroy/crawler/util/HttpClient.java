package club.bluetroy.crawler.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import static club.bluetroy.crawler.util.HttpConnectionUtils.getConnection;

/**
 * @author heyixin
 */
@Slf4j
@UtilityClass
public class HttpClient {
    private static final ExecutorService HTTP_GET_SERVICE;
    private static final Integer NOT_SUCCESS_RESPONSE_CODE = 300;

    static {
        HTTP_GET_SERVICE = new ThreadPoolExecutor(0, 5, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactoryBuilder()
                .setNameFormat("HTTP-GET-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    }

    public static Future<String> getInFuture(String url) {
        return HTTP_GET_SERVICE.submit(() -> getNow(url));
    }

    public static String getNow(String url) throws Exception {
        log.info("getNow  " + url);
        HttpURLConnection httpURLConnection = getConnection(url);
        if (httpURLConnection.getResponseCode() >= NOT_SUCCESS_RESPONSE_CODE) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        return getInputString(httpURLConnection);
    }

    private static String getInputString(HttpURLConnection connection) throws IOException {
        return new String(getInputBytes(connection), StandardCharsets.UTF_8);
    }

    private static byte[] getInputBytes(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream()) {
            return StreamUtils.copyToByteArray(inputStream);
        }
    }

    public static String post(String url, String params) throws Exception {
        HttpURLConnection connection = getConnection(url);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(params.getBytes());
        }
        if (connection.getResponseCode() >= NOT_SUCCESS_RESPONSE_CODE) {
            throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
        }
        return getInputString(connection);
    }
}

