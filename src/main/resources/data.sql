-- XStore Fashion - Dữ liệu mặc định cho shop bán quần áo
-- Created: 2025-11-13

-- Tắt foreign key checks để có thể xóa dữ liệu
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM product_info WHERE id > 0;
DELETE FROM stock_items WHERE id > 0;
DELETE FROM products WHERE id > 0;
DELETE FROM product_types WHERE id > 0;
DELETE FROM stocks WHERE id > 0;
DELETE FROM discounts WHERE id > 0;
-- Note: Không xóa comments và comment_attachments để giữ dữ liệu user

-- Bật lại foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create comment_attachments table
CREATE TABLE IF NOT EXISTS comment_attachments (
                                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   comment_id BIGINT NOT NULL,
                                                   image_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(10) NOT NULL DEFAULT 'image',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
    );

-- Add author_name column to comments table if it doesn't exist
ALTER TABLE comments ADD COLUMN IF NOT EXISTS author_name VARCHAR(255) NOT NULL DEFAULT 'Unknown';

-- Create comments table if it doesn't exist
CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        product_id BIGINT NOT NULL,
                                        author_id BIGINT NOT NULL,
                                        author_name VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    comment_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rate INT NOT NULL CHECK (rate >= 1 AND rate <= 5),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
    );

-- Insert sample stocks (kho hàng)
INSERT IGNORE INTO stocks (id, name, phone, email, address_id) VALUES
(1, 'Kho Miền Bắc - Hà Nội', '0243.456.789', 'hanoi@xstore.vn', null),
(2, 'Kho Miền Nam - TP.HCM', '0283.456.789', 'hcm@xstore.vn', null),
(3, 'Kho Miền Trung - Đà Nẵng', '0236.456.789', 'danang@xstore.vn', null),

-- Insert product types (danh mục sản phẩm)
INSERT IGNORE INTO product_types (id, name, description) VALUES
(1, 'Đồ Nam', 'Quần áo và trang phục dành cho nam giới'),
(2, 'Đồ Nữ', 'Quần áo và trang phục dành cho nữ giới'),
(3, 'Phụ Kiện', 'Giày dép, túi xách, nón, thắt lưng và các phụ kiện khác');

-- Insert sample products với ảnh thật từ Unsplash
INSERT IGNORE INTO products (id, name, description, image, product_type_id, brand, fabric, price_in_stock, price) VALUES
-- Đồ Nam
(1, 'Áo Thun Nam Basic Cotton', 'Áo thun nam basic chất liệu cotton 100%, form regular fit thoải mái. Phù hợp mặc hàng ngày, đi chơi hay đi làm.', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400', 1, 'X-Store', 'Cotton 100%', 120000, 179000),

(2, 'Áo Sơ Mi Nam Công Sở', 'Áo sơ mi nam công sở chất vải cotton pha, form slim fit hiện đại. Thiết kế lịch lãm, phù hợp môi trường công sở.', 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=400', 1, 'X-Store', 'Cotton Blend', 280000, 399000),

(3, 'Áo Hoodie Nam Streetwear', 'Áo hoodie nam phong cách streetwear, chất nỉ cotton dày dặn. Form oversize trendy với hood và túi kangaroo.', 'https://images.unsplash.com/photo-1556821840-3a9fbc86339e?w=400', 1, 'X-Store', 'Cotton Fleece', 350000, 499000),

-- Đồ Nữ
(4, 'Áo Thun Nữ Crop Top', 'Áo thun nữ crop top trendy chất cotton co giãn. Form fitted ôm dáng, phù hợp mix đồ năng động.', 'https://images.unsplash.com/photo-1594223274512-ad4803739b7c?w=400', 2, 'X-Store', 'Cotton Spandex', 140000, 199000),

(5, 'Áo Blouse Nữ Công Sở', 'Áo blouse nữ công sở chất vải lụa mềm mại. Thiết kế thanh lịch với tay bồng nhẹ, phù hợp đi làm.', 'https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=400', 2, 'X-Store', 'Polyester Silk', 320000, 459000),

(6, 'Áo Khoác Blazer Nữ', 'Áo khoác blazer nữ form fitted sang trọng. Chất vải polyester cao cấp, thiết kế 1 button hiện đại.', 'https://images.unsplash.com/photo-1591369823-a8e19c5f06d6?w=400', 2, 'X-Store', 'Polyester Premium', 450000, 649000),

(7, 'Quần Jeans Nam Slim Fit', 'Quần jeans nam slim fit chất denim cotton co giãn. Wash nhẹ tự nhiên, phom dáng ôm vừa phải.', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', 1, 'X-Store', 'Denim Cotton', 380000, 549000),

(8, 'Quần Kaki Nam Chinos', 'Quần kaki nam chinos chất cotton twill mềm mại. Form straight leg thoải mái, phù hợp nhiều dịp.', 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400', 1, 'X-Store', 'Cotton Twill', 290000, 419000),

(9, 'Quần Short Nam Thể Thao', 'Quần short nam thể thao chất polyester thấm hút mồ hôi. Có lót bên trong và túi zip tiện lợi.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 1, 'X-Store', 'Polyester DryFit', 180000, 259000),

(10, 'Quần Jeans Nữ Skinny', 'Quần jeans nữ skinny fit ôm dáng hoàn hảo. Chất denim co giãn 4 chiều, tôn lên đường cong cơ thể.', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400', 2, 'X-Store', 'Denim Stretch', 420000, 599000),

(11, 'Chân Váy Chữ A Vintage', 'Chân váy chữ A vintage chất vải cotton pha. Thiết kế xoè nhẹ, độ dài qua gối thanh lịch.', 'https://images.unsplash.com/photo-1583496661160-fb5886a13d75?w=400', 2, 'X-Store', 'Cotton Blend', 250000, 359000),

(12, 'Quần Culottes Nữ Wide Leg', 'Quần culottes nữ wide leg thời thượng. Chất vải polyester mềm mát, phù hợp thời tiết nóng.', 'https://images.unsplash.com/photo-1571513722275-4b8c0215cd62?w=400', 2, 'X-Store', 'Polyester Crepe', 330000, 479000),

-- Phụ Kiện
(13, 'Giày Sneaker Nam Basic', 'Giày sneaker nam basic phong cách minimalist. Upper da synthetic bền bỉ, đế rubber chống trượt.', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400', 3, 'X-Store', 'Synthetic Leather', 580000, 799000),

(14, 'Giày Boot Nữ Chelsea', 'Giày boot nữ chelsea cổ điển. Chất da PU cao cấp, gót cao 5cm thanh lịch và thoải mái.', 'https://images.unsplash.com/photo-1515347619252-60a4bf4fff4f?w=400', 3, 'X-Store', 'PU Leather', 650000, 899000),

(15, 'Sandal Nữ Đế Xuồng', 'Sandal nữ đế xuồng phong cách bohemian. Quai ngang mềm mại, đế cao 7cm ổn định.', 'https://images.unsplash.com/photo-1560343090-f0409e92791a?w=400', 3, 'X-Store', 'Synthetic', 320000, 459000),

(16, 'Túi Tote Canvas Unisex', 'Túi tote canvas unisex phong cách vintage. Chất canvas dày bền, size vừa phải đựng đồ hàng ngày.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400', 3, 'X-Store', 'Canvas', 180000, 259000),

(17, 'Nón Snapback Streetwear', 'Nón snapback streetwear chất cotton twill. Logo thêu nổi 3D, viền nón cong phong cách Mỹ.', 'https://images.unsplash.com/photo-1521119989659-a83eee488004?w=400', 3, 'X-Store', 'Cotton Twill', 150000, 219000),

(18, 'Thắt Lưng Da Nam', 'Thắt lưng da nam genuine leather. Mặt khóa kim loại cao cấp, thiết kế cổ điển sang trọng.', 'https://images.unsplash.com/photo-1624222247344-550fb60583dc?w=400', 3, 'X-Store', 'Genuine Leather', 290000, 419000);

-- Insert product_info (thông tin biến thể: color, size, quantity)
INSERT IGNORE INTO product_info (id, product_id, color_name, color_hex_code, size_name, image) VALUES
-- Áo Thun Nam Basic (Product 1) - 4 colors x 4 sizes = 16 variants
(1, 1, 'Trắng', '#FFFFFF', 'S', null), (2, 1, 'Trắng', '#FFFFFF', 'M', null), (3, 1, 'Trắng', '#FFFFFF', 'L', null), (4, 1, 'Trắng', '#FFFFFF', 'XL', null),
(5, 1, 'Đen', '#000000', 'S', null), (6, 1, 'Đen', '#000000', 'M', null), (7, 1, 'Đen', '#000000', 'L', null), (8, 1, 'Đen', '#000000', 'XL', null),
(9, 1, 'Xám', '#6B7280', 'S', null), (10, 1, 'Xám', '#6B7280', 'M', null), (11, 1, 'Xám', '#6B7280', 'L', null), (12, 1, 'Xám', '#6B7280', 'XL', null),
(13, 1, 'Navy', '#1E3A8A', 'S', null), (14, 1, 'Navy', '#1E3A8A', 'M', null), (15, 1, 'Navy', '#1E3A8A', 'L', null), (16, 1, 'Navy', '#1E3A8A', 'XL', null),

-- Áo Sơ Mi Nam (Product 2) - 3 colors x 4 sizes = 12 variants
(17, 2, 'Trắng', '#FFFFFF', 'S', null), (18, 2, 'Trắng', '#FFFFFF', 'M', null), (19, 2, 'Trắng', '#FFFFFF', 'L', null), (20, 2, 'Trắng', '#FFFFFF', 'XL', null),
(21, 2, 'Xanh Nhạt', '#DBEAFE', 'S', null), (22, 2, 'Xanh Nhạt', '#DBEAFE', 'M', null), (23, 2, 'Xanh Nhạt', '#DBEAFE', 'L', null), (24, 2, 'Xanh Nhạt', '#DBEAFE', 'XL', null),
(25, 2, 'Hồng Nhạt', '#FCE7F3', 'S', null), (26, 2, 'Hồng Nhạt', '#FCE7F3', 'M', null), (27, 2, 'Hồng Nhạt', '#FCE7F3', 'L', null), (28, 2, 'Hồng Nhạt', '#FCE7F3', 'XL', null),

-- Áo Hoodie Nam (Product 3) - 3 colors x 4 sizes = 12 variants
(29, 3, 'Đen', '#000000', 'S', null), (30, 3, 'Đen', '#000000', 'M', null), (31, 3, 'Đen', '#000000', 'L', null), (32, 3, 'Đen', '#000000', 'XL', null),
(33, 3, 'Xám', '#6B7280', 'S', null), (34, 3, 'Xám', '#6B7280', 'M', null), (35, 3, 'Xám', '#6B7280', 'L', null), (36, 3, 'Xám', '#6B7280', 'XL', null),
(37, 3, 'Navy', '#1E3A8A', 'S', null), (38, 3, 'Navy', '#1E3A8A', 'M', null), (39, 3, 'Navy', '#1E3A8A', 'L', null), (40, 3, 'Navy', '#1E3A8A', 'XL', null),

-- Áo Thun Nữ Crop Top (Product 4) - 3 colors x 3 sizes = 9 variants
(41, 4, 'Trắng', '#FFFFFF', 'S', null), (42, 4, 'Trắng', '#FFFFFF', 'M', null), (43, 4, 'Trắng', '#FFFFFF', 'L', null),
(44, 4, 'Hồng', '#EC4899', 'S', null), (45, 4, 'Hồng', '#EC4899', 'M', null), (46, 4, 'Hồng', '#EC4899', 'L', null),
(47, 4, 'Vàng', '#F59E0B', 'S', null), (48, 4, 'Vàng', '#F59E0B', 'M', null), (49, 4, 'Vàng', '#F59E0B', 'L', null),

-- Áo Blouse Nữ (Product 5) - 3 colors x 3 sizes = 9 variants
(50, 5, 'Trắng', '#FFFFFF', 'S', null), (51, 5, 'Trắng', '#FFFFFF', 'M', null), (52, 5, 'Trắng', '#FFFFFF', 'L', null),
(53, 5, 'Be', '#F3E8FF', 'S', null), (54, 5, 'Be', '#F3E8FF', 'M', null), (55, 5, 'Be', '#F3E8FF', 'L', null),
(56, 5, 'Xanh Mint', '#A7F3D0', 'S', null), (57, 5, 'Xanh Mint', '#A7F3D0', 'M', null), (58, 5, 'Xanh Mint', '#A7F3D0', 'L', null),

-- Áo Khoác Blazer Nữ (Product 6) - 3 colors x 3 sizes = 9 variants
(59, 6, 'Đen', '#000000', 'S', null), (60, 6, 'Đen', '#000000', 'M', null), (61, 6, 'Đen', '#000000', 'L', null),
(62, 6, 'Navy', '#1E3A8A', 'S', null), (63, 6, 'Navy', '#1E3A8A', 'M', null), (64, 6, 'Navy', '#1E3A8A', 'L', null),
(65, 6, 'Xám', '#6B7280', 'S', null), (66, 6, 'Xám', '#6B7280', 'M', null), (67, 6, 'Xám', '#6B7280', 'L', null),

-- Quần Jeans Nam (Product 7) - 3 colors x 6 sizes = 18 variants
(68, 7, 'Xanh Đậm', '#1E40AF', '28', null), (69, 7, 'Xanh Đậm', '#1E40AF', '29', null), (70, 7, 'Xanh Đậm', '#1E40AF', '30', null), (71, 7, 'Xanh Đậm', '#1E40AF', '31', null), (72, 7, 'Xanh Đậm', '#1E40AF', '32', null), (73, 7, 'Xanh Đậm', '#1E40AF', '34', null),
(74, 7, 'Xanh Nhạt', '#3B82F6', '28', null), (75, 7, 'Xanh Nhạt', '#3B82F6', '29', null), (76, 7, 'Xanh Nhạt', '#3B82F6', '30', null), (77, 7, 'Xanh Nhạt', '#3B82F6', '31', null), (78, 7, 'Xanh Nhạt', '#3B82F6', '32', null), (79, 7, 'Xanh Nhạt', '#3B82F6', '34', null),
(80, 7, 'Đen', '#000000', '28', null), (81, 7, 'Đen', '#000000', '29', null), (82, 7, 'Đen', '#000000', '30', null), (83, 7, 'Đen', '#000000', '31', null), (84, 7, 'Đen', '#000000', '32', null), (85, 7, 'Đen', '#000000', '34', null),

-- Quần Kaki Nam (Product 8) - 3 colors x 5 sizes = 15 variants
(86, 8, 'Be', '#D97706', '28', null), (87, 8, 'Be', '#D97706', '29', null), (88, 8, 'Be', '#D97706', '30', null), (89, 8, 'Be', '#D97706', '32', null), (90, 8, 'Be', '#D97706', '34', null),
(91, 8, 'Xám', '#6B7280', '28', null), (92, 8, 'Xám', '#6B7280', '29', null), (93, 8, 'Xám', '#6B7280', '30', null), (94, 8, 'Xám', '#6B7280', '32', null), (95, 8, 'Xám', '#6B7280', '34', null),
(96, 8, 'Navy', '#1E3A8A', '28', null), (97, 8, 'Navy', '#1E3A8A', '29', null), (98, 8, 'Navy', '#1E3A8A', '30', null), (99, 8, 'Navy', '#1E3A8A', '32', null), (100, 8, 'Navy', '#1E3A8A', '34', null),

-- Quần Short Nam (Product 9) - 3 colors x 4 sizes = 12 variants
(101, 9, 'Đen', '#000000', 'S', null), (102, 9, 'Đen', '#000000', 'M', null), (103, 9, 'Đen', '#000000', 'L', null), (104, 9, 'Đen', '#000000', 'XL', null),
(105, 9, 'Xám', '#6B7280', 'S', null), (106, 9, 'Xám', '#6B7280', 'M', null), (107, 9, 'Xám', '#6B7280', 'L', null), (108, 9, 'Xám', '#6B7280', 'XL', null),
(109, 9, 'Navy', '#1E3A8A', 'S', null), (110, 9, 'Navy', '#1E3A8A', 'M', null), (111, 9, 'Navy', '#1E3A8A', 'L', null), (112, 9, 'Navy', '#1E3A8A', 'XL', null),

-- Quần Jeans Nữ (Product 10) - 3 colors x 5 sizes = 15 variants
(113, 10, 'Xanh Đậm', '#1E40AF', '25', null), (114, 10, 'Xanh Đậm', '#1E40AF', '26', null), (115, 10, 'Xanh Đậm', '#1E40AF', '27', null), (116, 10, 'Xanh Đậm', '#1E40AF', '28', null), (117, 10, 'Xanh Đậm', '#1E40AF', '29', null),
(118, 10, 'Xanh Nhạt', '#3B82F6', '25', null), (119, 10, 'Xanh Nhạt', '#3B82F6', '26', null), (120, 10, 'Xanh Nhạt', '#3B82F6', '27', null), (121, 10, 'Xanh Nhạt', '#3B82F6', '28', null), (122, 10, 'Xanh Nhạt', '#3B82F6', '29', null),
(123, 10, 'Đen', '#000000', '25', null), (124, 10, 'Đen', '#000000', '26', null), (125, 10, 'Đen', '#000000', '27', null), (126, 10, 'Đen', '#000000', '28', null), (127, 10, 'Đen', '#000000', '29', null),

-- Chân Váy (Product 11) - 3 colors x 3 sizes = 9 variants
(128, 11, 'Đen', '#000000', 'S', null), (129, 11, 'Đen', '#000000', 'M', null), (130, 11, 'Đen', '#000000', 'L', null),
(131, 11, 'Be', '#D97706', 'S', null), (132, 11, 'Be', '#D97706', 'M', null), (133, 11, 'Be', '#D97706', 'L', null),
(134, 11, 'Hồng', '#EC4899', 'S', null), (135, 11, 'Hồng', '#EC4899', 'M', null), (136, 11, 'Hồng', '#EC4899', 'L', null),

-- Quần Culottes (Product 12) - 3 colors x 3 sizes = 9 variants
(137, 12, 'Đen', '#000000', 'S', null), (138, 12, 'Đen', '#000000', 'M', null), (139, 12, 'Đen', '#000000', 'L', null),
(140, 12, 'Trắng', '#FFFFFF', 'S', null), (141, 12, 'Trắng', '#FFFFFF', 'M', null), (142, 12, 'Trắng', '#FFFFFF', 'L', null),
(143, 12, 'Xám', '#6B7280', 'S', null), (144, 12, 'Xám', '#6B7280', 'M', null), (145, 12, 'Xám', '#6B7280', 'L', null),

-- Giày Sneaker Nam (Product 13) - 3 colors x 5 sizes = 15 variants
(146, 13, 'Trắng', '#FFFFFF', '39', null), (147, 13, 'Trắng', '#FFFFFF', '40', null), (148, 13, 'Trắng', '#FFFFFF', '41', null), (149, 13, 'Trắng', '#FFFFFF', '42', null), (150, 13, 'Trắng', '#FFFFFF', '43', null),
(151, 13, 'Đen', '#000000', '39', null), (152, 13, 'Đen', '#000000', '40', null), (153, 13, 'Đen', '#000000', '41', null), (154, 13, 'Đen', '#000000', '42', null), (155, 13, 'Đen', '#000000', '43', null),
(156, 13, 'Xám', '#6B7280', '39', null), (157, 13, 'Xám', '#6B7280', '40', null), (158, 13, 'Xám', '#6B7280', '41', null), (159, 13, 'Xám', '#6B7280', '42', null), (160, 13, 'Xám', '#6B7280', '43', null),

-- Giày Boot Nữ (Product 14) - 2 colors x 4 sizes = 8 variants
(161, 14, 'Đen', '#000000', '36', null), (162, 14, 'Đen', '#000000', '37', null), (163, 14, 'Đen', '#000000', '38', null), (164, 14, 'Đen', '#000000', '39', null),
(165, 14, 'Nâu', '#92400E', '36', null), (166, 14, 'Nâu', '#92400E', '37', null), (167, 14, 'Nâu', '#92400E', '38', null), (168, 14, 'Nâu', '#92400E', '39', null),

-- Sandal Nữ (Product 15) - 3 colors x 4 sizes = 12 variants
(169, 15, 'Đen', '#000000', '36', null), (170, 15, 'Đen', '#000000', '37', null), (171, 15, 'Đen', '#000000', '38', null), (172, 15, 'Đen', '#000000', '39', null),
(173, 15, 'Nâu', '#92400E', '36', null), (174, 15, 'Nâu', '#92400E', '37', null), (175, 15, 'Nâu', '#92400E', '38', null), (176, 15, 'Nâu', '#92400E', '39', null),
(177, 15, 'Be', '#D97706', '36', null), (178, 15, 'Be', '#D97706', '37', null), (179, 15, 'Be', '#D97706', '38', null), (180, 15, 'Be', '#D97706', '39', null),

-- Túi Tote (Product 16) - 3 colors x 1 size = 3 variants
(181, 16, 'Be', '#D97706', 'Free Size', null),
(182, 16, 'Đen', '#000000', 'Free Size', null),
(183, 16, 'Navy', '#1E3A8A', 'Free Size', null),

-- Nón Snapback (Product 17) - 3 colors x 1 size = 3 variants
(184, 17, 'Đen', '#000000', 'Free Size', null),
(185, 17, 'Xám', '#6B7280', 'Free Size', null),
(186, 17, 'Navy', '#1E3A8A', 'Free Size', null),

-- Thắt Lưng (Product 18) - 2 colors x 3 sizes = 6 variants
(187, 18, 'Đen', '#000000', 'S', null), (188, 18, 'Đen', '#000000', 'M', null), (189, 18, 'Đen', '#000000', 'L', null),
(190, 18, 'Nâu', '#92400E', 'S', null), (191, 18, 'Nâu', '#92400E', 'M', null), (192, 18, 'Nâu', '#92400E', 'L', null);

-- Insert product_info for new products (19-50)
INSERT IGNORE INTO product_info (id, product_id, color_name, color_hex_code, size_name, image) VALUES
-- Áo Polo Nam (19) - 3 colors x 4 sizes = 12 variants
(193, 19, 'Trắng', '#FFFFFF', 'S', null), (194, 19, 'Trắng', '#FFFFFF', 'M', null), (195, 19, 'Trắng', '#FFFFFF', 'L', null), (196, 19, 'Trắng', '#FFFFFF', 'XL', null),
(197, 19, 'Xanh Navy', '#1E3A8A', 'S', null), (198, 19, 'Xanh Navy', '#1E3A8A', 'M', null), (199, 19, 'Xanh Navy', '#1E3A8A', 'L', null), (200, 19, 'Xanh Navy', '#1E3A8A', 'XL', null),
(201, 19, 'Đỏ', '#DC2626', 'S', null), (202, 19, 'Đỏ', '#DC2626', 'M', null), (203, 19, 'Đỏ', '#DC2626', 'L', null), (204, 19, 'Đỏ', '#DC2626', 'XL', null),

-- Áo Len Nam (20) - 3 colors x 4 sizes = 12 variants
(205, 20, 'Xám', '#6B7280', 'S', null), (206, 20, 'Xám', '#6B7280', 'M', null), (207, 20, 'Xám', '#6B7280', 'L', null), (208, 20, 'Xám', '#6B7280', 'XL', null),
(209, 20, 'Đen', '#000000', 'S', null), (210, 20, 'Đen', '#000000', 'M', null), (211, 20, 'Đen', '#000000', 'L', null), (212, 20, 'Đen', '#000000', 'XL', null),
(213, 20, 'Navy', '#1E3A8A', 'S', null), (214, 20, 'Navy', '#1E3A8A', 'M', null), (215, 20, 'Navy', '#1E3A8A', 'L', null), (216, 20, 'Navy', '#1E3A8A', 'XL', null),

-- Áo Khoác Nam (21) - 3 colors x 4 sizes = 12 variants
(217, 21, 'Đen', '#000000', 'S', null), (218, 21, 'Đen', '#000000', 'M', null), (219, 21, 'Đen', '#000000', 'L', null), (220, 21, 'Đen', '#000000', 'XL', null),
(221, 21, 'Xanh', '#2563EB', 'S', null), (222, 21, 'Xanh', '#2563EB', 'M', null), (223, 21, 'Xanh', '#2563EB', 'L', null), (224, 21, 'Xanh', '#2563EB', 'XL', null),
(225, 21, 'Xám', '#6B7280', 'S', null), (226, 21, 'Xám', '#6B7280', 'M', null), (227, 21, 'Xám', '#6B7280', 'L', null), (228, 21, 'Xám', '#6B7280', 'XL', null),

-- Áo Vest Nam (22) - 2 colors x 4 sizes = 8 variants
(229, 22, 'Đen', '#000000', 'S', null), (230, 22, 'Đen', '#000000', 'M', null), (231, 22, 'Đen', '#000000', 'L', null), (232, 22, 'Đen', '#000000', 'XL', null),
(233, 22, 'Xám', '#6B7280', 'S', null), (234, 22, 'Xám', '#6B7280', 'M', null), (235, 22, 'Xám', '#6B7280', 'L', null), (236, 22, 'Xám', '#6B7280', 'XL', null),

-- Áo Thun Graphic (23) - 3 colors x 4 sizes = 12 variants
(237, 23, 'Trắng', '#FFFFFF', 'S', null), (238, 23, 'Trắng', '#FFFFFF', 'M', null), (239, 23, 'Trắng', '#FFFFFF', 'L', null), (240, 23, 'Trắng', '#FFFFFF', 'XL', null),
(241, 23, 'Đen', '#000000', 'S', null), (242, 23, 'Đen', '#000000', 'M', null), (243, 23, 'Đen', '#000000', 'L', null), (244, 23, 'Đen', '#000000', 'XL', null),
(245, 23, 'Xám', '#6B7280', 'S', null), (246, 23, 'Xám', '#6B7280', 'M', null), (247, 23, 'Xám', '#6B7280', 'L', null), (248, 23, 'Xám', '#6B7280', 'XL', null),

-- Áo Cardigan Nam (24) - 3 colors x 4 sizes = 12 variants
(249, 24, 'Be', '#D97706', 'S', null), (250, 24, 'Be', '#D97706', 'M', null), (251, 24, 'Be', '#D97706', 'L', null), (252, 24, 'Be', '#D97706', 'XL', null),
(253, 24, 'Xanh', '#2563EB', 'S', null), (254, 24, 'Xanh', '#2563EB', 'M', null), (255, 24, 'Xanh', '#2563EB', 'L', null), (256, 24, 'Xanh', '#2563EB', 'XL', null),
(257, 24, 'Đen', '#000000', 'S', null), (258, 24, 'Đen', '#000000', 'M', null), (259, 24, 'Đen', '#000000', 'L', null), (260, 24, 'Đen', '#000000', 'XL', null),

-- Áo Tank Top Nam (25) - 3 colors x 4 sizes = 12 variants
(261, 25, 'Trắng', '#FFFFFF', 'S', null), (262, 25, 'Trắng', '#FFFFFF', 'M', null), (263, 25, 'Trắng', '#FFFFFF', 'L', null), (264, 25, 'Trắng', '#FFFFFF', 'XL', null),
(265, 25, 'Đen', '#000000', 'S', null), (266, 25, 'Đen', '#000000', 'M', null), (267, 25, 'Đen', '#000000', 'L', null), (268, 25, 'Đen', '#000000', 'XL', null),
(269, 25, 'Xanh', '#2563EB', 'S', null), (270, 25, 'Xanh', '#2563EB', 'M', null), (271, 25, 'Xanh', '#2563EB', 'L', null), (272, 25, 'Xanh', '#2563EB', 'XL', null),

-- Áo Cardigan Nữ (26) - 3 colors x 3 sizes = 9 variants
(273, 26, 'Trắng', '#FFFFFF', 'S', null), (274, 26, 'Trắng', '#FFFFFF', 'M', null), (275, 26, 'Trắng', '#FFFFFF', 'L', null),
(276, 26, 'Hồng', '#EC4899', 'S', null), (277, 26, 'Hồng', '#EC4899', 'M', null), (278, 26, 'Hồng', '#EC4899', 'L', null),
(279, 26, 'Xanh', '#2563EB', 'S', null), (280, 26, 'Xanh', '#2563EB', 'M', null), (281, 26, 'Xanh', '#2563EB', 'L', null),

-- Áo Trench Coat (27) - 2 colors x 3 sizes = 6 variants
(282, 27, 'Be', '#D97706', 'S', null), (283, 27, 'Be', '#D97706', 'M', null), (284, 27, 'Be', '#D97706', 'L', null),
(285, 27, 'Đen', '#000000', 'S', null), (286, 27, 'Đen', '#000000', 'M', null), (287, 27, 'Đen', '#000000', 'L', null),

-- Áo Thun Oversize (28) - 3 colors x 3 sizes = 9 variants
(288, 28, 'Trắng', '#FFFFFF', 'S', null), (289, 28, 'Trắng', '#FFFFFF', 'M', null), (290, 28, 'Trắng', '#FFFFFF', 'L', null),
(291, 28, 'Đen', '#000000', 'S', null), (292, 28, 'Đen', '#000000', 'M', null), (293, 28, 'Đen', '#000000', 'L', null),
(294, 28, 'Xám', '#6B7280', 'S', null), (295, 28, 'Xám', '#6B7280', 'M', null), (296, 28, 'Xám', '#6B7280', 'L', null),

-- Áo Blazer Slim (29) - 2 colors x 3 sizes = 6 variants
(297, 29, 'Đen', '#000000', 'S', null), (298, 29, 'Đen', '#000000', 'M', null), (299, 29, 'Đen', '#000000', 'L', null),
(300, 29, 'Navy', '#1E3A8A', 'S', null), (301, 29, 'Navy', '#1E3A8A', 'M', null), (302, 29, 'Navy', '#1E3A8A', 'L', null),

-- Áo Sweater Nữ (30) - 3 colors x 3 sizes = 9 variants
(303, 30, 'Trắng', '#FFFFFF', 'S', null), (304, 30, 'Trắng', '#FFFFFF', 'M', null), (305, 30, 'Trắng', '#FFFFFF', 'L', null),
(306, 30, 'Xám', '#6B7280', 'S', null), (307, 30, 'Xám', '#6B7280', 'M', null), (308, 30, 'Xám', '#6B7280', 'L', null),
(309, 30, 'Hồng', '#EC4899', 'S', null), (310, 30, 'Hồng', '#EC4899', 'M', null), (311, 30, 'Hồng', '#EC4899', 'L', null),

-- Áo Vest Nữ (31) - 2 colors x 3 sizes = 6 variants
(312, 31, 'Đen', '#000000', 'S', null), (313, 31, 'Đen', '#000000', 'M', null), (314, 31, 'Đen', '#000000', 'L', null),
(315, 31, 'Xám', '#6B7280', 'S', null), (316, 31, 'Xám', '#6B7280', 'M', null), (317, 31, 'Xám', '#6B7280', 'L', null),

-- Áo Tank Top Nữ (32) - 3 colors x 3 sizes = 9 variants
(318, 32, 'Trắng', '#FFFFFF', 'S', null), (319, 32, 'Trắng', '#FFFFFF', 'M', null), (320, 32, 'Trắng', '#FFFFFF', 'L', null),
(321, 32, 'Đen', '#000000', 'S', null), (322, 32, 'Đen', '#000000', 'M', null), (323, 32, 'Đen', '#000000', 'L', null),
(324, 32, 'Hồng', '#EC4899', 'S', null), (325, 32, 'Hồng', '#EC4899', 'M', null), (326, 32, 'Hồng', '#EC4899', 'L', null),

-- Quần Jogger Nam (33) - 3 colors x 5 sizes = 15 variants
(327, 33, 'Đen', '#000000', '28', null), (328, 33, 'Đen', '#000000', '29', null), (329, 33, 'Đen', '#000000', '30', null), (330, 33, 'Đen', '#000000', '32', null), (331, 33, 'Đen', '#000000', '34', null),
(332, 33, 'Xám', '#6B7280', '28', null), (333, 33, 'Xám', '#6B7280', '29', null), (334, 33, 'Xám', '#6B7280', '30', null), (335, 33, 'Xám', '#6B7280', '32', null), (336, 33, 'Xám', '#6B7280', '34', null),
(337, 33, 'Navy', '#1E3A8A', '28', null), (338, 33, 'Navy', '#1E3A8A', '29', null), (339, 33, 'Navy', '#1E3A8A', '30', null), (340, 33, 'Navy', '#1E3A8A', '32', null), (341, 33, 'Navy', '#1E3A8A', '34', null),

-- Quần Cargo Nam (34) - 2 colors x 5 sizes = 10 variants
(342, 34, 'Xanh', '#2563EB', '28', null), (343, 34, 'Xanh', '#2563EB', '29', null), (344, 34, 'Xanh', '#2563EB', '30', null), (345, 34, 'Xanh', '#2563EB', '32', null), (346, 34, 'Xanh', '#2563EB', '34', null),
(347, 34, 'Đen', '#000000', '28', null), (348, 34, 'Đen', '#000000', '29', null), (349, 34, 'Đen', '#000000', '30', null), (350, 34, 'Đen', '#000000', '32', null), (351, 34, 'Đen', '#000000', '34', null),

-- Quần Tây Nam (35) - 2 colors x 5 sizes = 10 variants
(352, 35, 'Đen', '#000000', '28', null), (353, 35, 'Đen', '#000000', '29', null), (354, 35, 'Đen', '#000000', '30', null), (355, 35, 'Đen', '#000000', '32', null), (356, 35, 'Đen', '#000000', '34', null),
(357, 35, 'Xám', '#6B7280', '28', null), (358, 35, 'Xám', '#6B7280', '29', null), (359, 35, 'Xám', '#6B7280', '30', null), (360, 35, 'Xám', '#6B7280', '32', null), (361, 35, 'Xám', '#6B7280', '34', null),

-- Quần Ống Rộng Nam (36) - 3 colors x 4 sizes = 12 variants
(362, 36, 'Trắng', '#FFFFFF', '28', null), (363, 36, 'Trắng', '#FFFFFF', '29', null), (364, 36, 'Trắng', '#FFFFFF', '30', null), (365, 36, 'Trắng', '#FFFFFF', '32', null),
(366, 36, 'Be', '#D97706', '28', null), (367, 36, 'Be', '#D97706', '29', null), (368, 36, 'Be', '#D97706', '30', null), (369, 36, 'Be', '#D97706', '32', null),
(370, 36, 'Xanh', '#2563EB', '28', null), (371, 36, 'Xanh', '#2563EB', '29', null), (372, 36, 'Xanh', '#2563EB', '30', null), (373, 36, 'Xanh', '#2563EB', '32', null),

-- Quần Legging Nữ (37) - 3 colors x 4 sizes = 12 variants
(374, 37, 'Đen', '#000000', 'S', null), (375, 37, 'Đen', '#000000', 'M', null), (376, 37, 'Đen', '#000000', 'L', null), (377, 37, 'Đen', '#000000', 'XL', null),
(378, 37, 'Xám', '#6B7280', 'S', null), (379, 37, 'Xám', '#6B7280', 'M', null), (380, 37, 'Xám', '#6B7280', 'L', null), (381, 37, 'Xám', '#6B7280', 'XL', null),
(382, 37, 'Hồng', '#EC4899', 'S', null), (383, 37, 'Hồng', '#EC4899', 'M', null), (384, 37, 'Hồng', '#EC4899', 'L', null), (385, 37, 'Hồng', '#EC4899', 'XL', null),

-- Quần Tây Nữ (38) - 2 colors x 5 sizes = 10 variants
(386, 38, 'Đen', '#000000', '25', null), (387, 38, 'Đen', '#000000', '26', null), (388, 38, 'Đen', '#000000', '27', null), (389, 38, 'Đen', '#000000', '28', null), (390, 38, 'Đen', '#000000', '29', null),
(391, 38, 'Xanh', '#2563EB', '25', null), (392, 38, 'Xanh', '#2563EB', '26', null), (393, 38, 'Xanh', '#2563EB', '27', null), (394, 38, 'Xanh', '#2563EB', '28', null), (395, 38, 'Xanh', '#2563EB', '29', null),

-- Quần Short Nữ (39) - 3 colors x 4 sizes = 12 variants
(396, 39, 'Xanh', '#2563EB', '25', null), (397, 39, 'Xanh', '#2563EB', '26', null), (398, 39, 'Xanh', '#2563EB', '27', null), (399, 39, 'Xanh', '#2563EB', '28', null),
(400, 39, 'Đen', '#000000', '25', null), (401, 39, 'Đen', '#000000', '26', null), (402, 39, 'Đen', '#000000', '27', null), (403, 39, 'Đen', '#000000', '28', null),
(404, 39, 'Trắng', '#FFFFFF', '25', null), (405, 39, 'Trắng', '#FFFFFF', '26', null), (406, 39, 'Trắng', '#FFFFFF', '27', null), (407, 39, 'Trắng', '#FFFFFF', '28', null),

-- Quần Jogger Nữ (40) - 3 colors x 4 sizes = 12 variants
(408, 40, 'Đen', '#000000', 'S', null), (409, 40, 'Đen', '#000000', 'M', null), (410, 40, 'Đen', '#000000', 'L', null), (411, 40, 'Đen', '#000000', 'XL', null),
(412, 40, 'Xám', '#6B7280', 'S', null), (413, 40, 'Xám', '#6B7280', 'M', null), (414, 40, 'Xám', '#6B7280', 'L', null), (415, 40, 'Xám', '#6B7280', 'XL', null),
(416, 40, 'Hồng', '#EC4899', 'S', null), (417, 40, 'Hồng', '#EC4899', 'M', null), (418, 40, 'Hồng', '#EC4899', 'L', null), (419, 40, 'Hồng', '#EC4899', 'XL', null),

-- Giày Loafers (41) - 2 colors x 5 sizes = 10 variants
(420, 41, 'Nâu', '#92400E', '39', null), (421, 41, 'Nâu', '#92400E', '40', null), (422, 41, 'Nâu', '#92400E', '41', null), (423, 41, 'Nâu', '#92400E', '42', null), (424, 41, 'Nâu', '#92400E', '43', null),
(425, 41, 'Đen', '#000000', '39', null), (426, 41, 'Đen', '#000000', '40', null), (427, 41, 'Đen', '#000000', '41', null), (428, 41, 'Đen', '#000000', '42', null), (429, 41, 'Đen', '#000000', '43', null),

-- Giày Pumps (42) - 2 colors x 4 sizes = 8 variants
(430, 42, 'Đen', '#000000', '36', null), (431, 42, 'Đen', '#000000', '37', null), (432, 42, 'Đen', '#000000', '38', null), (433, 42, 'Đen', '#000000', '39', null),
(434, 42, 'Nâu', '#92400E', '36', null), (435, 42, 'Nâu', '#92400E', '37', null), (436, 42, 'Nâu', '#92400E', '38', null), (437, 42, 'Nâu', '#92400E', '39', null),

-- Giày Thể Thao (43) - 3 colors x 5 sizes = 15 variants
(438, 43, 'Trắng', '#FFFFFF', '39', null), (439, 43, 'Trắng', '#FFFFFF', '40', null), (440, 43, 'Trắng', '#FFFFFF', '41', null), (441, 43, 'Trắng', '#FFFFFF', '42', null), (442, 43, 'Trắng', '#FFFFFF', '43', null),
(443, 43, 'Đen', '#000000', '39', null), (444, 43, 'Đen', '#000000', '40', null), (445, 43, 'Đen', '#000000', '41', null), (446, 43, 'Đen', '#000000', '42', null), (447, 43, 'Đen', '#000000', '43', null),
(448, 43, 'Xanh', '#2563EB', '39', null), (449, 43, 'Xanh', '#2563EB', '40', null), (450, 43, 'Xanh', '#2563EB', '41', null), (451, 43, 'Xanh', '#2563EB', '42', null), (452, 43, 'Xanh', '#2563EB', '43', null),

-- Sandal Flip Flops (44) - 3 colors x 4 sizes = 12 variants
(453, 44, 'Đen', '#000000', '39', null), (454, 44, 'Đen', '#000000', '40', null), (455, 44, 'Đen', '#000000', '41', null), (456, 44, 'Đen', '#000000', '42', null),
(457, 44, 'Xanh', '#2563EB', '39', null), (458, 44, 'Xanh', '#2563EB', '40', null), (459, 44, 'Xanh', '#2563EB', '41', null), (460, 44, 'Xanh', '#2563EB', '42', null),
(461, 44, 'Đỏ', '#DC2626', '39', null), (462, 44, 'Đỏ', '#DC2626', '40', null), (463, 44, 'Đỏ', '#DC2626', '41', null), (464, 44, 'Đỏ', '#DC2626', '42', null),

-- Túi Tote Nữ (45) - 3 colors x 1 size = 3 variants
(465, 45, 'Đen', '#000000', 'Free Size', null),
(466, 45, 'Be', '#D97706', 'Free Size', null),
(467, 45, 'Hồng', '#EC4899', 'Free Size', null),

-- Ví Nam (46) - 2 colors x 1 size = 2 variants
(468, 46, 'Nâu', '#92400E', 'Free Size', null),
(469, 46, 'Đen', '#000000', 'Free Size', null),

-- Kính Mát (47) - 3 colors x 1 size = 3 variants
(470, 47, 'Đen', '#000000', 'Free Size', null),
(471, 47, 'Xanh', '#2563EB', 'Free Size', null),
(472, 47, 'Hồng', '#EC4899', 'Free Size', null),

-- Khăn Scarf (48) - 3 colors x 1 size = 3 variants
(473, 48, 'Đỏ', '#DC2626', 'Free Size', null),
(474, 48, 'Xanh', '#2563EB', 'Free Size', null),
(475, 48, 'Vàng', '#F59E0B', 'Free Size', null),

-- Vòng Tay (49) - 2 colors x 1 size = 2 variants
(476, 49, 'Đen', '#000000', 'Free Size', null),
(477, 49, 'Nâu', '#92400E', 'Free Size', null),

-- Mũ Lưỡi Trai (50) - 3 colors x 1 size = 3 variants
(478, 50, 'Đen', '#000000', 'Free Size', null),
(479, 50, 'Trắng', '#FFFFFF', 'Free Size', null),
(480, 50, 'Navy', '#1E3A8A', 'Free Size', null);
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
(1, 'WELCOME10', 'Ưu đãi khách mới', 'Giảm 10% cho khách hàng mới', 'PERCENT', 'PRODUCT', 0, 10.0, 0, 1000, '2025-01-01', '2025-12-31', true),
(2, 'SALE50K', 'Giảm 50K', 'Giảm 50k cho đơn hàng từ 500k', 'FIXED', 'PRODUCT', 50000, 0.0, 0, 500, '2025-01-01', '2025-12-31', true),
(3, 'SUMMER20', 'Khuyến mãi hè', 'Khuyến mãi hè - Giảm 20%', 'PERCENT', 'PRODUCT', 0, 20.0, 0, 200, '2025-06-01', '2025-08-31', true),
(4, 'FREESHIP', 'Miễn phí ship', 'Miễn phí vận chuyển', 'FIXED', 'SHIPPING', 30000, 0.0, 0, 2000, '2025-01-01', '2025-12-31', true),
(5, 'VIP15', 'Ưu đãi VIP', 'Ưu đãi VIP - Giảm 15%', 'PERCENT', 'PRODUCT', 0, 15.0, 0, 100, '2025-01-01', '2025-12-31', true),
(6, 'FLASH30', 'Flash Sale 30%', 'Giảm 30% cho sản phẩm flash sale', 'PERCENT', 'PRODUCT', 0, 30.0, 0, 300, '2025-01-01', '2025-12-31', true),
(7, 'SHIP20K', 'Giảm ship 20K', 'Giảm 20k phí vận chuyển', 'FIXED', 'SHIPPING', 20000, 0.0, 0, 500, '2025-01-01', '2025-12-31', true),
(8, 'NEWYEAR25', 'Tết Nguyên Đán', 'Giảm 25% mừng năm mới', 'PERCENT', 'PRODUCT', 0, 25.0, 0, 200, '2025-02-01', '2025-02-15', true);

-- Insert stock items with product_info_id (updated schema)
INSERT IGNORE INTO stock_items (id, stock_id, product_info_id, quantity) VALUES
-- Kho Hà Nội (1) - một số sản phẩm phổ biến
(1, 1, 1, 25), (2, 1, 2, 30), (3, 1, 5, 22), (4, 1, 6, 35), (5, 1, 9, 18),
(6, 1, 10, 27), (7, 1, 13, 20), (8, 1, 14, 28), (9, 1, 17, 15), (10, 1, 18, 22),
-- Kho TP.HCM (2) - nhiều sản phẩm hơn
(11, 2, 1, 30), (12, 2, 2, 35), (13, 2, 3, 28), (14, 2, 4, 20), (15, 2, 5, 25),
(16, 2, 6, 40), (17, 2, 7, 32), (18, 2, 8, 25), (19, 2, 9, 20), (20, 2, 10, 30),
(21, 2, 11, 24), (22, 2, 12, 18), (23, 2, 13, 26), (24, 2, 14, 30), (25, 2, 15, 22),
-- Kho Đà Nẵng (3) - ít sản phẩm hơn
(26, 3, 1, 15), (27, 3, 2, 20), (28, 3, 5, 12), (29, 3, 6, 18), (30, 3, 9, 10),
(31, 3, 10, 15), (32, 3, 13, 12), (33, 3, 14, 16), (34, 3, 17, 8), (35, 3, 18, 12),
-- Kho Cần Thơ (4) - mới thêm
(36, 4, 1, 20), (37, 4, 2, 25), (38, 4, 5, 18), (39, 4, 6, 22), (40, 4, 9, 15),
(41, 4, 10, 20), (42, 4, 13, 16), (43, 4, 14, 20), (44, 4, 17, 12), (45, 4, 18, 18),
-- Kho Bình Dương (5) - mới thêm
(46, 5, 1, 18), (47, 5, 2, 22), (48, 5, 3, 15), (49, 5, 4, 12), (50, 5, 5, 20),
(51, 5, 6, 25), (52, 5, 7, 18), (53, 5, 8, 15), (54, 5, 9, 12), (55, 5, 10, 18),
(56, 5, 11, 14), (57, 5, 12, 10), (58, 5, 13, 16), (59, 5, 14, 18), (60, 5, 15, 12);

-- Reset AUTO_INCREMENT
ALTER TABLE stocks AUTO_INCREMENT = 6;
ALTER TABLE product_types AUTO_INCREMENT = 4;
ALTER TABLE products AUTO_INCREMENT = 51;
ALTER TABLE product_info AUTO_INCREMENT = 481;
ALTER TABLE stock_items AUTO_INCREMENT = 61;
ALTER TABLE discounts AUTO_INCREMENT = 9;

-- Create chat_rooms table
CREATE TABLE IF NOT EXISTS chat_rooms (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          user_id BIGINT,
                                          session_id VARCHAR(255),
    name VARCHAR(255) NOT NULL
    );

-- Create chats table for customer support chat system
CREATE TABLE IF NOT EXISTS chats (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     is_read BOOLEAN DEFAULT FALSE,
                                     message TEXT NOT NULL,
                                     name VARCHAR(255) NOT NULL,
    sender BIGINT NOT NULL, -- 0 for admin, userId for user
    chat_room_id BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_room_id) REFERENCES chat_rooms(id)
    );

-- Insert additional 32 products to reach 50 total
INSERT IGNORE INTO products (id, name, description, image, product_type_id, brand, fabric, price_in_stock, price) VALUES
-- Áo Nam (19-25)
(19, 'Áo Polo Nam Classic', 'Áo polo nam cổ bẻ chất cotton pha, form regular fit. Thiết kế cổ bẻ truyền thống, phù hợp công sở và dạo phố.', 'https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=400', 1, 'X-Store', 'Cotton Blend', 220000, 319000),
(20, 'Áo Len Nam Sweater', 'Áo len nam sweater cổ lọ chất acrylic ấm áp. Form oversize trendy, phù hợp mùa đông.', 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400', 1, 'X-Store', 'Acrylic', 380000, 549000),
(21, 'Áo Khoác Nam Jacket', 'Áo khoác nam jacket da PU chống nước. Form bomber hiện đại, túi zip tiện lợi.', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400', 1, 'X-Store', 'PU Leather', 550000, 799000),
(22, 'Áo Vest Nam Công Sở', 'Áo vest nam công sở chất vải len dạ. Thiết kế 3 khuy lịch lãm, phù hợp tiệc tùng.', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400', 1, 'X-Store', 'Wool Blend', 450000, 649000),
(23, 'Áo Thun Nam Graphic', 'Áo thun nam in họa tiết graphic chất cotton organic. Form relaxed fit thoải mái.', 'https://images.unsplash.com/photo-1529139574466-a303027c1d8b?w=400', 1, 'X-Store', 'Organic Cotton', 160000, 239000),
(24, 'Áo Cardigan Nam', 'Áo cardigan nam len dệt kim. Form dài qua hông, khuy cài tiện lợi.', 'https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=400', 1, 'X-Store', 'Wool', 420000, 599000),
(25, 'Áo Tank Top Nam', 'Áo tank top nam thể thao chất polyester thấm hút. Form fitted, phù hợp gym.', 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400', 1, 'X-Store', 'Polyester', 120000, 179000),

-- Áo Nữ (26-32)
(26, 'Áo Len Nữ Cardigan', 'Áo len nữ cardigan dệt kim mềm mại. Form dài midi, khuy cài trang trí.', 'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=400', 2, 'X-Store', 'Acrylic', 350000, 499000),
(27, 'Áo Khoác Nữ Trench Coat', 'Áo khoác nữ trench coat chất gabardine chống nước. Thiết kế cổ đứng, dây nịt eo.', 'https://images.unsplash.com/photo-1544966503-7cc5ac882d5f?w=400', 2, 'X-Store', 'Gabardine', 680000, 969000),
(28, 'Áo Thun Nữ Oversize', 'Áo thun nữ oversize chất cotton dày dặn. Form rộng rãi, tay dài.', 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400', 2, 'X-Store', 'Cotton', 180000, 259000),
(29, 'Áo Blazer Nữ Slim Fit', 'Áo blazer nữ slim fit chất polyester cao cấp. Form ôm dáng thanh lịch.', 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=400', 2, 'X-Store', 'Polyester', 520000, 749000),
(30, 'Áo Sweater Nữ', 'Áo sweater nữ cổ tròn chất len acrylic. Form regular, ấm áp mùa lạnh.', 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400', 2, 'X-Store', 'Acrylic', 320000, 459000),
(31, 'Áo Vest Nữ', 'Áo vest nữ công sở chất vải tweed. Thiết kế không khuy, form fitted.', 'https://images.unsplash.com/photo-1583195764036-6dc248ac07d9?w=400', 2, 'X-Store', 'Tweed', 480000, 689000),
(32, 'Áo Tank Top Nữ', 'Áo tank top nữ chất modal thoáng mát. Form fitted, phù hợp layering.', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400', 2, 'X-Store', 'Modal', 140000, 199000),

-- Quần Nam (33-36) - Đồ Nam
(33, 'Quần Jogger Nam', 'Quần jogger nam thể thao chất cotton pha. Ống bo gấu, túi zip tiện lợi.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 1, 'X-Store', 'Cotton Blend', 280000, 399000),
(34, 'Quần Cargo Nam', 'Quần cargo nam nhiều túi chất cotton canvas. Form straight leg rugged.', 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400', 1, 'X-Store', 'Canvas', 350000, 499000),
(35, 'Quần Tây Nam Công Sở', 'Quần tây nam công sở chất wool pha. Form slim fit lịch lãm.', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', 1, 'X-Store', 'Wool Blend', 420000, 599000),
(36, 'Quần Ống Rộng Nam', 'Quần ống rộng nam chất linen thoáng mát. Form wide leg bohemian.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 1, 'X-Store', 'Linen', 320000, 459000),

-- Quần Nữ (37-40) - Đồ Nữ
(37, 'Quần Legging Nữ', 'Quần legging nữ thể thao chất spandex co giãn. Form ôm sát, thấm hút mồ hôi.', 'https://images.unsplash.com/photo-1583496661160-fb5886a13d75?w=400', 2, 'X-Store', 'Spandex', 220000, 319000),
(38, 'Quần Tây Nữ', 'Quần tây nữ công sở chất gabardine. Form straight leg thanh lịch.', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400', 2, 'X-Store', 'Gabardine', 380000, 549000),
(39, 'Quần Short Nữ', 'Quần short nữ chất denim co giãn. Form high waist trendy.', 'https://images.unsplash.com/photo-1571513722275-4b8c0215cd62?w=400', 2, 'X-Store', 'Denim', 240000, 349000),
(40, 'Quần Jogger Nữ', 'Quần jogger nữ thể thao chất polyester. Ống bo gấu, dây rút eo.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 2, 'X-Store', 'Polyester', 260000, 379000),

-- Giày Dép (41-44) - Phụ Kiện
(41, 'Giày Lười Nam Loafers', 'Giày lười nam loafers da thật. Thiết kế penny slot cổ điển.', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400', 3, 'X-Store', 'Genuine Leather', 650000, 899000),
(42, 'Giày Cao Gót Nữ Pumps', 'Giày cao gót nữ pumps da PU. Gót 8cm thanh lịch, mũi nhọn.', 'https://images.unsplash.com/photo-1515347619252-60a4bf4fff4f?w=400', 3, 'X-Store', 'PU Leather', 480000, 689000),
(43, 'Giày Thể Thao Unisex', 'Giày thể thao unisex chất mesh thoáng khí. Đế EVA êm ái.', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', 3, 'X-Store', 'Mesh', 420000, 599000),
(44, 'Sandal Nam Flip Flops', 'Sandal nam flip flops chất EVA nhẹ nhàng. Thiết kế đơn giản, dễ chịu.', 'https://images.unsplash.com/photo-1560343090-f0409e92791a?w=400', 3, 'X-Store', 'EVA', 120000, 179000),

-- Phụ Kiện (45-50)
(45, 'Túi Xách Nữ Tote', 'Túi xách nữ tote canvas in họa tiết. Size lớn đựng laptop.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400', 3, 'X-Store', 'Canvas', 250000, 359000),
(46, 'Ví Nam Da', 'Ví nam da thật bifold. 6 ngăn thẻ, 2 ngăn tiền tiện lợi.', 'https://images.unsplash.com/photo-1624222247344-550fb60583dc?w=400', 3, 'X-Store', 'Genuine Leather', 350000, 499000),
(47, 'Kính Mát Unisex', 'Kính mát unisex chất acetate. Thiết kế wayfarer cổ điển.', 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400', 3, 'X-Store', 'Acetate', 280000, 399000),
(48, 'Khăn Choàng Nữ Scarf', 'Khăn choàng nữ scarf lụa mềm mại. Họa tiết hoa văn tinh tế.', 'https://images.unsplash.com/photo-1601762603339-fd61e28b698a?w=400', 3, 'X-Store', 'Silk', 220000, 319000),
(49, 'Vòng Tay Nữ Bracelet', 'Vòng tay nữ bracelet da thắt nút. Thiết kế minimalist.', 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400', 3, 'X-Store', 'Leather', 150000, 219000),
(50, 'Mũ Lưỡi Trai Unisex', 'Mũ lưỡi trai unisex cotton. Thiết kế baseball cap phong cách Mỹ.', 'https://images.unsplash.com/photo-1521119989659-a83eee488004?w=400', 3, 'X-Store', 'Cotton', 180000, 259000);

-- Insert sample comments
INSERT IGNORE INTO comments (id, product_id, author_id, author, text, comment_at, rate) VALUES
-- Comments for Áo Thun Nam Basic Cotton (Product 1)
(1, 1, 1, 'Trần Minh Tuấn', 'Áo chất vải mát, form đẹp, mặc rất thoải mái. Sẽ ủng hộ shop tiếp.', '2025-10-20 10:30:00', 5),
(2, 1, 2, 'Lê Thị Hoa', 'Chất lượng tốt, giá cả hợp lý. Giao hàng nhanh. Rất hài lòng.', '2025-10-21 15:00:00', 5),
(3, 1, 3, 'Phạm Văn Hùng', 'Áo màu đen đẹp, không bị ra màu khi giặt. Form áo vừa vặn.', '2025-10-22 09:00:00', 4),

-- Comments for Áo Sơ Mi Nam Công Sở (Product 2)
(4, 2, 1, 'Trần Minh Tuấn', 'Sơ mi mặc đi làm rất lịch sự. Vải ít nhăn, dễ ủi. Sẽ mua thêm màu khác.', '2025-11-01 11:00:00', 5),
(5, 2, 2, 'Lê Thị Hoa', 'Form slim fit tôn dáng, chất vải thoáng mát. Rất đáng tiền.', '2025-11-02 14:20:00', 5),

-- Comments for Quần Jeans Nữ Skinny (Product 10)
(6, 10, 3, 'Phạm Văn Hùng', 'Quần jeans co giãn tốt, mặc rất thoải mái. Dáng skinny tôn chân dài.', '2025-11-05 18:00:00', 5),
(7, 10, 4, 'Hoàng Thị Thu', 'Bạn gái mình rất thích quần này. Chất jean mềm, không bị cứng.', '2025-11-06 20:10:00', 4),

-- Comments for Giày Sneaker Nam Basic (Product 13)
(8, 13, 5, 'Vũ Đức Anh', 'Giày đi êm chân, kiểu dáng basic dễ phối đồ. Rất ưng ý.', '2025-11-10 08:45:00', 5),
(9, 13, 6, 'Đặng Thuỳ Linh', 'Đã mua đôi thứ 2 ở shop. Chất lượng không đổi, giao hàng vẫn nhanh như một cơn gió.', '2025-11-11 12:00:00', 5),

-- Comments for Áo Khoác Blazer Nữ (Product 6)
(10, 6, 7, 'Bùi Quang Huy', 'Blazer form đẹp, mặc lên sang chảnh. Phù hợp cho các buổi tiệc hoặc đi làm.', '2025-11-12 19:30:00', 5);

-- Insert addresses for stocks
INSERT IGNORE INTO addresses (id, street_number, street_name, ward, district, city, is_default) VALUES
(1, '123', 'Đường Nguyễn Trãi', 'Thanh Xuân Trung', 'Thanh Xuân', 'Hà Nội', true),
(2, '456', 'Đường Nguyễn Huệ', 'Bến Nghé', 'Quận 1', 'TP.HCM', true),
(3, '789', 'Đường Trần Phú', 'Thạch Thang', 'Hải Châu', 'Đà Nẵng', true),
(4, '101', 'Đường Nguyễn Văn Linh', 'An Bình', 'Ninh Kiều', 'Cần Thơ', true),
(5, '202', 'Đường Đại Lộ Bình Dương', 'Hòa Phú', 'Thuận An', 'Bình Dương', true);

-- Update stocks with address_id
UPDATE stocks SET address_id = 1 WHERE id = 1; -- Kho Hà Nội
UPDATE stocks SET address_id = 2 WHERE id = 2; -- Kho TP.HCM
UPDATE stocks SET address_id = 3 WHERE id = 3; -- Kho Đà Nẵng
UPDATE stocks SET address_id = 4 WHERE id = 4; -- Kho Cần Thơ
UPDATE stocks SET address_id = 5 WHERE id = 5; -- Kho Bình Dương

-- Reset AUTO_INCREMENT for addresses
ALTER TABLE addresses AUTO_INCREMENT = 6;
