import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Digital Point of Sale (POS) System
 *
 * This program simulates the billing section of a supermarket.
 *
 * Main flow:
 *
 * Product
 *    ↓
 * Add to Cart
 *    ↓
 * Cart
 *    ↓
 * Calculate Total
 *    ↓
 * Checkout
 *    ↓
 * Payment
 *    ↓
 * Change
 */

public class Main {

    // ============================================================
    // CART ITEM CLASS
    // ============================================================

    /*
     * CartItem represents a product that has been added
     * to the customer's virtual shopping cart.
     */
    static class CartItem {

        Product product;  // The product that was added to the cart it is from the Product inner class
        int quantity;

        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

    
        double getTotal() {           // Calculates the total price for this cart item based on its quantity and the product's price.
            return product.price * quantity;  // Returns the total price for this cart item. this `getTotal()` method is used in the checkout process to calculate the total amount due for all items in the cart.
        }
    }


    // ============================================================
    // PRODUCT DATA
    // ============================================================

    /*
     * For now, products are stored in memory.
     *
     * Later we will replace this with MySQL database data.
     */
    static List<Product> products = new ArrayList<>();

    /*
     * This list represents the customer's virtual cart.
     */
    static List<CartItem> cart = new ArrayList<>();


    // ============================================================
    // GUI COMPONENTS
    // ============================================================

    static JFrame frame;

    static JTextField searchField;

    static JPanel productPanel;

    static DefaultTableModel tableModel;

    static JTable cartTable;

    static JLabel totalLabel;

    static JLabel itemCountLabel;


    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        // Load sample products
        products = ProductDAO.getAllProducts();

        /*
         * SwingUtilities.invokeLater()
         *
         * Starts the GUI safely on Swing's Event Dispatch Thread.
         */
        SwingUtilities.invokeLater(() -> createGUI());
    }


    // ============================================================
    // LOAD PRODUCTS
    // ============================================================



    // ============================================================
    // CREATE GUI
    // ============================================================

    static void createGUI() {

        // Create the main application window
        frame = new JFrame("Digital POS System");

        // Set window size
        frame.setSize(1100, 700);

        // Close program when X is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        /*
         * JFrame uses BorderLayout by default.
         */
        frame.setLayout(new BorderLayout());


        // ========================================================
        // HEADER
        // ========================================================

        JPanel headerPanel = new JPanel();

        JLabel title = new JLabel("DIGITAL POS SYSTEM");

        title.setFont(new Font("Arial", Font.BOLD, 28));

        headerPanel.add(title);

        frame.add(headerPanel, BorderLayout.NORTH);


        // ========================================================
        // SEARCH SECTION
        // ========================================================

        JPanel searchPanel = new JPanel();

        JLabel searchLabel = new JLabel("Search Product:");

        searchField = new JTextField(20);

        JButton searchButton = new JButton("SEARCH");

        JButton showAllButton = new JButton("SHOW ALL");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);


        // ========================================================
        // PRODUCT PANEL
        // ========================================================

        productPanel = new JPanel();

        /*
         * GridLayout creates a grid.
         *
         * 2 rows and 4 columns means our 8 products
         * can initially be displayed in a grid.
         */
        productPanel.setLayout(new GridLayout(2, 4, 10, 10));  // 10px horizontal and vertical gaps between buttons

        showProducts(products);


        // ========================================================
        // LEFT SIDE
        // ========================================================

        JPanel leftPanel = new JPanel(new BorderLayout());

        leftPanel.add(searchPanel, BorderLayout.NORTH);

        leftPanel.add(
                new JScrollPane(productPanel),
                BorderLayout.CENTER
        );


        // ========================================================
        // CART TABLE
        // ========================================================

        String[] columns = {
                "Product",
                "Price",
                "Quantity",
                "Total"
        };

        /*
         * DefaultTableModel stores the data displayed by JTable.
         */
        tableModel = new DefaultTableModel(columns, 0) {

            /*
             * Prevent the user from directly editing
             * table cells.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(tableModel);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);


        // ========================================================
        // CART HEADER
        // ========================================================

        JPanel cartHeader = new JPanel(new BorderLayout());

        JLabel cartTitle = new JLabel("SHOPPING CART");

        cartTitle.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        cartHeader.add(cartTitle, BorderLayout.WEST);


        // ========================================================
        // CART BUTTONS
        // ========================================================

        JPanel cartButtonPanel = new JPanel();

        JButton removeButton = new JButton("REMOVE");

        JButton clearButton = new JButton("CLEAR CART");

        cartButtonPanel.add(removeButton);
        cartButtonPanel.add(clearButton);


        // ========================================================
        // TOTAL SECTION
        // ========================================================

        JPanel bottomPanel = new JPanel(
                new BorderLayout()
        );

        itemCountLabel = new JLabel("Items: 0");

        totalLabel = new JLabel("TOTAL: ₹0.00");

        totalLabel.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        bottomPanel.add(
                itemCountLabel,
                BorderLayout.WEST
        );

        bottomPanel.add(
                totalLabel,
                BorderLayout.CENTER
        );

        bottomPanel.add(
                cartButtonPanel,
                BorderLayout.EAST
        );


        // ========================================================
        // CHECKOUT BUTTON
        // ========================================================

        JButton checkoutButton = new JButton("CHECKOUT");

        checkoutButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );


        // ========================================================
        // RIGHT SIDE
        // ========================================================

        JPanel rightPanel = new JPanel(
                new BorderLayout()
        );

        rightPanel.setPreferredSize(
                new Dimension(450, 0)
        );

        rightPanel.add(
                cartHeader,
                BorderLayout.NORTH
        );

        rightPanel.add(
                cartScrollPane,
                BorderLayout.CENTER
        );

        rightPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // ========================================================
        // ADD EVERYTHING TO FRAME
        // ========================================================

        frame.add(
                leftPanel,
                BorderLayout.CENTER
        );

        frame.add(
                rightPanel,
                BorderLayout.EAST
        );


        // ========================================================
        // SEARCH BUTTON EVENT
        // ========================================================

        searchButton.addActionListener(e -> {

            String searchText =
                    searchField.getText().trim();

            searchProducts(searchText);
        });


        // ========================================================
        // SHOW ALL BUTTON EVENT
        // ========================================================

        showAllButton.addActionListener(e -> {

            searchField.setText("");

            showProducts(products);
        });


        // ========================================================
        // REMOVE BUTTON EVENT
        // ========================================================

        removeButton.addActionListener(e -> {

            removeSelectedItem();
        });


        // ========================================================
        // CLEAR CART BUTTON EVENT
        // ========================================================

        clearButton.addActionListener(e -> {

            clearCart();
        });


        // ========================================================
        // CHECKOUT BUTTON EVENT
        // ========================================================

        checkoutButton.addActionListener(e -> {

            checkout();
        });


        // Add checkout button to bottom area
        JPanel checkoutPanel = new JPanel();

        checkoutPanel.add(checkoutButton);

        frame.add(
                checkoutPanel,
                BorderLayout.SOUTH
        );


        // Display window
        frame.setVisible(true);
    }


    // ============================================================
    // SHOW PRODUCTS
    // ============================================================

    static void showProducts(List<Product> productList) {

        productPanel.removeAll();

        for (Product product : productList) {

            JButton productButton = new JButton(
                    "<html>" +
                    "<b>" + product.name + "</b><br>" +
                    "₹" + String.format("%.2f", product.price) +
                    "<br>" +
                    "Stock: " + product.stock +
                    "</html>"
            );

            /*
             * Clicking the product button adds it to the cart.
             */
            productButton.addActionListener(e -> {

                addToCart(product);
            });

            productPanel.add(productButton);
        }

        /*
         * Refresh the panel after changing its contents.
         */
        productPanel.revalidate();
        productPanel.repaint();
    }


    // ============================================================
    // SEARCH PRODUCTS
    // ============================================================

    static void searchProducts(String searchText) {

        /*
         * If search box is empty, show all products.
         */
        if (searchText.isEmpty()) {

            showProducts(products);

            return;
        }


        List<Product> results = new ArrayList<>();

        /*
         * Search product names.
         */
        for (Product product : products) {

            if (
                    product.name
                            .toLowerCase()
                            .contains(searchText.toLowerCase())
            ) {

                results.add(product);
            }
        }


        if (results.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "No product found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            showProducts(results);
        }
    }


    // ============================================================
    // ADD PRODUCT TO CART
    // ============================================================

    static void addToCart(Product product) {

        /*
         * Check whether product is already in cart.
         */
        for (CartItem item : cart) {

            if (item.product.id == product.id) {

                /*
                 * Don't allow quantity greater than stock.
                 */
                if (item.quantity < product.stock) {

                    item.quantity++;

                    updateCartTable();

                } else {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Not enough stock available."
                    );
                }

                return;
            }
        }


        /*
         * Product is not already in cart.
         *
         * Add it with quantity 1.
         */
        cart.add(
                new CartItem(product, 1)
        );

        updateCartTable();
    }


    // ============================================================
    // UPDATE CART TABLE
    // ============================================================

    static void updateCartTable() {

        /*
         * Remove existing table rows.
         */
        tableModel.setRowCount(0);


        double total = 0;

        int itemCount = 0;


        /*
         * Add current cart items to JTable.
         */
        for (CartItem item : cart) {

            double itemTotal =
                    item.product.price * item.quantity;

            tableModel.addRow(
                    new Object[]{
                            item.product.name,
                            String.format(
                                    "₹%.2f",
                                    item.product.price
                            ),
                            item.quantity,
                            String.format(
                                    "₹%.2f",
                                    itemTotal
                            )
                    }
            );

            total += itemTotal;

            itemCount += item.quantity;
        }


        /*
         * Update total displayed on screen.
         */
        totalLabel.setText(
                String.format(
                        "TOTAL: ₹%.2f",
                        total
                )
        );


        /*
         * Update item count.
         */
        itemCountLabel.setText(
                "Items: " + itemCount
        );
    }


    // ============================================================
    // REMOVE SELECTED ITEM
    // ============================================================

    static void removeSelectedItem() {

        int selectedRow =
                cartTable.getSelectedRow();


        /*
         * -1 means no row was selected.
         */
        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please select an item to remove."
            );

            return;
        }


        /*
         * Remove the selected item from cart.
         */
        cart.remove(selectedRow);

        updateCartTable();
    }


    // ============================================================
    // CLEAR CART
    // ============================================================

    static void clearCart() {

        if (cart.isEmpty()) {

            return;
        }


        int result = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to clear the cart?",
                "Clear Cart",
                JOptionPane.YES_NO_OPTION
        );


        if (result == JOptionPane.YES_OPTION) {

            cart.clear();

            updateCartTable();
        }
    }


    // ============================================================
    // CHECKOUT
    // ============================================================

    static void checkout() {

        /*
         * Can't checkout an empty cart.
         */
        if (cart.isEmpty()) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Cart is empty.",
                    "Checkout",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        /*
         * Calculate total.
         */
        double total = 0;

        for (CartItem item : cart) {

    // Calculate the total price of all items
    total += item.getTotal();
}


        /*
         * Ask customer how much they paid.
         */
        String paymentInput =
                JOptionPane.showInputDialog(
                        frame,
                        String.format(
                                "Total amount: ₹%.2f%nEnter payment amount:",
                                total
                        )
                );


        /*
         * User cancelled the dialog.
         */
        if (paymentInput == null) {

            return;
        }


        double payment;


        try {

            payment =
                    Double.parseDouble(
                            paymentInput
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter a valid amount.",
                    "Invalid Payment",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        /*
         * Payment cannot be less than total.
         */
        if (payment < total) {

            JOptionPane.showMessageDialog(
                    frame,
                    String.format(
                            "Insufficient payment.%nAmount required: ₹%.2f",
                            total
                    ),
                    "Payment Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        /*
         * Reduce stock after successful payment.
         */
        for (CartItem item : cart) {

        // Update stock in the database
        ProductDAO.reduceStock(
        item.product.id,
        item.quantity
        );

            item.product.stock -= item.quantity;
        }


        double change = payment - total;


        /*
         * Show successful transaction.
         */
        JOptionPane.showMessageDialog(
                frame,
                String.format(
                        "Payment Successful!%n%n" +
                        "Total: ₹%.2f%n" +
                        "Paid: ₹%.2f%n" +
                        "Change: ₹%.2f",
                        total,
                        payment,
                        change
                ),
                "Checkout Complete",
                JOptionPane.INFORMATION_MESSAGE
        );


        /*
         * Clear cart after successful checkout.
         */
        cart.clear();

        updateCartTable();

        /*
         * Refresh product buttons so that
         * updated stock is displayed.
         */
        showProducts(products);
    }
}