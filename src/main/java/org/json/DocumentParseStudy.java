package org.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//jackson这个第三方库即可以解析xml又可以解析json
//他是把xml或者json转换成javabean
public class DocumentParseStudy {

    //读取json
    @Test
    public void test() throws IOException {
        InputStream input = DocumentParseStudy.class.getResourceAsStream("/book.json");
        ObjectMapper mapper = new ObjectMapper();
        //反序列化时忽略不存在的javaBean属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Book book = mapper.readValue(input, Book.class);
        System.out.println(book);
    }
    //javabean序列化
    @Test
    public void test2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Book book = new Book(1, "C++核心结束", new Author("lisi", "wangwu"), new BigInteger("9787115422635"), new ArrayList<String>(List.of("Python", "cpp")));
        //启用格式化输出
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(book);
        byte[] bytes = mapper.writeValueAsBytes(book);
        OutputStream output = new FileOutputStream("./book.json");
        output.write(bytes);
    }
}
//要反序列化的类一定要有无参构造函数
class Book{
    private int id;
    private String name;
    private Author author;
    @JsonDeserialize(using = IsbnDeserializer.class) //使用注解标注反序列化isbn的时候使用的自定义类
    private BigInteger isbn;
    List<String> tags;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", isbn='" + isbn + '\'' +
                ", tags=" + tags +
                '}';
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public BigInteger getIsbn() {
        return isbn;
    }

    public void setIsbn(BigInteger isbn) {
        this.isbn = isbn;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Book(int id, String name, Author author, BigInteger isbn, List<String> tags) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.tags = tags;
    }
}

class Author{
    private String firstName;
    private String lastName;

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

    public Author() {
    }

    @Override
    public String toString() {
        return "Author{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

//自定义反序列化方法, 用于解析含有非数字的字符串
class IsbnDeserializer extends JsonDeserializer<BigInteger>{
    @Override
    public BigInteger deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String s = jsonParser.getValueAsString();
        if(s != null){
            try{
                return new BigInteger(s.replace("-", ""));
            }catch (NumberFormatException e){
                throw new JsonParseException(jsonParser, s, e);
            }
        }
        return null;
    }
}
