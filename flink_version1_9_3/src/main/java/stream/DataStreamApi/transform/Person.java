package stream.DataStreamApi.transform;

public class Person{
    private String name;//姓名
    private Integer age;//年龄
    private String fale;//性别

    public String getName() {
        return name;
    }

    public Person(String name, Integer age, String fale) {
        this.name = name;
        this.age = age;
        this.fale = fale;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFale() {
        return fale;
    }

    public void setFale(String fale) {
        this.fale = fale;
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", fale='" + fale + '\'' +
                '}';
    }
}