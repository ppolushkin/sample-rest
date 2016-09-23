package hello.domain.client;

import com.sun.istack.internal.NotNull;
import hello.domain.vo.VkUserData;

import java.util.concurrent.CompletableFuture;

/**
 * Created by pavel on 21.09.16.
 */
public interface AsyncVkClient {

    String URL = "https://api.vk.com/method/users.get?user_ids=%s&fields=photo_50,city,verified,sex,bdate,interests&v=5.8";

    @NotNull
    CompletableFuture<VkUserData> getUserDataAsync(String vkId);

}
