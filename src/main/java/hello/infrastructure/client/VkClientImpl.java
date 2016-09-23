package hello.infrastructure.client;

import hello.domain.client.VkClient;
import hello.domain.vo.VkUserData;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by pavel on 21.09.16.
 */
@Component
public class VkClientImpl implements VkClient {

    @Override
    public VkUserData getUserData(String vkId) {
        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setConnectTimeout(PerformanceSettings.CONNECTION_TIMEOUT);
        rf.setReadTimeout(PerformanceSettings.READ_TIMEOUT);

        String url = String.format(URL, vkId);
        VkUserData result = restTemplate.getForObject(url, VkUserData.class);
        result.setVkId(vkId);
        return result;
    }

}
