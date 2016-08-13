package hello.infrastructure.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import hello.domain.client.VkClient;
import hello.domain.vo.VkUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static net.javacrumbs.futureconverter.springjava.FutureConverter.toCompletableFuture;


/**
 * Created by pavel on 12.08.16.
 */
@Component
public class VkClientImpl implements VkClient {

    private final static String URL = "https://api.vk.com/method/users.get?user_ids=%s&fields=photo_50,city,verified,sex,bdate,interests&v=5.8";

    private final static int CONNECTION_TIMEOUT = 1000;

    private final static int READ_TIMEOUT = 3500;

    @Autowired
    private AsyncListenableTaskExecutor executor;

    @Override
    @Nullable
    public VkUserData getUserData(String vkId) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        String url = String.format(URL, vkId);

        VkUserData result = restTemplate.getForObject(url, VkUserData.class);
        return result;
    }

    @Override
    @NotNull
    public CompletableFuture<VkUserData> getUserDataAsync(String vkId) {
        CompletableFuture<VkUserData> future = CompletableFuture.supplyAsync(() -> {

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
            requestFactory.setReadTimeout(READ_TIMEOUT);

            RestTemplate restTemplate = new RestTemplate(requestFactory);
            String url = String.format(URL, vkId);
            VkUserData result = restTemplate.getForObject(url, VkUserData.class);
            result.setVkId(vkId);
            return result;
        });
        return future;
    }

    @Override
    public CompletableFuture<VkUserData> getUserDataAsyncV2(String vkId) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        requestFactory.setTaskExecutor(executor);

        AsyncRestTemplate restTemplate = new AsyncRestTemplate(requestFactory);
        String url = String.format(URL, vkId);
        ListenableFuture<ResponseEntity<VkUserData>> listenableFuture = restTemplate.getForEntity(url, VkUserData.class);
        return toCompletableFuture(listenableFuture).
                thenApply(httpEntity -> {
                    VkUserData vkUserData = httpEntity.getBody();
                    vkUserData.setVkId(vkId);
                    return vkUserData;
                });
    }

}
