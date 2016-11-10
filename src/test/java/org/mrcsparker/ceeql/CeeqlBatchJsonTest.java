package org.mrcsparker.ceeql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mrcsparker.ceeql.model.Product;
import org.mrcsparker.ceeql.model.ProductDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CeeqlBatchJsonTest {

    private final static Logger log = LogManager.getLogger(CeeqlBatchJsonTest.class);

    @Test
    public void dynamic_parameterized_not_supported() throws Exception {

        Ceeql c = DbCreator.create(false);

        StringBuilder s = new StringBuilder();
                s.append("{{#each batch}}\n");
                s.append("  INSERT INTO products(\n");
                s.append("    name, price, vendor_id\n");
                s.append("  ) VALUES (\n");
                s.append("    {{s name}}, {{safe price}}, {{safe vendor_id}}\n");
                s.append("  );\n");
                s.append("{{/each}}\n");
        String sql = s.toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("batch", buildJson());

        assertEquals(true, c.batch(sql, map).contains("Dynamic sql for parameterized batch not supported"));

        c.close();
    }

    @Test
    public void can_insert_batch_named() throws Exception {

        Ceeql c = DbCreator.create(false);

        StringBuilder s = new StringBuilder();
                s.append("{{#each batch}}\n");
                s.append("  INSERT INTO products(\n");
                s.append("    name, price, vendor_id\n");
                s.append("  ) VALUES (\n");
                s.append("    :name, :price, :vendor_id\n");
                s.append("  );\n");
                s.append("{{/each}}\n");
        String sql = s.toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("batch", buildJson());

        c.batch(sql, map);

        String output = c.select("SELECT * FROM products", new HashMap<>());

        assertEquals("[{\"price\":1.0000,\"vendor_id\":1,\"name\":\"batch1\",\"id\":1},{\"price\":2.0000,\"vendor_id\":2,\"name\":\"batch2\",\"id\":2}]", output);

        ObjectMapper mapper = new ObjectMapper();
        List<ProductDTO> productDTOList = mapper.readValue(output, new TypeReference<List<ProductDTO>>() { });

        assertEquals(buildProducts().size(), productDTOList.size());

        c.close();
    }

    @Test
    public void can_insert_mixed_parameters() throws Exception {

        Ceeql c = DbCreator.create(false);

        StringBuilder s = new StringBuilder();
                s.append("{{#each batch}}\n");
                s.append("  INSERT INTO products(\n");
                s.append("    name, price, vendor_id\n");
                s.append("  ) VALUES (\n");
                s.append("    :name, {{s price}}, {{s vendor_id}}\n");
                s.append("  );\n");
                s.append("{{/each}}\n");
        String sql = s.toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("batch", buildJson());

        c.batch(sql, map);

        String output = c.select("SELECT * FROM products", new HashMap<>());

        assertEquals("[{\"price\":1.0000,\"vendor_id\":1,\"name\":\"batch1\",\"id\":1},{\"price\":2.0000,\"vendor_id\":2,\"name\":\"batch2\",\"id\":2}]", output);

        ObjectMapper mapper = new ObjectMapper();
        List<ProductDTO> productDTOList = mapper.readValue(output, new TypeReference<List<ProductDTO>>() { });

        assertEquals(buildProducts().size(), productDTOList.size());

        c.close();
    }

    @Test
    public void can_insert_pure_dynamic() throws Exception {

        Ceeql c = DbCreator.create(false);

        StringBuilder s = new StringBuilder();
                s.append("{{#each batch}}\n");
                s.append("  INSERT INTO products(\n");
                s.append("    name, price, vendor_id\n");
                s.append("  ) VALUES (\n");
                s.append("    '{{safe name}}', {{safe price}}, {{safe vendor_id}}\n");
                s.append("  );\n");
                s.append("{{/each}}\n");
        String sql = s.toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("batch", buildJson());

        c.batch(sql, map);

        String output = c.select("SELECT * FROM products", new HashMap<>());

        assertEquals("[{\"price\":1.0000,\"vendor_id\":1,\"name\":\"batch1\",\"id\":1},{\"price\":2.0000,\"vendor_id\":2,\"name\":\"batch2\",\"id\":2}]", output);

        ObjectMapper mapper = new ObjectMapper();
        List<ProductDTO> productDTOList = mapper.readValue(output, new TypeReference<List<ProductDTO>>() { });

        assertEquals(buildProducts().size(), productDTOList.size());

        c.close();
    }

    public List<Product> buildProducts() {
        ArrayList<Product> products = new ArrayList<>();
        //for (int i = 1; i <= 1000; i++) {
        for (int i = 1; i <= 2; i++) {
        	products.add(new Product("batch" + i, i, i));
        }

        return products;
    }

    public String buildJson() {
        return Product.toJson(buildProducts());
    }
}
