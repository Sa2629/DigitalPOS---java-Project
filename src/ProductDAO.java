import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

        public static void reduceStock(int productId, int quantity) {

    String sql =
            "UPDATE Products " +
            "SET stock = stock - ? " +
            "WHERE product_id = ?";

    try {

        Connection connection =
                DatabaseConnection.geConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setInt(1, quantity);
        statement.setInt(2, productId);

        statement.executeUpdate();

        statement.close();
        connection.close();

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    public static List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM Products";

        try {

            Connection connection =
                    DatabaseConnection.geConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                int id =
                        resultSet.getInt("product_id");

                String name =
                        resultSet.getString("product_name");

                double price =
                        resultSet.getDouble("price");

                int stock =
                        resultSet.getInt("stock");

                Product product =
                        new Product(
                                id,
                                name,
                                price,
                                stock
                        );

                products.add(product);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return products;
    }
    public static void main(String[] args) {

    List<Product> products = getAllProducts();

    for (Product product : products) {

        System.out.println(
                product.id + " | " +
                product.name + " | ₹" +
                product.price + " | Stock: " +
                product.stock
        );
    }
}
}