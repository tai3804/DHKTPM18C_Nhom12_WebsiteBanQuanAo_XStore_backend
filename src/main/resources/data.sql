-- Sample data for testing stock items functionality

-- Insert sample stocks (warehouses)
INSERT INTO stocks (id, name, phone, email, address_id) VALUES
(1, 'Kho Hà Nội', '0123456789', 'hanoi@xstore.com', null),
(2, 'Kho Hồ Chí Minh', '0987654321', 'hcm@xstore.com', null),
(3, 'Kho Đà Nẵng', '0555666777', 'danang@xstore.com', null);

-- Insert sample product types
INSERT INTO product_types (id, name, description) VALUES
(1, 'Áo thun', 'Áo thun nam nữ'),
(2, 'Quần jeans', 'Quần jeans các loại'),
(3, 'Giày dép', 'Giày dép thời trang');

-- Insert sample products (removed size and color fields)
INSERT INTO products (id, name, description, image, product_type_id, brand, fabric, price_in_stock, price) VALUES
(1, 'Áo thun cổ tròn', 'Áo thun cổ tròn basic thoải mái', 'https://via.placeholder.com/400', 1, 'X-Store', 'Cotton', 150000, 200000),
(2, 'Quần jeans slim fit', 'Quần jeans slim fit hiện đại', 'https://via.placeholder.com/400', 2, 'X-Store', 'Denim', 400000, 500000),
(3, 'Giày sneaker', 'Giày sneaker thể thao', 'https://via.placeholder.com/400', 3, 'X-Store', 'Canvas', 600000, 750000);

-- Insert product colors
INSERT INTO product_colors (id, name, hex_code, product_id) VALUES
-- Áo thun colors
(1, 'Trắng', '#FFFFFF', 1),
(2, 'Đen', '#000000', 1),
(3, 'Xanh Navy', '#1E3A8A', 1),

-- Quần jeans colors
(4, 'Xanh đậm', '#1E40AF', 2),
(5, 'Đen', '#000000', 2),

-- Giày sneaker colors
(6, 'Trắng', '#FFFFFF', 3),
(7, 'Đen', '#000000', 3);

-- Insert product sizes
INSERT INTO product_sizes (id, name, description, product_id) VALUES
-- Áo thun sizes
(1, 'S', 'Small - 48-52kg', 1),
(2, 'M', 'Medium - 53-58kg', 1),
(3, 'L', 'Large - 59-65kg', 1),
(4, 'XL', 'Extra Large - 66-72kg', 1),

-- Quần jeans sizes
(5, '29', '29 inch - Eo 74cm', 2),
(6, '30', '30 inch - Eo 76cm', 2),
(7, '32', '32 inch - Eo 81cm', 2),
(8, '34', '34 inch - Eo 86cm', 2),

-- Giày sneaker sizes
(9, '39', '39 EU - 25cm', 3),
(10, '40', '40 EU - 25.5cm', 3),
(11, '41', '41 EU - 26cm', 3),
(12, '42', '42 EU - 26.5cm', 3);

-- Insert stock items (quantity in each warehouse for each product)
INSERT INTO stock_items (id, stock_id, product_id, quantity) VALUES
-- Áo thun cổ tròn
(1, 1, 1, 50),  -- Kho Hà Nội: 50 cái
(2, 2, 1, 30),  -- Kho HCM: 30 cái
(3, 3, 1, 20),  -- Kho Đà Nẵng: 20 cái

-- Quần jeans slim fit
(4, 1, 2, 25),  -- Kho Hà Nội: 25 cái
(5, 2, 2, 40),  -- Kho HCM: 40 cái
(6, 3, 2, 15),  -- Kho Đà Nẵng: 15 cái

-- Giày sneaker
(7, 1, 3, 10),  -- Kho Hà Nội: 10 đôi
(8, 2, 3, 20),  -- Kho HCM: 20 đôi
(9, 3, 3, 5);   -- Kho Đà Nẵng: 5 đôi


-- -- Sample data for testing stock items functionality
--
-- -- Insert sample stocks (warehouses)
-- INSERT INTO stocks (name, phone, email, address_id) VALUES
--                                                         ('Kho Hà Nội', '0123456789', 'hanoi@xstore.com', null),
--                                                         ('Kho Hồ Chí Minh', '0987654321', 'hcm@xstore.com', null),
--                                                         ('Kho Đà Nẵng', '0555666777', 'danang@xstore.com', null);
--
-- -- Insert sample product types
-- INSERT INTO product_types (name, description) VALUES
--                                                   ('Áo thun', 'Áo thun nam nữ'),
--                                                   ('Quần jeans', 'Quần jeans các loại'),
--                                                   ('Giày dép', 'Giày dép thời trang');
--
-- -- Insert sample products
-- INSERT INTO products (name, description, image, product_type_id, brand, fabric, price_in_stock, price) VALUES
--                                                                                                            ('Áo thun cổ tròn', 'Áo thun cổ tròn basic thoải mái', 'https://via.placeholder.com/400', 1, 'X-Store', 'Cotton', 150000, 200000),
--                                                                                                            ('Quần jeans slim fit', 'Quần jeans slim fit hiện đại', 'https://via.placeholder.com/400', 2, 'X-Store', 'Denim', 400000, 500000),
--                                                                                                            ('Giày sneaker', 'Giày sneaker thể thao', 'https://via.placeholder.com/400', 3, 'X-Store', 'Canvas', 600000, 750000);
--
-- -- Insert product colors
-- INSERT INTO product_colors (name, hex_code, product_id) VALUES
-- -- Áo thun colors
-- ('Trắng', '#FFFFFF', 1),
-- ('Đen', '#000000', 1),
-- ('Xanh Navy', '#1E3A8A', 1),
-- -- Quần jeans colors
-- ('Xanh đậm', '#1E40AF', 2),
-- ('Đen', '#000000', 2),
-- -- Giày sneaker colors
-- ('Trắng', '#FFFFFF', 3),
-- ('Đen', '#000000', 3);
--
-- -- Insert product sizes
-- INSERT INTO product_sizes (name, description, product_id) VALUES
-- -- Áo thun sizes
-- ('S', 'Small - 48-52kg', 1),
-- ('M', 'Medium - 53-58kg', 1),
-- ('L', 'Large - 59-65kg', 1),
-- ('XL', 'Extra Large - 66-72kg', 1),
-- -- Quần jeans sizes
-- ('29', '29 inch - Eo 74cm', 2),
-- ('30', '30 inch - Eo 76cm', 2),
-- ('32', '32 inch - Eo 81cm', 2),
-- ('34', '34 inch - Eo 86cm', 2),
-- -- Giày sneaker sizes
-- ('39', '39 EU - 25cm', 3),
-- ('40', '40 EU - 25.5cm', 3),
-- ('41', '41 EU - 26cm', 3),
-- ('42', '42 EU - 26.5cm', 3);
--
-- -- Insert stock items (quantity in each warehouse for each product)
-- INSERT INTO stock_items (stock_id, product_id, quantity) VALUES
-- -- Áo thun cổ tròn
-- (1, 1, 50),  -- Kho Hà Nội: 50 cái
-- (2, 1, 30),  -- Kho HCM: 30 cái
-- (3, 1, 20),  -- Kho Đà Nẵng: 20 cái
-- -- Quần jeans slim fit
-- (1, 2, 25),  -- Kho Hà Nội: 25 cái
-- (2, 2, 40),  -- Kho HCM: 40 cái
-- (3, 2, 15),  -- Kho Đà Nẵng: 15 cái
-- -- Giày sneaker
-- (1, 3, 10),  -- Kho Hà Nội: 10 đôi
-- (2, 3, 20),  -- Kho HCM: 20 đôi
-- (3, 3, 5);   -- Kho Đà Nẵng: 5 đôi

