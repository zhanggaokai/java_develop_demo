package stream.DataStreamApi.watermark;

public class Person {

    public String name;
    public Integer id;
    public Long event_time;

    public Person() {
    }

    public Person(String name, Integer id, Long event_time) {
        this.name = name;
        this.id = id;
        this.event_time = event_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Long event_time) {
        this.event_time = event_time;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", event_time=" + event_time +
                '}';
    }
}
