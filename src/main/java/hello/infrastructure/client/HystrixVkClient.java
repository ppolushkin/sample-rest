package hello.infrastructure.client;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.sun.istack.internal.NotNull;
import hello.domain.client.AsyncVkClient;
import hello.domain.vo.VkUserData;
import net.javacrumbs.futureconverter.java8rx.FutureConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pavel on 12.08.16.
 */
@Component
@Qualifier(value = "hystrixVkClient")
public class HystrixVkClient implements AsyncVkClient {

    private static final Logger logger = Logger.getLogger(HystrixVkClient.class.getName());


    @Autowired
    private AsyncListenableTaskExecutor executor;

    @Autowired
    private Executor commonExecutor;


    @Override
    @NotNull
    public CompletableFuture<VkUserData> getUserDataAsync(String vkId) {

        CommandVkCall commandVkCall = new CommandVkCall(vkId);
        rx.Observable<VkUserData> observable = commandVkCall.observe();
        return FutureConverter.toCompletableFuture(observable).
                thenApply((vkUserData -> {
                    if (vkUserData == null) {
                        throw new RuntimeException("data fetch error");
                    }
                    return vkUserData;
                }));

//        CompletableFuture<VkUserData> future = CompletableFuture.supplyAsync(() -> {
//            CommandVkCall commandVkCall = new CommandVkCall(vkId);
//            VkUserData vkUserData = commandVkCall.execute();
//            if (vkUserData == null) {
//                throw new RuntimeException("data fetch error");
//            }
//            return vkUserData;
//        }, commonExecutor);
//        return future;
    }

    private VkUserData mockResult() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        VkUserData result = new VkUserData();
        result.setResponse(Arrays.asList(new VkUserData.Response()));
        result.getResponse().get(0).setFirst_name("Mock");
        result.getResponse().get(0).setLast_name("Mock");
        result.getResponse().get(0).setBdate("mock");
        return result;
    }

    public class CommandVkCall extends HystrixCommand<VkUserData> {

        private final String vkId;

        public CommandVkCall(String vkId) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")).
                    andCommandPropertiesDefaults(HystrixCommandProperties.Setter().
                            withExecutionTimeoutEnabled(true).
                            withExecutionTimeoutInMilliseconds(4500)
                    ).
                    andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().
                            withCoreSize((50 * 10 + 100) / 3).withMaxQueueSize(50 * 10 + 100).withQueueSizeRejectionThreshold(50 * 10 + 100)
                    ));
            this.vkId = vkId;
        }

        @Override
        protected VkUserData run() {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format(URL, vkId);
            VkUserData result = restTemplate.getForObject(url, VkUserData.class);
            result.setVkId(vkId);
            return result;
        }

        @Override
        protected VkUserData getFallback() {
            logger.log(Level.SEVERE, "Hystrix fallback", getExecutionException());
            return null;
        }

    }


}
