package hello.infrastructure.client;

import hello.domain.client.AsyncVkClient;
import hello.domain.vo.VkUserData;
import net.javacrumbs.futureconverter.springjava.FutureConverter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.CompletableFuture;


/**
 *
 * Created by pavel on 21.09.16.
 */
@Component
@Qualifier(value = "apacheVkClient")
public class ApacheAsyncVkClient implements AsyncVkClient {

    private AsyncRestTemplate template;

    @PostConstruct
    private void initialize() throws IOReactorException {
        AsyncClientHttpRequestFactory requestFactory = asyncClientHttpRequestFactory(HttpClients.createDefault(),
                closeableHttpAsyncClient());

        template = new AsyncRestTemplate(requestFactory);
    }

    @Override
    public CompletableFuture<VkUserData> getUserDataAsync(String vkId) {
        String url = String.format(URL, vkId);
        ListenableFuture<ResponseEntity<VkUserData>> future = template.getForEntity(URI.create(url), VkUserData.class);
        return FutureConverter.toCompletableFuture(future).
                handle((vkUserDataResponseEntity, throwable) -> {
                    if (throwable != null) {
                        throw new RuntimeException("Could not fetch data from " + url, throwable);
                    }
                    if (vkUserDataResponseEntity.getStatusCode().is2xxSuccessful()) {
                        return vkUserDataResponseEntity.getBody();
                    } else {
                        throw new RuntimeException("Could not fetch data from " + url);
                    }
                });
    }

    private AsyncClientHttpRequestFactory asyncClientHttpRequestFactory(CloseableHttpClient closeableHttpClient,
                                                                        CloseableHttpAsyncClient closeableHttpAsyncClient) {
        return new HttpComponentsAsyncClientHttpRequestFactory(closeableHttpClient, closeableHttpAsyncClient);
    }

    private CloseableHttpAsyncClient closeableHttpAsyncClient() throws IOReactorException {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(1)
                .setConnectTimeout(PerformanceSettings.CONNECTION_TIMEOUT)
                .setSoTimeout(PerformanceSettings.READ_TIMEOUT)
                .build();

        ConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        PoolingNHttpClientConnectionManager clientConnectionManager = new PoolingNHttpClientConnectionManager(connectingIOReactor);
        clientConnectionManager.setDefaultMaxPerRoute(30);
        clientConnectionManager.setMaxTotal(300);

        return HttpAsyncClientBuilder.create()
                .setConnectionManager(clientConnectionManager)
                .build();
    }

}
