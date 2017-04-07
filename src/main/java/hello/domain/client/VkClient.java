package hello.domain.client;

import hello.domain.vo.VkUserData;
import org.springframework.stereotype.Component;


/**
 * Created by pavel on 12.08.16.
 */
@Component
public interface VkClient {

    String URL = "https://api.vk.com/method/users.get?user_ids=%s&fields=photo_50,city,verified,sex,bdate,interests&v=5.8";

    VkUserData getUserData(String vkId);


}
