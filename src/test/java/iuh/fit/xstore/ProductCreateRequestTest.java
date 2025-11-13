package iuh.fit.xstore.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductCreateRequestTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        
        // Test JSON deserialization
        String jsonInput = """
        {
            "id": 0,
            "name": "Quần jeans slim fit",
            "description": "Quần jeans tuyệt vời",
            "image": "https://example.com/img.jpg",
            "typeId": 1,
            "brand": "X-Store",
            "fabric": "Denim",
            "priceInStock": 10,
            "price": 299000,
            "colors": [
                {"name": "Đỏ", "hexCode": "#FF0000"},
                {"name": "Xanh", "hexCode": "#0000FF"}
            ],
            "sizes": [
                {"name": "M", "description": "Size trung bình"},
                {"name": "L", "description": "Size lớn"}
            ]
        }
        """;
        
        try {
            ProductCreateRequest request = mapper.readValue(jsonInput, ProductCreateRequest.class);
            System.out.println("✅ Deserialization successful!");
            System.out.println("Name: " + request.getName());
            System.out.println("Colors: " + request.getColors().size());
            request.getColors().forEach(c -> System.out.println("  - " + c.getName() + ": " + c.getHexCode()));
            System.out.println("Sizes: " + request.getSizes().size());
            request.getSizes().forEach(s -> System.out.println("  - " + s.getName() + ": " + s.getDescription()));
        } catch (Exception e) {
            System.err.println("❌ Deserialization failed!");
            e.printStackTrace();
        }
    }
}
