package hello.application.vo;

import hello.domain.entity.Person;
import hello.domain.vo.VkUserData;

/**
 * Created by pavel on 12.08.16.
 */
public class ApiPerson {

    private Long id;

    private String vkId;

    private String firstName;

    private String lastName;

    private Integer sex;

    private String bdate;

    private String photo;

    private String city;

    public static ApiPerson of(Person person) {
        ApiPerson apiPerson = new ApiPerson();
        apiPerson.setId(person.getId());
        apiPerson.setVkId(person.getVkId());
        apiPerson.setFirstName(person.getFirstName());
        apiPerson.setLastName(person.getLastName());
        return apiPerson;
    }

    public ApiPerson enrich(VkUserData vkUserData) {
        if (vkUserData!=null && vkUserData.getResponse() != null) {
            VkUserData.Response response = vkUserData.getResponse().get(0);
            if (response != null) {
                this.setFirstName(response.getFirst_name());
                this.setLastName(response.getLast_name());
                this.setBdate(response.getBdate());
                this.setSex(response.getSex());
                if (response.getCity()!=null) {
                    this.setCity(response.getCity().getTitle());
                }
                this.setPhoto(response.getPhoto_50());
            }
        }
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
