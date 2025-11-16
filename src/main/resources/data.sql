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
(1, 'WELCOME10', 'Ưu đãi khách mới', 'Giảm 10% cho khách hàng mới', 'PERCENT', 0, 10.0, 0, 1000, '2025-01-01', '2025-12-31', true),
(2, 'SALE50K', 'Giảm 50K', 'Giảm 50k cho đơn hàng từ 500k', 'FIXED', 50000, 0.0, 0, 500, '2025-01-01', '2025-12-31', true),
(3, 'SUMMER20', 'Khuyến mãi hè', 'Khuyến mãi hè - Giảm 20%', 'PERCENT', 0, 20.0, 0, 200, '2025-06-01', '2025-08-31', true),
(4, 'FREESHIP', 'Miễn phí ship', 'Miễn phí vận chuyển', 'FIXED', 30000, 0.0, 0, 2000, '2025-01-01', '2025-12-31', true),
(5, 'VIP15', 'Ưu đãi VIP', 'Ưu đãi VIP - Giảm 15%', 'PERCENT', 0, 15.0, 0, 100, '2025-01-01', '2025-12-31', true);

-- Reset AUTO_INCREMENT
ALTER TABLE stocks AUTO_INCREMENT = 4;
ALTER TABLE product_types AUTO_INCREMENT = 7;
ALTER TABLE products AUTO_INCREMENT = 19;
ALTER TABLE product_info AUTO_INCREMENT = 193;
ALTER TABLE stock_items AUTO_INCREMENT = 55;
ALTER TABLE discounts AUTO_INCREMENT = 6;

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

-- Reset AUTO_INCREMENT
ALTER TABLE chat_rooms AUTO_INCREMENT = 1;
ALTER TABLE chats AUTO_INCREMENT = 1;

