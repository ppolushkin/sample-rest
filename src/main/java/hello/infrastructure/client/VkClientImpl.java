package hello.infrastructure.client;

import com.sun.istack.internal.Nullable;
import hello.domain.client.VkClient;
import hello.domain.vo.VkUserData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by pavel on 12.08.16.
 */
@Component
public class VkClientImpl implements VkClient {

    private final static String URL = "https://api.vk.com/method/users.get?user_ids=%s&fields=photo_50,city,verified,sex,bdate,interests&v=5.8";

    @Override
    @Nullable
    public VkUserData getUserData(String vkId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = String.format(URL, vkId);

        VkUserData result = restTemplate.getForObject(url, VkUserData.class);
        return result;
    }


}
