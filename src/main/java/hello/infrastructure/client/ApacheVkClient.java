package hello.infrastructure.client;

import hello.domain.client.AsyncVkClient;
import hello.domain.vo.VkUserData;
import net.javacrumbs.futureconverter.springjava.FutureConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.CompletableFuture;


/**
 * Created by pavel on 21.09.16.
 */
@Component
@Qualifier(value = "apacheVkClient")
public class ApacheVkClient implements AsyncVkClient {


    private AsyncRestTemplate template;

    @PostConstruct
    private void initialize() {
        template = new AsyncRestTemplate();
    }

    @Override
    public CompletableFuture<VkUserData> getUserDataAsync(String vkId) {
        String url = String.format(URL, vkId);
        ListenableFuture<ResponseEntity<VkUserData>> future = template.getForEntity(URI.create(url), VkUserData.class);
        return FutureConverter.toCompletableFuture(future).
                thenApply(vkUserDataResponseEntity -> {
                    if (vkUserDataResponseEntity.getStatusCode().is2xxSuccessful()) {
                        return vkUserDataResponseEntity.getBody();
                    } else {
                        throw new RuntimeException("Could not fetch data from " + url);
                    }
                });
    }

}
