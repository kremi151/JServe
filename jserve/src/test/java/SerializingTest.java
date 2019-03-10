import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lu.mkremer.jserve.api.annotation.ConfigField;
import lu.mkremer.jserve.api.annotation.Configurable;
import lu.mkremer.jserve.api.mapper.PathMapper;
import lu.mkremer.jserve.api.mapper.state.MapperState;
import lu.mkremer.jserve.conf.PathMapperFactory;
import lu.mkremer.jserve.io.WritableObjectNode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SerializingTest {

    private static final String TYPE_TEST_MAPPER = "test";

    private JsonNode createTestJson(boolean setOptionals) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();

        json.put("type", TYPE_TEST_MAPPER);
        json.put("custom_prop_a", "valueA");
        json.put("custom_prop_b", "valueB");
        json.put("custom_prop_c", "valueC");
        json.put("required_field_d", "valueD");
        json.put("required_field_i", "valueI");
        json.put("required_field_j", "valueJ");
        json.put("custom_prop_l", "valueL");
        json.put("required_field_p", "valueP");

        if (setOptionals) {
            json.put("field_e", "valueE");
            json.put("custom_prop_f", "valueF");
            json.put("custom_prop_g", "valueG");
            json.put("custom_prop_h", "valueH");
            json.put("field_k", "valueK");
            json.put("field_m", "valueM");
            json.put("field_n", "valueN");
            json.put("custom_prop_o", "valueO");
        }

        return json;
    }

    @Tag("fast")
    @Test
    public void deserializationAllSetTest() {
        PathMapperFactory factory = new PathMapperFactory();
        factory.registerPathMapper(TestMapper.class, TestMapper::new);

        JsonNode json = createTestJson(true);
        TestMapper mapper = factory.parseMapper(json);

        assertEquals("valueA", mapper.requiredFieldA);
        assertEquals("valueB", mapper.requiredFieldB);
        assertEquals("valueC", mapper.requiredFieldC);
        assertEquals("valueD", mapper.requiredFieldD);
        assertEquals("valueE", mapper.fieldE);
        assertEquals("valueF", mapper.fieldF);
        assertEquals("valueG", mapper.fieldG);
        assertEquals("valueH", mapper.fieldH);
        assertEquals("valueI", mapper.requiredFieldI);
        assertEquals("valueJ", mapper.requiredFieldJ);
        assertEquals("valueK", mapper.fieldK);
        assertEquals("valueL", mapper.requiredFieldL);
        assertEquals("valueM", mapper.fieldM);
        assertEquals("valueN", mapper.fieldN);
        assertEquals("valueO", mapper.fieldO);
        assertEquals("valueP", mapper.requiredFieldP);
    }

    @Tag("fast")
    @Test
    public void deserializationWithOptionalsTest() {
        PathMapperFactory factory = new PathMapperFactory();
        factory.registerPathMapper(TestMapper.class, TestMapper::new);

        JsonNode json = createTestJson(false);
        TestMapper mapper = factory.parseMapper(json);

        assertEquals("valueA", mapper.requiredFieldA);
        assertEquals("valueB", mapper.requiredFieldB);
        assertEquals("valueC", mapper.requiredFieldC);
        assertEquals("valueD", mapper.requiredFieldD);
        assertEquals("abc", mapper.fieldE);
        assertEquals("hello", mapper.fieldF);
        assertEquals("hi", mapper.fieldG);
        assertEquals("howdy", mapper.fieldH);
        assertEquals("valueI", mapper.requiredFieldI);
        assertEquals("valueJ", mapper.requiredFieldJ);
        assertEquals("xyz", mapper.fieldK);
        assertEquals("valueL", mapper.requiredFieldL);
        assertEquals("mno", mapper.fieldM);
        assertEquals("stu", mapper.fieldN);
        assertEquals("uvw", mapper.fieldO);
        assertEquals("valueP", mapper.requiredFieldP);
    }

    private TestMapper createTestMapper(boolean setOptionals) {
        TestMapper mapper = new TestMapper();
        mapper.requiredFieldA = "magicA";
        mapper.requiredFieldB = "magicB";
        mapper.requiredFieldC = "magicC";
        mapper.requiredFieldD = "magicD";
        mapper.requiredFieldI = "magicI";
        mapper.requiredFieldJ = "magicJ";
        mapper.requiredFieldL = "magicL";
        mapper.requiredFieldP = "magicP";
        if (setOptionals) {
            mapper.fieldE = "magicE";
            mapper.fieldF = "magicF";
            mapper.fieldG = "magicG";
            mapper.fieldH = "magicH";
            mapper.fieldK = "magicK";
            mapper.fieldM = "magicM";
            mapper.fieldN = "magicN";
            mapper.fieldO = "magicO";
        }
        return mapper;
    }

    @Tag("fast")
    @Test
    public void serializationAllSetTest() throws IOException {
        PathMapperFactory factory = new PathMapperFactory();
        factory.registerPathMapper(TestMapper.class, TestMapper::new);
        TestMapper mapper = createTestMapper(true);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        WritableObjectNode node = new WritableObjectNode(json, objectMapper);
        factory.serializeMapper(node, mapper);

        assertEquals(TYPE_TEST_MAPPER, json.get("type").asText());
        assertEquals("magicA", json.get("custom_prop_a").asText());
        assertEquals("magicB", json.get("custom_prop_b").asText());
        assertEquals("magicC", json.get("custom_prop_c").asText());
        assertEquals("magicD", json.get("required_field_d").asText());
        assertEquals("magicE", json.get("field_e").asText());
        assertEquals("magicF", json.get("custom_prop_f").asText());
        assertEquals("magicG", json.get("custom_prop_g").asText());
        assertEquals("magicH", json.get("custom_prop_h").asText());
        assertEquals("magicI", json.get("required_field_i").asText());
        assertEquals("magicJ", json.get("required_field_j").asText());
        assertEquals("magicK", json.get("field_k").asText());
        assertEquals("magicL", json.get("custom_prop_l").asText());
        assertEquals("magicM", json.get("field_m").asText());
        assertEquals("magicN", json.get("field_n").asText());
        assertEquals("magicO", json.get("custom_prop_o").asText());
        assertEquals("magicP", json.get("required_field_p").asText());
    }

    @Tag("fast")
    @Test
    public void serializationWithOptionalsTest() throws IOException {
        PathMapperFactory factory = new PathMapperFactory();
        factory.registerPathMapper(TestMapper.class, TestMapper::new);
        TestMapper mapper = createTestMapper(false);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        WritableObjectNode node = new WritableObjectNode(json, objectMapper);
        factory.serializeMapper(node, mapper);

        assertEquals(TYPE_TEST_MAPPER, json.get("type").asText());
        assertEquals("magicA", json.get("custom_prop_a").asText());
        assertEquals("magicB", json.get("custom_prop_b").asText());
        assertEquals("magicC", json.get("custom_prop_c").asText());
        assertEquals("magicD", json.get("required_field_d").asText());
        assertEquals("abc", json.get("field_e").asText());
        assertEquals("hello", json.get("custom_prop_f").asText());
        assertEquals("hi", json.get("custom_prop_g").asText());
        assertEquals("howdy", json.get("custom_prop_h").asText());
        assertEquals("magicI", json.get("required_field_i").asText());
        assertEquals("magicJ", json.get("required_field_j").asText());
        assertEquals("xyz", json.get("field_k").asText());
        assertEquals("magicL", json.get("custom_prop_l").asText());
        assertEquals("mno", json.get("field_m").asText());
        assertEquals("stu", json.get("field_n").asText());
        assertEquals("uvw", json.get("custom_prop_o").asText());
        assertEquals("magicP", json.get("required_field_p").asText());
    }

    @Configurable(id = TYPE_TEST_MAPPER)
    private static class TestMapper implements PathMapper {

        @ConfigField(required = true, name = "custom_prop_a")
        private String requiredFieldA; // Required, name, getter, setter
        @ConfigField(required = true, name = "custom_prop_b")
        private String requiredFieldB; // Required, name, getter
        @ConfigField(required = true, name = "custom_prop_c")
        private String requiredFieldC; // Required, name
        @ConfigField(required = true)
        private String requiredFieldD; // Required
        @ConfigField(defaultValue = "abc")
        private String fieldE; //
        @ConfigField(name = "custom_prop_f", defaultValue = "hello")
        private String fieldF; // Name, getter, setter
        @ConfigField(name = "custom_prop_g", defaultValue = "hi")
        private String fieldG; // Name, getter
        @ConfigField(name = "custom_prop_h", defaultValue = "howdy")
        private String fieldH; // Name
        @ConfigField(required = true)
        private String requiredFieldI; // Required, getter, setter
        @ConfigField(required = true)
        private String requiredFieldJ; // Required, getter
        @ConfigField(defaultValue = "xyz")
        private String fieldK; // Getter, setter
        @ConfigField(required = true, name = "custom_prop_l")
        private String requiredFieldL; // Required, name, setter
        @ConfigField(defaultValue = "mno")
        private String fieldM; // Getter
        @ConfigField(defaultValue = "stu")
        private String fieldN; // Setter
        @ConfigField(name = "custom_prop_o", defaultValue = "uvw")
        private String fieldO;
        @ConfigField(required = true)
        private String requiredFieldP;

        @Override
        public MapperState applies(String path) {
            return MapperState.ACCEPT_FINISH;
        }

        @Override
        public String map(String path) {
            return "noop";
        }

        public String getRequiredFieldA() {
            return requiredFieldA;
        }

        public void setRequiredFieldA(String requiredFieldA) {
            this.requiredFieldA = requiredFieldA;
        }

        public String getRequiredFieldB() {
            return requiredFieldB;
        }

        public String getFieldF() {
            return fieldF;
        }

        public void setFieldF(String requiredFieldF) {
            this.fieldF = requiredFieldF;
        }

        public String getFieldG() {
            return fieldG;
        }

        public String getRequiredFieldI() {
            return requiredFieldI;
        }

        public void setRequiredFieldI(String requiredFieldI) {
            this.requiredFieldI = requiredFieldI;
        }

        public String getRequiredFieldJ() {
            return requiredFieldJ;
        }

        public void setRequiredFieldL(String requiredFieldL) {
            this.requiredFieldL = requiredFieldL;
        }

        public String getFieldK() {
            return fieldK;
        }

        public void setFieldK(String fieldK) {
            this.fieldK = fieldK;
        }

        public String getFieldM() {
            return fieldM;
        }

        public void setFieldN(String fieldN) {
            this.fieldN = fieldN;
        }

        public void setFieldO(String fieldO) {
            this.fieldO = fieldO;
        }

        public void setRequiredFieldP(String requiredFieldP) {
            this.requiredFieldP = requiredFieldP;
        }
    }

}
