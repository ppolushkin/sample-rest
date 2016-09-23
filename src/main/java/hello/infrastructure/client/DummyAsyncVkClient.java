package hello.infrastructure.client;

import hello.domain.client.AsyncVkClient;
import hello.domain.vo.VkUserData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by pp on 23.09.2016.
 */
@Component
@Qualifier(value = "dummyVkClient")
public class DummyAsyncVkClient implements AsyncVkClient {

    private Executor executor;

    @PostConstruct
    private void initialize() {
        executor = Executors.newFixedThreadPool(50);
    }

    @Override
    public CompletableFuture<VkUserData> getUserDataAsync(String vkId) {

        CompletableFuture<VkUserData> future = CompletableFuture.supplyAsync(() -> {
            RestTemplate restTemplate = new RestTemplate();

            SimpleClientHttpRequestFactory rf =
                    (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setConnectTimeout(PerformanceSettings.CONNECTION_TIMEOUT);
            rf.setReadTimeout(PerformanceSettings.READ_TIMEOUT);

            String url = String.format(URL, vkId);
            VkUserData result = restTemplate.getForObject(url, VkUserData.class);
            result.setVkId(vkId);
            return result;
        }, executor);

        return future;
    }

}
