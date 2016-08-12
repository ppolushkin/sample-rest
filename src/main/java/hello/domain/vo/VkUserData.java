package hello.domain.vo;

import java.util.List;

/**
 * Created by pavel on 12.08.16.
 */
public class VkUserData {

    private List<Response> response;

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public static class Response {
        private Long id;
        private String first_name;
        private String last_name;
        private Integer sex;
        private String bdate;
        private String photo_50;
        private City city;

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public String getBdate() {
            return bdate;
        }

        public void setBdate(String bdate) {
            this.bdate = bdate;
        }

        public String getPhoto_50() {
            return photo_50;
        }

        public void setPhoto_50(String photo_50) {
            this.photo_50 = photo_50;
        }
    }

    public static class City {
        private Long id;
        private String title;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
