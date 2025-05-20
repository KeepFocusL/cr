-- 车站表
INSERT INTO station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228636137132032, '厦门', 'xiamen', 'XM', '2025-04-13 09:23:10.423', '2025-04-13 09:23:10.423');
INSERT INTO station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228680609337344, '厦门北', 'xiamenbei', 'XMB', '2025-04-13 09:23:21.026', '2025-04-13 09:23:21.026');
INSERT INTO station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228704349097984, '福州南', 'fuzhounan', 'FZN', '2025-04-13 09:23:26.686', '2025-04-13 09:23:26.686');
INSERT INTO station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228722762092544, '福州', 'fuzhou', 'FZ', '2025-04-13 09:23:31.076', '2025-04-13 09:23:31.076');
INSERT INTO station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228762733809664, '北京', 'beijing', 'BJ', '2025-04-13 09:23:40.606', '2025-04-13 09:23:40.606');
INSERT INTO station (id, name, name_pinyin, name_py, created_at, updated_at) VALUES (1911228782132465664, '南京', 'nanjing', 'NJ', '2025-04-13 09:23:45.231', '2025-04-13 09:23:45.231');


-- 车次
INSERT INTO train (id, code, type, start, start_pinyin, start_time, end, end_pinyin, end_time, created_at, updated_at) VALUES (1911229416948764672, 'G5114', 'G', '厦门', 'xiamen', '14:33:00', '福州', 'fuzhou', '16:09:00', '2025-04-13 09:26:16.583', '2025-04-13 09:26:16.583');


-- 火车车站
INSERT INTO train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230072740777984, 'G5114', 0, '厦门', 'xiamen', '14:33:00', '14:33:00', '00:00:00', 0.00, '2025-04-13 09:28:52.936', '2025-04-13 09:28:52.936');
INSERT INTO train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230297316397056, 'G5114', 1, '厦门北', 'xiamenbei', '14:55:00', '14:57:00', '00:02:00', 10.00, '2025-04-13 09:29:46.479', '2025-04-13 09:29:46.479');
INSERT INTO train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230560727076864, 'G5114', 2, '福州南', 'fuzhounan', '15:52:00', '15:54:00', '00:02:00', 125.00, '2025-04-13 09:30:49.281', '2025-04-13 09:30:49.281');
INSERT INTO train_station (id, train_code, `index`, name, name_pinyin, in_time, out_time, stop_time, km, created_at, updated_at) VALUES (1911230828139122688, 'G5114', 3, '福州', 'fuzhou', '16:09:00', '16:09:00', '00:00:00', 38.00, '2025-04-13 09:31:53.037', '2025-04-13 09:31:53.037');

-- 火车车厢（不完整）
INSERT INTO train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1911231416759357440, 'G5114', 1, '1', 12, 3, 4, '2025-04-13 09:34:13.375', '2025-04-13 09:34:13.375');
INSERT INTO train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1911231342205603840, 'G5114', 2, '2', 15, 3, 5, '2025-04-13 09:33:55.600', '2025-04-13 09:33:55.600');
INSERT INTO train_carriage (id, train_code, `index`, seat_type, seat_count, row_count, col_count, created_at, updated_at) VALUES (1911231416759357140, 'G5114', 3, '2', 20, 4, 5, '2025-04-13 09:34:13.375', '2025-04-13 09:34:13.375');

-- 火车座位(自动生成)