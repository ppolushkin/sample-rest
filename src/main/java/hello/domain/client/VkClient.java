package hello.domain.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import hello.domain.vo.VkUserData;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Created by pavel on 12.08.16.
 */
@Component
public interface VkClient {

    @Nullable
    VkUserData getUserData(String vkId);

    @NotNull
    CompletableFuture<VkUserData> getUserDataAsync(String vkId);

    @NotNull
    CompletableFuture<VkUserData> getUserDataAsyncV2(String vkId);

}
