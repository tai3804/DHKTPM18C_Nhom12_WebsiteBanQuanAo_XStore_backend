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
(4, 'Kho Miền Tây - Cần Thơ', '0292.456.789', 'cantho@xstore.vn', null),
(5, 'Kho Đông Nam Bộ - Bình Dương', '0274.456.789', 'binhduong@xstore.vn', null);

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

-- Insert product_info (thông tin biến thể: color, size, quantity)
INSERT IGNORE INTO product_info (id, product_id, color_name, color_hex_code, size_name, quantity, image) VALUES
-- Áo Thun Nam Basic (Product 1) - 4 colors x 4 sizes = 16 variants
(1, 1, 'Trắng', '#FFFFFF', 'S', 25, null), (2, 1, 'Trắng', '#FFFFFF', 'M', 30, null), (3, 1, 'Trắng', '#FFFFFF', 'L', 28, null), (4, 1, 'Trắng', '#FFFFFF', 'XL', 20, null),
(5, 1, 'Đen', '#000000', 'S', 22, null), (6, 1, 'Đen', '#000000', 'M', 35, null), (7, 1, 'Đen', '#000000', 'L', 32, null), (8, 1, 'Đen', '#000000', 'XL', 25, null),
(9, 1, 'Xám', '#6B7280', 'S', 18, null), (10, 1, 'Xám', '#6B7280', 'M', 27, null), (11, 1, 'Xám', '#6B7280', 'L', 24, null), (12, 1, 'Xám', '#6B7280', 'XL', 20, null),
(13, 1, 'Navy', '#1E3A8A', 'S', 20, null), (14, 1, 'Navy', '#1E3A8A', 'M', 28, null), (15, 1, 'Navy', '#1E3A8A', 'L', 26, null), (16, 1, 'Navy', '#1E3A8A', 'XL', 18, null),

-- Áo Sơ Mi Nam (Product 2) - 3 colors x 4 sizes = 12 variants
(17, 2, 'Trắng', '#FFFFFF', 'S', 15, null), (18, 2, 'Trắng', '#FFFFFF', 'M', 22, null), (19, 2, 'Trắng', '#FFFFFF', 'L', 20, null), (20, 2, 'Trắng', '#FFFFFF', 'XL', 12, null),
(21, 2, 'Xanh Nhạt', '#DBEAFE', 'S', 12, null), (22, 2, 'Xanh Nhạt', '#DBEAFE', 'M', 18, null), (23, 2, 'Xanh Nhạt', '#DBEAFE', 'L', 16, null), (24, 2, 'Xanh Nhạt', '#DBEAFE', 'XL', 10, null),
(25, 2, 'Hồng Nhạt', '#FCE7F3', 'S', 10, null), (26, 2, 'Hồng Nhạt', '#FCE7F3', 'M', 15, null), (27, 2, 'Hồng Nhạt', '#FCE7F3', 'L', 14, null), (28, 2, 'Hồng Nhạt', '#FCE7F3', 'XL', 8, null),

-- Áo Hoodie Nam (Product 3) - 3 colors x 4 sizes = 12 variants
(29, 3, 'Đen', '#000000', 'S', 12, null), (30, 3, 'Đen', '#000000', 'M', 18, null), (31, 3, 'Đen', '#000000', 'L', 20, null), (32, 3, 'Đen', '#000000', 'XL', 15, null),
(33, 3, 'Xám', '#6B7280', 'S', 10, null), (34, 3, 'Xám', '#6B7280', 'M', 15, null), (35, 3, 'Xám', '#6B7280', 'L', 17, null), (36, 3, 'Xám', '#6B7280', 'XL', 12, null),
(37, 3, 'Navy', '#1E3A8A', 'S', 8, null), (38, 3, 'Navy', '#1E3A8A', 'M', 12, null), (39, 3, 'Navy', '#1E3A8A', 'L', 14, null), (40, 3, 'Navy', '#1E3A8A', 'XL', 10, null),

-- Áo Thun Nữ Crop Top (Product 4) - 3 colors x 3 sizes = 9 variants
(41, 4, 'Trắng', '#FFFFFF', 'S', 30, null), (42, 4, 'Trắng', '#FFFFFF', 'M', 35, null), (43, 4, 'Trắng', '#FFFFFF', 'L', 25, null),
(44, 4, 'Hồng', '#EC4899', 'S', 28, null), (45, 4, 'Hồng', '#EC4899', 'M', 32, null), (46, 4, 'Hồng', '#EC4899', 'L', 22, null),
(47, 4, 'Vàng', '#F59E0B', 'S', 24, null), (48, 4, 'Vàng', '#F59E0B', 'M', 28, null), (49, 4, 'Vàng', '#F59E0B', 'L', 20, null),

-- Áo Blouse Nữ (Product 5) - 3 colors x 3 sizes = 9 variants
(50, 5, 'Trắng', '#FFFFFF', 'S', 15, null), (51, 5, 'Trắng', '#FFFFFF', 'M', 20, null), (52, 5, 'Trắng', '#FFFFFF', 'L', 18, null),
(53, 5, 'Be', '#F3E8FF', 'S', 12, null), (54, 5, 'Be', '#F3E8FF', 'M', 16, null), (55, 5, 'Be', '#F3E8FF', 'L', 14, null),
(56, 5, 'Xanh Mint', '#A7F3D0', 'S', 10, null), (57, 5, 'Xanh Mint', '#A7F3D0', 'M', 14, null), (58, 5, 'Xanh Mint', '#A7F3D0', 'L', 12, null),

-- Áo Khoác Blazer Nữ (Product 6) - 3 colors x 3 sizes = 9 variants
(59, 6, 'Đen', '#000000', 'S', 10, null), (60, 6, 'Đen', '#000000', 'M', 14, null), (61, 6, 'Đen', '#000000', 'L', 12, null),
(62, 6, 'Navy', '#1E3A8A', 'S', 8, null), (63, 6, 'Navy', '#1E3A8A', 'M', 12, null), (64, 6, 'Navy', '#1E3A8A', 'L', 10, null),
(65, 6, 'Xám', '#6B7280', 'S', 7, null), (66, 6, 'Xám', '#6B7280', 'M', 10, null), (67, 6, 'Xám', '#6B7280', 'L', 9, null),

-- Quần Jeans Nam (Product 7) - 3 colors x 6 sizes = 18 variants
(68, 7, 'Xanh Đậm', '#1E40AF', '28', 12, null), (69, 7, 'Xanh Đậm', '#1E40AF', '29', 15, null), (70, 7, 'Xanh Đậm', '#1E40AF', '30', 18, null), (71, 7, 'Xanh Đậm', '#1E40AF', '31', 16, null), (72, 7, 'Xanh Đậm', '#1E40AF', '32', 14, null), (73, 7, 'Xanh Đậm', '#1E40AF', '34', 10, null),
(74, 7, 'Xanh Nhạt', '#3B82F6', '28', 10, null), (75, 7, 'Xanh Nhạt', '#3B82F6', '29', 13, null), (76, 7, 'Xanh Nhạt', '#3B82F6', '30', 15, null), (77, 7, 'Xanh Nhạt', '#3B82F6', '31', 14, null), (78, 7, 'Xanh Nhạt', '#3B82F6', '32', 12, null), (79, 7, 'Xanh Nhạt', '#3B82F6', '34', 8, null),
(80, 7, 'Đen', '#000000', '28', 11, null), (81, 7, 'Đen', '#000000', '29', 14, null), (82, 7, 'Đen', '#000000', '30', 16, null), (83, 7, 'Đen', '#000000', '31', 15, null), (84, 7, 'Đen', '#000000', '32', 13, null), (85, 7, 'Đen', '#000000', '34', 9, null),

-- Quần Kaki Nam (Product 8) - 3 colors x 5 sizes = 15 variants
(86, 8, 'Be', '#D97706', '28', 14, null), (87, 8, 'Be', '#D97706', '29', 16, null), (88, 8, 'Be', '#D97706', '30', 18, null), (89, 8, 'Be', '#D97706', '32', 15, null), (90, 8, 'Be', '#D97706', '34', 12, null),
(91, 8, 'Xám', '#6B7280', '28', 12, null), (92, 8, 'Xám', '#6B7280', '29', 14, null), (93, 8, 'Xám', '#6B7280', '30', 16, null), (94, 8, 'Xám', '#6B7280', '32', 13, null), (95, 8, 'Xám', '#6B7280', '34', 10, null),
(96, 8, 'Navy', '#1E3A8A', '28', 10, null), (97, 8, 'Navy', '#1E3A8A', '29', 12, null), (98, 8, 'Navy', '#1E3A8A', '30', 14, null), (99, 8, 'Navy', '#1E3A8A', '32', 11, null), (100, 8, 'Navy', '#1E3A8A', '34', 8, null),

-- Quần Short Nam (Product 9) - 3 colors x 4 sizes = 12 variants
(101, 9, 'Đen', '#000000', 'S', 20, null), (102, 9, 'Đen', '#000000', 'M', 25, null), (103, 9, 'Đen', '#000000', 'L', 22, null), (104, 9, 'Đen', '#000000', 'XL', 18, null),
(105, 9, 'Xám', '#6B7280', 'S', 18, null), (106, 9, 'Xám', '#6B7280', 'M', 23, null), (107, 9, 'Xám', '#6B7280', 'L', 20, null), (108, 9, 'Xám', '#6B7280', 'XL', 16, null),
(109, 9, 'Navy', '#1E3A8A', 'S', 16, null), (110, 9, 'Navy', '#1E3A8A', 'M', 21, null), (111, 9, 'Navy', '#1E3A8A', 'L', 18, null), (112, 9, 'Navy', '#1E3A8A', 'XL', 14, null),

-- Quần Jeans Nữ (Product 10) - 3 colors x 5 sizes = 15 variants
(113, 10, 'Xanh Đậm', '#1E40AF', '25', 15, null), (114, 10, 'Xanh Đậm', '#1E40AF', '26', 18, null), (115, 10, 'Xanh Đậm', '#1E40AF', '27', 20, null), (116, 10, 'Xanh Đậm', '#1E40AF', '28', 17, null), (117, 10, 'Xanh Đậm', '#1E40AF', '29', 14, null),
(118, 10, 'Xanh Nhạt', '#3B82F6', '25', 13, null), (119, 10, 'Xanh Nhạt', '#3B82F6', '26', 16, null), (120, 10, 'Xanh Nhạt', '#3B82F6', '27', 18, null), (121, 10, 'Xanh Nhạt', '#3B82F6', '28', 15, null), (122, 10, 'Xanh Nhạt', '#3B82F6', '29', 12, null),
(123, 10, 'Đen', '#000000', '25', 14, null), (124, 10, 'Đen', '#000000', '26', 17, null), (125, 10, 'Đen', '#000000', '27', 19, null), (126, 10, 'Đen', '#000000', '28', 16, null), (127, 10, 'Đen', '#000000', '29', 13, null),

-- Chân Váy (Product 11) - 3 colors x 3 sizes = 9 variants
(128, 11, 'Đen', '#000000', 'S', 22, null), (129, 11, 'Đen', '#000000', 'M', 28, null), (130, 11, 'Đen', '#000000', 'L', 24, null),
(131, 11, 'Be', '#D97706', 'S', 18, null), (132, 11, 'Be', '#D97706', 'M', 24, null), (133, 11, 'Be', '#D97706', 'L', 20, null),
(134, 11, 'Hồng', '#EC4899', 'S', 16, null), (135, 11, 'Hồng', '#EC4899', 'M', 22, null), (136, 11, 'Hồng', '#EC4899', 'L', 18, null),

-- Quần Culottes (Product 12) - 3 colors x 3 sizes = 9 variants
(137, 12, 'Đen', '#000000', 'S', 16, null), (138, 12, 'Đen', '#000000', 'M', 20, null), (139, 12, 'Đen', '#000000', 'L', 18, null),
(140, 12, 'Trắng', '#FFFFFF', 'S', 14, null), (141, 12, 'Trắng', '#FFFFFF', 'M', 18, null), (142, 12, 'Trắng', '#FFFFFF', 'L', 16, null),
(143, 12, 'Xám', '#6B7280', 'S', 12, null), (144, 12, 'Xám', '#6B7280', 'M', 16, null), (145, 12, 'Xám', '#6B7280', 'L', 14, null),

-- Giày Sneaker Nam (Product 13) - 3 colors x 5 sizes = 15 variants
(146, 13, 'Trắng', '#FFFFFF', '39', 8, null), (147, 13, 'Trắng', '#FFFFFF', '40', 10, null), (148, 13, 'Trắng', '#FFFFFF', '41', 12, null), (149, 13, 'Trắng', '#FFFFFF', '42', 10, null), (150, 13, 'Trắng', '#FFFFFF', '43', 7, null),
(151, 13, 'Đen', '#000000', '39', 7, null), (152, 13, 'Đen', '#000000', '40', 9, null), (153, 13, 'Đen', '#000000', '41', 11, null), (154, 13, 'Đen', '#000000', '42', 9, null), (155, 13, 'Đen', '#000000', '43', 6, null),
(156, 13, 'Xám', '#6B7280', '39', 6, null), (157, 13, 'Xám', '#6B7280', '40', 8, null), (158, 13, 'Xám', '#6B7280', '41', 10, null), (159, 13, 'Xám', '#6B7280', '42', 8, null), (160, 13, 'Xám', '#6B7280', '43', 5, null),

-- Giày Boot Nữ (Product 14) - 2 colors x 4 sizes = 8 variants
(161, 14, 'Đen', '#000000', '36', 12, null), (162, 14, 'Đen', '#000000', '37', 15, null), (163, 14, 'Đen', '#000000', '38', 14, null), (164, 14, 'Đen', '#000000', '39', 10, null),
(165, 14, 'Nâu', '#92400E', '36', 10, null), (166, 14, 'Nâu', '#92400E', '37', 13, null), (167, 14, 'Nâu', '#92400E', '38', 12, null), (168, 14, 'Nâu', '#92400E', '39', 8, null),

-- Sandal Nữ (Product 15) - 3 colors x 4 sizes = 12 variants
(169, 15, 'Đen', '#000000', '36', 12, null), (170, 15, 'Đen', '#000000', '37', 15, null), (171, 15, 'Đen', '#000000', '38', 13, null), (172, 15, 'Đen', '#000000', '39', 10, null),
(173, 15, 'Nâu', '#92400E', '36', 10, null), (174, 15, 'Nâu', '#92400E', '37', 13, null), (175, 15, 'Nâu', '#92400E', '38', 11, null), (176, 15, 'Nâu', '#92400E', '39', 8, null),
(177, 15, 'Be', '#D97706', '36', 11, null), (178, 15, 'Be', '#D97706', '37', 14, null), (179, 15, 'Be', '#D97706', '38', 12, null), (180, 15, 'Be', '#D97706', '39', 9, null),

-- Túi Tote (Product 16) - 3 colors x 1 size = 3 variants
(181, 16, 'Be', '#D97706', 'Free Size', 45, null),
(182, 16, 'Đen', '#000000', 'Free Size', 42, null),
(183, 16, 'Navy', '#1E3A8A', 'Free Size', 38, null),

-- Nón Snapback (Product 17) - 3 colors x 1 size = 3 variants
(184, 17, 'Đen', '#000000', 'Free Size', 60, null),
(185, 17, 'Xám', '#6B7280', 'Free Size', 55, null),
(186, 17, 'Navy', '#1E3A8A', 'Free Size', 50, null),

-- Thắt Lưng (Product 18) - 2 colors x 3 sizes = 6 variants
(187, 18, 'Đen', '#000000', 'S', 15, null), (188, 18, 'Đen', '#000000', 'M', 18, null), (189, 18, 'Đen', '#000000', 'L', 16, null),
(190, 18, 'Nâu', '#92400E', 'S', 13, null), (191, 18, 'Nâu', '#92400E', 'M', 16, null), (192, 18, 'Nâu', '#92400E', 'L', 14, null);

-- Insert product_info for new products (19-50)
INSERT IGNORE INTO product_info (id, product_id, color_name, color_hex_code, size_name, quantity, image) VALUES
-- Áo Polo Nam (19) - 3 colors x 4 sizes = 12 variants
(193, 19, 'Trắng', '#FFFFFF', 'S', 20, null), (194, 19, 'Trắng', '#FFFFFF', 'M', 25, null), (195, 19, 'Trắng', '#FFFFFF', 'L', 22, null), (196, 19, 'Trắng', '#FFFFFF', 'XL', 18, null),
(197, 19, 'Xanh Navy', '#1E3A8A', 'S', 18, null), (198, 19, 'Xanh Navy', '#1E3A8A', 'M', 23, null), (199, 19, 'Xanh Navy', '#1E3A8A', 'L', 20, null), (200, 19, 'Xanh Navy', '#1E3A8A', 'XL', 16, null),
(201, 19, 'Đỏ', '#DC2626', 'S', 15, null), (202, 19, 'Đỏ', '#DC2626', 'M', 20, null), (203, 19, 'Đỏ', '#DC2626', 'L', 18, null), (204, 19, 'Đỏ', '#DC2626', 'XL', 14, null),

-- Áo Len Nam (20) - 3 colors x 4 sizes = 12 variants
(205, 20, 'Xám', '#6B7280', 'S', 15, null), (206, 20, 'Xám', '#6B7280', 'M', 20, null), (207, 20, 'Xám', '#6B7280', 'L', 18, null), (208, 20, 'Xám', '#6B7280', 'XL', 12, null),
(209, 20, 'Đen', '#000000', 'S', 12, null), (210, 20, 'Đen', '#000000', 'M', 18, null), (211, 20, 'Đen', '#000000', 'L', 16, null), (212, 20, 'Đen', '#000000', 'XL', 10, null),
(213, 20, 'Navy', '#1E3A8A', 'S', 10, null), (214, 20, 'Navy', '#1E3A8A', 'M', 16, null), (215, 20, 'Navy', '#1E3A8A', 'L', 14, null), (216, 20, 'Navy', '#1E3A8A', 'XL', 8, null),

-- Áo Khoác Nam (21) - 3 colors x 4 sizes = 12 variants
(217, 21, 'Đen', '#000000', 'S', 10, null), (218, 21, 'Đen', '#000000', 'M', 15, null), (219, 21, 'Đen', '#000000', 'L', 12, null), (220, 21, 'Đen', '#000000', 'XL', 8, null),
(221, 21, 'Xanh', '#2563EB', 'S', 8, null), (222, 21, 'Xanh', '#2563EB', 'M', 13, null), (223, 21, 'Xanh', '#2563EB', 'L', 10, null), (224, 21, 'Xanh', '#2563EB', 'XL', 6, null),
(225, 21, 'Xám', '#6B7280', 'S', 7, null), (226, 21, 'Xám', '#6B7280', 'M', 11, null), (227, 21, 'Xám', '#6B7280', 'L', 9, null), (228, 21, 'Xám', '#6B7280', 'XL', 5, null),

-- Áo Vest Nam (22) - 2 colors x 4 sizes = 8 variants
(229, 22, 'Đen', '#000000', 'S', 8, null), (230, 22, 'Đen', '#000000', 'M', 12, null), (231, 22, 'Đen', '#000000', 'L', 10, null), (232, 22, 'Đen', '#000000', 'XL', 6, null),
(233, 22, 'Xám', '#6B7280', 'S', 6, null), (234, 22, 'Xám', '#6B7280', 'M', 10, null), (235, 22, 'Xám', '#6B7280', 'L', 8, null), (236, 22, 'Xám', '#6B7280', 'XL', 4, null),

-- Áo Thun Graphic (23) - 3 colors x 4 sizes = 12 variants
(237, 23, 'Trắng', '#FFFFFF', 'S', 25, null), (238, 23, 'Trắng', '#FFFFFF', 'M', 30, null), (239, 23, 'Trắng', '#FFFFFF', 'L', 28, null), (240, 23, 'Trắng', '#FFFFFF', 'XL', 20, null),
(241, 23, 'Đen', '#000000', 'S', 22, null), (242, 23, 'Đen', '#000000', 'M', 28, null), (243, 23, 'Đen', '#000000', 'L', 25, null), (244, 23, 'Đen', '#000000', 'XL', 18, null),
(245, 23, 'Xám', '#6B7280', 'S', 20, null), (246, 23, 'Xám', '#6B7280', 'M', 26, null), (247, 23, 'Xám', '#6B7280', 'L', 23, null), (248, 23, 'Xám', '#6B7280', 'XL', 16, null),

-- Áo Cardigan Nam (24) - 3 colors x 4 sizes = 12 variants
(249, 24, 'Be', '#D97706', 'S', 12, null), (250, 24, 'Be', '#D97706', 'M', 16, null), (251, 24, 'Be', '#D97706', 'L', 14, null), (252, 24, 'Be', '#D97706', 'XL', 10, null),
(253, 24, 'Xanh', '#2563EB', 'S', 10, null), (254, 24, 'Xanh', '#2563EB', 'M', 14, null), (255, 24, 'Xanh', '#2563EB', 'L', 12, null), (256, 24, 'Xanh', '#2563EB', 'XL', 8, null),
(257, 24, 'Đen', '#000000', 'S', 8, null), (258, 24, 'Đen', '#000000', 'M', 12, null), (259, 24, 'Đen', '#000000', 'L', 10, null), (260, 24, 'Đen', '#000000', 'XL', 6, null),

-- Áo Tank Top Nam (25) - 3 colors x 4 sizes = 12 variants
(261, 25, 'Trắng', '#FFFFFF', 'S', 30, null), (262, 25, 'Trắng', '#FFFFFF', 'M', 35, null), (263, 25, 'Trắng', '#FFFFFF', 'L', 32, null), (264, 25, 'Trắng', '#FFFFFF', 'XL', 25, null),
(265, 25, 'Đen', '#000000', 'S', 28, null), (266, 25, 'Đen', '#000000', 'M', 33, null), (267, 25, 'Đen', '#000000', 'L', 30, null), (268, 25, 'Đen', '#000000', 'XL', 23, null),
(269, 25, 'Xanh', '#2563EB', 'S', 25, null), (270, 25, 'Xanh', '#2563EB', 'M', 30, null), (271, 25, 'Xanh', '#2563EB', 'L', 28, null), (272, 25, 'Xanh', '#2563EB', 'XL', 20, null),

-- Áo Cardigan Nữ (26) - 3 colors x 3 sizes = 9 variants
(273, 26, 'Trắng', '#FFFFFF', 'S', 18, null), (274, 26, 'Trắng', '#FFFFFF', 'M', 22, null), (275, 26, 'Trắng', '#FFFFFF', 'L', 20, null),
(276, 26, 'Hồng', '#EC4899', 'S', 16, null), (277, 26, 'Hồng', '#EC4899', 'M', 20, null), (278, 26, 'Hồng', '#EC4899', 'L', 18, null),
(279, 26, 'Xanh', '#2563EB', 'S', 14, null), (280, 26, 'Xanh', '#2563EB', 'M', 18, null), (281, 26, 'Xanh', '#2563EB', 'L', 16, null),

-- Áo Trench Coat (27) - 2 colors x 3 sizes = 6 variants
(282, 27, 'Be', '#D97706', 'S', 8, null), (283, 27, 'Be', '#D97706', 'M', 12, null), (284, 27, 'Be', '#D97706', 'L', 10, null),
(285, 27, 'Đen', '#000000', 'S', 6, null), (286, 27, 'Đen', '#000000', 'M', 10, null), (287, 27, 'Đen', '#000000', 'L', 8, null),

-- Áo Thun Oversize (28) - 3 colors x 3 sizes = 9 variants
(288, 28, 'Trắng', '#FFFFFF', 'S', 35, null), (289, 28, 'Trắng', '#FFFFFF', 'M', 40, null), (290, 28, 'Trắng', '#FFFFFF', 'L', 38, null),
(291, 28, 'Đen', '#000000', 'S', 32, null), (292, 28, 'Đen', '#000000', 'M', 38, null), (293, 28, 'Đen', '#000000', 'L', 35, null),
(294, 28, 'Xám', '#6B7280', 'S', 30, null), (295, 28, 'Xám', '#6B7280', 'M', 35, null), (296, 28, 'Xám', '#6B7280', 'L', 32, null),

-- Áo Blazer Slim (29) - 2 colors x 3 sizes = 6 variants
(297, 29, 'Đen', '#000000', 'S', 10, null), (298, 29, 'Đen', '#000000', 'M', 14, null), (299, 29, 'Đen', '#000000', 'L', 12, null),
(300, 29, 'Navy', '#1E3A8A', 'S', 8, null), (301, 29, 'Navy', '#1E3A8A', 'M', 12, null), (302, 29, 'Navy', '#1E3A8A', 'L', 10, null),

-- Áo Sweater Nữ (30) - 3 colors x 3 sizes = 9 variants
(303, 30, 'Trắng', '#FFFFFF', 'S', 20, null), (304, 30, 'Trắng', '#FFFFFF', 'M', 25, null), (305, 30, 'Trắng', '#FFFFFF', 'L', 22, null),
(306, 30, 'Xám', '#6B7280', 'S', 18, null), (307, 30, 'Xám', '#6B7280', 'M', 23, null), (308, 30, 'Xám', '#6B7280', 'L', 20, null),
(309, 30, 'Hồng', '#EC4899', 'S', 16, null), (310, 30, 'Hồng', '#EC4899', 'M', 21, null), (311, 30, 'Hồng', '#EC4899', 'L', 18, null),

-- Áo Vest Nữ (31) - 2 colors x 3 sizes = 6 variants
(312, 31, 'Đen', '#000000', 'S', 12, null), (313, 31, 'Đen', '#000000', 'M', 16, null), (314, 31, 'Đen', '#000000', 'L', 14, null),
(315, 31, 'Xám', '#6B7280', 'S', 10, null), (316, 31, 'Xám', '#6B7280', 'M', 14, null), (317, 31, 'Xám', '#6B7280', 'L', 12, null),

-- Áo Tank Top Nữ (32) - 3 colors x 3 sizes = 9 variants
(318, 32, 'Trắng', '#FFFFFF', 'S', 28, null), (319, 32, 'Trắng', '#FFFFFF', 'M', 32, null), (320, 32, 'Trắng', '#FFFFFF', 'L', 30, null),
(321, 32, 'Đen', '#000000', 'S', 25, null), (322, 32, 'Đen', '#000000', 'M', 30, null), (323, 32, 'Đen', '#000000', 'L', 28, null),
(324, 32, 'Hồng', '#EC4899', 'S', 22, null), (325, 32, 'Hồng', '#EC4899', 'M', 28, null), (326, 32, 'Hồng', '#EC4899', 'L', 25, null),

-- Quần Jogger Nam (33) - 3 colors x 5 sizes = 15 variants
(327, 33, 'Đen', '#000000', '28', 18, null), (328, 33, 'Đen', '#000000', '29', 20, null), (329, 33, 'Đen', '#000000', '30', 22, null), (330, 33, 'Đen', '#000000', '32', 19, null), (331, 33, 'Đen', '#000000', '34', 15, null),
(332, 33, 'Xám', '#6B7280', '28', 16, null), (333, 33, 'Xám', '#6B7280', '29', 18, null), (334, 33, 'Xám', '#6B7280', '30', 20, null), (335, 33, 'Xám', '#6B7280', '32', 17, null), (336, 33, 'Xám', '#6B7280', '34', 13, null),
(337, 33, 'Navy', '#1E3A8A', '28', 14, null), (338, 33, 'Navy', '#1E3A8A', '29', 16, null), (339, 33, 'Navy', '#1E3A8A', '30', 18, null), (340, 33, 'Navy', '#1E3A8A', '32', 15, null), (341, 33, 'Navy', '#1E3A8A', '34', 11, null),

-- Quần Cargo Nam (34) - 2 colors x 5 sizes = 10 variants
(342, 34, 'Xanh', '#2563EB', '28', 12, null), (343, 34, 'Xanh', '#2563EB', '29', 15, null), (344, 34, 'Xanh', '#2563EB', '30', 17, null), (345, 34, 'Xanh', '#2563EB', '32', 14, null), (346, 34, 'Xanh', '#2563EB', '34', 10, null),
(347, 34, 'Đen', '#000000', '28', 10, null), (348, 34, 'Đen', '#000000', '29', 13, null), (349, 34, 'Đen', '#000000', '30', 15, null), (350, 34, 'Đen', '#000000', '32', 12, null), (351, 34, 'Đen', '#000000', '34', 8, null),

-- Quần Tây Nam (35) - 2 colors x 5 sizes = 10 variants
(352, 35, 'Đen', '#000000', '28', 15, null), (353, 35, 'Đen', '#000000', '29', 18, null), (354, 35, 'Đen', '#000000', '30', 20, null), (355, 35, 'Đen', '#000000', '32', 17, null), (356, 35, 'Đen', '#000000', '34', 12, null),
(357, 35, 'Xám', '#6B7280', '28', 13, null), (358, 35, 'Xám', '#6B7280', '29', 16, null), (359, 35, 'Xám', '#6B7280', '30', 18, null), (360, 35, 'Xám', '#6B7280', '32', 15, null), (361, 35, 'Xám', '#6B7280', '34', 10, null),

-- Quần Ống Rộng Nam (36) - 3 colors x 4 sizes = 12 variants
(362, 36, 'Trắng', '#FFFFFF', '28', 14, null), (363, 36, 'Trắng', '#FFFFFF', '29', 16, null), (364, 36, 'Trắng', '#FFFFFF', '30', 18, null), (365, 36, 'Trắng', '#FFFFFF', '32', 15, null),
(366, 36, 'Be', '#D97706', '28', 12, null), (367, 36, 'Be', '#D97706', '29', 14, null), (368, 36, 'Be', '#D97706', '30', 16, null), (369, 36, 'Be', '#D97706', '32', 13, null),
(370, 36, 'Xanh', '#2563EB', '28', 10, null), (371, 36, 'Xanh', '#2563EB', '29', 12, null), (372, 36, 'Xanh', '#2563EB', '30', 14, null), (373, 36, 'Xanh', '#2563EB', '32', 11, null),

-- Quần Legging Nữ (37) - 3 colors x 4 sizes = 12 variants
(374, 37, 'Đen', '#000000', 'S', 25, null), (375, 37, 'Đen', '#000000', 'M', 30, null), (376, 37, 'Đen', '#000000', 'L', 28, null), (377, 37, 'Đen', '#000000', 'XL', 20, null),
(378, 37, 'Xám', '#6B7280', 'S', 22, null), (379, 37, 'Xám', '#6B7280', 'M', 28, null), (380, 37, 'Xám', '#6B7280', 'L', 25, null), (381, 37, 'Xám', '#6B7280', 'XL', 18, null),
(382, 37, 'Hồng', '#EC4899', 'S', 20, null), (383, 37, 'Hồng', '#EC4899', 'M', 26, null), (384, 37, 'Hồng', '#EC4899', 'L', 23, null), (385, 37, 'Hồng', '#EC4899', 'XL', 16, null),

-- Quần Tây Nữ (38) - 2 colors x 5 sizes = 10 variants
(386, 38, 'Đen', '#000000', '25', 18, null), (387, 38, 'Đen', '#000000', '26', 20, null), (388, 38, 'Đen', '#000000', '27', 22, null), (389, 38, 'Đen', '#000000', '28', 19, null), (390, 38, 'Đen', '#000000', '29', 15, null),
(391, 38, 'Xanh', '#2563EB', '25', 16, null), (392, 38, 'Xanh', '#2563EB', '26', 18, null), (393, 38, 'Xanh', '#2563EB', '27', 20, null), (394, 38, 'Xanh', '#2563EB', '28', 17, null), (395, 38, 'Xanh', '#2563EB', '29', 13, null),

-- Quần Short Nữ (39) - 3 colors x 4 sizes = 12 variants
(396, 39, 'Xanh', '#2563EB', '25', 20, null), (397, 39, 'Xanh', '#2563EB', '26', 22, null), (398, 39, 'Xanh', '#2563EB', '27', 24, null), (399, 39, 'Xanh', '#2563EB', '28', 21, null),
(400, 39, 'Đen', '#000000', '25', 18, null), (401, 39, 'Đen', '#000000', '26', 20, null), (402, 39, 'Đen', '#000000', '27', 22, null), (403, 39, 'Đen', '#000000', '28', 19, null),
(404, 39, 'Trắng', '#FFFFFF', '25', 16, null), (405, 39, 'Trắng', '#FFFFFF', '26', 18, null), (406, 39, 'Trắng', '#FFFFFF', '27', 20, null), (407, 39, 'Trắng', '#FFFFFF', '28', 17, null),

-- Quần Jogger Nữ (40) - 3 colors x 4 sizes = 12 variants
(408, 40, 'Đen', '#000000', 'S', 22, null), (409, 40, 'Đen', '#000000', 'M', 26, null), (410, 40, 'Đen', '#000000', 'L', 24, null), (411, 40, 'Đen', '#000000', 'XL', 18, null),
(412, 40, 'Xám', '#6B7280', 'S', 20, null), (413, 40, 'Xám', '#6B7280', 'M', 24, null), (414, 40, 'Xám', '#6B7280', 'L', 22, null), (415, 40, 'Xám', '#6B7280', 'XL', 16, null),
(416, 40, 'Hồng', '#EC4899', 'S', 18, null), (417, 40, 'Hồng', '#EC4899', 'M', 22, null), (418, 40, 'Hồng', '#EC4899', 'L', 20, null), (419, 40, 'Hồng', '#EC4899', 'XL', 14, null),

-- Giày Loafers (41) - 2 colors x 5 sizes = 10 variants
(420, 41, 'Nâu', '#92400E', '39', 10, null), (421, 41, 'Nâu', '#92400E', '40', 12, null), (422, 41, 'Nâu', '#92400E', '41', 14, null), (423, 41, 'Nâu', '#92400E', '42', 11, null), (424, 41, 'Nâu', '#92400E', '43', 8, null),
(425, 41, 'Đen', '#000000', '39', 8, null), (426, 41, 'Đen', '#000000', '40', 10, null), (427, 41, 'Đen', '#000000', '41', 12, null), (428, 41, 'Đen', '#000000', '42', 9, null), (429, 41, 'Đen', '#000000', '43', 6, null),

-- Giày Pumps (42) - 2 colors x 4 sizes = 8 variants
(430, 42, 'Đen', '#000000', '36', 12, null), (431, 42, 'Đen', '#000000', '37', 15, null), (432, 42, 'Đen', '#000000', '38', 13, null), (433, 42, 'Đen', '#000000', '39', 10, null),
(434, 42, 'Nâu', '#92400E', '36', 10, null), (435, 42, 'Nâu', '#92400E', '37', 13, null), (436, 42, 'Nâu', '#92400E', '38', 11, null), (437, 42, 'Nâu', '#92400E', '39', 8, null),

-- Giày Thể Thao (43) - 3 colors x 5 sizes = 15 variants
(438, 43, 'Trắng', '#FFFFFF', '39', 15, null), (439, 43, 'Trắng', '#FFFFFF', '40', 18, null), (440, 43, 'Trắng', '#FFFFFF', '41', 20, null), (441, 43, 'Trắng', '#FFFFFF', '42', 17, null), (442, 43, 'Trắng', '#FFFFFF', '43', 12, null),
(443, 43, 'Đen', '#000000', '39', 13, null), (444, 43, 'Đen', '#000000', '40', 16, null), (445, 43, 'Đen', '#000000', '41', 18, null), (446, 43, 'Đen', '#000000', '42', 15, null), (447, 43, 'Đen', '#000000', '43', 10, null),
(448, 43, 'Xanh', '#2563EB', '39', 11, null), (449, 43, 'Xanh', '#2563EB', '40', 14, null), (450, 43, 'Xanh', '#2563EB', '41', 16, null), (451, 43, 'Xanh', '#2563EB', '42', 13, null), (452, 43, 'Xanh', '#2563EB', '43', 8, null),

-- Sandal Flip Flops (44) - 3 colors x 4 sizes = 12 variants
(453, 44, 'Đen', '#000000', '39', 25, null), (454, 44, 'Đen', '#000000', '40', 28, null), (455, 44, 'Đen', '#000000', '41', 30, null), (456, 44, 'Đen', '#000000', '42', 27, null),
(457, 44, 'Xanh', '#2563EB', '39', 22, null), (458, 44, 'Xanh', '#2563EB', '40', 25, null), (459, 44, 'Xanh', '#2563EB', '41', 27, null), (460, 44, 'Xanh', '#2563EB', '42', 24, null),
(461, 44, 'Đỏ', '#DC2626', '39', 20, null), (462, 44, 'Đỏ', '#DC2626', '40', 23, null), (463, 44, 'Đỏ', '#DC2626', '41', 25, null), (464, 44, 'Đỏ', '#DC2626', '42', 22, null),

-- Túi Tote Nữ (45) - 3 colors x 1 size = 3 variants
(465, 45, 'Đen', '#000000', 'Free Size', 30, null),
(466, 45, 'Be', '#D97706', 'Free Size', 28, null),
(467, 45, 'Hồng', '#EC4899', 'Free Size', 25, null),

-- Ví Nam (46) - 2 colors x 1 size = 2 variants
(468, 46, 'Nâu', '#92400E', 'Free Size', 40, null),
(469, 46, 'Đen', '#000000', 'Free Size', 35, null),

-- Kính Mát (47) - 3 colors x 1 size = 3 variants
(470, 47, 'Đen', '#000000', 'Free Size', 50, null),
(471, 47, 'Xanh', '#2563EB', 'Free Size', 45, null),
(472, 47, 'Hồng', '#EC4899', 'Free Size', 40, null),

-- Khăn Scarf (48) - 3 colors x 1 size = 3 variants
(473, 48, 'Đỏ', '#DC2626', 'Free Size', 35, null),
(474, 48, 'Xanh', '#2563EB', 'Free Size', 32, null),
(475, 48, 'Vàng', '#F59E0B', 'Free Size', 30, null),

-- Vòng Tay (49) - 2 colors x 1 size = 2 variants
(476, 49, 'Đen', '#000000', 'Free Size', 60, null),
(477, 49, 'Nâu', '#92400E', 'Free Size', 55, null),

-- Mũ Lưỡi Trai (50) - 3 colors x 1 size = 3 variants
(478, 50, 'Đen', '#000000', 'Free Size', 45, null),
(479, 50, 'Trắng', '#FFFFFF', 'Free Size', 42, null),
(480, 50, 'Navy', '#1E3A8A', 'Free Size', 38, null);
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

-- Insert stock items for new products (19-50)
INSERT IGNORE INTO stock_items (id, stock_id, product_id, quantity) VALUES
-- Áo Polo Nam (19)
(55, 1, 19, 50), (56, 2, 19, 70), (57, 3, 19, 40),
-- Áo Len Nam (20)
(58, 1, 20, 35), (59, 2, 20, 50), (60, 3, 20, 25),
-- Áo Khoác Nam (21)
(61, 1, 21, 20), (62, 2, 21, 30), (63, 3, 21, 15),
-- Áo Vest Nam (22)
(64, 1, 22, 15), (65, 2, 22, 25), (66, 3, 22, 12),
-- Áo Thun Graphic (23)
(67, 1, 23, 60), (68, 2, 23, 80), (69, 3, 23, 45),
-- Áo Cardigan Nam (24)
(70, 1, 24, 30), (71, 2, 24, 45), (72, 3, 24, 22),
-- Áo Tank Top Nam (25)
(73, 1, 25, 75), (74, 2, 25, 95), (75, 3, 25, 55),
-- Áo Cardigan Nữ (26)
(76, 1, 26, 40), (77, 2, 26, 60), (78, 3, 26, 30),
-- Áo Trench Coat (27)
(79, 1, 27, 12), (80, 2, 27, 18), (81, 3, 27, 10),
-- Áo Thun Oversize (28)
(82, 1, 28, 80), (83, 2, 28, 100), (84, 3, 28, 60),
-- Áo Blazer Slim (29)
(85, 1, 29, 18), (86, 2, 29, 25), (87, 3, 29, 15),
-- Áo Sweater Nữ (30)
(88, 1, 30, 45), (89, 2, 30, 65), (90, 3, 30, 35),
-- Áo Vest Nữ (31)
(91, 1, 31, 25), (92, 2, 31, 35), (93, 3, 31, 20),
-- Áo Tank Top Nữ (32)
(94, 1, 32, 55), (95, 2, 32, 75), (96, 3, 32, 45),
-- Quần Jogger Nam (33)
(97, 1, 33, 50), (98, 2, 33, 70), (99, 3, 33, 40),
-- Quần Cargo Nam (34)
(100, 1, 34, 35), (101, 2, 34, 50), (102, 3, 34, 30),
-- Quần Tây Nam (35)
(103, 1, 35, 40), (104, 2, 35, 55), (105, 3, 35, 32),
-- Quần Ống Rộng Nam (36)
(106, 1, 36, 45), (107, 2, 36, 60), (108, 3, 36, 35),
-- Quần Legging Nữ (37)
(109, 1, 37, 65), (110, 2, 37, 85), (111,  3, 37, 50),
-- Quần Tây Nữ (38)
(112, 1, 38, 50), (113, 2, 38, 70), (114, 3, 38, 40),
-- Quần Short Nữ (39)
(115, 1, 39, 55), (116, 2, 39, 75), (117, 3, 39, 45),
-- Quần Jogger Nữ (40)
(118, 1, 40, 48), (119, 2, 40, 65), (120, 3, 40, 38),
-- Giày Loafers (41)
(121, 1, 41, 25), (122, 2, 41, 35), (123, 3, 41, 20),
-- Giày Pumps (42)
(124, 1, 42, 30), (125, 2, 42, 40), (126, 3, 42, 25),
-- Giày Thể Thao (43)
(127, 1, 43, 45), (128, 2, 43, 60), (129, 3, 43, 35),
-- Sandal Flip Flops (44)
(130, 1, 44, 70), (131, 2, 44, 90), (132, 3, 44, 55),
-- Túi Tote Nữ (45)
(133, 1, 45, 50), (134, 2, 45, 70), (135, 3, 45, 40),
-- Ví Nam (46)
(136, 1, 46, 40), (137, 2, 46, 55), (138, 3, 46, 32),
-- Kính Mát (47)
(139, 1, 47, 60), (140, 2, 47, 80), (141, 3, 47, 50),
-- Khăn Scarf (48)
(142, 1, 48, 45), (143, 2, 48, 60), (144, 3, 48, 35),
-- Vòng Tay (49)
(145, 1, 49, 55), (146, 2, 49, 75), (147, 3, 49, 45),
-- Mũ Lưỡi Trai (50)
(148, 1, 50, 50), (149, 2, 50, 70), (150, 3, 50, 40);
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
ALTER TABLE product_types AUTO_INCREMENT = 7;
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

-- Quần Nam (33-36)
(33, 'Quần Jogger Nam', 'Quần jogger nam thể thao chất cotton pha. Ống bo gấu, túi zip tiện lợi.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 3, 'X-Store', 'Cotton Blend', 280000, 399000),
(34, 'Quần Cargo Nam', 'Quần cargo nam nhiều túi chất cotton canvas. Form straight leg rugged.', 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400', 3, 'X-Store', 'Canvas', 350000, 499000),
(35, 'Quần Tây Nam Công Sở', 'Quần tây nam công sở chất wool pha. Form slim fit lịch lãm.', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', 3, 'X-Store', 'Wool Blend', 420000, 599000),
(36, 'Quần Ống Rộng Nam', 'Quần ống rộng nam chất linen thoáng mát. Form wide leg bohemian.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 3, 'X-Store', 'Linen', 320000, 459000),

-- Quần Nữ (37-40)
(37, 'Quần Legging Nữ', 'Quần legging nữ thể thao chất spandex co giãn. Form ôm sát, thấm hút mồ hôi.', 'https://images.unsplash.com/photo-1583496661160-fb5886a13d75?w=400', 4, 'X-Store', 'Spandex', 220000, 319000),
(38, 'Quần Tây Nữ', 'Quần tây nữ công sở chất gabardine. Form straight leg thanh lịch.', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400', 4, 'X-Store', 'Gabardine', 380000, 549000),
(39, 'Quần Short Nữ', 'Quần short nữ chất denim co giãn. Form high waist trendy.', 'https://images.unsplash.com/photo-1571513722275-4b8c0215cd62?w=400', 4, 'X-Store', 'Denim', 240000, 349000),
(40, 'Quần Jogger Nữ', 'Quần jogger nữ thể thao chất polyester. Ống bo gấu, dây rút eo.', 'https://images.unsplash.com/photo-1506629905607-0667baa7b8b1?w=400', 4, 'X-Store', 'Polyester', 260000, 379000),

-- Giày Dép (41-44)
(41, 'Giày Lười Nam Loafers', 'Giày lười nam loafers da thật. Thiết kế penny slot cổ điển.', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400', 5, 'X-Store', 'Genuine Leather', 650000, 899000),
(42, 'Giày Cao Gót Nữ Pumps', 'Giày cao gót nữ pumps da PU. Gót 8cm thanh lịch, mũi nhọn.', 'https://images.unsplash.com/photo-1515347619252-60a4bf4fff4f?w=400', 5, 'X-Store', 'PU Leather', 480000, 689000),
(43, 'Giày Thể Thao Unisex', 'Giày thể thao unisex chất mesh thoáng khí. Đế EVA êm ái.', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', 5, 'X-Store', 'Mesh', 420000, 599000),
(44, 'Sandal Nam Flip Flops', 'Sandal nam flip flops chất EVA nhẹ nhàng. Thiết kế đơn giản, dễ chịu.', 'https://images.unsplash.com/photo-1560343090-f0409e92791a?w=400', 5, 'X-Store', 'EVA', 120000, 179000),

-- Phụ Kiện (45-50)
(45, 'Túi Xách Nữ Tote', 'Túi xách nữ tote canvas in họa tiết. Size lớn đựng laptop.', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400', 6, 'X-Store', 'Canvas', 250000, 359000),
(46, 'Ví Nam Da', 'Ví nam da thật bifold. 6 ngăn thẻ, 2 ngăn tiền tiện lợi.', 'https://images.unsplash.com/photo-1624222247344-550fb60583dc?w=400', 6, 'X-Store', 'Genuine Leather', 350000, 499000),
(47, 'Kính Mát Unisex', 'Kính mát unisex chất acetate. Thiết kế wayfarer cổ điển.', 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400', 6, 'X-Store', 'Acetate', 280000, 399000),
(48, 'Khăn Choàng Nữ Scarf', 'Khăn choàng nữ scarf lụa mềm mại. Họa tiết hoa văn tinh tế.', 'https://images.unsplash.com/photo-1601762603339-fd61e28b698a?w=400', 6, 'X-Store', 'Silk', 220000, 319000),
(49, 'Vòng Tay Nữ Bracelet', 'Vòng tay nữ bracelet da thắt nút. Thiết kế minimalist.', 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400', 6, 'X-Store', 'Leather', 150000, 219000),
(50, 'Mũ Lưỡi Trai Unisex', 'Mũ lưỡi trai unisex cotton. Thiết kế baseball cap phong cách Mỹ.', 'https://images.unsplash.com/photo-1521119989659-a83eee488004?w=400', 6, 'X-Store', 'Cotton', 180000, 259000);

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

