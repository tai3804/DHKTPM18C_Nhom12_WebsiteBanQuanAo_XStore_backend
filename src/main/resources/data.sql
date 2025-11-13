-- XStore Fashion - Dữ liệu mặc định cho shop bán quần áo
-- Created: 2025-11-13

-- Tắt foreign key checks để có thể xóa dữ liệu
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM stock_items WHERE id > 0;
DELETE FROM product_sizes WHERE id > 0;
DELETE FROM product_colors WHERE id > 0;
DELETE FROM products WHERE id > 0;
DELETE FROM product_types WHERE id > 0;
DELETE FROM stocks WHERE id > 0;
DELETE FROM discounts WHERE id > 0;

-- Bật lại foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Insert sample stocks (kho hàng)
INSERT IGNORE INTO stocks (id, name, phone, email, address_id) VALUES
(1, 'Kho Miền Bắc - Hà Nội', '0243.456.789', 'hanoi@xstore.vn', null),
(2, 'Kho Miền Nam - TP.HCM', '0283.456.789', 'hcm@xstore.vn', null),
(3, 'Kho Miền Trung - Đà Nẵng', '0236.456.789', 'danang@xstore.vn', null);

-- Insert product types (danh mục sản phẩm)
INSERT IGNORE INTO product_types (id, name, description) VALUES
(1, 'Áo Nam', 'Các loại áo dành cho nam giới'),
(2, 'Áo Nữ', 'Các loại áo dành cho nữ giới'),
(3, 'Quần Nam', 'Quần dài, quần short nam'),
(4, 'Quần Nữ', 'Quần dài, chân váy nữ'),
(5, 'Giày Dép', 'Giày thể thao, sandal, boot'),
(6, 'Phụ Kiện', 'Túi xách, nón, thắt lưng');

-- Insert sample products với ảnh thật từ Unsplash
INSERT IGNORE INTO products (id, name, description, image, product_type_id, brand, fabric, price_in_stock, price) VALUES
-- Áo Nam
(1, 'Áo Thun Nam Basic Cotton', 'Áo thun nam basic chất liệu cotton 100%, form regular fit thoải mái. Phù hợp mặc hàng ngày, đi chơi hay đi làm.', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400', 1, 'X-Store', 'Cotton 100%', 120000, 179000),

(2, 'Áo Sơ Mi Nam Công Sở', 'Áo sơ mi nam công sở chất vải cotton pha, form slim fit hiện đại. Thiết kế lịch lãm, phù hợp môi trường công sở.', 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=400', 1, 'X-Store', 'Cotton Blend', 280000, 399000),

(3, 'Áo Hoodie Nam Streetwear', 'Áo hoodie nam phong cách streetwear, chất nỉ cotton dày dặn. Form oversize trendy với hood và túi kangaroo.', 'https://images.unsplash.com/photo-1556821840-3a9fbc86339e?w=400', 1, 'X-Store', 'Cotton Fleece', 350000, 499000),

-- Áo Nữ
(4, 'Áo Thun Nữ Crop Top', 'Áo thun nữ crop top trendy chất cotton co giãn. Form fitted ôm dáng, phù hợp mix đồ năng động.', 'https://images.unsplash.com/photo-1594223274512-ad4803739b7c?w=400', 2, 'X-Store', 'Cotton Spandex', 140000, 199000),

(5, 'Áo Blouse Nữ Công Sở', 'Áo blouse nữ công sở chất vải lụa mềm mại. Thiết kế thanh lịch với tay bồng nhẹ, phù hợp đi làm.', 'https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=400', 2, 'X-Store', 'Polyester Silk', 320000, 459000),

(6, 'Áo Khoác Blazer Nữ', 'Áo khoác blazer nữ form fitted sang trọng. Chất vải polyester cao cấp, thiết kế 1 button hiện đại.', 'https://images.unsplash.com/photo-1591369823-a8e19c5f06d6?w=400', 2, 'X-Store', 'Polyester Premium', 450000, 649000),

-- Quần Nam
(7, 'Quần Jeans Nam Slim Fit', 'Quần jeans nam slim fit chất denim cotton co giãn. Wash nhẹ tự nhiên, phom dáng ôm vừa phải.', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', 3, 'X-Store', 'Denim Cotton', 380000, 549000),

(8, 'Quần Kaki Nam Chinos', 'Quần kaki nam chinos chất cotton twill mềm mại. Form straight leg thoải mái, phù hợp nhiều dịp.', 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400', 3, 'X-Store', 'Cotton Twill', 290000, 419000),

(9, 'Quần Short Nam Thể Thao', 'Quần short nam thể thao chất polyester thấm hút mồ hôi. Có lót bên trong và túi zip tiện lợi.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 3, 'X-Store', 'Polyester DryFit', 180000, 259000),

-- Quần Nữ
(10, 'Quần Jeans Nữ Skinny', 'Quần jeans nữ skinny fit ôm dáng hoàn hảo. Chất denim co giãn 4 chiều, tôn lên đường cong cơ thể.', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400', 4, 'X-Store', 'Denim Stretch', 420000, 599000),

(11, 'Chân Váy Chữ A Vintage', 'Chân váy chữ A vintage chất vải cotton pha. Thiết kế xoè nhẹ, độ dài qua gối thanh lịch.', 'https://images.unsplash.com/photo-1583496661160-fb5886a13d75?w=400', 4, 'X-Store', 'Cotton Blend', 250000, 359000),

(12, 'Quần Culottes Nữ Wide Leg', 'Quần culottes nữ wide leg thời thưởng. Chất vải polyester mềm mát, phù hợp thời tiết nóng.', 'https://images.unsplash.com/photo-1571513722275-4b8c0215cd62?w=400', 4, 'X-Store', 'Polyester Crepe', 330000, 479000),

-- Giày Dép
(13, 'Giày Sneaker Nam Basic', 'Giày sneaker nam basic phong cách minimalist. Upper da synthetic bền bỉ, đế rubber chống trượt.', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400', 5, 'X-Store', 'Synthetic Leather', 580000, 799000),

(14, 'Giày Boot Nữ Chelsea', 'Giày boot nữ chelsea cổ điển. Chất da PU cao cấp, gót cao 5cm thanh lịch và thoải mái.', 'https://images.unsplash.com/photo-1515347619252-60a4bf4fff4f?w=400', 5, 'X-Store', 'PU Leather', 650000, 899000),

(15, 'Sandal Nữ Đế Xuồng', 'Sandal nữ đế xuồng phong cách bohemian. Quai ngang mềm mại, đế cao 7cm ổn định.', 'https://images.unsplash.com/photo-1560343090-f0409e92791a?w=400', 5, 'X-Store', 'Synthetic', 320000, 459000),

-- Phụ Kiện
(16, 'Túi Tote Canvas Unisex', 'Túi tote canvas unisex phong cách vintage. Chất canvas dày bền, size vừa phải đựng đồ hàng ngày.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400', 6, 'X-Store', 'Canvas', 180000, 259000),

(17, 'Nón Snapback Streetwear', 'Nón snapback streetwear chất cotton twill. Logo thêu nổi 3D, viền nón cong phong cách Mỹ.', 'https://images.unsplash.com/photo-1521119989659-a83eee488004?w=400', 6, 'X-Store', 'Cotton Twill', 150000, 219000),

(18, 'Thắt Lưng Da Nam', 'Thắt lưng da nam genuine leather. Mặt khóa kim loại cao cấp, thiết kế cổ điển sang trọng.', 'https://images.unsplash.com/photo-1624222247344-550fb60583dc?w=400', 6, 'X-Store', 'Genuine Leather', 290000, 419000);

-- Insert product colors (màu sắc sản phẩm)
INSERT IGNORE INTO product_colors (id, name, hex_code, product_id) VALUES
-- Áo Thun Nam Basic (1)
(1, 'Trắng', '#FFFFFF', 1), (2, 'Đen', '#000000', 1), (3, 'Xám', '#6B7280', 1), (4, 'Navy', '#1E3A8A', 1),
-- Áo Sơ Mi Nam (2) 
(5, 'Trắng', '#FFFFFF', 2), (6, 'Xanh Nhạt', '#DBEAFE', 2), (7, 'Hồng Nhạt', '#FCE7F3', 2),
-- Áo Hoodie Nam (3)
(8, 'Đen', '#000000', 3), (9, 'Xám', '#6B7280', 3), (10, 'Navy', '#1E3A8A', 3),
-- Áo Thun Nữ (4)
(11, 'Trắng', '#FFFFFF', 4), (12, 'Hồng', '#EC4899', 4), (13, 'Vàng', '#F59E0B', 4),
-- Áo Blouse Nữ (5)
(14, 'Trắng', '#FFFFFF', 5), (15, 'Be', '#F3E8FF', 5), (16, 'Xanh Mint', '#A7F3D0', 5),
-- Blazer Nữ (6)
(17, 'Đen', '#000000', 6), (18, 'Navy', '#1E3A8A', 6), (19, 'Xám', '#6B7280', 6),
-- Jeans Nam (7)
(20, 'Xanh Đậm', '#1E40AF', 7), (21, 'Xanh Nhạt', '#3B82F6', 7), (22, 'Đen', '#000000', 7),
-- Kaki Nam (8)
(23, 'Be', '#D97706', 8), (24, 'Xám', '#6B7280', 8), (25, 'Navy', '#1E3A8A', 8),
-- Short Nam (9)
(26, 'Đen', '#000000', 9), (27, 'Xám', '#6B7280', 9), (28, 'Navy', '#1E3A8A', 9),
-- Jeans Nữ (10)
(29, 'Xanh Đậm', '#1E40AF', 10), (30, 'Xanh Nhạt', '#3B82F6', 10), (31, 'Đen', '#000000', 10),
-- Chân Váy (11)
(32, 'Đen', '#000000', 11), (33, 'Be', '#D97706', 11), (34, 'Hồng', '#EC4899', 11),
-- Culottes (12)
(35, 'Đen', '#000000', 12), (36, 'Trắng', '#FFFFFF', 12), (37, 'Xám', '#6B7280', 12),
-- Sneaker Nam (13)
(38, 'Trắng', '#FFFFFF', 13), (39, 'Đen', '#000000', 13), (40, 'Xám', '#6B7280', 13),
-- Boot Nữ (14)
(41, 'Đen', '#000000', 14), (42, 'Nâu', '#92400E', 14),
-- Sandal Nữ (15)
(43, 'Đen', '#000000', 15), (44, 'Nâu', '#92400E', 15), (45, 'Be', '#D97706', 15),
-- Túi Tote (16)
(46, 'Be', '#D97706', 16), (47, 'Đen', '#000000', 16), (48, 'Navy', '#1E3A8A', 16),
-- Nón Snapback (17)
(49, 'Đen', '#000000', 17), (50, 'Xám', '#6B7280', 17), (51, 'Navy', '#1E3A8A', 17),
-- Thắt Lưng (18)
(52, 'Đen', '#000000', 18), (53, 'Nâu', '#92400E', 18);

-- Insert product sizes (kích cỡ sản phẩm)
INSERT IGNORE INTO product_sizes (id, name, description, product_id) VALUES
-- Áo sizes (S, M, L, XL) - Products 1-6
(1, 'S', 'Size S - Ngực 88cm, Vai 42cm', 1), (2, 'M', 'Size M - Ngực 92cm, Vai 44cm', 1), (3, 'L', 'Size L - Ngực 96cm, Vai 46cm', 1), (4, 'XL', 'Size XL - Ngực 100cm, Vai 48cm', 1),
(5, 'S', 'Size S - Ngực 88cm, Vai 42cm', 2), (6, 'M', 'Size M - Ngực 92cm, Vai 44cm', 2), (7, 'L', 'Size L - Ngực 96cm, Vai 46cm', 2), (8, 'XL', 'Size XL - Ngực 100cm, Vai 48cm', 2),
(9, 'S', 'Size S - Ngực 100cm, Vai 50cm', 3), (10, 'M', 'Size M - Ngực 104cm, Vai 52cm', 3), (11, 'L', 'Size L - Ngực 108cm, Vai 54cm', 3), (12, 'XL', 'Size XL - Ngực 112cm, Vai 56cm', 3),
(13, 'S', 'Size S - Ngực 82cm, Vai 38cm', 4), (14, 'M', 'Size M - Ngực 86cm, Vai 40cm', 4), (15, 'L', 'Size L - Ngực 90cm, Vai 42cm', 4),
(16, 'S', 'Size S - Ngực 86cm, Vai 40cm', 5), (17, 'M', 'Size M - Ngực 90cm, Vai 42cm', 5), (18, 'L', 'Size L - Ngực 94cm, Vai 44cm', 5),
(19, 'S', 'Size S - Ngực 86cm, Vai 40cm', 6), (20, 'M', 'Size M - Ngực 90cm, Vai 42cm', 6), (21, 'L', 'Size L - Ngực 94cm, Vai 44cm', 6),

-- Quần sizes (28-34) - Products 7-12
(22, '28', 'Size 28 - Eo 71cm, Mông 86cm', 7), (23, '29', 'Size 29 - Eo 74cm, Mông 89cm', 7), (24, '30', 'Size 30 - Eo 76cm, Mông 91cm', 7), (25, '31', 'Size 31 - Eo 79cm, Mông 94cm', 7), (26, '32', 'Size 32 - Eo 81cm, Mông 96cm', 7), (27, '34', 'Size 34 - Eo 86cm, Mông 101cm', 7),
(28, '28', 'Size 28 - Eo 71cm, Mông 86cm', 8), (29, '29', 'Size 29 - Eo 74cm, Mông 89cm', 8), (30, '30', 'Size 30 - Eo 76cm, Mông 91cm', 8), (31, '32', 'Size 32 - Eo 81cm, Mông 96cm', 8), (32, '34', 'Size 34 - Eo 86cm, Mông 101cm', 8),
(33, 'S', 'Size S - Eo 66cm, Mông 86cm', 9), (34, 'M', 'Size M - Eo 71cm, Mông 91cm', 9), (35, 'L', 'Size L - Eo 76cm, Mông 96cm', 9), (36, 'XL', 'Size XL - Eo 81cm, Mông 101cm', 9),
(37, '25', 'Size 25 - Eo 64cm, Mông 84cm', 10), (38, '26', 'Size 26 - Eo 66cm, Mông 86cm', 10), (39, '27', 'Size 27 - Eo 69cm, Mông 89cm', 10), (40, '28', 'Size 28 - Eo 71cm, Mông 91cm', 10), (41, '29', 'Size 29 - Eo 74cm, Mông 94cm', 10),
(42, 'S', 'Size S - Eo 64cm, Dài 40cm', 11), (43, 'M', 'Size M - Eo 69cm, Dài 41cm', 11), (44, 'L', 'Size L - Eo 74cm, Dài 42cm', 11),
(45, 'S', 'Size S - Eo 64cm, Dài 85cm', 12), (46, 'M', 'Size M - Eo 69cm, Dài 86cm', 12), (47, 'L', 'Size L - Eo 74cm, Dài 87cm', 12),

-- Giày sizes (36-43) - Products 13-15
(48, '39', 'Size 39 - 25cm', 13), (49, '40', 'Size 40 - 25.5cm', 13), (50, '41', 'Size 41 - 26cm', 13), (51, '42', 'Size 42 - 26.5cm', 13), (52, '43', 'Size 43 - 27cm', 13),
(53, '36', 'Size 36 - 23cm', 14), (54, '37', 'Size 37 - 23.5cm', 14), (55, '38', 'Size 38 - 24cm', 14), (56, '39', 'Size 39 - 24.5cm', 14),
(57, '36', 'Size 36 - 23cm', 15), (58, '37', 'Size 37 - 23.5cm', 15), (59, '38', 'Size 38 - 24cm', 15), (60, '39', 'Size 39 - 24.5cm', 15),

-- Phụ kiện - One size - Products 16-18
(61, 'Free Size', 'Size tự do 38x32cm', 16),
(62, 'Free Size', 'Size tự do 56-60cm', 17),
(63, 'S', 'Size S 80-85cm', 18), (64, 'M', 'Size M 85-90cm', 18), (65, 'L', 'Size L 90-95cm', 18);

-- Insert stock items (số lượng tồn kho)
INSERT IGNORE INTO stock_items (id, stock_id, product_id, quantity) VALUES
-- Áo Thun Nam Basic (1)
(1, 1, 1, 80), (2, 2, 1, 120), (3, 3, 1, 60),
-- Áo Sơ Mi Nam (2)
(4, 1, 2, 45), (5, 2, 2, 70), (6, 3, 2, 35),
-- Hoodie Nam (3)
(7, 1, 3, 30), (8, 2, 3, 50), (9, 3, 3, 25),
-- Áo Thun Nữ (4)
(10, 1, 4, 90), (11, 2, 4, 110), (12, 3, 4, 70),
-- Blouse Nữ (5)
(13, 1, 5, 40), (14, 2, 5, 60), (15, 3, 5, 30),
-- Blazer Nữ (6)
(16, 1, 6, 25), (17, 2, 6, 35), (18, 3, 6, 20),
-- Jeans Nam (7)
(19, 1, 7, 60), (20, 2, 7, 85), (21, 3, 7, 45),
-- Kaki Nam (8)
(22, 1, 8, 50), (23, 2, 8, 70), (24, 3, 8, 40),
-- Short Nam (9)
(25, 1, 9, 75), (26, 2, 9, 95), (27, 3, 9, 55),
-- Jeans Nữ (10)
(28, 1, 10, 55), (29, 2, 10, 80), (30, 3, 10, 40),
-- Chân Váy (11)
(31, 1, 11, 65), (32, 2, 11, 85), (33, 3, 11, 50),
-- Culottes (12)
(34, 1, 12, 45), (35, 2, 12, 65), (36, 3, 12, 35),
-- Sneaker Nam (13)
(37, 1, 13, 25), (38, 2, 13, 40), (39, 3, 13, 20),
-- Boot Nữ (14)
(40, 1, 14, 20), (41, 2, 14, 35), (42, 3, 14, 15),
-- Sandal Nữ (15)
(43, 1, 15, 35), (44, 2, 15, 50), (45, 3, 15, 25),
-- Túi Tote (16)
(46, 1, 16, 40), (47, 2, 16, 60), (48, 3, 16, 30),
-- Nón Snapback (17)
(49, 1, 17, 55), (50, 2, 17, 75), (51, 3, 17, 45),
-- Thắt Lưng (18)
(52, 1, 18, 30), (53, 2, 18, 45), (54, 3, 18, 25);

-- Insert sample discounts (mã giảm giá)
INSERT IGNORE INTO discounts (id, name, title, description, type, discount_amount, discount_percent, usage_count, max_usage, start_date, end_date, is_active) VALUES
(1, 'WELCOME10', 'Ưu đãi khách mới', 'Giảm 10% cho khách hàng mới', 'PERCENT', 0, 10.0, 0, 1000, '2024-01-01', '2024-12-31', true),
(2, 'SALE50K', 'Giảm 50K', 'Giảm 50k cho đơn hàng từ 500k', 'FIXED', 50000, 0.0, 0, 500, '2024-01-01', '2024-12-31', true),
(3, 'SUMMER20', 'Khuyến mãi hè', 'Khuyến mãi hè - Giảm 20%', 'PERCENT', 0, 20.0, 0, 200, '2024-06-01', '2024-08-31', true),
(4, 'FREESHIP', 'Miễn phí ship', 'Miễn phí vận chuyển', 'FIXED', 30000, 0.0, 0, 2000, '2024-01-01', '2024-12-31', true),
(5, 'VIP15', 'Ưu đãi VIP', 'Ưu đãi VIP - Giảm 15%', 'PERCENT', 0, 15.0, 0, 100, '2024-01-01', '2024-12-31', true);

-- Reset AUTO_INCREMENT
ALTER TABLE stocks AUTO_INCREMENT = 4;
ALTER TABLE product_types AUTO_INCREMENT = 7;
ALTER TABLE products AUTO_INCREMENT = 19;
ALTER TABLE product_colors AUTO_INCREMENT = 54;
ALTER TABLE product_sizes AUTO_INCREMENT = 66;
ALTER TABLE stock_items AUTO_INCREMENT = 55;
ALTER TABLE discounts AUTO_INCREMENT = 6;

